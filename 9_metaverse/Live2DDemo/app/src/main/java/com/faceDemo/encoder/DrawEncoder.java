package com.faceDemo.encoder;

import android.graphics.Canvas;

/**
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public abstract class DrawEncoder {
    abstract void setFrameConfiguration(final int width, final int height);

    abstract void draw(final Canvas canvas);

    abstract void processResults(Object object);
}
