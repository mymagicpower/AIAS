/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.framework;

import java.util.HashMap;
import java.util.Map;

import jp.live2d.ALive2DModel;
import jp.live2d.Live2D;
import jp.live2d.motion.AMotion;
import jp.live2d.motion.Live2DMotion;
import jp.live2d.motion.MotionQueueManager;

public class L2DBaseModel {
	
	protected ALive2DModel		 		live2DModel=null;			
	protected L2DModelMatrix 			modelMatrix=null;			

	
	protected Map<String,AMotion> 	expressions ;			
	protected Map<String,AMotion> 	motions ;		

	protected L2DMotionManager 		mainMotionManager;		
	protected L2DMotionManager 		expressionManager;		
	protected L2DEyeBlink 			eyeBlink;				
	protected L2DPhysics 			physics;				
	protected L2DPose 				pose;					

	protected boolean				debugMode=false;
	protected boolean 				initialized = false;	
	protected boolean 				updating = false;		
	protected float 				alpha = 1;				
	protected float 				accAlpha = 0;			
	protected boolean 				lipSync = false;		
	protected float 				lipSyncValue;			

	
	protected float 				accelX=0;
	protected float 				accelY=0;
	protected float 				accelZ=0;

	
	protected float 				dragX=0;
	protected float 				dragY=0;

	protected long 					startTimeMSec;


	public L2DBaseModel()
	{
		
		mainMotionManager=new L2DMotionManager();
		expressionManager=new L2DMotionManager();

		motions=new HashMap< String , AMotion >();
		expressions=new HashMap< String , AMotion >();
	}


	public L2DModelMatrix getModelMatrix()
	{
		return modelMatrix;
	}


	public void setAlpha(float a)
	{
		if(a>0.999)a=1;
		if(a<0.001)a=0;
		alpha=a;
	}


	public float getAlpha()
	{
		return alpha;
	}


	
	public boolean isInitialized() {
		return initialized;
	}


	public void setInitialized(boolean v)
	{
		initialized=v;
	}


	
	public boolean isUpdating() {
		return updating;
	}


	public void setUpdating(boolean v)
	{
		updating=v;
	}


	
	public ALive2DModel getLive2DModel() {
		return live2DModel;
	}


	public void setLipSync(boolean v)
	{
		lipSync=v;
	}


	public void setLipSyncValue(float v)
	{
		lipSyncValue=v;
	}


	public void setAccel(float x,float y,float z)
	{
		accelX=x;
		accelY=y;
		accelZ=z;
	}


	public void setDrag(float x,float y)
	{
		dragX=x;
		dragY=y;
	}


	public MotionQueueManager getMainMotionManager()
	{
		return mainMotionManager;
	}


	public MotionQueueManager getExpressionManager()
	{
		return expressionManager;
	}

	public void loadModelData(String path)
	{
		if(live2DModel!=null)
		{
			live2DModel.deleteTextures();
		}
		IPlatformManager pm=Live2DFramework.getPlatformManager();


		if(debugMode)pm.log( "Load model : "+path);

		live2DModel = pm.loadLive2DModel(path);
		live2DModel.saveParam();

		if(Live2D.getError()!=Live2D.L2D_NO_ERROR)
		{
			
			pm.log("Error : Failed to loadModelData().");
			return;
		}

		modelMatrix=new L2DModelMatrix(live2DModel.getCanvasWidth(),live2DModel.getCanvasHeight());
		modelMatrix.setWidth(2);
		modelMatrix.setCenterPosition(0, 0);
	}


	public void loadTexture(int no,String path)
	{
		IPlatformManager pm=Live2DFramework.getPlatformManager();
		if(debugMode)pm.log( "Load Texture : "+path);

		pm.loadTexture(live2DModel, no, path);
	}

	public AMotion loadMotion(String name,String path)
	{
		IPlatformManager pm=Live2DFramework.getPlatformManager();
		if(debugMode)pm.log("Load Motion : "+path);


		Live2DMotion motion=null;


		byte[] buf = pm.loadBytes(path) ;
		motion=Live2DMotion.loadMotion(buf);

		if ( name !=null)
		{
			motions.put(name, motion);
		}

		return motion;
	}

	public void loadExpression(String name,String path)
	{
		IPlatformManager pm=Live2DFramework.getPlatformManager();
		if(debugMode)pm.log("Load Expression : "+path);

		try {
			expressions.put(name, L2DExpressionMotion.loadJson(pm.loadBytes(path)) );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void loadPose( String path )
	{
		IPlatformManager pm=Live2DFramework.getPlatformManager();
		if(debugMode) pm.log("Load Pose : "+path);
		try
		{
			pose = L2DPose.load(pm.loadBytes(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void loadPhysics( String path )
	{
		IPlatformManager pm=Live2DFramework.getPlatformManager();
		if(debugMode) pm.log("Load Physics : "+path);
		try
		{
			physics = L2DPhysics.load(pm.loadBytes(path));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hitTestSimple(String drawID,float testX,float testY)
	{
		if(alpha<1)return false;

		int drawIndex=live2DModel.getDrawDataIndex(drawID);
		if(drawIndex<0)return false;
		float[] points=live2DModel.getTransformedPoints(drawIndex);

		float left=live2DModel.getCanvasWidth();
		float right=0;
		float top=live2DModel.getCanvasHeight();
		float bottom=0;

		for (int j = 0; j < points.length; j=j+2)
		{
			float x = points[j];
			float y = points[j+1];
			if(x<left)left=x;	
			if(x>right)right=x;	
			if(y<top)top=y;		
			if(y>bottom)bottom=y;
		}

		float tx=modelMatrix.invertTransformX(testX);
		float ty=modelMatrix.invertTransformY(testY);

		return ( left <= tx && tx <= right && top <= ty && ty <= bottom ) ;
	}
}
