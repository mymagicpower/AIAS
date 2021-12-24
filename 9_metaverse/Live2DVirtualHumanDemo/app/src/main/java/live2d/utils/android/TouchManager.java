/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.utils.android;




public class TouchManager {

	static boolean SCALING_ENABLED = true ;

	private float startY ;
	private float startX ;

	private float lastX=0 ;
	private float lastY=0 ;
	private float lastX1=0 ;
	private float lastY1=0 ;
	private float lastX2=0 ;
	private float lastY2=0 ;

	private float lastTouchDistance = -1 ;

	private float moveX;
	private float moveY;

	private float scale;

	private boolean touchSingle ;
	private boolean flipAvailable ;

	
	public void touchBegan(float x1, float y1, float x2, float y2)
	{
		float dist=distance( x1,  y1,  x2,  y2);
		float centerX = (lastX1 + lastX2) * 0.5f ;
		float centerY = (-lastY1 -lastY2) * 0.5f ;

		lastX = centerX ;
		lastY = centerY ;
		startX=centerX;
		startY=centerY;
		lastTouchDistance = dist ;
		flipAvailable = true ;
		touchSingle = false ;
	}


	
	public void touchBegan(float x, float y)
	{
		lastX = x ;
		lastY = -y ;
		startX=x;
		startY=-y;
		lastTouchDistance = -1 ;
		flipAvailable = true ;
		touchSingle = true;
	}


	
	public void touchesMoved(float x, float y)
	{
		lastX = x ;
		lastY = -y ;
		lastTouchDistance = -1 ;
		touchSingle =true;
	}


	
	public void touchesMoved(float x1, float y1, float x2, float y2)
	{
		float dist = distance(x1, y1, x2, y2);
		float centerX = (x1 + x2) * 0.5f ;
		float centerY = (-y1 + -y2) * 0.5f ;

		if( lastTouchDistance > 0 )
		{
			scale = (float) Math.pow( dist / lastTouchDistance , 0.75 ) ;
			moveX = calcShift( x1 - lastX1 , x2 - lastX2 ) ;
			moveY = calcShift( -y1 - lastY1 , -y2 - lastY2 ) ;
		}
		else
		{
			scale =1;
			moveX=0;
			moveY=0;
		}

		lastX = centerX ;
		lastY = centerY ;
		lastX1 = x1 ;
		lastY1 = -y1 ;
		lastX2 = x2 ;
		lastY2 = -y2 ;
		lastTouchDistance = dist ;
		touchSingle =false;
	}


	public float getCenterX()
	{
		return lastX ;
	}


	public float getCenterY()
	{
		return lastY ;
	}


	public float getDeltaX()
	{
		return moveX;
	}


	public float getDeltaY()
	{
		return moveY;
	}


	public float getStartX()
	{
		return startX;
	}


	public float getStartY()
	{
		return startY;
	}


	public float getScale()
	{
		return scale;
	}


	public float getX()
	{
		return lastX;
	}


	public float getY()
	{
		return lastY;
	}


	public float getX1()
	{
		return lastX1;
	}


	public float getY1()
	{
		return lastY1;
	}


	public float getX2()
	{
		return lastX2;
	}


	public float getY2()
	{
		return lastY2;
	}


	
	private float distance(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt( (x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2) ) ;
	}


	
	private float calcShift( float v1 , float v2 )
	{
		if( (v1>0) != (v2>0) ) return 0 ;

		float fugou = v1 > 0 ? 1 : -1 ;
		float a1 = Math.abs( v1 ) ;
		float a2 = Math.abs( v2 ) ;
		return fugou * ( ( a1 < a2 ) ? a1 : a2 ) ;
	}


	
	public float getFlickDistance()
	{
		return distance(startX, startY, lastX, lastY);
	}


	public boolean isSingleTouch()
	{
		return touchSingle;
	}


	public boolean isFlickAvailable()
	{
		return flipAvailable;
	}


	public void disableFlick()
	{
		flipAvailable=false;

	}
}
