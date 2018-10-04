package com.leaptechjsc.game.offline.galaxian;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.leaptechjsc.game.offline.galaxian.R;

public class MainActivity extends Activity {
	
//	private SunnetLoadParam loadParam;
//	private PaymentScriptVisit visit;
	private AudioClip click;
	private AudioClip nhacnen;
	AlertDialog ad;
	DialogInterface.OnClickListener dialogClickListener = null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        click = new AudioClip(this, R.raw.click);
        nhacnen = new AudioClip(this, R.raw.nhacmenu);
        if (Setting.getMusic_Background(this)) nhacnen.loop();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);


    }
    
    public void onPause() {
    	super.onPause();
    	nhacnen.stop();
    }
    
    public void onResume() {
    	super.onResume();
    	if (Setting.getMusic_Background(this)) {
    		nhacnen.loop();
    	}
    }
    
    public void onStop() {
    	super.onStop();
    }
    
    public void b_start_Click(View v)
    {
    	if (Setting.getMusic_Sound(this)) click.play();
//		if (visit.doVisitForPayment()) {//son
		if (true) {
    		nhacnen.stop();
            GameSurfaceView.life = 5;
    		GameSurfaceView._preScore = 0;
    		GameSurfaceView._score = 0;
    		GameSurfaceView.level = 1;
    		GameSurfaceView.map = 1;
        	Intent i = new Intent(this, ShopActivity.class);
        	startActivity(i);
    	}
    }
    
    public void b_setting_Click(View v) {
    	if (Setting.getMusic_Sound(this)) click.play();
    	nhacnen.stop();
    	Intent i = new Intent(this, Setting.class);
    	startActivity(i);
    }
    
    public void b_shop_Click(View v) {

    }
    
    public void b_exit_Click(View v) {
    	dialogClickListener = null;
    	dialogClickListener = new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {
        		switch (which) {
            		case DialogInterface.BUTTON_POSITIVE:
            			finish();
            		break;
            		case DialogInterface.BUTTON_NEGATIVE:            			
            		break;
        		}
    		}
    	};
		createDialog();
    }
    
    public void b_download_Click(View v) {
//    	loadParam.openDownloadLink();
    }
    
    public void b_info_Click(View v) {
    	LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.about, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();
    }
    
    public void b_share_Click(View v) {
//    	loadParam.openShareDialog(getString(R.string.title_share), getString(R.string.message_share_subject), getString(R.string.message_share_content));
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode)
        {      
        case KeyEvent.KEYCODE_BACK:
        	dialogClickListener = null;
        	dialogClickListener = new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {
            		switch (which) {
	            		case DialogInterface.BUTTON_POSITIVE:
	            			finish();
	            		break;
	            		case DialogInterface.BUTTON_NEGATIVE:            			
	            		break;
            		}
        		}
        	};
    		createDialog();
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
	
}
