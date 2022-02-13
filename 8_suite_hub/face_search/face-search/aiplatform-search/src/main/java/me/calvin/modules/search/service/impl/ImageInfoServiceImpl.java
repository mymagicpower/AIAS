package me.calvin.modules.search.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calvin.exception.BadRequestException;
import me.calvin.modules.search.common.exception.BusinessException;
import me.calvin.modules.search.common.utils.common.ImageUtil;
import me.calvin.modules.search.common.utils.common.UUIDUtil;
import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.domain.enums.ImageType;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Calvin
 * @description 服务实现
 * @date 2021-02-17
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageInfoServiceImpl implements ImageInfoService {
    @Value("${image.baseurl}")
    String baseurl;

    private final ImageInfoRepository imageInfoRepository;
    private final ImageInfoMapper imageInfoMapper;
    @PersistenceContext
    private EntityManager em;

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
    public void update(ImageInfo resources) {
        ImageInfo imageInfo = imageInfoRepository.findById(resources.getImageId()).orElseGet(ImageInfo::new);
        ValidationUtil.isNull(imageInfo.getImageId(), "ImageInfo", "id", resources.getImageId());
        imageInfo.copy(resources);
        imageInfoRepository.save(imageInfo);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            imageInfoRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ImageInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ImageInfoDto imageInfo : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("图片uuid", imageInfo.getUuid());
            map.put(" preName", imageInfo.getPreName());
            map.put("图片分组id", imageInfo.getGroupId());
            map.put("检测目标json", imageInfo.getDetectObjs());
            map.put("图片相对路径", imageInfo.getImgUrl());
            map.put(" fullPath", imageInfo.getFullPath());
            map.put("1: 本地url，0: 远程图片url", imageInfo.getType());
            map.put("创建时间", imageInfo.getCreateTime());
            map.put("创建人", imageInfo.getCreateBy());
            map.put("修改时间", imageInfo.getUpdateTime());
            map.put("修改人", imageInfo.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    // 将base64转换为图片
    @Override
    public ImageInfo uploadBase64Image(String base64, String imageRootPath) throws IOException {
        if (!new File(imageRootPath).exists()) {
            new File(imageRootPath).mkdirs();
        }
        ImageInfo imageInfo = new ImageInfo();
        String ext =
                StringUtils.defaultIfEmpty(
                        StringUtils.substringBetween(base64, "data:image/", ";"), "jpg"); // 图片后缀
        // 去掉前面的“data:image/jpeg;base64,”的字样
        String base64ImgData = base64.split("base64,")[1]; // 图片数据
        if (StringUtils.isNotBlank(ext) && StringUtils.isNotBlank(base64ImgData)) {
            if ("jpeg".equalsIgnoreCase(ext)) {
                ext = "jpg";
            }
            singleBase64Image(imageInfo, base64ImgData, ext, imageRootPath);
            return imageInfo;
        }
        return null;
    }

    /**
     * 上传图片
     *
     * @param imageFile
     * @return
     */
    @Override
    public ImageInfo uploadImage(MultipartFile imageFile, String imageRootPath) throws IOException {
        if (!new File(imageRootPath).exists()) {
            new File(imageRootPath).mkdirs();
        }

        ImageInfo imageInfo = new ImageInfo();
        // 将文件转换为图片
        byte[] bytes = ImageUtil.multipartFileToBytes(imageFile);
        String ext =
                imageFile
                        .getOriginalFilename()
                        .substring(imageFile.getOriginalFilename().lastIndexOf(".") + 1);

        singleImage(imageInfo, bytes, ext, imageRootPath);
        return imageInfo;
    }

    @Override
    public List<ImageInfo> uploadImages(String imageRootPath, String UUID)
            throws BusinessException, IOException {
        if (!new File(imageRootPath).exists()) {
            new File(imageRootPath).mkdirs();
        }
        String unZipFilePath = imageRootPath + UUID; // 以压缩文件名为新目录
        try {
            // 记录图片集合
            List<ImageInfo> resultList = new ArrayList<>();
            // 解压缩后的文件
            File[] listFiles = new File(unZipFilePath).listFiles();

            // 判断上传文件是否包含图片
            boolean imgFound = false;
            for (File file : listFiles) {
                String suffix = ImageUtil.getImageFormat(file);
                if ((suffix.equals(ImageType.FILE_JPEG.key)
                        || suffix.equals(ImageType.FILE_JPG.key)
                        || suffix.equals(ImageType.FILE_PNG.key))) {
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
                String ext = ImageUtil.getImageFormat(file);
                if ((ext.equals(ImageType.FILE_JPEG.key)
                        || ext.equals(ImageType.FILE_JPG.key)
                        || ext.equals(ImageType.FILE_PNG.key))) {

                    byte[] bytes = ImageUtil.file2Byte(file);
                    singleImage(imageInfo, bytes, ext, imageRootPath);
                    resultList.add(imageInfo);
                }
            }
            return resultList;
        } finally {
            // unZipFilePath = imageRootPath + uuid; // 以压缩文件名为新目录
            FileUtil.delete(unZipFilePath);

        }
    }

    private void singleBase64Image(
            ImageInfo imageInfo, String base64ImgData, String ext, String imageRootPath) throws IOException {
        String uuid = UUIDUtil.getUUID();
        imageInfo.setUuid(uuid);
        String fileName = uuid + "." + ext; // 待存储的文件名
        String relativePath = ImageUtil.generatePath(imageRootPath);
        // filePath 图片完整路径（含uuid文件名）
        String filePath = imageRootPath + relativePath + fileName;
        imageInfo.setFullPath(filePath); // 图片本地Path
        imageInfo.setImgUrl(baseurl + relativePath + fileName);
        // 转成文件保存
        ImageUtil.base64ToImageFile(base64ImgData, filePath);
    }

    private void singleImage(
            ImageInfo imageInfo, byte[] bytes, String ext, String imageRootPath)
            throws IOException {
        String uuid = UUIDUtil.getUUID();
        imageInfo.setUuid(uuid);
        String fileName = uuid + "." + ext; // 待存储的文件名
        String relativePath = ImageUtil.generatePath(imageRootPath);
        // filePath 图片完整路径（含uuid文件名）
        String filePath = imageRootPath + relativePath + fileName;
        imageInfo.setFullPath(filePath); // 图片本地Path
        imageInfo.setImgUrl(baseurl + relativePath + fileName);

        // 转成文件保存
        ImageUtil.bytesToImageFile(bytes, filePath);
    }
}