/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.framework;

import jp.live2d.ALive2DModel;

public interface IPlatformManager {
	public byte[] loadBytes(String path);
	public String loadString(String path);
	public ALive2DModel loadLive2DModel(String path);
	public void loadTexture(ALive2DModel model, int no, String path);
	public void log(String txt);
}
