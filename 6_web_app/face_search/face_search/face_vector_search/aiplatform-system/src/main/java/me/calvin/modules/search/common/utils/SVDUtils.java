package me.calvin.modules.search.common.utils;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;

/**
 * 仿射变换处理工具
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class SVDUtils {
    // 计算全局标准差
    // Calculate global standard deviation
    public static NDArray point112X96(NDManager manager) {
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

    public static NDArray point112x112(NDManager manager) {
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

    // 计算仿射变换矩阵
    // Calculate affine transformation matrix
    public static NDArray transformationFromPoints(
            NDManager manager, NDArray points1, NDArray points2) {
        // 按列计算均值
        // Calculate column-wise mean
        NDArray c1 = points1.mean(new int[] {0}); // axis=0 列操作 - axis=0 column operation
        NDArray c2 = points2.mean(new int[] {0}); // axis=0 列操作 - axis=0 column operation
        // 按列减去均值
        // Subtract column-wise mean
        points1 = points1.sub(c1);
        points2 = points2.sub(c2);

        // 计算全局标准差
        // Calculate global standard deviation
        double s1 = std(points1);
        double s2 = std(points2);

        // 矩阵除以全局标准差
        // Matrix divided by global standard deviation
        NDArray djl_s1 = manager.create(s1);
        NDArray djl_s2 = manager.create(s2);
        points1 = points1.div(djl_s1);
        points2 = points2.div(djl_s2);

        double[] points1D = points1.toDoubleArray();
        double[] points2D = points2.toDoubleArray();

        // DJL 格式转换成Jamma格式
        // Convert DJL format to Jama format
        double[][] m1 = new double[5][2];
        double[][] m2 = new double[5][2];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                m1[i][j] = points1D[i * 2 + j];
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                m2[i][j] = points2D[i * 2 + j];
            }
        }
        Matrix p1 = new Matrix(m1);
        Matrix p2 = new Matrix(m2);

        // 进行奇异值分解
        // Perform singular value decomposition
        Matrix p3 = p1.transpose().times(p2);
        SingularValueDecomposition s = p3.svd();

        Matrix U = s.getU();
        Matrix S = s.getS();
        Matrix V = s.getV();
        // TODO 为什么第2列的符号是反的？
        // Why is the sign of the second column opposite?
        m1 = U.getArray();
        m1[0][1] = -m1[0][1];
        m1[1][1] = -m1[1][1];
        m2 = V.getArray();
        m2[0][1] = -m2[0][1];
        m2[1][1] = -m2[1][1];

        Matrix R = (U.times(V)).transpose();

        double[][] rArray = R.getArray();
        NDArray newR = manager.create(rArray);

        // np.vstack([np.hstack(((s2 / s1) * R, c2.T - (s2 / s1) * R * c1.T)), np.matrix([0.,0., 1.])])
        // TODO NDArray 单行向量直接转置无效，用reshape代替
        // // TODO NDArray single-row vector transpose is invalid, use reshape instead
        // (s2 / s1) * R
        NDArray leftPart = djl_s2.div(djl_s1).mul(newR);
        // c2.T - (s2 / s1) * R * c1.T)
        NDArray rightPart = c2.reshape(2, 1).sub(leftPart.matMul(c1.reshape(2, 1)));
        // numpy.hstack(((s2 / s1) * R, c2.T - (s2 / s1) * R * c1.T))
        NDArray upPart = leftPart.concat(rightPart, 1);
        // np.matrix([0.,0., 1.])
        double[] downArray = {0d, 0d, 1d};
        NDArray downPart = manager.create(downArray).reshape(1, 3);

        NDArray all = upPart.concat(downPart, 0);
        //    System.out.println("all: " + all);

        return upPart;
    }

    // 计算全局标准差
    // Calculate global standard deviation
    public static double std(NDArray points) {
        points = points.square();
        double[] doubleResult = points.toDoubleArray();
        double std = 0;
        for (int i = 0; i < doubleResult.length; i++) {
            std = std + doubleResult[i];
        }
        std = (float) Math.sqrt(std / doubleResult.length);
        return std;
    }
}

