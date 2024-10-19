package top.aias.sd.utils;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.util.Pair;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class PoseUtils {
    public static ArrayList<int[]> faceDetect(NDArray candidate, NDArray subset, NDArray oriImg) {
        ArrayList<int[]> detect_result = new ArrayList<>();
        float image_width = oriImg.getShape().get(1);
        float image_height = oriImg.getShape().get(0);
        subset = subset.toType(DataType.INT32, false);
        int num = (int) subset.getShape().get(0);
        for (int i = 0; i < num; i++) {
            NDArray personArray = subset.get(i);
            int[] person = personArray.toIntArray();
            int head = person[0];
            int left_eye = person[14];
            int right_eye = person[15];
            int left_ear = person[16];
            int right_ear = person[17];

            if (!(head > -1)) {
                continue;
            }
            boolean has_left_eye = false;
            if (left_eye > -1) {
                has_left_eye = true;
            }
            boolean has_right_eye = false;
            if (right_eye > -1) {
                has_right_eye = true;
            }
            boolean has_left_ear = false;
            if (left_ear > -1) {
                has_left_ear = true;
            }
            boolean has_right_ear = false;
            if (right_ear > -1) {
                has_right_ear = true;
            }

            if (!(has_left_eye || has_right_eye || has_left_ear || has_right_ear)) {
                continue;
            }

            float width = 0.0f;

            float[] f = candidate.get(new NDIndex(head + ",:2")).toFloatArray();
            float x0 = f[0];
            float y0 = f[1];

            if (has_left_eye) {
                f = candidate.get(new NDIndex(left_eye + ",:2")).toFloatArray();
                float x1 = f[0];
                float y1 = f[1];
                float d = Math.max(Math.abs(x0 - x1), Math.abs(y0 - y1));
                width = Math.max(width, d * 3.0f);
            }

            if (has_right_eye) {
                f = candidate.get(new NDIndex(right_eye + ",:2")).toFloatArray();
                float x1 = f[0];
                float y1 = f[1];
                float d = Math.max(Math.abs(x0 - x1), Math.abs(y0 - y1));
                width = Math.max(width, d * 3.0f);
            }

            if (has_left_ear) {
                f = candidate.get(new NDIndex(left_ear + ",:2")).toFloatArray();
                float x1 = f[0];
                float y1 = f[1];
                float d = Math.max(Math.abs(x0 - x1), Math.abs(y0 - y1));
                width = Math.max(width, d * 1.5f);
            }

            if (has_right_ear) {
                f = candidate.get(new NDIndex(right_ear + ",:2")).toFloatArray();
                float x1 = f[0];
                float y1 = f[1];
                float d = Math.max(Math.abs(x0 - x1), Math.abs(y0 - y1));
                width = Math.max(width, d * 1.5f);
            }

            float x = x0;
            float y = y0;
            x -= width;
            y -= width;
            if (x < 0)
                x = 0;
            if (y < 0)
                y = 0;

            float width1 = width * 2;
            float width2 = width * 2;

            if ((x + width) > image_width) {
                width1 = image_width - x;
            }

            if ((y + width) > image_height) {
                width2 = image_height - y;
            }

            width = Math.min(width1, width2);

            if (width >= 20) {
                detect_result.add(new int[]{(int) x, (int) y, (int) width});
            }
        }

        return detect_result;
    }

    public static ArrayList<int[]> handDetect(NDArray candidate, NDArray subset, NDArray oriImg) {
        // right hand: wrist 4, elbow 3, shoulder 2
        // left hand: wrist 7, elbow 6, shoulder 5
        float ratioWristElbow = 0.33f;
        ArrayList<int[]> detect_result = new ArrayList<>();
        float image_width = oriImg.getShape().get(1);
        float image_height = oriImg.getShape().get(0);
        subset = subset.toType(DataType.INT32, false);
        int num = (int) subset.getShape().get(0);
        for (int i = 0; i < num; i++) {
            NDArray personArray = subset.get(i);
            int[] person = personArray.toIntArray();
            boolean has_left = true;
            boolean has_right = true;
            if (person[5] == -1 || person[6] == -1 || person[7] == -1) {
                has_left = false;
            }
            if (person[2] == -1 || person[3] == -1 || person[4] == -1) {
                has_right = false;
            }
            if (!(has_left || has_right)) {
                continue;
            }

            ArrayList<float[]> hands = new ArrayList<>();
            if (has_left) {
                int left_shoulder_index = person[5];
                int left_elbow_index = person[6];
                int left_wrist_index = person[7];
                float[] f = candidate.get(new NDIndex(left_shoulder_index + ",:2")).toFloatArray();
                float x1 = f[0];
                float y1 = f[1];
                f = candidate.get(new NDIndex(left_elbow_index + ",:2")).toFloatArray();
                float x2 = f[0];
                float y2 = f[1];
                f = candidate.get(new NDIndex(left_wrist_index + ",:2")).toFloatArray();
                float x3 = f[0];
                float y3 = f[1];
                hands.add(new float[]{x1, y1, x2, y2, x3, y3, 1});
            }

            if (has_right) {
                int right_shoulder_index = person[2];
                int right_elbow_index = person[3];
                int right_wrist_index = person[4];
                float[] f = candidate.get(new NDIndex(right_shoulder_index + ",:2")).toFloatArray();
                float x1 = f[0];
                float y1 = f[1];
                f = candidate.get(new NDIndex(right_elbow_index + ",:2")).toFloatArray();
                float x2 = f[0];
                float y2 = f[1];
                f = candidate.get(new NDIndex(right_wrist_index + ",:2")).toFloatArray();
                float x3 = f[0];
                float y3 = f[1];
                hands.add(new float[]{x1, y1, x2, y2, x3, y3, 0});
            }

            for (int index = 0; index < hands.size(); index++) {
                float[] f = hands.get(index);
                float x1 = f[0];
                float y1 = f[1];
                float x2 = f[2];
                float y2 = f[3];
                float x3 = f[4];
                float y3 = f[5];
                float is_left = f[6];

                float x = x3 + ratioWristElbow * (x3 - x2);
                float y = y3 + ratioWristElbow * (y3 - y2);
                float distanceWristElbow = (float) Math.sqrt(Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
                float distanceElbowShoulder = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
                float width = 1.5f * Math.max(distanceWristElbow, 0.9f * distanceElbowShoulder);
                x -= width / 2;
                y -= width / 2;  // width = height
                // overflow the image
                if (x < 0) x = 0;
                if (y < 0) y = 0;
                float width1 = width;
                float width2 = width;
                if (x + width > image_width) {
                    width1 = image_width - x;
                }
                if (y + width > image_height) {
                    width2 = image_height - y;
                }
                width = Math.min(width1, width2);
                // the max hand box value is 20 pixels
                if (width >= 20) {
                    detect_result.add(new int[]{(int) x, (int) y, (int) width, (int) is_left});
                }
            }
        }
        return detect_result;
    }

    public static NDArray draw_facepose(NDManager manager, NDArray canvas, ArrayList<ArrayList<float[]>> all_lmks) {
        long H = canvas.getShape().get(0);
        long W = canvas.getShape().get(1);
        long C = canvas.getShape().get(2);
        float eps = 0.01f;

        Image image = ImageFactory.getInstance().fromNDArray(canvas);
        org.opencv.core.Mat mat = (org.opencv.core.Mat) image.getWrappedImage();


        for (int i = 0; i < all_lmks.size(); i++) {
            ArrayList<float[]> lmk = all_lmks.get(i);
            for (int index = 0; index < lmk.size(); index++) {
                float[] point = lmk.get(index);

                float x = point[0];
                float y = point[1];

                x = (int) (x * W);
                y = (int) (y * H);

                if (x > eps && y > eps) {
                    Imgproc.circle(mat, new Point(x, y), 3, new Scalar(255, 255, 255), -1);
                }
            }
        }
        canvas = image.toNDArray(manager);
        return canvas;
    }

    public static NDArray draw_handpose(NDManager manager, NDArray canvas, ArrayList<ArrayList<float[]>> all_hand_peaks) {
        long H = canvas.getShape().get(0);
        long W = canvas.getShape().get(1);
        long C = canvas.getShape().get(2);
        float eps = 0.01f;

        Image image = ImageFactory.getInstance().fromNDArray(canvas);
        org.opencv.core.Mat mat = (org.opencv.core.Mat) image.getWrappedImage();

        int[][] edges = new int[][]{{0, 1}, {1, 2}, {2, 3}, {3, 4}, {0, 5}, {5, 6}, {6, 7}, {7, 8}, {0, 9}, {9, 10},
                {10, 11}, {11, 12}, {0, 13}, {13, 14}, {14, 15}, {15, 16}, {0, 17}, {17, 18}, {18, 19}, {19, 20}};

        for (int i = 0; i < all_hand_peaks.size(); i++) {
            ArrayList<float[]> peaks = all_hand_peaks.get(i);
            for (int index = 0; index < edges.length; index++) {
                int[] e = edges[index];
                float[] point1 = peaks.get(e[0]);
                float x1 = point1[0];
                float y1 = point1[1];
                float[] point2 = peaks.get(e[1]);
                float x2 = point2[0];
                float y2 = point2[1];
                x1 = (int) (x1 * W);
                y1 = (int) (y1 * H);
                x2 = (int) (x2 * W);
                y2 = (int) (y2 * H);

                if (x1 > eps && y1 > eps && x2 > eps && y2 > eps) {
                    Point pointStart = new Point(x1, y1);
                    Point pointEnd = new Point(x2, y2);
                    int[] rgb = hsvToRgb(index / (float) edges.length, 1.0f, 1.0f);
                    Imgproc.line(mat, pointStart, pointEnd, new Scalar(rgb[2], rgb[1], rgb[0]), 2);

                }
            }

            for (int index = 0; index < peaks.size(); index++) {
                float[] point = peaks.get(index);
                float x = point[0];
                float y = point[1];
                x = (int) (x * W);
                y = (int) (y * H);
                if (x > eps && y > eps) {
                    Imgproc.circle(mat, new Point(x, y), 4, new Scalar(255, 0, 0), -1);
                }

            }

        }

        canvas = image.toNDArray(manager);
        return canvas;
    }

    public static NDArray draw_bodypose(NDManager manager, NDArray oriImg, NDArray canvas, NDArray candidate, NDArray subset) {
        float oriImgW = oriImg.getShape().get(1);
        float oriImgH = oriImg.getShape().get(0);

        candidate = candidate.get(new NDIndex(":, :2"));
        NDArray newCandidate = candidate.duplicate();
        newCandidate.get(new NDIndex(":, 0")).divi(oriImgW);
        newCandidate.get(new NDIndex(":, 1")).divi(oriImgH);

        long H = canvas.getShape().get(0);
        long W = canvas.getShape().get(1);
        long C = canvas.getShape().get(2);

        Image image = ImageFactory.getInstance().fromNDArray(canvas);
        org.opencv.core.Mat mat = (org.opencv.core.Mat) image.getWrappedImage();

        int stickwidth = 4;

        int[][] limbSeq = new int[][]{{2, 3}, {2, 6}, {3, 4}, {4, 5}, {6, 7}, {7, 8}, {2, 9}, {9, 10},
                {10, 11}, {2, 12}, {12, 13}, {13, 14}, {2, 1}, {1, 15}, {15, 17},
                {1, 16}, {16, 18}, {3, 17}, {6, 18}};

        double[][] colors = new double[][]{{255, 0, 0}, {255, 85, 0}, {255, 170, 0}, {255, 255, 0}, {170, 255, 0}, {85, 255, 0}, {0, 255, 0},
                {0, 255, 85}, {0, 255, 170}, {0, 255, 255}, {0, 170, 255}, {0, 85, 255}, {0, 0, 255}, {85, 0, 255},
                {170, 0, 255}, {255, 0, 255}, {255, 0, 170}, {255, 0, 85}};

        for (int i = 0; i < 17; i++) {
            for (int n = 0; n < subset.getShape().get(0); n++) {
                int[] limb = limbSeq[i];
                int index0 = (int) subset.get(n, limb[0] - 1).toFloatArray()[0];
                int index1 = (int) subset.get(n, limb[1] - 1).toFloatArray()[0];

                if (index0 == -1 || index1 == -1) {
                    continue;
                }

                float Y0 = newCandidate.get(index0, 0).toFloatArray()[0] * (float) W;
                float Y1 = newCandidate.get(index1, 0).toFloatArray()[0] * (float) W;
                float X0 = newCandidate.get(index0, 1).toFloatArray()[0] * (float) H;
                float X1 = newCandidate.get(index1, 1).toFloatArray()[0] * (float) H;

                float mX = (X0 + X1) / 2;
                float mY = (Y0 + Y1) / 2;

                float length = (float) Math.sqrt(Math.pow(X0 - X1, 2) + Math.pow(Y0 - Y1, 2));

                int angle = (int) Math.toDegrees(Math.atan2(X0 - X1, Y0 - Y1));

                Point point = new Point((int) mY, (int) mX);
                Size size = new Size((int) (length / 2), stickwidth);
                MatOfPoint matOfPoint = new MatOfPoint();
                Imgproc.ellipse2Poly(point, size, angle, 0, 360, 1, matOfPoint);
                double[] color = colors[i];
                Imgproc.fillConvexPoly(mat, matOfPoint, new Scalar(color[2], color[1], color[0]));
            }
        }

        canvas = image.toNDArray(manager);
        canvas = canvas.mul(0.6f).toType(DataType.UINT8, false);
        image = ImageFactory.getInstance().fromNDArray(canvas);
        mat = (org.opencv.core.Mat) image.getWrappedImage();
        for (int i = 0; i < 18; i++) {
            for (int n = 0; n < subset.getShape().get(0); n++) {
                int index = (int) subset.get(n, i).toFloatArray()[0];
                if (index == -1) {
                    continue;
                }
                int x = (int) (newCandidate.get(index, 0).toFloatArray()[0] * W);
                int y = (int) (newCandidate.get(index, 1).toFloatArray()[0] * H);
                double[] color = colors[i];
                Imgproc.circle(mat, new Point(x, y), 4, new Scalar(color[2], color[1], color[0]), -1);
            }

        }

        canvas = image.toNDArray(manager);
        return canvas;
    }

    public static int[] hsvToRgb(float hue, float saturation, float value) {
        int r, g, b;
        int i = (int) (hue * 6);
        float f = hue * 6 - i;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);
        switch (i % 6) {
            case 0:
                r = (int) (value * 255);
                g = (int) (t * 255);
                b = (int) (p * 255);
                break;
            case 1:
                r = (int) (q * 255);
                g = (int) (value * 255);
                b = (int) (p * 255);
                break;
            case 2:
                r = (int) (p * 255);
                g = (int) (value * 255);
                b = (int) (t * 255);
                break;
            case 3:
                r = (int) (p * 255);
                g = (int) (q * 255);
                b = (int) (value * 255);
                break;
            case 4:
                r = (int) (t * 255);
                g = (int) (p * 255);
                b = (int) (value * 255);
                break;
            default:
                r = (int) (value * 255);
                g = (int) (p * 255);
                b = (int) (q * 255);
                break;
        }
        return new int[]{r, g, b};
    }

    public static Pair<int[][], Integer> label(int[][] binary) {
        int height = binary.length;
        int width = binary[0].length;

        int[][] labels = new int[height][width];
        int label_numbers = 1;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (binary[i][j] == 0) {
                    continue;
                }
                if (j > 0 && binary[i][j - 1] == binary[i][j]) {
                    labels[i][j] = labels[i][j - 1];
                } else if (i > 0 && j > 0 && binary[i - 1][j - 1] == binary[i][j]) {
                    labels[i][j] = labels[i - 1][j - 1];
                } else if (i > 0 && binary[i - 1][j] == binary[i][j]) {
                    labels[i][j] = labels[i - 1][j];
                } else if (i > 0 && j < width - 1 && binary[i - 1][j + 1] == binary[i][j]) {
                    labels[i][j] = labels[i - 1][j + 1];
                } else {
                    labels[i][j] = label_numbers;
                    label_numbers++;
                }
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (binary[i][j] == 0) {
                    labels[i][j] = 0;
                }
            }
        }

        Pair<int[][], Integer> pair = new Pair<>(labels, label_numbers);
        return pair;
    }

    public static float[] npmax(NDArray map_ori) {
        NDArray arrayindex = map_ori.argMax(1);
        NDArray arrayvalue = map_ori.max(new int[]{1});
        int i = (int) arrayvalue.argMax().toLongArray()[0]; // y
        int j = (int) arrayindex.get(i).toLongArray()[0]; // x

        return new float[]{j, i};
    }

    public static NDArray padRightDownCorner(NDManager manager, NDArray imageToTest, int[] pad, int stride, int padValue) {
        int h = (int) imageToTest.getShape().get(0);
        int w = (int) imageToTest.getShape().get(1);

        pad[0] = 0; // up
        pad[1] = 0; // left
        if ((h % stride) == 0) {
            pad[2] = 0; // down
        } else {
            pad[2] = stride - (h % stride);
        }
        if ((w % stride) == 0) {
            pad[3] = 0; // right
        } else {
            pad[3] = stride - (w % stride);
        }

        NDArray img_padded = manager.zeros(new Shape(imageToTest.getShape().get(0) + pad[2], imageToTest.getShape().get(1) + pad[3], imageToTest.getShape().get(2)));
        img_padded = img_padded.add(padValue);
        img_padded.set(new NDIndex(":" + imageToTest.getShape().get(0) + ",:" + imageToTest.getShape().get(1) + ",:3"), imageToTest);

        return img_padded;
    }

    public static NDArray smart_resize(NDArray x, int Ht, int Wt) {
        int Ho = 0, Wo = 0, Co = 0;
        if (x.getShape().dimension() == 2) {
            Ho = (int) x.getShape().get(0);
            Ho = (int) x.getShape().get(1);
            Co = 1;
        } else {
            Ho = (int) x.getShape().get(0);
            Ho = (int) x.getShape().get(1);
            Co = (int) x.getShape().get(2);
        }

        if (Co == 3 || Co == 1) {
            float k = (float) (Ht + Wt) / (float) (Ho + Wo);
            // cv2.resize(x, (int(Wt), int(Ht)), interpolation=cv2.INTER_AREA if k < 1 else cv2.INTER_LANCZOS4)
            NDArray ndArray = NDImageUtils.resize(x, Wt, Ht, Image.Interpolation.AREA);
            return ndArray;
        } else {
            return null;
        }
    }

    public static int[] resizeImage(double h, double w, double resolution) {
        double k = resolution / Math.min(h, w);
        h *= k;
        w *= k;

        int height = (int) (Math.round(h / 64.0)) * 64;
        int width = (int) (Math.round(w / 64.0)) * 64;

        return new int[]{height, width};
    }
}
