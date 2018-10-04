package com.leaptechjsc.game.offline.galaxian;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.leaptechjsc.game.offline.galaxian.R;

public class ShopActivity extends Activity implements RewardedVideoAdListener {
	
	public static byte level;
	
	AlertDialog ad;
	DialogInterface.OnClickListener dialogClickListener = null;
	
	private TextView txtCoinsAvaible;
	private TextView txtLife;
	private TextView txtBullet;
	private TextView txtRocket;
	
	private WakeLock wl;


    private RewardedVideoAd mRewardedVideoAd = null;
	
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

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
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

	int click_type = -1;

    public void b_click_BuyLife_video(View v) {
        click_type = 0;
        loadRewardedVideoAd();
    }

    public void b_click_BuyBullet_video(View v) {
        click_type = 1;
        loadRewardedVideoAd();
    }

    public void b_click_BuyRocket_video(View v) {
        click_type = 2;
        loadRewardedVideoAd();
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

    public void loadRewardedVideoAd() {
        if(isLoadingVideo) return;
        isLoadingVideo = true;
        try {
//            Log.d("LIBGDX", "================> loadRewardedVideoAd");
            this.runOnUiThread(new Runnable() {
                public void run() {
                    mRewardedVideoAd.loadAd(String.valueOf(R.string.video_id),
                            new PublisherAdRequest.Builder().addTestDevice("F8F696DA2727EFA792F5159C8FEA0E18").build());
                    mRewardedVideoAd.show();
                }
            });
        }catch (Exception e){
//            Log.d("LIBGDX", "================>Exception loadRewardedVideoAd");
            e.printStackTrace();
        }
    }

    public void onRewarded(RewardItem reward) {
//        Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
//                reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.

//        Log.d("LIBGDX", "================> onRewarded1   " + F.money);
        if(reward != null){
//            int cc = new Random().nextInt(3) + 1;
//
//            Toast.makeText(this, String.format(Language.General.ADD_MONEY_VIEW_AD.getStr(), cc), Toast.LENGTH_SHORT).show();
//            F.money += cc;

            if(click_type == 0){
                GameSurfaceView.life += 1;
            }else if(click_type == 1){
                GameSurfaceView.numOfBullet += 1;
            }else if(click_type == 2){
                GameSurfaceView.numOfRocket += 1;
            }

            click_type = -1;
        }

//        Log.d("LIBGDX", "================> onRewarded2   " + F.money);
    }

    public void onRewardedVideoAdLeftApplication() {
//        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
//                Toast.LENGTH_SHORT).show();
    }
    int indexLoadVideo = 0;
    boolean isLoadingVideo = false;
    public void onRewardedVideoAdClosed() {
//        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    public void onRewardedVideoAdFailedToLoad(int errorCode) {
//        Toast.makeText(this, "Video Failed To Load...", Toast.LENGTH_SHORT).show();
        if(indexLoadVideo >= 3){
            isLoadingVideo = false;
            return;
        }
        loadRewardedVideoAd();
        indexLoadVideo++;
    }

    public void onRewardedVideoAdLoaded() {
//        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        mRewardedVideoAd.show();

        indexLoadVideo = 0;
        isLoadingVideo = false;
        click_type = -1;
    }

    public void onRewardedVideoAdOpened() {
//        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    public void onRewardedVideoStarted() {
//        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    public void onRewardedVideoCompleted() {
//        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();

        indexLoadVideo = 0;
        isLoadingVideo = false;
    }
}
