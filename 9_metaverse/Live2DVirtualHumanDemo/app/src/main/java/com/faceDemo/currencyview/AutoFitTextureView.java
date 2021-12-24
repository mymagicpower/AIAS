package com.faceDemo.currencyview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

import com.faceDemo.activity.CameraActivity;

/**
 * Texture View
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public class AutoFitTextureView extends TextureView {
    private int ratioWidth = 0;
    private int ratioHeight = 0;

    public AutoFitTextureView(final Context context) {
        this(context, null);
    }

    public AutoFitTextureView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextureView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAspectRatio(final int width, final int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        ratioWidth = width;
        ratioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = getResources().getDisplayMetrics().widthPixels;
        final int height = getResources().getDisplayMetrics().heightPixels;

        if (0 == ratioWidth || 0 == ratioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * ratioWidth / ratioHeight) {
                CameraActivity.ScreenWidth = width;
                CameraActivity.ScreenHeight = width * ratioHeight / ratioWidth;
                setMeasuredDimension(width, width * ratioHeight / ratioWidth);
            } else {
                CameraActivity.ScreenWidth = height * ratioWidth / ratioHeight;
                CameraActivity.ScreenHeight = height;
                setMeasuredDimension(height * ratioWidth / ratioHeight, height);
            }
        }
    }
}
