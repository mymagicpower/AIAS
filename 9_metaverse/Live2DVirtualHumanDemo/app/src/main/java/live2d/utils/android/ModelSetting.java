/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.utils.android;

import java.util.Map;


public interface ModelSetting {

	
	String getModelName()		 ;
	String getModelFile()		 ;

	
	int getTextureNum()			 ;
	String getTextureFile(int n) ;
	String[] getTextureFiles() ;

	
	int getHitAreasNum()		;
	String getHitAreaID(int n)	;
	String getHitAreaName(int n);

	
	String getPhysicsFile()		;
	String getPoseFile()		;
	int getExpressionNum();
	String getExpressionFile(int n)	;
	String[] getExpressionFiles()	;
	String getExpressionName(int n)	;
	String[] getExpressionNames()	;

	
	String[] getMotionGroupNames();
	int getMotionNum(String name);

	String getMotionFile(String name, int n)		;
	String getMotionSound(String name, int n)	;
	int getMotionFadeIn(String name, int n)		;
	int getMotionFadeOut(String name, int n)		;


	boolean getLayout(Map<String, Float> layout);

	
	int getInitParamNum();
	float getInitParamValue(int n);
	String getInitParamID(int n);

	
	int getInitPartsVisibleNum();
	float getInitPartsVisibleValue(int n);
	String getInitPartsVisibleID(int n);

	String[] getSoundPaths() ;
}
