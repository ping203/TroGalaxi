package com.leaptechjsc.game.offline.galaxian;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.leaptechjsc.game.offline.galaxian.R;

public class ShopActivity extends Activity {
	
	public static byte level;
	
	AlertDialog ad;
	DialogInterface.OnClickListener dialogClickListener = null;
	
	private TextView txtCoinsAvaible;
	private TextView txtLife;
	private TextView txtBullet;
	private TextView txtRocket;
	
	private WakeLock wl;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        PowerManager pm = (PowerManager) getSystemService(GameActivity.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        wl.acquire();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.shop);
		txtCoinsAvaible = (TextView) findViewById(R.id.xu);
		txtCoinsAvaible.setText("You have "+GameSurfaceView._preScore+" point");
		txtLife = (TextView) findViewById(R.id.life);
		txtLife.setText("x "+GameSurfaceView.life);
		txtBullet = (TextView) findViewById(R.id.bullet);
		txtBullet.setText("x "+GameSurfaceView.numOfBullet);
		txtRocket = (TextView) findViewById(R.id.rocket);
		txtRocket.setText("x "+GameSurfaceView.numOfRocket);
	}
	
	public void b_click_BuyLife(View v) {
//    	dialogClickListener = null;
//    	dialogClickListener = new DialogInterface.OnClickListener() {
//    		public void onClick(DialogInterface dialog, int which) {
//        		switch (which) {
//            		case DialogInterface.BUTTON_POSITIVE:
            			if (GameSurfaceView._preScore >= 300) {
            				GameSurfaceView._preScore -= 300;
            				GameSurfaceView.life += 1;
            				txtCoinsAvaible.setText("You have "+ GameSurfaceView._preScore+" point");
            				txtLife.setText("x "+GameSurfaceView.life);
            				Toast.makeText(getBaseContext(), "You have buy a life with the price 300 point!", Toast.LENGTH_SHORT).show();
            			} else Toast.makeText(getBaseContext(), "Not enough point!", Toast.LENGTH_SHORT).show();
//            		break;
//            		case DialogInterface.BUTTON_NEGATIVE:            			
//            		break;
//        		}
//    		}
//    	};
//		createDialog("You have muốn mua?");
	}
	
	public void b_click_BuyBullet(View v) {
		if (GameSurfaceView._preScore >= 300) {
			GameSurfaceView._preScore -= 300;
			GameSurfaceView.numOfBullet += 1;
			txtCoinsAvaible.setText("You have "+GameSurfaceView._preScore+" point");
			txtBullet.setText("x "+GameSurfaceView.numOfBullet);
			Toast.makeText(getBaseContext(), "You have buy grape-shot with the price 300 point!", Toast.LENGTH_SHORT).show();
		} else Toast.makeText(getBaseContext(), "Not enough point!", Toast.LENGTH_SHORT).show();
	}
	
	public void b_click_BuyRocket(View v) {
		if (GameSurfaceView._preScore >= 300) {
			GameSurfaceView._preScore -= 300;
			GameSurfaceView.numOfRocket += 1;
			txtCoinsAvaible.setText("You have "+GameSurfaceView._preScore+" point");
			txtRocket.setText("x "+GameSurfaceView.numOfRocket);
			Toast.makeText(getBaseContext(), "You have buy a rocket with the price 300 point!", Toast.LENGTH_SHORT).show();
		} else Toast.makeText(getBaseContext(), "Not enough point!", Toast.LENGTH_SHORT).show();
	}
	
	public void b_click_NextLevel(View v) {
		if (GameSurfaceView.life > 0) {
			this.finish();
			Runtime.getRuntime().gc();
			Intent i = new Intent(this, GameActivity.class);
			startActivity(i);
		} else Toast.makeText(getBaseContext(), "GAME OVER", Toast.LENGTH_SHORT).show();
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
	            			wl.release();
	            			finish();
	            		break;
	            		case DialogInterface.BUTTON_NEGATIVE:            			
	            		break;
            		}
        		}
        	};
    		createDialog("Do you want exit?");
        }
        return super.onKeyDown(keyCode, event);
    }
	
    private void createDialog(String mess) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(mess)
    	.setPositiveButton("Yes", dialogClickListener)
    	.setNegativeButton("No", dialogClickListener);
    	 if (ad != null) {
    		 ad = null;
    	 }
		 ad = builder.create();
		 ad.show();
    }
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	protected void onStop() {
		super.onStop();
	}
	
	protected void onPause() {
		Runtime.getRuntime().gc();
		super.onPause();
	}
}
