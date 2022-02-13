package me.aias.controller;

import ai.djl.ModelException;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.translate.TranslateException;
import io.milvus.param.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.common.face.FaceObject;
import me.aias.config.FileProperties;
import me.aias.domain.*;
import me.aias.service.DetectService;
import me.aias.service.ImageService;
import me.aias.service.LocalStorageService;
import me.aias.service.SearchService;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 视频管理
 * @author Calvin
 * @date 2021-12-12
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "视频管理")
@RequestMapping("/api/video")
public class VideoController {
    private final FileProperties properties;

    @Autowired
    private ImageService imageService;

    @Autowired
    private DetectService detectService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private LocalStorageService localStorageService;

    @Value("${image.baseurl}")
    String baseurl;

    @Value("${face.mod}")
    int mod;

    @ApiOperation(value = "视频解析图片帧并提取特征值")
    @GetMapping("/extractFeatures")
    public ResponseEntity<Object> extractFeatures(@RequestParam(value = "id") String id) throws IOException {
        LocalStorage localStorage = localStorageService.findById(Integer.parseInt(id));

        if (!new File(properties.getPath().getImageRootPath()).exists()) {
            new File(properties.getPath().getImageRootPath()).mkdirs();
        }
        //生成视频文件提取的图片帧目录
        String imagesPath = properties.getPath().getImageRootPath();
        if (!new File(imagesPath).exists()) {
            new File(imagesPath).mkdirs();
        }

        String input = localStorage.getPath();
        //支持视频文件（mp4,flv,avi等）,流媒体地址（rtmp，rtsp,http-flv等）
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input)) {
            grabber.start();

            Frame frame;
            Java2DFrameConverter converter = new Java2DFrameConverter();
            // 抓取图像画面
            int i = 1;
            int count = 1;
            for (; (frame = grabber.grabImage()) != null; ) {
                BufferedImage image = converter.convert(frame);
                try {
                    List<FaceObject> faceObjects = detectService.faceDetect(image);
                    if (faceObjects.size() > 0) {
                        if (count++ < mod)
                            continue;
                        count = 1;
                        ImageIO.write(image, "png", new FileOutputStream(imagesPath + i + ".png"));
                        ImageInfoDto imageInfoDto = new ImageInfoDto();
                        // 保存图片信息
                        ConcurrentHashMap<String, String> map = imageService.getMap();
                        int size = map.size();
                        imageService.addImageFile(String.valueOf(size + 1), +i + ".png");

                        // 将向量插入向量引擎
                        try {
                            imageInfoDto.setImageId(Long.valueOf(size + 1));
                            List<SimpleFaceObject> faceList = new ArrayList<>();
                            //转换检测对象，方便后面json转换
                            List<Long> vectorIds = new ArrayList<>();
                            List<List<Float>> vectors = new ArrayList<>();
                            for (FaceObject faceObject : faceObjects) {
                                Rectangle rect = faceObject.getBoundingBox().getBounds();
                                SimpleFaceObject faceDTO = new SimpleFaceObject();
                                faceDTO.setScore(faceObject.getScore());
                                faceDTO.setFeature(faceObject.getFeature());
                                faceDTO.setX((int) rect.getX());
                                faceDTO.setY((int) rect.getY());
                                faceDTO.setWidth((int) rect.getWidth());
                                faceDTO.setHeight((int) rect.getHeight());
                                faceList.add(faceDTO);

                                vectorIds.add(imageInfoDto.getImageId());
                                vectors.add(faceObject.getFeature());
                            }
                            imageInfoDto.setFaceObjects(faceList);
                            R<Boolean> response = searchService.hasCollection();
                            if (!response.getData()) {
                                searchService.initSearchEngine();
                            }
                            searchService.insert(vectorIds, vectors);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error(e.getMessage());
                            return new ResponseEntity<>(ResultRes.error(ResEnum.MILVUS_CONNECTION_ERROR.KEY, ResEnum.MILVUS_CONNECTION_ERROR.VALUE), HttpStatus.OK);
                        }
                        i++;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ModelException e) {
                    e.printStackTrace();
                } catch (TranslateException e) {
                    e.printStackTrace();
                }
            }
        }

        return new ResponseEntity<>(ResultBean.success(), HttpStatus.OK);
    }
}