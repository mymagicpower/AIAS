/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.sample;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import jp.live2d.android.Live2DModelAndroid;
import jp.live2d.motion.AMotion;
import jp.live2d.util.UtSystem;
import live2d.framework.L2DBaseModel;
import live2d.framework.L2DEyeBlink;
import live2d.framework.L2DStandardID;
import live2d.framework.Live2DFramework;
import live2d.utils.android.BufferUtil;
import live2d.utils.android.FileManager;
import live2d.utils.android.ModelSetting;
import live2d.utils.android.ModelSettingJson;
import live2d.utils.android.OffscreenImage;
import live2d.utils.android.SoundManager;


public class LAppModel extends L2DBaseModel
{
	
	public String 					TAG = "LAppModel ";

	
	private ModelSetting modelSetting = null;
	private String 					modelHomeDir;			

	
	static FloatBuffer 				debugBufferVer = null ;
	static FloatBuffer 				debugBufferColor = null ;

	static Object lock = new Object() ;

	public LAppModel()
	{
		super();

		if(LAppDefine.DEBUG_LOG)
		{
			debugMode=true;
//			mainMotionManager.setMotionDebugMode(true);
		}
	}


	public void release()
	{
		if(live2DModel==null)return;
		live2DModel.deleteTextures();
	}


	
	public void load(GL10 gl,String modelSettingPath) throws Exception
	{
		updating=true;
		initialized=false;

		modelHomeDir=modelSettingPath.substring(0,modelSettingPath.lastIndexOf("/") + 1);//live2d/model/xxx/
		PlatformManager pm=(PlatformManager) Live2DFramework.getPlatformManager();
		pm.setGL(gl);

		if(LAppDefine.DEBUG_LOG) Log.d(TAG, "json : "+modelSettingPath);

		try
		{
			InputStream in = FileManager.open(modelSettingPath);
			modelSetting = new ModelSettingJson(in);
			in.close() ;
		}
		catch (IOException e)
		{
			e.printStackTrace();

			
			throw new Exception();
		}

		if(modelSetting.getModelName()!=null)
		{
			TAG+=modelSetting.getModelName();
		}

		if(LAppDefine.DEBUG_LOG)Log.d(TAG, "Load model.");

		loadModelData(modelHomeDir+modelSetting.getModelFile());
		String[] texPaths=modelSetting.getTextureFiles();
		for (int i = 0; i < texPaths.length; i++) {
			loadTexture(i,modelHomeDir+texPaths[i]);
		}
		
		String[] expressionNames=modelSetting.getExpressionNames();
		String[] expressionPaths=modelSetting.getExpressionFiles();

		for (int i = 0; i < expressionPaths.length; i++) {
			loadExpression(expressionNames[i],modelHomeDir+ expressionPaths[i]);
		}

		
		loadPhysics( modelHomeDir+modelSetting.getPhysicsFile() );

		
		loadPose(modelHomeDir+modelSetting.getPoseFile());

		
		HashMap<String, Float> layout = new HashMap<String,Float>();
		if (modelSetting.getLayout(layout) )
		{
			if (layout.get("width")!=null)modelMatrix.setWidth(layout.get("width"));
			if (layout.get("height")!=null)modelMatrix.setHeight(layout.get("height"));
			if (layout.get("x")!=null)modelMatrix.setX(layout.get("x"));
			if (layout.get("y")!=null)modelMatrix.setY(layout.get("y"));
			if (layout.get("center_x")!=null)modelMatrix.centerX(layout.get("center_x"));
			if (layout.get("center_y")!=null)modelMatrix.centerY(layout.get("center_y"));
			if (layout.get("top")!=null)modelMatrix.top(layout.get("top"));
			if (layout.get("bottom")!=null)modelMatrix.bottom(layout.get("bottom"));
			if (layout.get("left")!=null)modelMatrix.left(layout.get("left"));
			if (layout.get("right")!=null)modelMatrix.right(layout.get("right"));
		}

		//Sound
		String[] soundPaths=modelSetting.getSoundPaths();
		for (int i = 0; i < soundPaths.length; i++) {
			String path = soundPaths[i];
			SoundManager.load(modelHomeDir+path);
		}

		
		for(int i=0; i<modelSetting.getInitParamNum() ;i++)
		{
			String id = modelSetting.getInitParamID(i);
			float value = modelSetting.getInitParamValue(i);
			live2DModel.setParamFloat(id, value);
		}

		for(int i=0; i<modelSetting.getInitPartsVisibleNum() ;i++)
		{
			String id = modelSetting.getInitPartsVisibleID(i);
			float value = modelSetting.getInitPartsVisibleValue(i);
			live2DModel.setPartsOpacity(id, value);
		}

		
		eyeBlink=new L2DEyeBlink();

		updating=false;
		initialized=true;
	}


	public void preloadMotionGroup( String name)
	{
		int len = modelSetting.getMotionNum(name);
		for (int i = 0; i < len; i++)
		{
			String fileName = modelSetting.getMotionFile(name, i);
			AMotion motion = loadMotion(fileName,modelHomeDir + fileName);
			motion.setFadeIn(modelSetting.getMotionFadeIn(name, i));
			motion.setFadeOut(modelSetting.getMotionFadeOut(name, i));
		}
	}


	public void update()
	{
		if(live2DModel == null)
		{
			if(LAppDefine.DEBUG_LOG)Log.d(TAG, "Failed to update.");
			return;
		}

		long timeMSec = UtSystem.getUserTimeMSec() - startTimeMSec  ;
		double timeSec = timeMSec / 1000.0 ;
		double t = timeSec * 2 * Math.PI  ;


		
		if(mainMotionManager.isFinished())
		{
			
			startRandomMotion(LAppDefine.MOTION_GROUP_IDLE, LAppDefine.PRIORITY_IDLE);
		}

		//-----------------------------------------------------------------
		live2DModel.loadParam();

		boolean update = mainMotionManager.updateParam(live2DModel);
		if( ! update)
		{
			
			eyeBlink.updateParam(live2DModel);
		}

		live2DModel.saveParam();
		//-----------------------------------------------------------------


		if(expressionManager!=null)expressionManager.updateParam(live2DModel);


		
		
		live2DModel.addToParamFloat( L2DStandardID.PARAM_ANGLE_X, dragX *  30 , 1 );
		live2DModel.addToParamFloat( L2DStandardID.PARAM_ANGLE_Y, dragY *  30 , 1 );
		live2DModel.addToParamFloat( L2DStandardID.PARAM_ANGLE_Z, (dragX*dragY) * -30 , 1 );

		
		live2DModel.addToParamFloat( L2DStandardID.PARAM_BODY_ANGLE_X    , dragX * 10 , 1 );

		
		live2DModel.addToParamFloat( L2DStandardID.PARAM_EYE_BALL_X, dragX  , 1 );
		live2DModel.addToParamFloat( L2DStandardID.PARAM_EYE_BALL_Y, dragY  , 1 );

		
		live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_X,	(float) (15 * Math.sin( t/ 6.5345 )) , 0.5f);
		live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_Y,	(float) ( 8 * Math.sin( t/ 3.5345 )) , 0.5f);
		live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_Z,	(float) (10 * Math.sin( t/ 5.5345 )) , 0.5f);
		live2DModel.addToParamFloat(L2DStandardID.PARAM_BODY_ANGLE_X,(float) ( 4 * Math.sin( t/15.5345 )) , 0.5f);
		live2DModel.setParamFloat(L2DStandardID.PARAM_BREATH,	(float) (0.5f + 0.5f * Math.sin( t/3.2345 )),1);

		
		live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_Z,	 90 * accelX  ,0.5f);

		if(physics!=null)physics.updateParam(live2DModel);

		
		if(lipSync)
		{
			live2DModel.setParamFloat(L2DStandardID.PARAM_MOUTH_OPEN_Y, lipSyncValue ,0.8f);
		}

		
		if(pose!=null)pose.updateParam(live2DModel);

		live2DModel.update();
	}


	
	private void drawHitArea(GL10 gl) {
		gl.glDisable( GL10.GL_TEXTURE_2D ) ;
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY) ;
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glPushMatrix() ;
		{
			gl.glMultMatrixf(modelMatrix.getArray(), 0) ;
			int len = modelSetting.getHitAreasNum();
			for (int i=0;i<len;i++)
			{
				String drawID=modelSetting.getHitAreaID(i);
				int drawIndex=live2DModel.getDrawDataIndex(drawID);
				if(drawIndex<0)continue;
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

				float[] vertex={left,top,right,top,right,bottom,left,bottom,left,top};
				float r=1;
				float g=0;
				float b=0;
				float a=0.5f;
				int size=5;
				float color[] = {r,g,b,a,r,g,b,a,r,g,b,a,r,g,b,a,r,g,b,a};


				gl.glLineWidth( size );	
				gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, BufferUtil.setupFloatBuffer( debugBufferVer,vertex));
				gl.glColorPointer( 4, GL10.GL_FLOAT, 0, BufferUtil.setupFloatBuffer( debugBufferColor,color ) );	
		    	gl.glDrawArrays( GL10.GL_LINE_STRIP, 0, 5 );	
			}
		}
		gl.glPopMatrix() ;
		gl.glEnable( GL10.GL_TEXTURE_2D ) ;
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY) ;
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}


	public void startRandomMotion(String name ,int priority)
	{
		int max=modelSetting.getMotionNum(name);
		int no=(int)(Math.random() * max);
		startMotion(name,no,priority);
	}


	
	public void startMotion(String name, int no,int priority)
	{
		String motionName=modelSetting.getMotionFile(name, no);

		if( motionName==null || motionName.equals(""))
		{
			if(LAppDefine.DEBUG_LOG){Log.d(TAG, "Failed to motion.");}
			return;//
		}

		AMotion motion;

		
		
		//
		
		
		if (priority == LAppDefine.PRIORITY_FORCE) {
			mainMotionManager.setReservePriority(priority);
		}
		else if( ! mainMotionManager.reserveMotion(priority))
		{
			if(LAppDefine.DEBUG_LOG){Log.d(TAG, "Failed to motion.");}
			return ;
		}

		String motionPath=modelHomeDir + motionName;
		motion = loadMotion(null,motionPath);

		if(motion==null)
		{
			Log.w(TAG, "Failed to load motion.");
			mainMotionManager.setReservePriority(0);
			return;
		}
		

		
		motion.setFadeIn(modelSetting.getMotionFadeIn(name, no));
		motion.setFadeOut(modelSetting.getMotionFadeOut(name, no));

		if(LAppDefine.DEBUG_LOG)Log.d(TAG,"Start motion : "+motionName);

		
		if( modelSetting.getMotionSound(name, no) == null)
		{
			mainMotionManager.startMotionPrio(motion,priority);
		}
		
		else
		{
			String soundName=modelSetting.getMotionSound(name, no);
			String soundPath=modelHomeDir + soundName;

			if(LAppDefine.DEBUG_LOG)Log.d(TAG,"sound : "+soundName);

			SoundManager.play(soundPath);
			mainMotionManager.startMotionPrio(motion,priority);
		}
	}


	
	public void setExpression(String name)
	{
		if( ! expressions.containsKey(name))return;
		if(LAppDefine.DEBUG_LOG)Log.d(TAG,"Expression : "+name);
		AMotion motion=expressions.get(name);
		expressionManager.startMotion(motion,false);
	}


	
	public void setRandomExpression()
	{
		int no=(int)(Math.random() * expressions.size());

		String[] keys = expressions.keySet().toArray(new String[expressions.size()]);

		setExpression(keys[no]);
	}


	public void draw(GL10 gl)
	{
		((Live2DModelAndroid) live2DModel).setGL(gl);

		alpha+=accAlpha;

		if (alpha<0)
		{
			alpha=0;
			accAlpha=0;
		}
		else if (alpha>1)
		{
			alpha=1;
			accAlpha=0;
		}

		if(alpha<0.001)return;

		if (alpha<0.999)
		{
			
			
			OffscreenImage.setOffscreen(gl);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			gl.glPushMatrix() ;
			{
				gl.glMultMatrixf( modelMatrix.getArray(), 0) ;
				live2DModel.draw();
			}
			gl.glPopMatrix() ;

			
			OffscreenImage.setOnscreen(gl);
			gl.glPushMatrix() ;
			{
				gl.glLoadIdentity();
				OffscreenImage.drawDisplay(gl,alpha);
			}
			gl.glPopMatrix() ;
		}
		else
		{
			
			gl.glPushMatrix() ;
			{
				gl.glMultMatrixf(modelMatrix.getArray(), 0) ;
				live2DModel.draw();
			}
			gl.glPopMatrix() ;

			if(LAppDefine.DEBUG_DRAW_HIT_AREA )
			{
				
				 drawHitArea(gl);
			}
		}
	}


	
	public boolean hitTest(String id,float testX,float testY)
	{
		if(alpha<1)return false;
		if(modelSetting==null)return false;
		int len=modelSetting.getHitAreasNum();
		for (int i = 0; i < len; i++)
		{
			if( id.equals(modelSetting.getHitAreaName(i)) )
			{
				return hitTestSimple(modelSetting.getHitAreaID(i),testX,testY) ;
			}
		}
		return false;
	}


	public void feedIn()
	{
		alpha=0;
		accAlpha=0.1f;
	}
}