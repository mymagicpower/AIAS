/**
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.utils.android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileManager {
	static Context context ;


	public static void init( Context c ){
		context = c ;
	}


	public static boolean exists_resource( String path ){
		try {
			InputStream afd = context.getAssets().open(path) ;
			afd.close() ;
			return true ;
		} catch (IOException e) {
			return false ;
		}
	}


	public static InputStream open_resource( String path ) throws IOException{
		return context.getAssets().open(path) ;
	}


	public static boolean exists_cache( String path ){
		File f = new File( context.getCacheDir() , path ) ;
		return f.exists() ;
	}


	public static InputStream open_cache( String path ) throws FileNotFoundException{
		File f = new File( context.getCacheDir() , path ) ;
		return new FileInputStream(f) ;
	}


	
	public static InputStream open( String path , boolean isCache ) throws IOException{
		if( isCache ){
			return open_cache(path) ;
		}
		else{
			return open_resource(path) ;
		}

	}


	public static InputStream open( String path  ) throws IOException{
		return open(path,false);
	}


	public static AssetFileDescriptor openFd( String path ) throws IOException
	{
		return context.getAssets().openFd(path);
	}
}
