/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.utils.android;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class ImageClip {
	static FloatBuffer drawRectBuffer ;
	static ByteBuffer colorBuffer ;
	static float[] squareVertices = new float[8] ;


	
	public static void drawClippedRect( GL10 gl, float left , float right , float bottom , float top ,
			  float clipLeft , float clipRight , float clipBottom , float clipTop ,
			  int color ){

		drawRect( gl, left      , clipLeft , bottom , top , color) ;
		drawRect( gl, clipRight , right    , bottom , top , color) ;


		drawRect( gl, clipLeft , clipRight , bottom  , clipBottom , color) ;
		drawRect( gl, clipLeft , clipRight , clipTop , top        , color) ;
	}



	public static void drawRect( GL10 gl, float left , float right , float bottom , float top , int argb ){
		int alpha = (argb >> 24)&0xFF ;
		int red   = (argb >> 16)&0xFF ;
		int green = (argb >>  8)&0xFF ;
		int blue  = (argb      )&0xFF ;

		squareVertices[0] = left ;
		squareVertices[1] = top ;
		squareVertices[2] = right ;
		squareVertices[3] = top ;

		squareVertices[4] = left ;
		squareVertices[5] = bottom ;
		squareVertices[6] = right ;
		squareVertices[7] = bottom ;

		byte squareColors[] = {
					(byte) red,  (byte) green,  (byte) blue, (byte) alpha,
					(byte) red,  (byte) green,  (byte) blue, (byte) alpha,
					(byte) red,  (byte) green,  (byte) blue, (byte) alpha,
					(byte) red,  (byte) green,  (byte) blue, (byte) alpha,
			};

		drawRectBuffer = BufferUtil.setupFloatBuffer( drawRectBuffer , squareVertices) ;
		colorBuffer  = BufferUtil.setupByteBuffer( colorBuffer , squareColors) ;

		gl.glDisable( GL10.GL_TEXTURE_2D ) ;

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, drawRectBuffer );
		gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, colorBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
