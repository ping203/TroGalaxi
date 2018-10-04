package com.leaptechjsc.game.offline.galaxian;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.leaptechjsc.game.offline.galaxian.R;

@SuppressLint({ "DrawAllocation", "DrawAllocation" })
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	
	public GameThread thread;
	private Boss bigenemy;
	private String score = "Điểm";
	private String waitting;
	private String gamepause = "Game Paused";
	private Paint paint;
	
	public static float w, h;
	public static byte level;
	public static byte life;
	public static int _score;
	public static int _preScore;
	public static byte numOfBullet;
	public static byte numOfRocket;
	public static byte map = 1;
	
	private byte bulletCount = 0;
	private float yRocket;
	private byte move = 0;
	private int g = 0, p = 0, r = 0, b = 0;
	private byte row = 3;
	private byte bossCount = 0;
	private byte num = 0;
	private byte gc = 0;
	private byte wait = 1;
	private byte bulletmode = 1;
	
	private Bitmap nv_bmp;
	private Bitmap hieuung_bmp;
	private Bitmap life_bmp;
	private Bitmap rocket_icon;
	
	private Sprite2D nv;
	private Sprite2D hieuung;
	private Sprite2D rocket;
	
	private AirCraft aircraft;
	private ArrayList<Bullet> bullet = new ArrayList<Bullet>();
	private ArrayList<Enemy> enemy = new ArrayList<Enemy>();
	private ArrayList<Star> star = new ArrayList<Star>();
	private Comet comet;
	private int pos[][];
	
	private long EnemyTime = 0;
	private long AttackTime = 0;
	private long CurrentEnemyTime;
	private long initTime = 0;
	
	private boolean levelup = false;
	private boolean gameover = false;
	private boolean lr;
	private boolean initAll;
	private boolean GamePause;
	public static boolean boss;
	public static boolean shop;
	
	private Random rand;
	private Context context;
	private AudioClip ruoibay;
	private AudioClip phithuyenno;
	private AudioClip bossno;
	private AudioClip nhacnen;
	private AudioClip ban;
	
	private Timer timer;
	private TimerTask timertask;
	
	private GameActivity activity;
	
	public GameSurfaceView(Context context, GameActivity GA, float w, float h) {
		super(context);
		this.context = context;
		this.activity = GA;
		getHolder().addCallback(this);
		GamePause = false;
		_score = _preScore;
		paint = new Paint();
		paint.setAntiAlias(true);
		GameSurfaceView.w = w;
		GameSurfaceView.h = h;
		init();
		setFocusable(true);
		timer = new Timer();
		timertask = new TimerTask() {	
			@Override
			public void run() {
//				System.out.println("Task");
				if (wait < 10) wait++;
				if (wait%3 == 0) waitting = "Waiting.";
				if (wait%3 == 1) waitting = "Waiting..";
				if (wait%3 == 2) waitting = "Waiting...";
				gc++;
				if (gc == 60) {
					gc = 0;
					if (comet != null && !comet.state) {
						comet.free();
						comet = null;
					}
					if (comet == null) {
						switch (Math.abs(getRandom()) % 3) {
							case 0: comet = new Comet(getBitmap(R.drawable.bluecomet)); break;
							case 1: comet = new Comet(getBitmap(R.drawable.redcomet)); break;
							case 2: comet = new Comet(getBitmap(R.drawable.yellowcomet)); break;
						}
					}
				}
				if (lr) {
					move += 10;
					if (move == 20) lr = false;
				} else {
					move -= 10;
					if (move == -20) lr = true;
				}
			}
		};
		timer.schedule(timertask, 0, 1000);
	}
	
	//Khởi tạo các đối tượng
	private void init() {
		life_bmp = getBitmap(R.drawable.life);
		rocket = new Sprite2D();
		rocket.init(getBitmap(R.drawable.rocket), 140, 140, 4, 4);
		nv_bmp = getBitmap(R.drawable.nv);
		hieuung_bmp = getBitmap(R.drawable.hieuung);
		rocket_icon = getBitmap(R.drawable.rocket_shop);
		rocket_icon = Bitmap.createScaledBitmap(rocket_icon, 40, 40, true);
		nv = new Sprite2D();
		hieuung = new Sprite2D();
		nv.init(nv_bmp, 70, 70, 1, 3);
		hieuung.init(hieuung_bmp, 140, 140, 3, 4);
		if (h < 800) aircraft = new AirCraft(nv, hieuung, w/2, h - 100);
		else aircraft = new AirCraft(nv, hieuung, w/2, h - 150);
		rand = new Random();
		pos = new int[4][8];
		pos[0][0] = 1; pos[0][1] = 1; pos[0][2] = 1; pos[0][5] = 1; pos[0][6] = 1; pos[0][7] = 1;
		pos[1][0] = 1; pos[1][1] = 1; pos[1][6] = 1; pos[1][7] = 1;
		pos[2][0] = 1; pos[2][7] = 1;
		firstInit();
		initAll = true;
		ruoibay = new AudioClip(context, R.raw.ruoibay);
		phithuyenno = new AudioClip(context, R.raw.tiengno);
		bossno = new AudioClip(context, R.raw.tiengno);
		nhacnen = new AudioClip(context, R.raw.ingame);
		ban = new AudioClip(context, R.raw.tiengbanruoi);
		boss = false;
	}
	
	private void firstInit() {
		for (byte i = 0; i < 4; i++)
			for (byte j = 0; j < 8; j++) {
				if (pos[i][j] != 1) {
					pos[i][j] = 1;
					if (i == 0) {
						Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoiboss), getBitmap(R.drawable.ruoiboss_hieuung), getBitmap(R.drawable.hieuung1), (byte)4);
						e.setRowPos(i);
						e.setColPos(j);
						e.tinhGoc();
						enemy.add(e);
					}
					if (i == 1) {
						Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoixanh), getBitmap(R.drawable.ruoixanh_hieuung), getBitmap(R.drawable.hieuung1), (byte)0);
						e.setRowPos(i);
						e.setColPos(j);
						e.tinhGoc();
						enemy.add(e);
					}
					if (i == 2) {
						Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoihong), getBitmap(R.drawable.ruoihong_hieuung), getBitmap(R.drawable.hieuung1), (byte)1);
						e.setRowPos(i);
						e.setColPos(j);
						e.tinhGoc();
						enemy.add(e);
					}
					if (i == 3) {
						Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoido), getBitmap(R.drawable.ruoido_hieuung), getBitmap(R.drawable.hieuung1), (byte)2);
						e.setRowPos(i);
						e.setColPos(j);
						e.tinhGoc();
						enemy.add(e);
					}
				}
			}
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				if (GamePause) GamePause = false;
				if (y > h - 60 && y < h) {
					if (x >= 0 && x < 60) bulletmode = 1;
					if (x > w/2 - 30 && x < w/2 + 30 && bulletmode == 1) 
						if (numOfBullet > 0) {
						bulletmode = 2;
						numOfBullet -= 1;
//						preferenceManager.saveValue("numOfBullet", numOfBullet);
					}
				}
				if (x >= w - rocket_icon.getWidth() - 50 && x <= w - 40)
					if (y >= h - rocket_icon.getHeight() - 10 && y <= h && numOfRocket > 0 && bulletmode == 1) {
					yRocket = AirCraft.getY();
					rocket.setXPosCenter(AirCraft.getX());
					bulletmode = 3;
					numOfRocket -= 1;
//					preferenceManager.saveValue("numOfRocket", numOfRocket);
				}
				if (levelup) {
					num += 1;
					if (map < 2) {
						map += 1;
					} else {
						map = 1;
						level += 1;
					}
					boss = false;
					initAll = true;
					_preScore = _score;
					Free();
					thread.setRunning(false);
					activity.finish();
					Runtime.getRuntime().gc();
					Intent i = new Intent(getContext(), ShopActivity.class);
					context.startActivity(i);
				}
				if (gameover) {
					_preScore = _score;
					_score = 0;
					level = 1;
					AirCraft.setState(true);
					this.Free();
					activity.finish();
					Intent i = new Intent(getContext(), HighScoreActivity.class);
					context.startActivity(i);
				}
			}; break;
		}
		synchronized (thread.getSurfaceHolder()) {
			if (x > 30 && x < w - 30 && y >= h - 200) {
				aircraft.move(System.currentTimeMillis(), x);
			}
			return true;
		}
	}
	
	public void onDraw(Canvas c) {
//		System.out.println("Level: "+level+" Map: "+map);
		if (life == 0) {
			gameover = true;
		}
		if (Setting.getMusic_Background(context) && !GamePause) nhacnen.play();
		if (GamePause) nhacnen.stop();
		if (wait < 10) drawWait(c);
		if (!gameover && !levelup && !GamePause && wait == 10) {
			drawScore(c);
			drawLife(c);
			drawStar(c);
			if (comet != null) comet.draw(c);
			if (!boss) {
				checkCollision();
				drawEnemy(c);
			} else {
				if (bigenemy == null) {
					if (map == 1) bigenemy = new Boss(getBitmap(R.drawable.boss1), (byte)1);
					if (map == 2) bigenemy = new Boss(getBitmap(R.drawable.boss2), (byte)2);
				}
				bigenemy.draw(c);
				drawEnemy(c);
				if (bigenemy.checkCollision()) {
					if (Setting.getMusic_Sound(context)) {
						phithuyenno.stop();
						phithuyenno.play();
					}
					life -= 1;
				}
				checkCollision();
			}
			drawBulletMode(c);
			drawBullet(c);
			aircraft.draw(c);
		}
		if (gameover) {
			drawScore(c);
			drawGameOver(c);
		}
		if (levelup) {
			drawScore(c);
			drawStar(c);
			drawLevelUp(c);
		}
		if (GamePause) drawGamePause(c);
	}
	
	//Vẽ màn hình chờ vào game
	private void drawWait(Canvas c) {
		c.drawColor(Color.BLACK);
		Rect rect = new Rect();
		paint.setColor(Color.WHITE);
		paint.setTextSize(30);
		paint.getTextBounds(waitting, 0, waitting.length(), rect);
		c.drawText(waitting, (w - rect.width())/2, (h - rect.height())/2, paint);
	}
	
	//Vẽ màn hình Pause
	private void drawGamePause(Canvas c) {
		c.drawColor(Color.BLACK);
		paint.setTextSize(40);
		Rect bounds = new Rect();
		paint.getTextBounds(gamepause, 0, gamepause.length(), bounds);
		c.drawText(gamepause, w/2 - bounds.width()/2, h/2 - bounds.height()/2, paint);
	}
	
	//Vẽ điểm
	private void drawScore(Canvas c) {
		paint.setStrokeWidth(0);
		Rect bounds = new Rect();
		Bitmap galaxian = BitmapFactory.decodeResource(getResources(), R.drawable.galaxian);
		galaxian = Bitmap.createScaledBitmap(galaxian, getWidth() - 200, 100, true);
		c.drawColor(Color.BLACK);
		paint.setColor(Color.RED);
		//Vẽ điểm
		paint.setTextSize(25);
		paint.getTextBounds(score, 0, score.length(), bounds);
		c.drawText(score, 0, score.length(), 30, 40, paint);
		paint.setColor(Color.WHITE);
		c.drawText(String.valueOf(_score), 30, 50 + bounds.height(), paint);
	}
	
	//Vẽ đạn của phi thuy�?n
	private void drawBullet(Canvas c) {
		initBullet();
		for (int i = 0; i < bullet.size(); i++) {
			if (bullet.get(i).state) bullet.get(i).draw(c);
		}
		for (int i = 0; i < bullet.size(); i++) {
			if (!bullet.get(i).state) bullet.remove(i);
		}
		if (bulletmode == 3) drawRocket(c);
	}
	
	//Vẽ kẻ địch
	private void drawEnemy(Canvas c) {
		initEnemy();
		if (initAll) {
			for (byte i = 0; i < 8; i++) {
				if (pos[row][i] == 0) {
					switch (row) {
						case 3: {
							Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoido), getBitmap(R.drawable.ruoido_hieuung), getBitmap(R.drawable.hieuung1), (byte)2);
							e.setRowPos(row);
							e.setColPos(i);
							e.tinhGoc();
							enemy.add(e);
							pos[row][i] = 1;						
						}; break;
						case 2: {
							Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoihong), getBitmap(R.drawable.ruoihong_hieuung), getBitmap(R.drawable.hieuung1), (byte)1);
							e.setRowPos(row);
							e.setColPos(i);
							e.tinhGoc();
							enemy.add(e);
							pos[row][i] = 1;						
						}; break;
						case 1: {
							Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoixanh), getBitmap(R.drawable.ruoixanh_hieuung), getBitmap(R.drawable.hieuung1), (byte)0);
							e.setRowPos(row);
							e.setColPos(i);
							e.tinhGoc();
							enemy.add(e);
							pos[row][i] = 1;						
						}; break;
						case 0: {
							Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoiboss), getBitmap(R.drawable.ruoiboss_hieuung), getBitmap(R.drawable.hieuung1), (byte)4);
							e.setRowPos(row);
							e.setColPos(i);
							e.tinhGoc();
							enemy.add(e);
							pos[row][i] = 1;	
						}
					}
				}
 			}
			byte size = (byte)enemy.size();
			for (byte i = 0; i < size; i++) enemy.get(i).draw(c, move);
			long tempTime = System.currentTimeMillis();
			if (tempTime >= initTime + 2000) {
				if (row > 0) row -= 1; else {
					row = 3;
					initAll = false;
				}
				initTime = tempTime;
			}
		} else
		for (int i = 0; i < enemy.size(); i++) {
			enemy.get(i).draw(c, move);
		}
		enemyAttack(System.currentTimeMillis());
	}
	
	//Vẽ mạng chơi
	private void drawLife(Canvas c) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(25);
		c.drawBitmap(life_bmp, w - life_bmp.getWidth() - 30, 0, null);
		c.drawText("x"+life, w - 45, 45, paint);
	}
	
	//Vẽ sao
	private void drawStar(Canvas c) {
		int size = star.size();
		if ( size == 0 || (size <= 4 && star.get(size - 1).getPos() > 200))
			star.add(new Star(getBitmap(R.drawable.whitestar)));
		for (int i = 0; i < star.size(); i++) star.get(i).draw(c);
	}
	
	//Vẽ Rocket
	private void drawRocket(Canvas c) {
		if (yRocket > 250) yRocket -= 14;
		else rocket.playToFrame(System.currentTimeMillis(), 4);
		rocket.setYPosCenter(yRocket);
		rocket.drawCenter(c, 255);
		if (rocket.currentFrame == 3) {
			rocket.gotoAndStop(1);
			yRocket = 0;
			bulletmode = 1;
		}
		for (int i = 0; i < enemy.size(); i++) {
			Enemy e = enemy.get(i);
			float xEnemy = e.getX();
			float yEnemy = e.getY();
			if (yRocket <= 250 && rocket.currentFrame <= 3 && e.state) {
				if (xEnemy >= rocket.getXPosCenter() - 80 && xEnemy <= rocket.getXPosCenter() + 80)
					if (yEnemy >= rocket.getYPosCenter() - 80 && yEnemy <= rocket.getYPosCenter() + 80) {
						switch (e.getID()) {
							case 0: {
								g += 1;
								_score += 20;
							}; break;
							case 1: {
								p += 1;
								_score += 15;
							}; break;
							case 2: {
								r += 1;
								_score += 10;
							}; break;
							case 4: {
								b += 1;
								_score += 25;
							}; break;
						}
						e.setState(false);
					}
			}
		}
//		System.out.println("Test Boss "+boss);
		if (boss && rocket.currentFrame == 2 && bigenemy != null) {
			rocket.gotoAndStop(3);
			if (bigenemy.getX() >= rocket.getXPosCenter() - 80 && bigenemy.getX() <= rocket.getXPosCenter() +80)
				if (bigenemy.getY() >= rocket.getYPosCenter() - 80 && bigenemy.getY() <= rocket.getYPosCenter() + 80) bigenemy.destroy(300);
		}
	}
	
	//Vẽ chế độ đạn
	private void drawBulletMode(Canvas c) {
		paint.setStyle(Style.FILL);
		paint.setColor(Color.GREEN);
		c.drawRect(24, h - 35, 26, h - 15, paint);
		
		if (numOfBullet > 0) paint.setColor(Color.YELLOW); else paint.setColor(Color.GRAY);
		c.drawRect(w/2 - 1, h - 35, w/2 + 1, h - 15, paint);
		c.drawRect(w/2 - 9, h - 30, w/2 - 7, h - 15, paint);
		c.drawRect(w/2 + 7, h - 30, w/2 + 9, h - 15, paint);
		paint.setColor(Color.WHITE);
		paint.setTextSize(20);
		c.drawText("x"+numOfBullet, w/2 + 25, h - 15, paint);
		
		if (numOfRocket > 0) c.drawBitmap(rocket_icon, w - rocket_icon.getWidth() - 45, h - rocket_icon.getHeight() - 5, paint);
		else {
			paint.setAlpha(100);
			c.drawBitmap(rocket_icon, w - rocket_icon.getWidth() - 45, h - rocket_icon.getHeight() - 5, paint);
			paint.setAlpha(255);
		}
		paint.setTextSize(20);
		c.drawText("x"+numOfRocket, w - 45, h - 15, paint);
		
		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(3);
		paint.setStyle(Style.STROKE);
		switch(bulletmode) {
			case 1: {
				c.drawCircle(25, h - 25, 20, paint);
			}; break;
			case 2: {
				c.drawCircle(w/2, h - 25, 20, paint);
			}; break;
			case 3: {
				c.drawCircle(w - 68, h - 25, 20, paint);
			}; break;
		}
	}
	
	//Vẽ màn hình Game Over
	private void drawGameOver(Canvas c) {
		Bitmap gameover = getBitmap(R.drawable.gameover);
		c.drawBitmap(gameover, (w - gameover.getWidth())/2, (h - gameover.getHeight() - 200)/2, null);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(30);
		Bitmap ruoixanh = getBitmap(R.drawable.g);
		c.drawBitmap(ruoixanh, (w - 105)/2, h/2 - 50, null);
		c.drawText("x "+String.valueOf(g), w/2 + 25, h/2 - 25, paint);
		Bitmap ruoihong = getBitmap(R.drawable.p);
		c.drawBitmap(ruoihong, (w - 105)/2, h/2 + 20, null);
		c.drawText("x "+String.valueOf(p), w/2 + 25, h/2 + 45, paint);
		Bitmap ruoido = getBitmap(R.drawable.r);
		c.drawBitmap(ruoido, (w - 105)/2, h/2 + 90, null);
		c.drawText("x "+String.valueOf(r), w/2 + 25, h/2 + 115, paint);
		Bitmap ruoiboss = getBitmap(R.drawable.b);
		c.drawBitmap(ruoiboss, (w - 105)/2, h/2 + 160, null);
		c.drawText("x "+String.valueOf(b), w/2 + 25, h/2 + 185, paint);	
	}
	
	//Vẽ màn hình qua level
	private void drawLevelUp(Canvas c) {
		Bitmap levelup = getBitmap(R.drawable.levelup);
		c.drawBitmap(levelup, (w - levelup.getWidth())/2, (h - levelup.getHeight() - 200)/2, null);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(30);
		Bitmap ruoixanh = getBitmap(R.drawable.g);
		c.drawBitmap(ruoixanh, (w - 105)/2, h/2 - 50, null);
		c.drawText("x "+String.valueOf(g), w/2 + 25, h/2 - 25, paint);
		Bitmap ruoihong = getBitmap(R.drawable.p);
		c.drawBitmap(ruoihong, (w - 105)/2, h/2 + 20, null);
		c.drawText("x "+String.valueOf(p), w/2 + 25, h/2 + 45, paint);
		Bitmap ruoido = getBitmap(R.drawable.r);
		c.drawBitmap(ruoido, (w - 105)/2, h/2 + 90, null);
		c.drawText("x "+String.valueOf(r), w/2 + 25, h/2 + 115, paint);
		Bitmap ruoiboss = getBitmap(R.drawable.b);
		c.drawBitmap(ruoiboss, (w - 105)/2, h/2 + 160, null);
		c.drawText("x "+String.valueOf(b), w/2 + 25, h/2 + 185, paint);	
	}
	
	//Kiểm tra va chạm
	private void checkCollision() {
		for (int i = 0; i < bullet.size(); i++) {
			float xBullet = bullet.get(i).getX();
			float yBullet = bullet.get(i).getY();
			for (int j = 0; j < enemy.size(); j++) {
				float xEnemy = enemy.get(j).getX();
				float yEnemy = enemy.get(j).getY();
				float xAirCraft = AirCraft.getX();
				float yAirCraft = AirCraft.getY();
				Enemy e = enemy.get(j);
				
				//Kiểm tra đạn bắn trúng ruồi
				if (Math.abs((double) (xBullet - xEnemy)) <= 20 && bullet.get(i).state && e.state)
					if (yBullet <= yEnemy + 35 && yBullet >= yEnemy - 15) { 
						bullet.get(i).setState(false);
						switch(e.getID()) {
							case 0: {
								g += 1;
								_score += 20;
							}; break;
							case 1: {
								p += 1;
								_score += 15;
							}; break;
							case 2: {
								r += 1;
								_score += 10;
							}; break;
							case 4: {
								b += 1;
								_score += 25;
							}; break;
						}
						e.setState(false);
					}
				
				//Kiểm tra ruồi lao vào phi thuy�?n
				if (e.state && AirCraft.state && Math.abs((double) (xEnemy - xAirCraft)) <= 30)
					if (yEnemy >= yAirCraft - 50 && yEnemy <= yAirCraft + 50) {
						e.setState(false);
						AirCraft.setState(false);
						if (Setting.getMusic_Sound(context)) {
							phithuyenno.stop();
							phithuyenno.play();
						}
						life -= 1;
					}
				
				//Kiểm tra phi thuyền dính đạn
				if (e.isShooted) {
					float xBullet1 = e.getXBullet();
					float yBullet1 = e.getYBullet();
					if (Math.abs(xBullet1 - xAirCraft) <= 35 && Math.abs(yBullet1 - yAirCraft) <= 30 && AirCraft.state) {
						AirCraft.setState(false);
						if (Setting.getMusic_Sound(context)) {
							phithuyenno.stop();
							phithuyenno.play();
						}
						e.isShooted = false;
						life -= 1;
					}
				}
				
				//Kiểm tra phi thuyền dính bomb
				if (e.checkCollision()) {
					if (Setting.getMusic_Sound(context)) {
						phithuyenno.stop();
						phithuyenno.play();
					}
					life -= 1;
				}
			}
			
			//Kiểm tra boss dính đạn
			if (boss) {
				if (map == 1)
					if (Math.abs(xBullet - bigenemy.getX()) <= (bigenemy.bossWidth/2 - 150) && Math.abs(yBullet - bigenemy.getY()) <= 20) {
					bigenemy.destroy(50);
					bullet.get(i).setState(false);
				}
				if (map == 2) 
					if (Math.abs(xBullet - bigenemy.getX()) <= (bigenemy.bossWidth/2 - 250) && Math.abs(yBullet - bigenemy.getY()) <= 20) {
						bigenemy.destroy(50);
						bullet.get(i).setState(false);
					}
				if (bigenemy.state == false) {
					boss = false;
					bigenemy.free();
					bigenemy = null;
					levelup = true;
				}
			}
		}
		removeEnemy();
		if (_score >= _preScore + map * 500 && enemy.size() < 8) {
			boss = true;
			_preScore = _score;
		}
	}
	
	//Sinh đạn
	private void initBullet() {
		if (bullet.size() == 0) {
			if (bulletmode == 1) bullet.add(new Bullet(AirCraft.getX(), AirCraft.getY()));
			if (bulletmode == 2) {
				bullet.add(new Bullet(AirCraft.getX() - 20, AirCraft.getY()));
				bullet.add(new Bullet(AirCraft.getX() + 20, AirCraft.getY()));
				bullet.add(new Bullet(AirCraft.getX(), AirCraft.getY() - 15));
				bulletCount += 1;
			}
			if (Setting.getMusic_Sound(context)) ban.play();
		}
		if (bulletmode == 1){
			if (bullet.get(bullet.size() - 1).getY() <= 200) {
				bullet.add(new Bullet(AirCraft.getX(), AirCraft.getY()));
				if (Setting.getMusic_Sound(context)) ban.play();
			}
		}
		if (bulletmode == 2) {
			if (bullet.get(bullet.size() - 1).getY() <= 300) {
				bullet.add(new Bullet(AirCraft.getX() - 20, AirCraft.getY()));
				bullet.add(new Bullet(AirCraft.getX() + 20, AirCraft.getY()));
				bullet.add(new Bullet(AirCraft.getX(), AirCraft.getY() - 15));
				bulletCount += 1;
				if (bulletCount == 10) {
					bulletmode = 1;
					bulletCount = 0;
				}
				if (Setting.getMusic_Sound(context)) ban.play();
			}
		}
	}
	
	//Sinh kẻ địch
	private void initEnemy() {
		CurrentEnemyTime = System.currentTimeMillis();
		if (enemy.size() < 10 && CurrentEnemyTime > EnemyTime + 3000) {
			if (!boss) {
				for (byte i = 0; i < 4; i++)
					for (byte j = 0; j < 8; j++) {
						int k1 = getRandom();
						int k2 = getRandom();
						if (pos[i][j] == 0 && k1 == k2) {
							if (i == 0) {
								Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoiboss), getBitmap(R.drawable.ruoiboss_hieuung), getBitmap(R.drawable.hieuung1), (byte)4);
								e.setRowPos(i);
								e.setColPos(j);
								e.tinhGoc();
								enemy.add(e);
							}
							if (i == 1) {
								Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoixanh), getBitmap(R.drawable.ruoixanh_hieuung), getBitmap(R.drawable.hieuung1), (byte)0);
								e.setRowPos(i);
								e.setColPos(j);
								e.tinhGoc();
								enemy.add(e);
								pos[i][j] = 1;
							}
							if (i == 2) {
								Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoihong), getBitmap(R.drawable.ruoihong_hieuung), getBitmap(R.drawable.hieuung1), (byte)1);
								e.setRowPos(i);
								e.setColPos(j);
								e.tinhGoc();
								enemy.add(e);
								pos[i][j] = 1;
							}
							if (i == 3) {
								Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoido), getBitmap(R.drawable.ruoido_hieuung), getBitmap(R.drawable.hieuung1), (byte)2);
								e.setRowPos(i);
								e.setColPos(j);
								e.tinhGoc();
								enemy.add(e);
								pos[i][j] = 1;
							}
						}
					}
				if (enemy.size() == 0) {
					initAll = true;
				}
			}	
			else if (enemy.size() == 0) {
				if (map == 1) {
					
				}
				if (map == 2 && bigenemy.enegy > 0) {
					Enemy e = new Enemy(this.context, getBitmap(R.drawable.ruoiboss), getBitmap(R.drawable.ruoiboss_hieuung), getBitmap(R.drawable.hieuung1), (byte)1);
					e.setColPos((byte)3);
					e.setX(w/2);
					e.setY(bigenemy.getY());
					e.setAttack();
					e.setStep((byte)3);
					enemy.add(e);
					Enemy e1 = new Enemy(this.context, getBitmap(R.drawable.ruoiboss), getBitmap(R.drawable.ruoiboss_hieuung), getBitmap(R.drawable.hieuung1), (byte)1);
					e1.setColPos((byte)4);
					e1.setX(w/2);
					e1.setY(bigenemy.getY());
					e1.setAttack();
					e1.setStep((byte)3);
					enemy.add(e1);	
				}
			}
			EnemyTime = CurrentEnemyTime;
		}
	}
	
	//Xóa ruồi đã biến mất
	private void removeEnemy() {
		for (int i = 0; i < enemy.size(); i++) {
			Enemy e = enemy.get(i);
			if (e.destroyed && !e.isShooted && !e.isBombed) {
				pos[e.getRowPos()][e.getColPos()] = 0;
				e.free();
				enemy.remove(e);
			}
		}
	}
	
	//Sinh quân
	private void enemyAttack(long Time) {
		if (Time > AttackTime + 3000) {
			AttackTime = Time;
			if (enemy.size() >= 8) {
				for (int i = 0; i < enemy.size(); i++) {
					Enemy e = enemy.get(i);
					if (getRandom() == 0) {
						if (e.getRowPos() == 3) {
							e.setAttack();
							num++;
						}
						if (num >= 8 && e.getRowPos() == 2) {
							e.setAttack();
							num++;
						}
						if (num >= 14 && e.getRowPos() == 1) {
							e.setAttack();
							num++;
						}
					}
				}
			} else {
				for (int i = 0; i < enemy.size(); i++) {
					Enemy e = enemy.get(i);
					if (e.getRowPos() == 0 && bossCount == 0) {
						e.setAttack();
						bossCount = 1;
					}
					if (e.getRowPos() != 0 && getRandom() == 0) e.setAttack();
				}
				bossCount = 0;
			}
			if (enemy.size() > 0 && !gameover && Setting.getMusic_Sound(context)) ruoibay.play();
		}
	}
	
	public void Free() {
		nv_bmp.recycle();
		hieuung_bmp.recycle();
		life_bmp.recycle();
		rocket_icon.recycle();
		timertask.cancel();
		nhacnen.release();
		ruoibay.release();
		phithuyenno.release();
		bossno.release();
		ban.release();
		if (comet != null) {
			comet.free();
			comet = null;
		}
		if (bigenemy != null) {
			bigenemy.free();
			bigenemy = null;
		}
		for (int i = 0; i < enemy.size(); i++) {
			enemy.get(i).free();
			enemy.remove(i);
		}
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		thread = new GameThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();		
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
	}
	
	public void setGamePause(boolean b) {
		GamePause = b;
	}
	
	public Bitmap getBitmap(int id) {
		return BitmapFactory.decodeResource(getResources(), id);
	}
	
	public void timertaskResume() {
		try {
			timer.cancel();
			timer = new Timer();
			timertask = new TimerTask() {	
				@Override
				public void run() {
//					System.out.println("Task");
					if (wait < 10) wait++;
					if (wait%3 == 0) waitting = "Waiting.";
					if (wait%3 == 1) waitting = "Waiting..";
					if (wait%3 == 2) waitting = "Waiting...";
					gc++;
					if (gc == 60) {
						gc = 0;
						if (comet != null && !comet.state) {
							comet.free();
							comet = null;
						}
						if (comet == null) {
							switch (Math.abs(getRandom()) % 3) {
								case 0: comet = new Comet(getBitmap(R.drawable.bluecomet)); break;
								case 1: comet = new Comet(getBitmap(R.drawable.redcomet)); break;
								case 2: comet = new Comet(getBitmap(R.drawable.yellowcomet)); break;
							}
						}
					}
					if (lr) {
						move += 10;
						if (move == 20) lr = false;
					} else {
						move -= 10;
						if (move == -20) lr = true;
					}
				}
			};
			timer.schedule(timertask, 0, 1000);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	private int getRandom() {
		return (rand.nextInt()%9);
	}

}

class GameThread extends Thread {
	private SurfaceHolder surfaceholder;
	private GameSurfaceView gsv;
	private boolean run = false;
	
	public GameThread(SurfaceHolder surfaceholder, GameSurfaceView gsv) {
		this.surfaceholder = surfaceholder;
		this.gsv = gsv;
	}
	
	public void setRunning(boolean run) {
		this.run = run;
	}
	
	public SurfaceHolder getSurfaceHolder() {
	    return surfaceholder;
	}
	
	public void run() {
		Canvas canvas;
		while(run) {
			canvas = null;
			try {
				canvas = surfaceholder.lockCanvas(null);
				synchronized (surfaceholder) {
                    if (canvas != null) try {
                    	gsv.onDraw(canvas);
                    } catch (Exception e) {e.printStackTrace();}
                }
			} finally {
				if (canvas != null)
				surfaceholder.unlockCanvasAndPost(canvas);
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
}
