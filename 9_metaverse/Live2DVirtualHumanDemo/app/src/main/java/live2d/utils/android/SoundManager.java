package live2d.utils.android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
	static private SoundPool soundPool;
	static private Context context;
	static private Map<String,Integer> soundList;

	static public void init(Context c)
	{
		context=c;

		soundPool = new SoundPool( 50, AudioManager.STREAM_MUSIC, 0 );
		soundList=new HashMap<String ,Integer>();
	}


	static public void load(String path)
	{
		if(soundList.containsKey(path))return;

		try {
			AssetFileDescriptor assetFileDescritorArticle = context.getAssets().openFd( path );
			int soundID=soundPool.load(assetFileDescritorArticle, 0);
			soundList.put(path, soundID);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	static public void play(String name)
	{
		if(!soundList.containsKey(name))return;

//		soundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
		soundPool.play(soundList.get(name),1f,1f,1,0,1);
	}

	static public void release()
	{
		soundList.clear();
		soundPool.release();
	}
}
