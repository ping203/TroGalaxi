package com.leaptechjsc.game.offline.galaxian;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.leaptechjsc.game.offline.galaxian.R;

public class HighScoreActivity extends Activity{// implements IQplayHighScore, IPaymentEvent {

	public static final String SMS_EXTRA_CODE = "leaptech_galaxian";
	public static final String SMS_EXTRA_VALUE = "leaptech_galaxian_buy_times";
	public static final String DEFAULT_PREFIX_NUMBER = "6711";
	public static final int DEFAULT_TIMES = 5;
	public static final int DEFAULT_POST_SCORE_COINS = 1000;
	public static String DEFAULT_MESSAGE_CODE = "GALAXIAN";
	private NumberFormat formatter = new DecimalFormat("$###,###,###");
	private boolean HAS_PAYMENT = false;

	AlertDialog ad;
	DialogInterface.OnClickListener dialogClickListener = null;

//	private SunnetPreferenceManager preferenceManager;
//	private PaymentManagerTakeItAll paymentUpdate;
//	private QplayHighScoreUserInfo highscore;
	private TextView name, score, money;
	int coinAvailable = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setContentView(R.layout.entername);

		//son
//		preferenceManager = new SunnetPreferenceManager(this,
//				"vn.sunnet.game.banruoi", "numOfGame",
//				SunnetPreferenceManager.SEC_ALGOR_DES);
//		coinAvailable = getCoins(preferenceManager);

		if (HAS_PAYMENT) {
			//son
//			paymentUpdate = new PaymentManagerTakeItAll(this, SMS_EXTRA_CODE,
//					this);
//			paymentUpdate.prepareScratch(R.layout.scratch_main,
//					(ViewGroup) findViewById(R.id.layout_root),
//					R.id.txt_scratch_code, R.id.radio_scratch_vina,
//					R.id.radio_scratch_mobi, R.id.radio_scratch_viettel,
//					R.id.tbr_scratch_seri, R.id.txt_scratch_seri,
//					R.id.tv_scratch_description,
//					getString(R.string.payment_description_scratch));
//			paymentUpdate.prepareCoupon(R.layout.coupon_main,
//					(ViewGroup) findViewById(R.id.layout_coupon_root),
//					R.id.txt_coupon_code, R.id.tv_coupon_description,
//					getString(R.string.payment_description_coupon));
//			paymentUpdate.prepareSMS(getString(R.string.buy_game_text_1),
//					DEFAULT_PREFIX_NUMBER);
//			paymentUpdate.buildPaymentMethodList();
		}

		//son
//		LoginIDInfo loginIDInfo = new LoginIDInfo(R.layout.sunnet_login,
//				R.id.text_login_name, R.id.text_password, R.id.check_remember,
//				R.id.button_login, R.id.button_regist, R.id.button_close, "");
//
//		RegisterIDInfo registerIDInfo = new RegisterIDInfo(
//				R.layout.sunnet_register, R.id.text_login_name,
//				R.id.text_password, R.id.text_confirm_password, R.id.rdb_male,
//				R.id.rdb_female, R.id.button_regist, R.id.button_close);
//
//		highscore = new QplayHighScoreUserInfo(this, this, loginIDInfo,
//				registerIDInfo);
//
//		// nạp điểm của người chơi (điểm phải lớn hơn 0)
//		if (GameSurfaceView._preScore > 0)
//			highscore.setScore(GameSurfaceView._preScore);
//		else
//			highscore.setScore(1);

//		name = (TextView) findViewById(R.id.ten);
//		score = (TextView) findViewById(R.id.diem);
//		score.setText(Html.fromHtml("<font color='#ff0000'>"
//				+ GameSurfaceView._preScore + "</font>" + " điểm"));
//		money = (TextView) findViewById(R.id.tien);

//		if (HAS_PAYMENT) {
//			money.setText("Bạn có: " + coinAvailable + " xu");
//		} else {
//			money.setVisibility(View.INVISIBLE);
//		}

//		highscore.prepareUserInfo();
//		if (highscore.isLogin())
//			name.setText("Xin chào: " + highscore.getUserName());
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
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
			createDialog("Bạn muốn thoát?");
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void post(View v) {//son
//		highscore.prepareUserInfoLogin();
//		if (highscore.isLogin()) {
//			dialogClickListener = new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					switch (which) {
//					case DialogInterface.BUTTON_POSITIVE: {
//						if (!HAS_PAYMENT
//								|| (coinAvailable >= DEFAULT_POST_SCORE_COINS)) {
//							highscore.postScore();
//						} else
//							buyCoin();
//					}
//						break;
//					case DialogInterface.BUTTON_NEGATIVE:
//						break;
//					}
//				}
//			};
//			createDialog("Bạn muốn gửi điểm cao?");
//		}
	}

	private void createDialog(String text) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(text).setPositiveButton("Có", dialogClickListener)
				.setNegativeButton("Không", dialogClickListener);
		if (ad != null) {
			ad = null;
		}
		ad = builder.create();
		ad.show();
	}

	public void logout(View v) {//son
//		highscore.prepareUserInfo();
//		if (highscore.isLogin())
//			highscore.logout();
//		else
//			Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT)
//					.show();
	}

	public void onPause() {
		super.onPause();
	}

	public void onResume() {
		super.onResume();
		//son
//		if (paymentUpdate != null)
//			paymentUpdate.onResume();
//		highscore.prepareUserInfo();
//		if (highscore.isLogin())
//			name.setText("Xin chào: " + highscore.getUserName());
		System.out.println("ON RESUME");
	}

	public void onStop() {
		super.onStop();
		//son
//		if (paymentUpdate != null)
//			paymentUpdate.release();
	}

	//son
//	public void onLogoutSuccess(QplayHighScoreUserInfo arg0, int arg1) {
//		name.setText("Xin chào:");
//		Toast.makeText(this, "Bạn đã đăng xuất", Toast.LENGTH_SHORT).show();
//	}
//
//	public void onSendScoreFailure(QplayHighScoreUserInfo arg0, int arg1) {
//		// TODO Auto-generated method stub
//		Toast.makeText(this, "Gửi điểm thất bại", Toast.LENGTH_SHORT).show();
//	}

	//son
//	public void onSendScoreSuccess(QplayHighScoreUserInfo arg0, int arg1) {
//		// TODO Auto-generated method stub
//		if (HAS_PAYMENT) {
//			if (coinAvailable >= DEFAULT_POST_SCORE_COINS)
//				coinAvailable -= DEFAULT_POST_SCORE_COINS;
//			money.setText("Bạn có: " + coinAvailable + " xu");
//			preferenceManager.saveValue("coins", coinAvailable);
//		}
//		Toast.makeText(this,
//				"Gửi điểm thành công, vui lòng đợi để xem kết quả",
//				Toast.LENGTH_SHORT).show();
//		try {
//			highscore.openScoreActivity();
//			this.finish();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public void onPaymentDeny(int arg0, String arg1, int arg2, String arg3,
			int arg4, String arg5, String arg6, String arg7) {
		// TODO Auto-generated method stub

	}

	public void onPaymentFailure(int arg0, String arg1, int arg2, String arg3,
			int arg4, String arg5, String arg6, String arg7) {
		// TODO Auto-generated method stub

	}

//	public void onPaymentSuccess(int type, String strRecognizeCode,
//			int resultCode, String strResultMsg, int value, String valueType,
//			String server, String requestContent) {
//		coinAvailable = getCoins(preferenceManager);
//		int mAddCoins = calcCoinsByValue(value, type);
//		coinAvailable += mAddCoins;
//		money.setText("Bạn có: " + coinAvailable + " xu");
//		preferenceManager.saveValue("coins", coinAvailable);
//		Toast.makeText(getBaseContext(), "Bạn được thêm " + mAddCoins + " xu!",
//				Toast.LENGTH_SHORT).show();
//	}
//
//	private void buyCoin() {
//		paymentUpdate.setPreDescription("Bạn không có đủ "
//				+ formatter.format(DEFAULT_POST_SCORE_COINS) + " để gửi điểm.");
//		paymentUpdate.setPaymentContent(DEFAULT_MESSAGE_CODE, "buy_coins");
//		paymentUpdate.showDefaultPayment();
//	}

//	private int getCoins(SunnetPreferenceManager preferenceManager) {
//		int numberOfGameAvalable = DEFAULT_TIMES;
//		int coinAvalable = 0;
//		try {
//			numberOfGameAvalable = preferenceManager.getIntValue("games",
//					DEFAULT_TIMES);
//			coinAvalable = preferenceManager.getIntValue("coins", -1);
//
//			// quy đổi một lượt chơi là 300 xu
//			if (coinAvalable < 0) {
//				coinAvalable = numberOfGameAvalable * 300;
//				preferenceManager.saveValue("coins", coinAvalable);
//			}
//
//		} catch (InvalidChecksumException e) {
//			coinAvalable = 0;
//		}
//		return coinAvalable;
//	}
//
//	private int calcCoinsByValue(int value, int type) {
//		int addCoins = value * 2;
//		if (type != PaymentManager.PAYMENT_TYPE_SMS)
//			if (value < 20000)
//				addCoins += addCoins / 100 * 5;
//			else if (value < 50000)
//				addCoins += addCoins / 100 * 10;
//			else if (value < 100000)
//				addCoins += addCoins / 100 * 15;
//			else if (value < 200000)
//				addCoins += addCoins / 100 * 20;
//			else if (value < 500000)
//				addCoins += addCoins / 100 * 25;
//			else
//				addCoins += addCoins / 100 * 30;
//		return addCoins;
//	}

}
