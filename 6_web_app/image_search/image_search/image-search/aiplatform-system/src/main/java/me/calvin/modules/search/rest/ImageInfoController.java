package me.calvin.modules.search.rest;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import cn.hutool.core.util.ObjectUtil;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.milvus.param.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calvin.annotation.Log;
import me.calvin.config.FileProperties;
import me.calvin.domain.LocalStorage;
import me.calvin.domain.ServerFileVO;
import me.calvin.exception.BadRequestException;
import me.calvin.modules.search.common.constant.Constant;
import me.calvin.modules.search.common.constant.ImageGroupConst;
import me.calvin.modules.search.common.utils.ImageUtil;
import me.calvin.modules.search.common.utils.UUIDUtil;
import me.calvin.modules.search.common.utils.UserAgentUtil;
import me.calvin.modules.search.common.utils.ZipUtil;
import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.domain.enums.ResEnum;
import me.calvin.modules.search.domain.request.B64ImageReq;
import me.calvin.modules.search.domain.request.ExtractFeatureReq;
import me.calvin.modules.search.domain.request.UrlImageReq;
import me.calvin.modules.search.domain.response.ImageInfoRes;
import me.calvin.modules.search.domain.response.ResultRes;
import me.calvin.modules.search.lmax.ImageEvent;
import me.calvin.modules.search.lmax.ImageEventFactory;
import me.calvin.modules.search.lmax.ImageEventHandler;
import me.calvin.modules.search.lmax.ImageProducer;
import me.calvin.modules.search.model.ImageEncoderModel;
import me.calvin.modules.search.service.FeatureService;
import me.calvin.modules.search.service.ImageInfoService;
import me.calvin.modules.search.service.SearchService;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import me.calvin.modules.search.service.dto.ImageInfoQueryCriteria;
import me.calvin.service.LocalStorageService;
import me.calvin.service.dto.LocalStorageDto;
import me.calvin.utils.FileUtil;
import me.calvin.utils.RedisUtils;
import me.calvin.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * 图片信息管理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "图片管理")
@RequestMapping("/api/imageInfo")
public class ImageInfoController {
    private final FileProperties properties;
    private final ImageInfoService imageInfoService;

    private static final String ID_FIELD = "imageId";
    private ConcurrentHashMap<String, Long> map = new ConcurrentHashMap();

    @Autowired
    private ImageEncoderModel imageEncoderModel;

    @Autowired
    private ImageInfoService imageService;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private LocalStorageService localStorageService;

    private final RedisUtils redisUtils;

    @Value("${search.dimension}")
    String dimension;

    @Value("${image.readerNum}")
    int readerNum;

    @Value("${image.bufferSize}")
    int bufferSize;

    @Value("${image.batchSize}")
    int batchSize;

    @Value("${image.threadNum}")
    int threadNum;

    @Value("${search.collectionName}")
    String collectionName;

    @Value("${search.partitionName}")
    String partitionName;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('imageInfo:list')")
    public void download(HttpServletResponse response, ImageInfoQueryCriteria criteria) throws IOException {
        imageInfoService.download(imageInfoService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ImageInfoService")
    @ApiOperation("查询ImageInfoService")
    @PreAuthorize("@el.check('imageInfo:list')")
    public ResponseEntity<Object> query(ImageInfoQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(imageInfoService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ImageInfoService")
    @ApiOperation("新增ImageInfoService")
    @PreAuthorize("@el.check('imageInfo:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ImageInfo resources) {
        return new ResponseEntity<>(imageInfoService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ImageInfoService")
    @ApiOperation("修改ImageInfoService")
    @PreAuthorize("@el.check('imageInfo:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ImageInfo resources) {
        imageInfoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ImageInfoService")
    @ApiOperation("删除ImageInfoService")
    @PreAuthorize("@el.check('imageInfo:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        imageInfoService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @Log("上传base64格式图片")
//    @ApiOperation(value = "上传base64格式图片")
//    @PostMapping(value = "/base64")
//    public ResponseEntity<Object> uploadBase64Image(@RequestBody B64ImageReq b64ImageReq) throws IOException {
//        // yyyy/MM/dd/ 日期便于查看，uuid 每次上传需要唯一路径，便于后续删除维护
//        String imageRootPath = properties.getPath().getImageRootPath();
//        String relativePath = ImageUtil.generatePath(imageRootPath);
//        ImageInfo imageInfo = imageService.uploadBase64Image(b64ImageReq.getBase64(), imageRootPath, relativePath);
//        return singleImage(imageInfo);
//    }

    @Log("上传url图片")
    @ApiOperation(value = "上传url图片")
    @PostMapping(value = "/url")
    public ResponseEntity<Object> uploadImageByUrl(@RequestBody UrlImageReq urlImageReq) throws IOException {
        String imageRootPath = properties.getPath().getImageRootPath();
        String relativePath = ImageUtil.generatePath(imageRootPath);
        ImageInfo imageInfo = imageService.uploadImageByUrl(urlImageReq.getUrl(), imageRootPath, relativePath);
        return singleImage(imageInfo);
    }

    @Log("上传图片文件")
    @ApiOperation(value = "上传图片文件")
    @PostMapping(value = "/image")
    public ResponseEntity<Object> uploadImage(@RequestParam("image") MultipartFile imageFile) throws IOException {
        String imageRootPath = properties.getPath().getImageRootPath();
        String relativePath = ImageUtil.generatePath(imageRootPath);
        ImageInfo imageInfo = imageService.uploadImage(imageFile, imageRootPath, relativePath);
        return singleImage(imageInfo);
    }

    @PostMapping("/localAdd")
    @ApiOperation("本地上传zip文件")
    public ResponseEntity<Object> create(@RequestParam String name, @RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        if (!Constant.ZIP_FILE_TYPE.equalsIgnoreCase(suffix.toUpperCase())) {
            return new ResponseEntity<>(ResultRes.error(ResEnum.PACKAGE_FILE_FAIL.KEY, ResEnum.PACKAGE_FILE_FAIL.VALUE), HttpStatus.OK);
        }

        FileUtil.checkSize(properties.getMaxSize(), multipartFile.getSize());
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, properties.getPath().getPath() + type + File.separator);
        if (ObjectUtil.isNull(file)) {
            throw new BadRequestException("上传失败");
        }

        // 获取上传者操作系统
        UserAgentUtil userAgentGetter = new UserAgentUtil(request);
        String os = userAgentGetter.getOS();

        String imageRootPath = properties.getPath().getImageRootPath();
        //生成UUID作为解压缩的目录
        String UUID = UUIDUtil.getUUID();
        String unZipFilePath = imageRootPath + UUID;
        if (!new File(unZipFilePath).exists()) {
            new File(unZipFilePath).mkdirs();
        }

        ZipUtil.unZip(file.getPath(), os, unZipFilePath);
        FileUtil.del(file);
        // yyyy/MM/dd/ 日期便于查看，uuid 每次上传需要唯一路径，便于后续删除维护
        String relativePath = ImageUtil.generatePath(imageRootPath) + UUIDUtil.getUUID() + File.separator;
        try {
            List<ImageInfo> imageInfoList = imageService.uploadImages(unZipFilePath, imageRootPath, relativePath, true);
            LocalStorage localStorage = localStorageService.create(name, imageRootPath, relativePath, "", "");
            for (ImageInfo imageInfo : imageInfoList) {
                imageInfo.setGroupId(localStorage.getId());
                imageInfo.setCreateTime(new Timestamp(new Date().getTime()));
                imageInfo.setCreateBy(ImageGroupConst.DEFAULT_OPERATE_PERSON);
            }
            List<ImageInfoDto> imageInfoDtoList = imageInfoService.createAll(imageInfoList);
            localStorage.setSize("" + imageInfoList.size());
            localStorageService.update(localStorage);

        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("上传失败");
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/uploadDir")
    @ApiOperation("本地上传文件夹")
    public ResponseEntity<Object> uploadDir(@RequestParam String uuid, @RequestParam MultipartFile imageFile) {
        // 判断文件是否为bmp jpg jpeg png图片, 不是图片不保存
        String suffix = FileUtil.getExtensionName(imageFile.getOriginalFilename());
        if (!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix.toLowerCase()))) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        if (StringUtils.isBlank(uuid)) {
            throw new BadRequestException("uuid为空，请刷新页面重试");
        }
        String imageRootPath = properties.getPath().getImageRootPath();
        // yyyy/MM/dd/ 日期便于查看，当前页面不刷新，上传多个文件夹共享一个uuid，点击提取特征会重新生成uuid
        String relativePath = ImageUtil.generatePath(imageRootPath) + uuid + File.separator;
        File imageFileDir = new File(imageRootPath + relativePath);
        if (!imageFileDir.exists()) {
            boolean success = imageFileDir.mkdirs();
            if (!success) {
                throw new BadRequestException("请检查配置文件中imageRootPath属性的路径格式");
            }
        }

        //更新图片数量
        try {
            Long id = map.get(uuid);
            //检查，数据库条目是否被删除。
            LocalStorageDto localStorageDto = null;
            if (!ObjectUtil.isNull(id)) {
                localStorageDto = localStorageService.findById(id);
                if (ObjectUtil.isNull(localStorageDto)) {
                    map.remove(uuid);
                    id = null;
                }
            }
            if (ObjectUtil.isNull(id)) {
                LocalStorage localStorage = localStorageService.create("name", imageRootPath, relativePath, "", "");
                ImageInfo imageInfo = imageService.uploadImage(imageFile, imageRootPath, relativePath);
                imageInfo.setGroupId(localStorage.getId());
                imageInfo.setPreName(imageFile.getOriginalFilename());
                imageInfo.setCreateTime(new Timestamp(new Date().getTime()));
                imageInfo.setCreateBy(ImageGroupConst.DEFAULT_OPERATE_PERSON);
                ImageInfoDto imageInfoDto = imageInfoService.create(imageInfo);
                map.put(uuid, localStorage.getId());
                localStorageService.update(localStorage);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                LocalStorage localStorage = new LocalStorage();
                BeanUtils.copyProperties(localStorageDto, localStorage);
                long count = FileUtil.getFileCount(new File(imageRootPath + relativePath));
                //保存图片信息
                ImageInfo imageInfo = imageService.uploadImage(imageFile, imageRootPath, relativePath);
                imageInfo.setGroupId(localStorage.getId());
                imageInfo.setPreName(imageFile.getOriginalFilename());
                imageInfo.setCreateTime(new Timestamp(new Date().getTime()));
                imageInfo.setCreateBy(ImageGroupConst.DEFAULT_OPERATE_PERSON);
                ImageInfoDto imageInfoDto = imageInfoService.create(imageInfo);
                localStorage.setSize("" + (count + 1));
                localStorageService.update(localStorage);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/serverAdd")
    @ApiOperation("服务器端上传文件夹")
    public ResponseEntity<Object> serverAdd(@RequestBody ServerFileVO serverFileVO) {
        // 判断文件是否为图片
        String fullPath = serverFileVO.getFullPath().replace("\\", "/");
        if (StringUtils.isBlank(fullPath)) {
            throw new BadRequestException("文件夹路径不能为空");
        }
        File file = new File(fullPath);
        if (!file.exists()) {
            throw new BadRequestException("文件夹路径不存在");
        }

        if (!file.isDirectory()) {
            throw new BadRequestException("请输入文件夹路径");
        }

        try {
            String imageRootPath = properties.getPath().getImageRootPath();
            // yyyy/MM/dd/ 日期便于查看，uuid 每次上传需要唯一路径，便于后续删除维护
            String relativePath = ImageUtil.generatePath(imageRootPath) + UUIDUtil.getUUID() + File.separator;
            List<ImageInfo> imageInfoList = imageService.uploadImages(fullPath, imageRootPath, relativePath, false);
            //用于查看路径，及删除操作
            LocalStorage localStorage = localStorageService.create("", imageRootPath, relativePath, "", "");
            for (ImageInfo imageInfo : imageInfoList) {
                imageInfo.setGroupId(localStorage.getId());
                imageInfo.setCreateTime(new Timestamp(new Date().getTime()));
                imageInfo.setCreateBy(ImageGroupConst.DEFAULT_OPERATE_PERSON);
            }
            List<ImageInfoDto> imageInfoDtoList = imageInfoService.createAll(imageInfoList);
            localStorage.setSize("" + imageInfoList.size());
            localStorageService.update(localStorage);
            return new ResponseEntity<>(localStorage, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("上传失败");
        }
    }

    @Log("批量提取特文件夹图片征值")
    @ApiOperation(value = "批量提取特文件夹图片征值")
    @PostMapping(value = "/extractFeatures")
    public ResponseEntity<Object> extractFeatures(@RequestBody ExtractFeatureReq extractFeatureReq) {
        Long id = Long.parseLong(extractFeatureReq.getId());
        LocalStorageDto localStorageDto = localStorageService.findById(id);

        //更新特征数量字段状态
        localStorageDto.setStatus("进行中...");
        LocalStorage localStorage = new LocalStorage();
        BeanUtils.copyProperties(localStorageDto, localStorage);
        localStorageService.update(localStorage);
        List<ImageInfoDto> imageInfoList = imageInfoService.queryByGroupId(id);
        // 分批次提取特征
        // 批次大小 batchSize
        long timeInferStart = System.currentTimeMillis();
        if (imageInfoList.size() <= batchSize) {
            batchImages(id, imageInfoList);
        } else {
            // 计算多少个批次
            int batch = imageInfoList.size() / batchSize;
            // 剩余未分配数据数量
            int left = imageInfoList.size() % batchSize;
            // 未分配数据集合
            List<ImageInfoDto> leftData = imageInfoList.subList(imageInfoList.size() - left, imageInfoList.size());
            List<List<ImageInfoDto>> buckets = new ArrayList<>();
            // 给每个批次分配数据
            for (int i = 0; i < batch; i++) {
                List<ImageInfoDto> bucket = new ArrayList<>();
                bucket.addAll(imageInfoList.subList(batchSize * i, batchSize * (i + 1)));
                buckets.add(bucket);
            }
            // 追加未分配的数据
            if (left > 0) {
                buckets.add(leftData);
            }
            for (int i = 0; i < buckets.size(); i++) {
                batchImages(id, buckets.get(i));
            }
        }

        long timeInferEnd = System.currentTimeMillis();
        long timeUsed = timeInferEnd - timeInferStart;
        float mins = timeUsed / 1000f / 60f;
        System.out.println("resultList size: " + imageInfoList.size());
        System.out.println("time used: " + timeUsed / 1000f + " s");
        System.out.println("time used: " + mins + " mins");
        //设置位数
        int scale = 2;
        //表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        int roundingMode = 4;
        BigDecimal bd = new BigDecimal(mins);
        bd = bd.setScale(scale, roundingMode);
        mins = bd.floatValue();
        this.updateTime(id, mins);

        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }

    @Log("删除所有图片信息(含特征)")
    @ApiOperation(value = "删除所有图片信息(含特征)")
    @PostMapping(value = "/deleteImages")
    public ResponseEntity<Object> deleteImages(@RequestBody Long[] ids) {
        for (Long id : ids) {
            LocalStorageDto localStorageDto = localStorageService.findById(id);
            List<ImageInfoDto> imageInfoList = imageInfoService.queryByGroupId(id);
            Long[] imageIds = new Long[imageInfoList.size()];
            List<Long> deleteIds = new ArrayList<>();
            for (int i = 0; i < imageInfoList.size(); i++) {
                imageIds[i] = imageInfoList.get(i).getImageId();
                deleteIds.add(imageInfoList.get(i).getImageId());
            }
            //删除图片信息
            imageInfoService.deleteAll(imageIds);
            //删除图片向量信息
            String deleteExpr = ID_FIELD + " in " + deleteIds;
            searchService.delete(deleteExpr);
            //删除图片文件
            FileUtil.delete(localStorageDto.getRootPath() + localStorageDto.getPath());
        }
        //删除存储记录信息
        localStorageService.deleteAll(ids);
        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }

    @Log("删除图片特征")
    @ApiOperation(value = "删除图片特征")
    @PostMapping(value = "/deleteFeatures")
    public ResponseEntity<Object> deleteFeatures(@RequestBody Long id) {
        LocalStorageDto localStorageDto = localStorageService.findById(id);
        List<ImageInfoDto> imageInfoList = imageInfoService.queryByGroupId(id);
        Long[] ids = new Long[imageInfoList.size()];
        List<Long> deleteIds = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ids[i] = imageInfoList.get(i).getImageId();
            deleteIds.add(imageInfoList.get(i).getImageId());
        }
        String deleteExpr = ID_FIELD + " in " + deleteIds.toString();
        searchService.delete(deleteExpr);

        //清空特征数量字段
        localStorageDto.setStatus("");
        localStorageDto.setTime("");
        LocalStorage localStorage = new LocalStorage();
        BeanUtils.copyProperties(localStorageDto, localStorage);
        localStorageService.update(localStorage);

        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }

    @Log("根据图片UUID查询信息")
    @ApiOperation(value = "根据图片UUID查询信息")
    @GetMapping(value = "/queryImageInfoByUuid/{uuid}")
    public ResponseEntity<Object> queryImageInfoByUuid(@PathVariable String uuid) {
        // 根据ID获取图片信息
        ImageInfoQueryCriteria imageInfoQueryCriteria = new ImageInfoQueryCriteria();
        imageInfoQueryCriteria.setUuid(uuid);
        List<ImageInfoDto> imageInfoList = imageInfoService.queryAll(imageInfoQueryCriteria);
        ImageInfoDto imageInfo = imageInfoList.get(0);
        return new ResponseEntity<>(imageInfo, HttpStatus.OK);
    }

    private ResponseEntity<Object> singleImage(ImageInfo imageInfo) {
        List<Float> feature;
        try {
            Image img = ImageFactory.getInstance().fromUrl(imageInfo.getFullPath());
            //特征提取
            feature = featureService.imageFeature(img);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MODEL_ERROR.KEY, ResEnum.MODEL_ERROR.VALUE), HttpStatus.OK);
        }
        imageInfo.setCreateTime(new Timestamp(new Date().getTime()));
        imageInfo.setCreateBy(ImageGroupConst.DEFAULT_OPERATE_PERSON);
        // 图片信息入库
        ImageInfoDto newImageInfoDto = imageService.create(imageInfo);

        //向量插入向量引擎
        try {
            List<Long> vectorIds = new ArrayList<>();
            vectorIds.add(newImageInfoDto.getImageId());
            List<List<Float>> vectors = new ArrayList<>();
            vectors.add(feature);

            // 初始化 Milvus 向量引擎
            R<Boolean> response = searchService.hasCollection();
            if (!response.getData()) {
                searchService.initSearchEngine();
            }

            searchService.insert(vectorIds, vectors);

            // 检查是否加载 collection， 如果没有，插入数据后加载
            boolean loaded = searchService.getCollectionState();
            if (!loaded) {
                searchService.loadCollection();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
        }

        ImageInfoRes imageInfoRes = new ImageInfoRes();
        BeanUtils.copyProperties(imageInfo, imageInfoRes);
        return new ResponseEntity<>(imageInfoRes, HttpStatus.OK);
    }

    private ResponseEntity<Object> batchImages(Long id, List<ImageInfoDto> imageInfoList) {
        try {
            ConcurrentLinkedQueue<ImageInfoDto> queue = new ConcurrentLinkedQueue<>();
            ConcurrentLinkedQueue<ImageInfoDto> result = new ConcurrentLinkedQueue<>();
            for (ImageInfoDto imageInfo : imageInfoList) {
                queue.add(imageInfo);
            }

            EventFactory<ImageEvent> eventFactory = new ImageEventFactory();
            ExecutorService executor = Executors.newFixedThreadPool(threadNum);
            // 定义Disruptor，基于多生产者，阻塞策略
            Disruptor<ImageEvent> disruptor = new Disruptor<ImageEvent>(eventFactory, bufferSize, executor, ProducerType.MULTI, new BlockingWaitStrategy());
            // 多个消费者，每个消费者消费不同数据。也就是说每个消费者竞争数据，竞争到消费，其他消费者没有机会
            ImageEventHandler handlers[] = new ImageEventHandler[threadNum];
            for (int i = 0; i < threadNum; i++) {
                ImageEventHandler handler = new ImageEventHandler(imageEncoderModel, i, result);
                handlers[i] = handler;
            }
            disruptor.handleEventsWithWorkerPool(handlers);
            disruptor.start();
            RingBuffer<ImageEvent> ringBuffer = disruptor.getRingBuffer();

            final CountDownLatch latch = new CountDownLatch(readerNum);
            long timeInferStart = System.currentTimeMillis();

            // 生产者产生数据
            for (int i = 0; i < readerNum; i++) {
                new Thread(new ImageProducer(i, queue, ringBuffer, latch)).start();
            }
            System.out.println("----------线程创建完毕，开始读取图片 ----------");
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 关闭disruptor和线程池, 会等待任务完成
                // 先通过CountDownLatch保证所有数据都已经添加到ringbuffer
                disruptor.shutdown();
                executor.shutdown();
                for (int i = 0; i < threadNum; i++) {
                    handlers[i].close();
                }
            }

            //插入向量引擎
            List<Long> vectorIds = new ArrayList<>();
            List<List<Float>> vectors = new ArrayList<>();
            int size = result.size();
            for (int i = 0; i < size; i++) {
                ImageInfoDto imageInfoDto = result.poll();
                vectorIds.add(imageInfoDto.getImageId());
                vectors.add(imageInfoDto.getFeature());
            }

            // 初始化 Milvus 向量引擎
            R<Boolean> response = searchService.hasCollection();
            if (!response.getData()) {
                searchService.initSearchEngine();
            }

            searchService.insert(vectorIds, vectors);

            // 检查是否加载 collection， 如果没有，插入数据后加载
            boolean loaded = searchService.getCollectionState();
            if (!loaded) {
                searchService.loadCollection();
            }

            updateStatus(id, size);

            long timeInferEnd = System.currentTimeMillis();
            long timeUsed = timeInferEnd - timeInferStart;
            float mins = timeUsed / 1000f / 60f;
            System.out.println("batch size: " + size);
            System.out.println("time used: " + timeUsed / 1000f + " s");
            System.out.println("time used: " + mins + " mins");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultRes.error(ResEnum.SYSTEM_ERROR.KEY, e.getMessage()), HttpStatus.OK);
        }

        return new ResponseEntity<>(ResultRes.success(), HttpStatus.OK);
    }

    private void updateStatus(Long id, int num) {
        LocalStorageDto localStorageDto = localStorageService.findById(id);
        if (StringUtils.isBlank(localStorageDto.getStatus())) {
            localStorageDto.setStatus("" + num);
        } else {
            if (localStorageDto.getStatus().equals("进行中...")) {
                localStorageDto.setStatus("0");
            }
            int sum = Integer.parseInt(localStorageDto.getStatus()) + num;
            localStorageDto.setStatus("" + sum);
        }
        LocalStorage localStorage = new LocalStorage();
        BeanUtils.copyProperties(localStorageDto, localStorage);
        localStorageService.update(localStorage);
    }

    private void updateTime(Long id, float time) {
        LocalStorageDto localStorageDto = localStorageService.findById(id);
        localStorageDto.setTime("" + time);
        LocalStorage localStorage = new LocalStorage();
        BeanUtils.copyProperties(localStorageDto, localStorage);
        localStorageService.update(localStorage);
    }
}