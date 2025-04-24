package top.aias.cv.ex1;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageShowNDArray {
    private static final Logger logger = LoggerFactory.getLogger(ImageShowNDArray.class);

    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {
        // 读取图像
        Mat img = Imgcodecs.imread("src/test/resources/girl.png");

        // 将 OpenCV 的 Mat 转换为 ND4J 的 INDArray
        int rows = img.rows();
        int cols = img.cols();
        int channels = img.channels();

        // 创建一个 ND4J 数组，存储图像数据
        INDArray imgArray = Nd4j.create(rows, cols, channels);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double[] pixel = img.get(i, j);
                imgArray.putScalar(new int[]{i, j, 0}, pixel[0]); // B 通道
                imgArray.putScalar(new int[]{i, j, 1}, pixel[1]); // G 通道
                imgArray.putScalar(new int[]{i, j, 2}, pixel[2]); // R 通道
            }
        }

        // 提取单独的通道
        INDArray bChannel = imgArray.get(NDArrayIndex.all(), NDArrayIndex.all(), NDArrayIndex.indices(0)); // B 通道
        INDArray gChannel = imgArray.get(NDArrayIndex.all(), NDArrayIndex.all(), NDArrayIndex.indices(1)); // G 通道
        INDArray rChannel = imgArray.get(NDArrayIndex.all(), NDArrayIndex.all(), NDArrayIndex.indices(2)); // R 通道

        // 将 ND4J 的通道数组转换回 OpenCV 的 Mat
        Mat bMat = new Mat(rows, cols, CvType.CV_8UC1);
        Mat gMat = new Mat(rows, cols, CvType.CV_8UC1);
        Mat rMat = new Mat(rows, cols, CvType.CV_8UC1);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                bMat.put(i, j, bChannel.getDouble(i, j));
                gMat.put(i, j, gChannel.getDouble(i, j));
                rMat.put(i, j, rChannel.getDouble(i, j));
            }
        }

        // 显示图像
        HighGui.imshow("Original Image", img);
        HighGui.imshow("B Channel", bMat);
        HighGui.imshow("G Channel", gMat);
        HighGui.imshow("R Channel", rMat);

        // 等待用户操作关闭窗口
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
