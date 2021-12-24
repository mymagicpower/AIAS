/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.framework;

import jp.live2d.util.UtSystem;

public class L2DTargetPoint {
	static public final int FRAME_RATE=30;

	private float faceTargetX = 0 ;
	private float faceTargetY = 0 ;

	private float faceX = 0 ;
	private float faceY = 0 ;

	private float faceVX = 0 ;
	private float faceVY = 0 ;

	private long lastTimeSec = 0 ;


	public void set( float x , float y  )
	{
		faceTargetX = x ;
		faceTargetY = y ;
	}


	
	public float getX()
	{
		return faceX;
	}


	
	public float getY()
	{
		return faceY;
	}


	
	public void update()
	{
		
		final float TIME_TO_MAX_SPEED = 0.15f ;
		final float FACE_PARAM_MAX_V = 40.0f / 7.5f ;

		final float MAX_V =  FACE_PARAM_MAX_V / FRAME_RATE ;

		if( lastTimeSec == 0 )
		{
			lastTimeSec = UtSystem.getUserTimeMSec() ;
			return ;
		}

		long curTimeSec = UtSystem.getUserTimeMSec() ;

		float deltaTimeWeight = (float)(curTimeSec - lastTimeSec)*FRAME_RATE/1000.0f ;
		lastTimeSec = curTimeSec ;

		final float FRAME_TO_MAX_SPEED = TIME_TO_MAX_SPEED * FRAME_RATE  ;//sec*frame/sec
		final float MAX_A = deltaTimeWeight * MAX_V / FRAME_TO_MAX_SPEED ;

		float dx = (faceTargetX - faceX) ;
		float dy = (faceTargetY - faceY) ;

		if( dx == 0 && dy == 0 ) return ;
		float d = (float) Math.sqrt( dx*dx + dy*dy ) ;

		float vx = MAX_V * dx / d ;
		float vy = MAX_V * dy / d ;

		float ax = vx - faceVX ;
		float ay = vy - faceVY ;

		float a = (float) Math.sqrt( ax*ax + ay*ay ) ;

		if( a < -MAX_A || a > MAX_A )
		{
			ax *= MAX_A / a ;
			ay *= MAX_A / a ;
			a = MAX_A ;
		}

		faceVX += ax ;
		faceVY += ay ;

		{
			//            2  6           2               3
			//      sqrt(a  t  + 16 a h t  - 8 a h) - a t
			// v = --------------------------------------
			//                    2
			//                 4 t  - 2
			//(t=1)

			float max_v = 0.5f * ( (float)Math.sqrt( MAX_A*MAX_A + 16*MAX_A * d - 8*MAX_A * d ) - MAX_A ) ;
			float cur_v = (float) Math.sqrt( faceVX*faceVX + faceVY*faceVY ) ;

			if( cur_v > max_v )
			{
				faceVX *= max_v / cur_v ;
				faceVY *= max_v / cur_v ;
			}
		}

		faceX += faceVX ;
		faceY += faceVY ;
	}
}
