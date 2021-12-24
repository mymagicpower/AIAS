/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package live2d.framework;

public class L2DMatrix44 {
	protected float tr[] = new float[16] ;


	public L2DMatrix44()
	{
		identity() ;
	}


	public void identity()
	{
		for (int i = 0 ; i < 16 ; i++ )	tr[i] = ((i%5) == 0) ? 1 : 0 ;
	}


	
	public float[] getArray()
	{
		return tr				;
	}


	
	public float[] getCopyMatrix()
	{
		return tr.clone();	
	}


	
	public void setMatrix( float tr[] )
	{
		
		if( tr == null || this.tr.length != tr.length ) return ;
		for (int i = 0 ; i < 16 ; i++ )	this.tr[i] = tr[i] ;
	}


	public float getScaleX()
	{
		return tr[0] ;
	}


	public float getScaleY()
	{
		return tr[5] ;
	}


	
	public float transformX( float src )
	{
		return tr[0]*src + tr[12] ;
	}


	
	public float transformY( float src )
	{
		return tr[5]*src + tr[13] ;
	}


	
	public float invertTransformX( float src )
	{
		return (src - tr[12]) / tr[0] ;
	}


	
	public float invertTransformY( float src )
	{
		return (src - tr[13]) / tr[5] ;
	}


	
	protected static void mul( float[] a , float []b , float[] dst)
	{
		float c[] = {0,0,0,0 , 0,0,0,0 , 0,0,0,0 , 0,0,0,0 } ;
		int n = 4 ;
		int i,j,k;

		for(i=0;i<n;i++) {
			for(j=0;j<n;j++) {
				for(k=0;k<n;k++) {
					c[i+j*4]+=a[i+k*4]*b[k+j*4];
				}
			}
		}

		for(i =0 ; i < 16 ; i++ ){
			dst[i] = c[i] ;
		}
	}


	
	public void multTranslate( float shiftX, float shiftY )
	{
		float tr1[] = { 1,0,0,0 , 0,1,0,0 , 0,0,1,0 , shiftX,shiftY,0,1 } ;
		mul( tr1 , tr , tr ) ;
	}


	public void translate( float x, float y )
	{
		tr[12]=x;
		tr[13]=y;
	}


	public void translateX( float x )
	{
		tr[12]=x;
	}


	public void translateY( float y )
	{
		tr[13]=y;
	}


	
	public void multScale(  float scaleX,float scaleY )
	{
		float tr1[] = { scaleX,0,0,0 , 0,scaleY,0,0 , 0,0,1,0 , 0,0,0,1 } ;
		mul( tr1 , tr , tr ) ;
	}


	public void scale(  float scaleX,float scaleY )
	{
		tr[0] = scaleX;
		tr[5] = scaleY;
	}
}
