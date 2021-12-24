/**
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */

package live2d.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import live2d.utils.android.FileManager;
import live2d.utils.android.SoundManager;


public class SampleApplication extends Activity
{
	
	private LAppLive2DManager live2DMgr ;
	static private Activity instance;

	public SampleApplication( )
	{
		instance=this;
		if(LAppDefine.DEBUG_LOG)
		{
			Log.d( "", "==============================================\n" ) ;
			Log.d( "", "   Live2D Sample  \n" ) ;
			Log.d( "", "==============================================\n" ) ;
		}

		SoundManager.init(this);
		live2DMgr = new LAppLive2DManager() ;
	}


	 static public void exit()
    {
		SoundManager.release();
    	instance.finish();
    }


	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        
      	//setupGUI();
      	FileManager.init(this.getApplicationContext());
    }


	
//	void setupGUI()
//	{
//    	setContentView(R.layout.activity_main);
//
//
//        LAppView view = live2DMgr.createView(this) ;
//
//
//        FrameLayout layout=(FrameLayout) findViewById(R.id.live2DLayout);
//		layout.addView(view, 0, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//
//
//
//		ImageButton iBtn = (ImageButton)findViewById(R.id.imageButton1);
//		ClickListener listener = new ClickListener();
//		iBtn.setOnClickListener(listener);
//	}


	
	class ClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "change model", Toast.LENGTH_SHORT).show();
			live2DMgr.changeModel();//Live2D Event
		}
	}


	
	@Override
	protected void onResume()
	{
		//live2DMgr.onResume() ;
		super.onResume();
	}


	
	@Override
	protected void onPause()
	{
		live2DMgr.onPause() ;
    	super.onPause();
	}
}
