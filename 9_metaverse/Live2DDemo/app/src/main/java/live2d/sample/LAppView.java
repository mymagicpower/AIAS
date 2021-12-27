/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.sample;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import live2d.framework.L2DMatrix44;
import live2d.framework.L2DTargetPoint;
import live2d.framework.L2DViewMatrix;
import live2d.utils.android.AccelHelper;
import live2d.utils.android.TouchManager;

public class LAppView extends GLSurfaceView {
	
	static public final String 		TAG = "LAppView";

	
	private LAppRenderer 				renderer ;

	
	private LAppLive2DManager 			delegate;
	private L2DMatrix44 deviceToScreen;
	private L2DViewMatrix viewMatrix;
	private AccelHelper accelHelper;
	private TouchManager touchMgr;
	private L2DTargetPoint dragMgr;

	GestureDetector 					gestureDetector;

	public LAppView(  Context context )
	{
		super( context ) ;
		setFocusable(true);
	}


	public void setLive2DManager( LAppLive2DManager live2DMgr)
	{
		this.delegate = live2DMgr ;
		this.renderer = new LAppRenderer( live2DMgr  ) ;

		setRenderer(renderer);

		gestureDetector = new GestureDetector(this.getContext()  , simpleOnGestureListener ) ;


		
		deviceToScreen=new L2DMatrix44();

		
		viewMatrix=new L2DViewMatrix();

		
		viewMatrix.setMaxScale( LAppDefine.VIEW_MAX_SCALE );
		viewMatrix.setMinScale( LAppDefine.VIEW_MIN_SCALE );


		
		viewMatrix.setMaxScreenRect(
				LAppDefine.VIEW_LOGICAL_MAX_LEFT,
				LAppDefine.VIEW_LOGICAL_MAX_RIGHT,
				LAppDefine.VIEW_LOGICAL_MAX_BOTTOM,
				LAppDefine.VIEW_LOGICAL_MAX_TOP
				);

		
		touchMgr=new TouchManager();

		dragMgr  = new L2DTargetPoint();
	}


	public void startAccel(Activity activity)
	{
		
		accelHelper = new AccelHelper(activity) ;
	}


	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
	    boolean ret = false ;
	    int touchNum;
	    switch (event.getAction())
	    {
	    case MotionEvent.ACTION_DOWN:
	        ret = true ;

			
			touchNum = event.getPointerCount() ;

			if( touchNum == 1 )
			{
				touchesBegan(event.getX(),event.getY());
			}
			else if( touchNum == 2 )
			{
				touchesBegan(event.getX(0),event.getY(0),event.getX(1),event.getY(1));
			}
			else
			{
				
			}

	        break;
	    case MotionEvent.ACTION_UP:
	    	touchesEnded();
	        break;
	    case MotionEvent.ACTION_MOVE:
	    	
			touchNum = event.getPointerCount() ;

			if( touchNum == 1 )
			{
				touchesMoved(event.getX(),event.getY());
			}
			else if( touchNum == 2 )
			{
				touchesMoved(event.getX(0),event.getY(0),event.getX(1),event.getY(1));
			}
			else
			{
				
			}
	        break;
	    case MotionEvent.ACTION_CANCEL:
	        break;
	    }
        ret |= gestureDetector.onTouchEvent(event) ;

        return ret ;
	}


	
	public void onResume()
	{
		if(accelHelper!=null)
		{
			if(LAppDefine.DEBUG_LOG)Log.d(TAG, "start accelHelper");
			accelHelper.start();
		}
	}


	
	public void onPause()
	{
		if(accelHelper!=null)
		{
			if(LAppDefine.DEBUG_LOG)Log.d(TAG, "stop accelHelper");
			accelHelper.stop();
		}
	}


	public void setupView( int width, int height)
	{
		float ratio=(float)height/width;
		float left = LAppDefine.VIEW_LOGICAL_LEFT;
		float right = LAppDefine.VIEW_LOGICAL_RIGHT;
		float bottom = -ratio;
		float top = ratio;

		viewMatrix.setScreenRect(left,right,bottom,top);

		float screenW=Math.abs(left-right);
		deviceToScreen.identity() ;
		deviceToScreen.multTranslate(-width/2.0f,height/2.0f );
		deviceToScreen.multScale( screenW/width , screenW/width );
	}


	public void update()
	{
		dragMgr.update();
		delegate.setDrag(dragMgr.getX(), dragMgr.getY());

		accelHelper.update();

		if( accelHelper.getShake() > 1.5f )
		{
			if(LAppDefine.DEBUG_LOG)Log.d(TAG, "shake event");
			
			delegate.shakeEvent() ;
			accelHelper.resetShake() ;
		}

		delegate.setAccel(accelHelper.getAccelX(), accelHelper.getAccelY(), accelHelper.getAccelZ());
		renderer.setAccel(accelHelper.getAccelX(), accelHelper.getAccelY(), accelHelper.getAccelZ());
	}


	
	public void updateViewMatrix(float dx, float dy, float cx, float cy,float scale,boolean enableEvent)
	{
		boolean isMaxScale=viewMatrix.isMaxScale();
		boolean isMinScale=viewMatrix.isMinScale();

		
		viewMatrix.adjustScale(cx, cy, scale);

		
		viewMatrix.adjustTranslate(dx, dy) ;

		if(enableEvent)
		{
			
			if( ! isMaxScale)
			{
				if(viewMatrix.isMaxScale())
				{
					delegate.maxScaleEvent();
				}
			}
			
			if( ! isMinScale)
			{
				if(viewMatrix.isMinScale())
				{
					delegate.minScaleEvent();
				}
			}
		}
	}


	private float transformDeviceToViewX(float deviceX)
	{
		float screenX = deviceToScreen.transformX( deviceX );
		return  viewMatrix.invertTransformX(screenX);
	}


	private float transformDeviceToViewY(float deviceY)
	{
		float screenY = deviceToScreen.transformY( deviceY );
		return  viewMatrix.invertTransformY(screenY);
	}


	
	public void touchesBegan(float p1x,float p1y)
	{
		if(LAppDefine.DEBUG_TOUCH_LOG)Log.v(TAG, "touchesBegan"+" x:"+p1x+" y:"+p1y);
		touchMgr.touchBegan(p1x,p1y);

		float x=transformDeviceToViewX( touchMgr.getX() );
		float y=transformDeviceToViewY( touchMgr.getY() );

		dragMgr.set(x, y);
	}


	public void touchesBegan(float p1x,float p1y,float p2x,float p2y)
	{
		if(LAppDefine.DEBUG_TOUCH_LOG)Log.v(TAG, "touchesBegan"+" x1:"+p1x+" y1:"+p1y+" x2:"+p2x+" y2:"+p2y);
		touchMgr.touchBegan(p1x,p1y,p2x,p2y);

		float x=transformDeviceToViewX( touchMgr.getX() );
		float y=transformDeviceToViewY( touchMgr.getY() );

		dragMgr.set(x, y);
	}


	
	public void touchesMoved(float p1x,float p1y)
	{
		if(LAppDefine.DEBUG_TOUCH_LOG)Log.v(TAG, "touchesMoved"+"x:"+p1x+" y:"+p1y);
		touchMgr.touchesMoved(p1x,p1y);
		float x=transformDeviceToViewX( touchMgr.getX() );
		float y=transformDeviceToViewY( touchMgr.getY() );

		dragMgr.set(x, y);

		final int FLICK_DISTANCE=100;

		

		if(touchMgr.isSingleTouch() && touchMgr.isFlickAvailable() )
		{
			float flickDist=touchMgr.getFlickDistance();
			if(flickDist>FLICK_DISTANCE)
			{

				float startX=transformDeviceToViewX( touchMgr.getStartX() );
				float startY=transformDeviceToViewY( touchMgr.getStartY() );
				delegate.flickEvent(startX,startY);
				touchMgr.disableFlick();
			}
		}
	}


	public void touchesMoved(float p1x,float p1y,float p2x,float p2y)
	{
		if(LAppDefine.DEBUG_TOUCH_LOG)Log.v(TAG, "touchesMoved"+" x1:"+p1x+" y1:"+p1y+" x2:"+p2x+" y2:"+p2y);
		touchMgr.touchesMoved(p1x,p1y,p2x,p2y);

		
		float dx= touchMgr.getDeltaX() * deviceToScreen.getScaleX();
		float dy= touchMgr.getDeltaY() * deviceToScreen.getScaleY() ;
		float cx= deviceToScreen.transformX( touchMgr.getCenterX() ) * touchMgr.getScale();
		float cy= deviceToScreen.transformY( touchMgr.getCenterY() ) * touchMgr.getScale();
		float scale=touchMgr.getScale();

		if(LAppDefine.DEBUG_TOUCH_LOG)Log.v(TAG, "view  dx:"+dx+" dy:"+dy+" cx:"+cx+" cy:"+cy+" scale:"+scale);

		updateViewMatrix(dx,dy,cx,cy,scale,true);

		float x=transformDeviceToViewX( touchMgr.getX() );
		float y=transformDeviceToViewY( touchMgr.getY() );

		dragMgr.set(x, y);
	}


	
	public void touchesEnded()
	{
		if(LAppDefine.DEBUG_TOUCH_LOG)Log.v(TAG, "touchesEnded");
		dragMgr.set(0,0);
	}


	public L2DViewMatrix getViewMatrix()
	{
		return viewMatrix;
	}


	
	private final SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener()
	{
        @Override
        public boolean onDoubleTap(MotionEvent event)
        {
        	return super.onDoubleTap(event) ;
        }

        @Override
        public boolean onDown(MotionEvent event)
        {
            super.onDown(event);
            return true ;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event)
        {
        	float x=transformDeviceToViewX( touchMgr.getX() );
    		float y=transformDeviceToViewY( touchMgr.getY() );
          	boolean ret = delegate.tapEvent(x,y);//Live2D Event
          	ret |= super.onSingleTapUp(event);
            return ret ;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event)
        {
            return super.onSingleTapUp(event) ;
        }
    };

}

