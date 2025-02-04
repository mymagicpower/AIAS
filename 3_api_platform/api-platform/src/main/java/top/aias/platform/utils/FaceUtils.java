package top.aias.platform.utils;

import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;

/**
 * 人脸工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class FaceUtils {
    // 计算全局标准差
    // Calculate global standard deviation

    /**
     * 112x96的目标点 - Target point of 112x96
     *
     * @param manager
     * @return
     */
    public static NDArray faceTemplate112X96(NDManager manager) {
        double[][] coord5point = {
                {30.2946f, 51.6963f}, // 112x96的目标点 - Target point of 112x96
                {65.5318f, 51.6963f},
                {48.0252f, 71.7366f},
                {33.5493f, 92.3655f},
                {62.7299f, 92.3655f}
        };
        NDArray points = manager.create(coord5point);
        return points;
    }

    /**
     * 112x112的目标点 - Target point of 112x112
     *
     * @param manager
     * @return
     */
    public static NDArray faceTemplate112x112(NDManager manager) {
        double[][] coord5point = {
                {30.2946f + 8.0000f, 51.6963f}, // 112x112的目标点 - Target point of 112x112
                {65.5318f + 8.0000f, 51.6963f},
                {48.0252f + 8.0000f, 71.7366f},
                {33.5493f + 8.0000f, 92.3655f},
                {62.7299f + 8.0000f, 92.3655f}
        };
        NDArray points = manager.create(coord5point);
        return points;
    }

    /**
     * 512x512的目标点 - Target point of 512x512
     * standard 5 landmarks for FFHQ faces with 512 x 512
     *
     * @param manager
     * @return
     */
    public static NDArray faceTemplate512x512(NDManager manager) {
        double[][] coord5point = {
                {192.98138, 239.94708}, // 512x512的目标点 - Target point of 512x512
                {318.90277, 240.1936},
                {256.63416, 314.01935},
                {201.26117, 371.41043},
                {313.08905, 371.15118}
        };
        NDArray points = manager.create(coord5point);
        return points;
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
     * 子图中人脸关键点坐标 - Coordinates of key points in the image
     *
     * @param points
     * @return
     */
    public static double[][] facePoints(Iterable<Point> points) {
        //      图中关键点坐标 - Coordinates of key points in the image
        //      1.  left_eye_x , left_eye_y
        //      2.  right_eye_x , right_eye_y
        //      3.  nose_x , nose_y
        //      4.  left_mouth_x , left_mouth_y
        //      5.  right_mouth_x , right_mouth_y
        double[][] pointsArray = new double[5][2]; // 保存人脸关键点 - Save facial key points
        int i = 0;
        for (Point point : points) {
            pointsArray[i][0] = point.getX();
            pointsArray[i][1] = point.getY();
            i++;
        }
        return pointsArray;
    }

    /**
     * 子图中人脸关键点坐标 - Coordinates of key points in the image
     *
     * @param subImageRect
     * @param points
     * @return
     */
    public static double[][] facePoints(Rectangle subImageRect, Iterable<Point> points) {
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
