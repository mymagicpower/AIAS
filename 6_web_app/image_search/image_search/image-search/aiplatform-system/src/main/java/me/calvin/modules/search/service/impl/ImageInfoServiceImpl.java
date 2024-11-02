package me.calvin.modules.search.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calvin.exception.BadRequestException;
import me.calvin.modules.search.common.exception.BusinessException;
import me.calvin.modules.search.common.utils.ImageUtil;
import me.calvin.modules.search.common.utils.UUIDUtil;
import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.domain.enums.ResEnum;
import me.calvin.modules.search.repository.ImageInfoRepository;
import me.calvin.modules.search.service.ImageInfoService;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import me.calvin.modules.search.service.dto.ImageInfoQueryCriteria;
import me.calvin.modules.search.service.mapstruct.ImageInfoMapper;
import me.calvin.utils.FileUtil;
import me.calvin.utils.PageUtil;
import me.calvin.utils.QueryHelp;
import me.calvin.utils.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 图像信息服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageInfoServiceImpl implements ImageInfoService {
    private final ImageInfoRepository imageInfoRepository;
    private final ImageInfoMapper imageInfoMapper;

    @Override
    public Map<String, Object> queryAll(ImageInfoQueryCriteria criteria, Pageable pageable) {
        Page<ImageInfo> page = imageInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(imageInfoMapper::toDto));
    }

    @Override
    public List<ImageInfoDto> queryAll(ImageInfoQueryCriteria criteria) {
        return imageInfoMapper.toDto(imageInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public List<ImageInfoDto> queryByGroupId(Long id) {
        List<ImageInfo> list = imageInfoRepository.findByGroupId(id);
        return imageInfoMapper.toDto(list);
    }

    @Override
    public List<ImageInfoDto> queryAll(ArrayList<Long> idList) {
        List<ImageInfo> list = imageInfoRepository.findAllById(idList);
        return imageInfoMapper.toDto(list);
    }

    @Override
    public List<ImageInfoDto> queryAll(Long id) {
        // 性能很差，一条一条读，不是批量读，需调研一下原因
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("group_id",  match -> match.equals(id));
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setGroupId(id);
        Example<ImageInfo> example = Example.of(imageInfo, matcher);
        List<ImageInfo> list = imageInfoRepository.findAll(example);
        return imageInfoMapper.toDto(list);
    }

    @Override
    @Transactional
    public ImageInfoDto findById(Long id) {
        ImageInfo imageInfo = imageInfoRepository.findById(id).orElseGet(ImageInfo::new);
        ValidationUtil.isNull(imageInfo.getImageId(), "ImageInfo", "id", id);
        return imageInfoMapper.toDto(imageInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImageInfoDto create(ImageInfo resources) {
        return imageInfoMapper.toDto(imageInfoRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ImageInfoDto> createAll(List<ImageInfo> imageInfoList) {
        return imageInfoMapper.toDto(imageInfoRepository.saveAll(imageInfoList));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ImageInfo resources) {
        ImageInfo imageInfo = imageInfoRepository.findById(resources.getImageId()).orElseGet(ImageInfo::new);
        ValidationUtil.isNull(imageInfo.getImageId(), "ImageInfo", "id", resources.getImageId());
        imageInfo.copy(resources);
        imageInfoRepository.save(imageInfo);
    }

    @Override
    public void deleteAll(Long[] ids) {
        List<ImageInfo> imageInfoList = new ArrayList<>();
        ImageInfo imageInfo;
        for (Long id : ids) {
            imageInfo = new ImageInfo();
            imageInfo.setImageId(id);
            imageInfoList.add(imageInfo);
        }
        imageInfoRepository.deleteAll(imageInfoList);
    }

    @Override
    public void download(List<ImageInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ImageInfoDto imageInfo : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("图片uuid", imageInfo.getUuid());
            map.put(" preName", imageInfo.getPreName());
            map.put("图片分组id", imageInfo.getGroupId());
            map.put("图片相对路径", imageInfo.getImgUri());
            map.put(" fullPath", imageInfo.getFullPath());
            map.put("创建时间", imageInfo.getCreateTime());
            map.put("创建人", imageInfo.getCreateBy());
            map.put("修改时间", imageInfo.getUpdateTime());
            map.put("修改人", imageInfo.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


//    // 将base64转换为图片
//    @Override
//    public ImageInfo uploadBase64Image(String base64, String imageRootPath, String relativePath) throws IOException {
//        if (!new File(imageRootPath + relativePath).exists()) {
//            new File(imageRootPath + relativePath).mkdirs();
//        }
//        ImageInfo imageInfo = new ImageInfo();
//        String ext = StringUtils.defaultIfEmpty(StringUtils.substringBetween(base64, "data:image/", ";"), "jpg"); // 图片后缀
//        // 去掉前面的“data:image/jpeg;base64,”的字样
//        String base64ImgData = base64.split("base64,")[1]; // 图片数据
//        if (StringUtils.isNotBlank(ext) && StringUtils.isNotBlank(base64ImgData)) {
//            if ("jpeg".equalsIgnoreCase(ext)) {
//                ext = "jpg";
//            }
//            saveBase64Image(imageInfo, base64ImgData, ext, imageRootPath,relativePath);
//            return imageInfo;
//        }
//        return null;
//    }

    // 将url转换为图片
    @Override
    public ImageInfo uploadImageByUrl(String url, String imageRootPath, String relativePath) throws IOException {
        if (!new File(imageRootPath + relativePath).exists()) {
            new File(imageRootPath + relativePath).mkdirs();
        }

        ImageInfo imageInfo = new ImageInfo();
        // img 单图片字节数组
        String ext = "jpg"; // 待存储的文件名统一成jpg
        byte[] bytes = ImageUtil.getImageByUrl(url);
        saveImage(imageInfo, bytes, ext, imageRootPath,relativePath);
        return imageInfo;
    }

    /**
     * 上传图片
     *
     * @param imageFile
     * @return
     */
    @Override
    public ImageInfo uploadImage(MultipartFile imageFile, String imageRootPath, String relativePath) throws IOException {
        if (!new File(imageRootPath + relativePath).exists()) {
            new File(imageRootPath + relativePath).mkdirs();
        }

        ImageInfo imageInfo = new ImageInfo();
        // 将文件转换为图片
        byte[] bytes = ImageUtil.multipartFileToBytes(imageFile);
        String ext = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".") + 1);

        saveImage(imageInfo, bytes, ext, imageRootPath, relativePath);
        return imageInfo;
    }

    @Override
    public List<ImageInfo> uploadImages(String sourcePath, String imageRootPath, String relativePath, boolean delete) throws BusinessException, IOException {
        if (!new File(imageRootPath + relativePath).exists()) {
            new File(imageRootPath + relativePath).mkdirs();
        }
        try {
            // 记录图片集合
            List<ImageInfo> resultList = new ArrayList<>();
            // 待复制的文件路径，如：解压缩后的文件路径
            File[] listFiles = new File(sourcePath).listFiles();

            // 判断上传文件是否包含图片
            boolean imgFound = false;
            for (File file : listFiles) {
                String suffix = ImageUtil.getImageFormat(file);
                if (FileUtil.IMAGE.equals(FileUtil.getFileType(suffix.toLowerCase()))) {
                    imgFound = true;
                    break;
                }
            }
            if (!imgFound) {
                // 通用异常
                throw new BadRequestException(ResEnum.IMAGE_NOT_FOUND.VALUE);
            }

            // 保存图片到可访问路径
            for (File file : listFiles) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setPreName(file.getName());
                String ext = FileUtil.getExtensionName(file.getName());
                if (StringUtils.isNotBlank(ext) && FileUtil.IMAGE.equals(FileUtil.getFileType(ext.toLowerCase()))) {
                    byte[] bytes = ImageUtil.file2Byte(file);
                    String uuid = UUIDUtil.getUUID();
                    imageInfo.setUuid(uuid);
                    String fileName = uuid + "." + ext; // 待存储的文件名
                    // filePath 图片完整路径（含uuid文件名）
                    String filePath = imageRootPath + relativePath + fileName;
                    imageInfo.setFullPath(filePath); // 图片本地Path
                    imageInfo.setImgUri(relativePath + fileName);

                    // 转成文件保存
                    ImageUtil.bytesToImageFile(bytes, filePath);


                    resultList.add(imageInfo);
                }
            }
            return resultList;
        } finally {
            if(delete){
                FileUtil.delete(sourcePath);
            }
        }
    }

//    private void saveBase64Image(ImageInfo imageInfo, String base64ImgData, String ext, String imageRootPath, String relativePath) throws IOException {
//        if (!new File(imageRootPath + relativePath).exists()) {
//            new File(imageRootPath + relativePath).mkdirs();
//        }
//        String uuid = UUIDUtil.getUUID();
//        imageInfo.setUuid(uuid);
//        String fileName = uuid + "." + ext; // 待存储的文件名
//        // filePath 图片完整路径（含uuid文件名）
//        String filePath = imageRootPath + relativePath + fileName;
//        imageInfo.setFullPath(filePath); // 图片本地Path
//        imageInfo.setImgUri(relativePath + fileName);
//
//        // 转成文件保存
//        ImageUtil.base64ToImageFile(base64ImgData, filePath);
//    }

    private void saveImage(ImageInfo imageInfo, byte[] bytes, String ext, String imageRootPath, String relativePath) throws IOException {
        if (!new File(imageRootPath + relativePath).exists()) {
            new File(imageRootPath + relativePath).mkdirs();
        }
        String uuid = UUIDUtil.getUUID();
        imageInfo.setUuid(uuid);
        String fileName = uuid + "." + ext; // 待存储的文件名
        // filePath 图片完整路径（含uuid文件名）
        String filePath = imageRootPath + relativePath + fileName;
        imageInfo.setFullPath(filePath); // 图片本地Path
        imageInfo.setImgUri(relativePath + fileName);

        // 转成文件保存
        ImageUtil.bytesToImageFile(bytes, filePath);
    }
}