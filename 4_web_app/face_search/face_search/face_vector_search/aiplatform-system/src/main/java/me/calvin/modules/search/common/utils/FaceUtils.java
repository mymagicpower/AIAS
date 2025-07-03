package me.calvin.modules.search.common.utils;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.List;

/**
 * 人脸工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class FaceUtils {
    /**
     * 根据目标5点，进行旋转仿射变换
     * Perform rotation and affine transformation based on the target 5 points
     *
     * @param src
     * @param rot_mat
     * @return
     */
    public static Mat get5WarpAffineImg(Mat src, Mat rot_mat) {
        Mat rot = new Mat();
        // 进行仿射变换，变换后大小为src的大小
        // Perform affine transformation, the size after transformation is the same as the size of src
        Imgproc.warpAffine(src, rot, rot_mat, src.size());
        return rot;
    }

    public static Image align(Image image, BoundingBox box, boolean save) {
        Rectangle rectangle = box.getBounds();
        // 人脸抠图
        // Crop the face image
        // factor = 0.1f, 意思是扩大10%，防止人脸仿射变换后，人脸被部分截掉
        // factor = 0.1f, meaning to expand by 10%, to prevent the face from being partially cut off after affine transformation
        Rectangle subImageRect =
                FaceUtils.getSubImageRect(rectangle, image.getWidth(), image.getHeight(), 0f);
        int x = (int) (subImageRect.getX());
        int y = (int) (subImageRect.getY());
        int w = (int) (subImageRect.getWidth());
        int h = (int) (subImageRect.getHeight());
        Image subImage = image.getSubImage(x, y, w, h);

        if (save) {
            // 保存，抠出的人脸图
            // Save the cropped face image
            String savePath =  "detected/";
            if (!new File(savePath).exists()) {
                new File(savePath).mkdirs();
            }
            DJLImageUtils.saveImage(subImage, "cropped_face.png", savePath);
        }

        // 获取人脸关键点列表
        // Get the list of face keypoints
        List<Point> points = (List<Point>) box.getPath();
        //      人脸关键点坐标对应的人脸部位
        //      Face parts corresponding to the coordinates of the facial keypoints
        //      1.  left_eye_x , left_eye_y
        //      2.  right_eye_x , right_eye_y
        //      3.  nose_x , nose_y
        //      4.  left_mouth_x , left_mouth_y
        //      5.  right_mouth_x , right_mouth_y
        // 计算人脸关键点在子图中的新坐标
        // Calculate the new coordinates of facial keypoints in the sub-image
        double[][] pointsArray = FaceUtils.pointsArray(subImageRect, points);

        // 转 NDArray - Convert to NDArray
        NDManager manager = NDManager.newBaseManager();
        NDArray srcPoints = manager.create(pointsArray);
        NDArray dstPoints = SVDUtils.point112x112(manager);

        // 定制的5点仿射变换 - Custom 5-point affine transformation
        Mat svdMat = NDArrayUtils.toOpenCVMat(manager, srcPoints, dstPoints);
        Mat mat = get5WarpAffineImg((Mat) subImage.getWrappedImage(), svdMat);

        int width = mat.width() > 112 ? 112 : mat.width();
        int height = mat.height() > 112 ? 112 : mat.height();
        Image img = OpenCVImageFactory.getInstance().fromImage(mat).getSubImage(0, 0, width, height);
        svdMat.release();
        return img;
    }

    /**
     * 返回外扩100%人脸 factor = 1, 100%, factor = 0.2, 20%
     * Returns a 100% expansion factor=1, 100%, factor=0.2, 20%
     *
     * @param rectangle
     * @param width
     * @param height
     * @param factor
     * @return
     */
    public static Rectangle getSubImageRect(
            Rectangle rectangle, int width, int height, float factor) {
        // 左上角坐标
        // Top left corner coordinates
        int x1 = (int) (rectangle.getX() * width);
        int y1 = (int) (rectangle.getY() * height);
        // 宽度，高度
        // Width, height
        int w = (int) (rectangle.getWidth() * width);
        int h = (int) (rectangle.getHeight() * height);
        // 左上角坐标
        // Top left corner coordinates
        int x2 = x1 + w;
        int y2 = y1 + h;

        // 外扩，防止对齐后人脸出现黑边
        // Expanding outwards to prevent black edges from appearing after alignment
        int new_x1 = Math.max((int) (x1 + x1 * factor / 2 - x2 * factor / 2), 0);
        int new_x2 = Math.min((int) (x2 + x2 * factor / 2 - x1 * factor / 2), width - 1);
        int new_y1 = Math.max((int) (y1 + y1 * factor / 2 - y2 * factor / 2), 0);
        int new_y2 = Math.min((int) (y2 + y2 * factor / 2 - y1 * factor / 2), height - 1);
        int new_w = new_x2 - new_x1;
        int new_h = new_y2 - new_y1;

        // double x, double y, double width, double height
        Rectangle newRect = new Rectangle(new_x1, new_y1, new_w, new_h);

        return newRect;
    }

    /**
     * 获取子图中关键点坐标 - Coordinates of key points in the image
     *
     * @param subImageRect
     * @param points
     * @return
     */
    public static double[][] pointsArray(Rectangle subImageRect, Iterable<Point> points) {
        int x = (int) (subImageRect.getX());
        int y = (int) (subImageRect.getY());
        //      图中关键点坐标 - Coordinates of key points in the image
        //      1.  left_eye_x , left_eye_y
        //      2.  right_eye_x , right_eye_y
        //      3.  nose_x , nose_y
        //      4.  left_mouth_x , left_mouth_y
        //      5.  right_mouth_x , right_mouth_y
        double[][] pointsArray = new double[5][2]; // 保存人脸关键点 - Save facial key points
        int i = 0;
        for (Point point : points) {
            pointsArray[i][0] = point.getX() - x;
            pointsArray[i][1] = point.getY() - y;
            i++;
        }
        return pointsArray;
    }
}
