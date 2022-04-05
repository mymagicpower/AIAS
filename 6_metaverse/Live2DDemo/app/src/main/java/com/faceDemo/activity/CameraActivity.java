package com.faceDemo.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.faceDemo.R;
import com.faceDemo.currencyview.LegacyCameraConnectionFragment;
import com.faceDemo.live2d.FaceDetector;
import com.faceDemo.live2d.Live2dGLSurfaceView;
import com.faceDemo.utils.MyLogger;
import com.faceDemo.utils.SensorEventUtil;
import com.tenginekit.AndroidConfig;
import com.tenginekit.KitCore;

/**
 * Camera Acticity
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public abstract class CameraActivity extends AppCompatActivity implements
        Camera.PreviewCallback {
    private static final String TAG = "CameraActicity";

    // 照相机预览宽
    protected int previewWidth = 0;
    // 照相机预览高
    protected int previewHeight = 0;
    // 展示区域宽
    public static float ScreenWidth;
    // 展示区域高
    public static float ScreenHeight;

    public static int CameraId = 0;

    private boolean isProcessingFrame = false;
    // 是否是前置摄像头
    public static boolean is_front_camera = true;

    private Handler handler;
    private HandlerThread handlerThread;
    protected SensorEventUtil sensorEventUtil;

    // 相机的数据 nv21格式
    protected byte[] mNV21Bytes;

    private Runnable postInferenceCallback;
    private Runnable imageConverter;

    // Live2D
    public FaceDetector mFaceDetector;
    public Live2dGLSurfaceView mGLSurfaceView;
    public final double[] emotion = new double[10];

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);
        setFragment();

        mFaceDetector = new FaceDetector(this);

        final String MODEL_PATH = "live2d/haru/haru.moc";
        final String[] TEXTURE_PATHS = {
                "live2d/haru/haru.1024/texture_00.png",
                "live2d/haru/haru.1024/texture_01.png",
                "live2d/haru/haru.1024/texture_02.png"
        };
        RelativeLayout container = (RelativeLayout) findViewById(R.id.relativeContainer);
        mGLSurfaceView = (Live2dGLSurfaceView) findViewById(R.id.live2d_preview);
//        mGLSurfaceView = new Live2dGLSurfaceView(CameraActivity.this);
        mGLSurfaceView.init(CameraActivity.this, MODEL_PATH, TEXTURE_PATHS, 1, 1);
//        container.addView(mGLSurfaceView);
    }

    public void Init() {
        mNV21Bytes = new byte[previewHeight * previewWidth];

        /**
         * 初始化
         * */
        KitCore.init(
                CameraActivity.this,
                AndroidConfig
                        .create()
                        .setCameraMode()
//                        .openFunc(AndroidConfig.Func.Detect)
                        .setDefaultFunc()
//                        .openFunc(AndroidConfig.Func.BlazeFace)//.openFunc(AndroidConfig.Func.Landmark)
                        .setDefaultInputImageFormat()
                        .setInputImageSize(previewWidth, previewHeight)
                        .setOutputImageSize((int) ScreenWidth, (int) ScreenHeight)
        );
        if (sensorEventUtil == null) {
            sensorEventUtil = new SensorEventUtil(this);
        }
    }

    /**
     * Callback for android.hardware.Camera API
     */
    @Override
    public void onPreviewFrame(final byte[] bytes, final Camera camera) {
        if (isProcessingFrame) {
            return;
        }
        isProcessingFrame = true;
        try {
            if (mNV21Bytes == null) {
                Camera.Size previewSize = camera.getParameters().getPreviewSize();
                previewHeight = previewSize.height;
                previewWidth = previewSize.width;
                Init();
                onPreviewSizeChosen(new Size(previewSize.width, previewSize.height));
            }
        } catch (final Exception e) {
            MyLogger.logError(TAG, "onPreviewFrame: " + e);
            return;
        }
        imageConverter = new Runnable() {
            @Override
            public void run() {
                mNV21Bytes = bytes;
            }
        };
        postInferenceCallback = new Runnable() {
            @Override
            public void run() {
                camera.addCallbackBuffer(bytes);
                isProcessingFrame = false;
            }
        };
        processImage();
    }

    @Override
    public synchronized void onStart() {
        super.onStart();
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        handlerThread = new HandlerThread("inference");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        processImage();
    }

    @Override
    public synchronized void onPause() {
        handlerThread.quitSafely();
        try {
            handlerThread.join();
            handlerThread = null;
            handler = null;
        } catch (final InterruptedException e) {
            MyLogger.logError(TAG, "onPause: " + e);
        }
        super.onPause();
    }

    @Override
    public synchronized void onStop() {
        super.onStop();
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        /**
         * 释放
         * */
        KitCore.release();
    }


    protected void setFragment() {
        LegacyCameraConnectionFragment fragment = new LegacyCameraConnectionFragment(this, getLayoutId(), getDesiredPreviewFrameSize());
        CameraId = fragment.getCameraId();
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    protected void readyForNextImage() {
        if (postInferenceCallback != null) {
            postInferenceCallback.run();
        }
    }

    protected synchronized void runInBackground(final Runnable r) {
        if (handler != null) {
            handler.post(r);
        }
    }

    protected abstract void processImage();

    protected abstract void onPreviewSizeChosen(final Size size);

    protected abstract int getLayoutId();

    protected abstract Size getDesiredPreviewFrameSize();

    //得到最新的bytes
    protected void getCameraBytes() {
        if (imageConverter != null) {
            imageConverter.run();
        }
    }

}
