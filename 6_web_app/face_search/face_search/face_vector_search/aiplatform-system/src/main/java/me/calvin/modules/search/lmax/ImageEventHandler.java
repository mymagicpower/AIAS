package me.calvin.modules.search.lmax;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import com.lmax.disruptor.WorkHandler;
import me.calvin.modules.search.common.utils.FaceUtils;
import me.calvin.modules.search.common.utils.NDArrayUtils;
import me.calvin.modules.search.common.utils.SVDUtils;
import me.calvin.modules.search.domain.FaceObject;
import me.calvin.modules.search.domain.SimpleFaceObject;
import me.calvin.modules.search.face.FaceDetectionModel;
import me.calvin.modules.search.face.FaceFeatureModel;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 该消费者执行特征提取操作
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class ImageEventHandler implements com.lmax.disruptor.EventHandler<ImageEvent>, WorkHandler<ImageEvent> {
    private long threadId;
    private ConcurrentLinkedQueue<ImageInfoDto> queue;
    private NDManager manager;
    private FaceDetectionModel faceDetectionModel;
    private FaceFeatureModel faceFeatureModel;
    private int count;

    public ImageEventHandler(FaceDetectionModel detectModel, FaceFeatureModel featureModel, int threadId, ConcurrentLinkedQueue<ImageInfoDto> queue) {
        System.out.println(threadId + ": ImageEventHandler init");
        this.threadId = threadId;
        this.queue = queue;
        this.faceDetectionModel = detectModel;
        this.faceFeatureModel = featureModel;
        manager = NDManager.newBaseManager();
    }

    @Override
    public void onEvent(ImageEvent event, long sequence, boolean endOfBatch) throws Exception {
//        onEvent(event);
    }

    @Override
    public void onEvent(ImageEvent event) throws Exception {
        ImageInfoDto imageInfo = event.getImageInfo();
        count++;
        long timeInferStart = System.currentTimeMillis();
        List<FaceObject> result = faceDetect(imageInfo);
        List<SimpleFaceObject> faceList = new ArrayList<>();
        //转换检测对象，方便后面json转换
        for (int i = 0; i < result.size(); i++) {
            FaceObject faceObject = result.get(i);
            Rectangle rect = faceObject.getBoundingBox().getBounds();
            SimpleFaceObject faceDTO = new SimpleFaceObject();
            faceDTO.setId(i + 1);
            faceDTO.setScore(faceObject.getScore());
            faceDTO.setFeature(faceObject.getFeature());
            faceDTO.setX((int) rect.getX());
            faceDTO.setY((int) rect.getY());
            faceDTO.setWidth((int) rect.getWidth());
            faceDTO.setHeight((int) rect.getHeight());
            faceList.add(faceDTO);
        }
        imageInfo.setFaceObjects(faceList);
        long timeInferEnd = System.currentTimeMillis();
        long timeUsed = timeInferEnd - timeInferStart;
        System.out.println("model thread: " + threadId + " extract image - " + count + " - time used - " + timeUsed);
        // 收回图像占据的内存
        imageInfo.setImage(null);
        queue.offer(imageInfo);


    }

    public List<FaceObject> faceDetect(ImageInfoDto imageInfo) throws TranslateException {
        DetectedObjects detections = faceDetectionModel.predict(imageInfo.getImage());
        List<DetectedObjects.DetectedObject> list = detections.items();
        List<FaceObject> faceObjects = new ArrayList<>();

        for (DetectedObjects.DetectedObject detectedObject : list) {
            BoundingBox box = detectedObject.getBoundingBox();
            Rectangle rectangle = box.getBounds();
            // 抠人脸图
            // factor = 0.1f, 意思是扩大10%，防止人脸仿射变换后，人脸被部分截掉
            Rectangle subImageRect =
                    FaceUtils.getSubImageRect(rectangle, imageInfo.getImage().getWidth(), imageInfo.getImage().getHeight(), 0f);
            int x = (int) (subImageRect.getX());
            int y = (int) (subImageRect.getY());
            int w = (int) (subImageRect.getWidth());
            int h = (int) (subImageRect.getHeight());
            Image subImage = imageInfo.getImage().getSubImage(x, y, w, h);
            // 获取人脸关键点列表
            List<Point> points = (List<Point>) box.getPath();
            // 计算人脸关键点在子图中的新坐标
            double[][] pointsArray = FaceUtils.pointsArray(subImageRect, points);
            // 转 NDArray
            NDArray srcPoints = manager.create(pointsArray);
            NDArray dstPoints = SVDUtils.point112x112(manager);

            // 定制的5点仿射变换
            Mat svdMat = NDArrayUtils.toOpenCVMat(manager, srcPoints, dstPoints);
            // 换仿射变换矩阵
            Mat mat = FaceUtils.get5WarpAffineImg((Mat)subImage.getWrappedImage(), svdMat);

            int width = mat.width() > 112 ? 112 : mat.width();
            int height = mat.height() > 112 ? 112 : mat.height();
            Image img = OpenCVImageFactory.getInstance().fromImage(mat).getSubImage(0, 0, width, height);

            //获取特征向量
            float[] embeddings = faceFeatureModel.predict(img);
            List<Float> feature = new ArrayList<>();
            for (int i = 0; i < embeddings.length; i++) {
                feature.add(embeddings[i]);
            }

            FaceObject faceObject = new FaceObject();
            faceObject.setFeature(feature);
            faceObject.setBoundingBox(subImageRect);
            faceObjects.add(faceObject);
        }
        return faceObjects;
    }

    public void close() {
        manager.close();
    }
}