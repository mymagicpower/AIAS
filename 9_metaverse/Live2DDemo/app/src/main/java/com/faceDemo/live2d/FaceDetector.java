package com.faceDemo.live2d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.faceDemo.activity.CameraActivity;
import com.tenginekit.face.Face;
import com.tenginekit.face.FaceDetectInfo;
import com.tenginekit.face.FaceLandmarkInfo;
import com.tenginekit.model.TenginekitPoint;

import java.util.ArrayList;
import java.util.List;
/**
 * 人脸检测
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public class FaceDetector {
    private String TAG = "FaceDetector";

    private CameraActivity mActivity;
    private Paint mLandmarkPaint;

    private final double r_m = 0.4, r_n = 0.5;

    public FaceDetector(CameraActivity activity) {
        Log.d(TAG, "constructed.");
        this.mActivity = activity;

        mLandmarkPaint = new Paint();
        mLandmarkPaint.setColor(Color.GREEN);
        mLandmarkPaint.setStrokeWidth(2);
        mLandmarkPaint.setStyle(Paint.Style.STROKE);

    }

    public void getLandmarks(byte[] data, Bitmap bmp) {
        synchronized (this) {
            //获取人脸信息
            Face.FaceDetect faceDetect = Face.detect(data);
            List<FaceDetectInfo> faceDetectInfos = new ArrayList<>();
            List<FaceLandmarkInfo> landmarkInfos = new ArrayList<>();
            if (faceDetect.getFaceCount() > 0) {
                faceDetectInfos = faceDetect.getDetectInfos();
                landmarkInfos = faceDetect.landmark2d();
            }else {
                Log.d(TAG, "no face found.");
                return;
            }
            FaceLandmarkInfo ret = landmarkInfos.get(0);

            //draw landmarks
            List<TenginekitPoint> landmarks = ret.landmarks;
            Canvas canvas = new Canvas(bmp);
            for (TenginekitPoint point : landmarks) {
                canvas.drawCircle(point.X, point.Y, 1, mLandmarkPaint);
            }
            if(landmarks!=null && landmarks.size()>0){
//                facePose(landmarks);
                this.mActivity.emotion[0] = ret.yaw;
                this.mActivity.emotion[1] = ret.pitch;
                this.mActivity.emotion[2] = ret.roll;

                mouthEmotion(landmarks);
            }
        }
    }

    public void facePose(List<TenginekitPoint> landmarks) {
        Point leftEye, rightEye, noseTip, mouthLeft, mouthRight;
        //37 102
        leftEye = new Point((int)landmarks.get(101).X,(int)landmarks.get(101).Y);//下标+1
        //46 118
        rightEye = new Point((int)landmarks.get(117).X,(int)landmarks.get(117).Y);//下标+1
        //31 178
        noseTip = new Point((int)landmarks.get(177).X,(int)landmarks.get(177).Y);//下标+1
        //49 181
        mouthLeft = new Point((int)landmarks.get(180).X,(int)landmarks.get(180).Y);//下标+1
        //55 189
        mouthRight = new Point((int)landmarks.get(188).X,(int)landmarks.get(188).Y);//下标+1

        Point noseBase = new Point((leftEye.x+rightEye.x)/2, (leftEye.y+rightEye.y)/2);
        Point mouth = new Point((mouthLeft.x+mouthRight.x)/2, (mouthLeft.y+mouthRight.y)/2);

        Point n = new Point((int)(mouth.x + (noseBase.x - mouth.x)*r_m),
                (int)(mouth.y + (noseBase.y - mouth.y)*r_m));

        double theta = Math.acos( (double)((noseBase.x-n.x)*(noseTip.x-n.x) + (noseBase.y-n.y)*(noseTip.y-n.y)) /
        Math.hypot(noseTip.x-n.x, noseTip.y-n.y) / Math.hypot(noseBase.x-n.x, noseBase.y-n.y));
        double tau = Math.atan2( (double)(n.y-noseTip.y), (double)(n.x-noseTip.x) );

        double m1 = (double)((noseTip.x-n.x)*(noseTip.x-n.x) + (noseTip.y-n.y)*(noseTip.y-n.y)) /
                        ((noseBase.x-mouth.x)*(noseBase.x-mouth.x) + (noseBase.y-mouth.y)*(noseBase.y-mouth.y)),
                m2 = Math.cos(theta)*Math.cos(theta);
        double a = r_n*r_n*(1-m2);
        double b = m1 - r_n*r_n + 2*m2*r_n*r_n;
        double c = - m2*r_n*r_n;

        double delta = Math.acos(Math.sqrt( (Math.sqrt(b*b-4*a*c) - b)/(2*a) ));

        //fn: facial normal, sfn: standard(no rotation) facial normal
        double[] fn = new double[3], sfn = new double[3];
        fn[0] = Math.sin(delta)*Math.cos(tau);
        fn[1] = Math.sin(delta)*Math.sin(tau);
        fn[2] = - Math.cos(delta);

        double alpha = Math.PI / 12;
        sfn[0] = 0;
        sfn[1] = Math.sin(alpha);
        sfn[2] = - Math.cos(alpha);

        //PITCH:X YAW:Y ROLL:X
        //Log.d(TAG, "facial normal: " + fn[0] + " " + fn[1] + " " + fn[2]);
        //Log.d(TAG, "standard facial normal: " + sfn[0] + " " + sfn[1] + " " + sfn[2]);

        /*
        live2d rotation order: ZXY
        live2d coordinate           my coordinate           my coordinate
        angle Z                     z axis                  Yaw
        angle X                     y axis                  Pitch
        angle Y                     x axis                  Roll

        my coordinate is same as the paper:
        Estimating Gaze from a Single View of a Face
        link: ieeexplore.ieee.org/document/576433/
         */

        // (w, x, y, z) is Euler quaternion
        double w, x, y, z;
        double angle = Math.acos((sfn[0]*fn[0] + sfn[1]*fn[1] + sfn[2]*fn[2]) /
                Math.sqrt(sfn[0]*sfn[0] + sfn[1]*sfn[1] + sfn[2]*sfn[2]) /
                Math.sqrt(fn[0]*fn[0] + fn[1]*fn[1] + fn[2]*fn[2]));
        w = Math.cos(0.5*angle);
        x = sfn[1]*fn[2] - sfn[2]*fn[1];
        y = sfn[2]*fn[0] - sfn[0]*fn[2];
        z = sfn[0]*fn[1] - sfn[1]*fn[0];

        double l = Math.sqrt(x*x + y*y + z*z);
        x = Math.sin(0.5*angle)*x/l;
        y = Math.sin(0.5*angle)*y/l;
        z = Math.sin(0.5*angle)*z/l;

        //Log.d(TAG, "Angle: " + w);
        
        double yaw, pitch, roll;
        roll = Math.atan2(2*(w*x+y*z), 1-2*(x*x+y*y));
        pitch = Math.asin(2*(w*y-z*x));
        yaw = Math.atan2(2*(w*z+x*y), 1-2*(y*y+z*z));

//        if(yaw < Math.PI / 18) {
        if(sfn[0] < 0.1 && sfn[1] < 0.1) {
            roll = 1.5*Math.atan2(rightEye.y-leftEye.y, rightEye.x-leftEye.x);
        }
        yaw = Math.max(-30, Math.min(30, yaw*180/Math.PI));
        pitch = Math.max(-30, Math.min(30, pitch*180/Math.PI));
        roll = Math.max(-30, Math.min(30, roll*180/Math.PI));

        Log.d(TAG, "Yaw: " + yaw + " Pitch: " + pitch + " Roll: " + roll);

        this.mActivity.emotion[0] = yaw;
        this.mActivity.emotion[1] = pitch;
        this.mActivity.emotion[2] = roll;
    }

    public void mouthEmotion(List<TenginekitPoint> landmarks) {
        //mouth open
        //52 185
        Point mouthUp = new Point((int)landmarks.get(184).X,(int)landmarks.get(184).Y);//下标+1
        //58 201
        Point mouthDown = new Point((int)landmarks.get(200).X,(int)landmarks.get(200).Y);//下标+1
        //65 205
        Point mouthInnerRight = new Point((int)landmarks.get(204).X,(int)landmarks.get(204).Y);//下标+1
        //61 212
        Point mouthInnerLeft = new Point((int)landmarks.get(211).X,(int)landmarks.get(211).Y);//下标+1

        this.mActivity.emotion[3] = (double)(mouthDown.y-mouthUp.y) /
                (mouthInnerRight.x-mouthInnerLeft.x) - 0.2;
    }

}
