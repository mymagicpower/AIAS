package top.aias.face.util;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * 人脸对齐
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class FaceAlignUtils {
    /**
     * 根据目标点，进行旋转仿射变换
     * Perform rotation and affine transformation based on the target 5 points
     *
     * @param src
     * @param rot_mat
     * @return
     */
    public static Mat warpAffine(Mat src, Mat rot_mat) {
        Mat rot = new Mat();
        // 进行仿射变换，变换后大小为src的大小
        // Perform affine transformation, the size after transformation is the same as the size of src
        Scalar scalar = new Scalar(135, 133, 132);
        Size size = new Size(512, 512);
        Imgproc.warpAffine(src, rot, rot_mat, size, 0, 0, scalar);
        return rot;
    }
    public static Mat warpAffine(Mat src, Mat rot_mat, int width, int height) {
        Mat rot = new Mat();
        Size size = new Size(width, height);
        Imgproc.warpAffine(src, rot, rot_mat, size);
        return rot;
    }


    public static Mat warpAffine(Mat src, Mat rot_mat, int width, int height, int flags) {
        Mat rot = new Mat();
        Size size = new Size(width, height);
        Imgproc.warpAffine(src, rot, rot_mat, size, flags);
        return rot;
    }
}
