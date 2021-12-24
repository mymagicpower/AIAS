/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.utils.android;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;


public class BufferUtil {

	
	public static FloatBuffer createFloatBuffer( int floatCount ) {
		ByteBuffer data = ByteBuffer.allocateDirect( floatCount * 4);
	    data.order(ByteOrder.nativeOrder());
		FloatBuffer p1 = data.asFloatBuffer() ;
		return p1;
	}


	
	public static FloatBuffer setupFloatBuffer( FloatBuffer preBuffer , float []array){
		
		if( preBuffer == null || preBuffer.capacity() < array.length ){
			preBuffer = createFloatBuffer( array.length * 2 ) ;
		}
		else{
			preBuffer.clear() ;
		}
		preBuffer.put(array) ;
		preBuffer.position(0) ;
		return preBuffer ;
	}


	
	public static ShortBuffer createShortBuffer( int shortCount ) {
		ByteBuffer data = ByteBuffer.allocateDirect( shortCount * 4);
	    data.order(ByteOrder.nativeOrder());
	    ShortBuffer p1 = data.asShortBuffer() ;
		return p1;
	}


	
	public static ShortBuffer setupShortBuffer( ShortBuffer preBuffer , short[] array ) {
		
		if( preBuffer == null || preBuffer.capacity() < array.length ){
			preBuffer = createShortBuffer( array.length * 2 ) ;
		}
		else{
			preBuffer.clear() ;
		}

		preBuffer.clear() ;
		preBuffer.put(array) ;
		preBuffer.position(0) ;

		return preBuffer ;
	}


	
	public static ByteBuffer createByteBuffer( int count ) {
		ByteBuffer data = ByteBuffer.allocateDirect( count * 4);
	    data.order(ByteOrder.nativeOrder());
		return data ;
	}


	public static ByteBuffer setupByteBuffer( ByteBuffer preBuffer , byte []array){
		
		if( preBuffer == null || preBuffer.capacity() < array.length ){
			preBuffer = createByteBuffer( array.length * 2 ) ;
		}
		else{
			preBuffer.clear() ;
		}
		preBuffer.put(array) ;
		preBuffer.position(0) ;
		return preBuffer ;
	}


	public static Buffer setupIntBuffer(IntBuffer preBuffer,int []array){
			
			if( preBuffer == null || preBuffer.capacity() < array.length ){
				preBuffer = createIntBuffer( array.length * 2 ) ;
			}
			else{
				preBuffer.clear() ;
			}

			preBuffer.clear() ;
			preBuffer.put(array) ;
			preBuffer.position(0) ;

			return preBuffer ;
	}


	public static IntBuffer createIntBuffer(int count) {
		ByteBuffer data = ByteBuffer.allocateDirect( count * 4);
	    data.order(ByteOrder.nativeOrder());
		IntBuffer p1 = data.asIntBuffer() ;
		return p1;
	}
}
