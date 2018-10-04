package com.leaptechjsc.game.offline.galaxian;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {
	
	GameSurfaceView gsv;
	AlertDialog ad;
	DialogInterface.OnClickListener dialogClickListener = null;
	
	private WakeLock wl;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PowerManager pm = (PowerManager) getSystemService(GameActivity.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        wl.acquire();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Display display = this.getWindow().getWindowManager().getDefaultDisplay();
        gsv = new GameSurfaceView(this, this, display.getWidth(), display.getHeight());
        setContentView(gsv);
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode)
        {      
        case KeyEvent.KEYCODE_BACK:
        	if (gsv != null) gsv.setGamePause(true);
        	dialogClickListener = new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {
            		switch (which) {
	            		case DialogInterface.BUTTON_POSITIVE:
	            			if (gsv != null) gsv.Free();
	            	    	wl.release();
	            			Runtime.getRuntime().gc();
	            			finish();
	            		break;
	            		case DialogInterface.BUTTON_NEGATIVE:  
	            		break;
            		}
        		}
        	};
    		createDialog();
    		break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void createDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Do you want exit?")
    	.setPositiveButton("Yes", dialogClickListener)
    	.setNegativeButton("No", dialogClickListener);
    	 if (ad != null) {
    		 ad = null;
    	 }
		 ad = builder.create();
		 ad.show();
    }
    
    public void onPause() {
    	super.onPause();
    	if (gsv != null) gsv.setGamePause(true);
    }
    
    public void onResume() {
    	super.onResume();
    	if (gsv != null) gsv.timertaskResume();
    }
}
