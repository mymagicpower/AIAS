/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.utils.android;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import jp.live2d.util.UtDebug;
import live2d.sample.LAppDefine;

public class LoadUtil {

	static final int GEN_TEX_LOOP = 999 ;

	public static int loadTexture(GL10 gl, InputStream in , boolean mipmap ) throws IOException
	{
		Bitmap bitmap = BitmapFactory.decodeStream( in );
		int texture ;

		if( mipmap )
		{
			texture = buildMipmap(gl, bitmap) ;
		}
		else
		{
			texture = genTexture(gl) ;
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture) ;

	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_WRAP_T,GL10.GL_CLAMP_TO_EDGE);
	        gl.glTexEnvf(GL10.GL_TEXTURE_ENV,GL10.GL_TEXTURE_ENV_MODE,GL10.GL_MODULATE);

			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0) ;
			bitmap.recycle();
		}

		return texture;
	}


	
	public static int genTexture(GL10 gl)
	{
		int texture = 0 ;
		int i = 0 ;

		for( ; i< GEN_TEX_LOOP ; i++ )
		{
			int [] ret = {0} ;
			gl.glGenTextures(1, ret  , 0 );
			texture = ret[0] ;

			if( texture < 0 )
			{
				gl.glDeleteTextures(1, ret, 0) ;
			}
			else
			{
				break ;
			}
		}
		if( i == GEN_TEX_LOOP )
		{
			UtDebug.error( "gen texture loops over " + GEN_TEX_LOOP + "times @UtOpenGL" ) ;
			texture = 0 ;
		}

		return texture ;
	}


	public static int buildMipmap(GL10 gl, Bitmap bitmap)
	{
		return buildMipmap( gl, bitmap , true ) ;
	}


	/**
	 * Mipmap texture
	 *
	 */
	public static int buildMipmap(GL10 gl, Bitmap srcBitmap , boolean recycle )
	{
		Bitmap bitmap = srcBitmap ;
		int level = 0;
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		int textureID = genTexture(gl);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);

		try
		{
			
			((GL11)gl).glTexParameteri(GL10.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL10.GL_TRUE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_LINEAR);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_WRAP_T,GL10.GL_CLAMP_TO_EDGE);
        gl.glTexEnvf(GL10.GL_TEXTURE_ENV,GL10.GL_TEXTURE_ENV_MODE,GL10.GL_MODULATE);

		while (height >= 1 && width >= 1)
		{
			//---- note ----
			// First of all, generate the texture from our bitmap and set it to
			// the according level
			
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);

			if (height == 1 || width == 1)
			{
				if( recycle || bitmap != srcBitmap ) bitmap.recycle() ;
				break;
			}

			level++;

			height /= 2;
			width /= 2;

			Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true );

			// Clean up
			if( recycle || bitmap != srcBitmap ) bitmap.recycle();
			bitmap = bitmap2;
		}

		return textureID;
	}


	
	static public MediaPlayer loadAssetsSound(String filename)
	{
		if(LAppDefine.DEBUG_LOG)Log.d("", "Load sound : "+filename);

		final MediaPlayer player = new MediaPlayer() ;

		try
		{
			final AssetFileDescriptor assetFileDescritorArticle = FileManager.openFd( filename );
			player.reset();

			player.setDataSource( assetFileDescritorArticle.getFileDescriptor(),
					assetFileDescritorArticle.getStartOffset(), assetFileDescritorArticle.getLength() );
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			assetFileDescritorArticle.close();

		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return player;
	}



}
