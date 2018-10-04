package com.leaptechjsc.game.offline.galaxian;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.leaptechjsc.game.offline.galaxian.R;

public class Enemy {
	
	private float x;
	private float y;
	private float xOriginal;
	private float xVelocity = 0;
	private float glideSpeed = 2;
	private float xBullet;
	private float yBullet;
	private float w;
	private float h;
	private float a, b;
	private float degrees1, degrees2;
	
	private String say;
	
	private AudioClip no;
	
	private long GlideTime = 0;
	private long EnemyTime = 0;
	private long CurrentTime;
	
	private byte rowPos;
	private byte colPos;
	
	private byte ID; //Loại ruồi
	public byte step = 0;
	private byte numOfBullet = 0;
	private byte numOfBomb = 0;
	
	private Random rand;
	
	private Sprite2D enemy;
	private Sprite2D enemy1;
	private Sprite2D enemy2;
	
	private ArrayList<Bomb> bomb = new ArrayList<Bomb>();
	
	public boolean state = true;
	public boolean destroyed = false;
	public boolean isBombed = false;
	public boolean isShooted = false;
	
	private Context context;
	
	public Enemy(Context context, Bitmap bitmap1, Bitmap bitmap2, Bitmap bitmap3, byte ID) {
		this.ID = ID;
		this.context = context;
		this.w = GameSurfaceView.w;
		this.h = GameSurfaceView.h;
		x = w/2;
		y = -30;
		enemy = new Sprite2D();
		enemy.init(bitmap1, 48, 48, 24, 9);
		enemy1 = new Sprite2D();
		enemy1.init(bitmap2, 48, 48, 3, 3);
		enemy2 = new Sprite2D();
		enemy2.init(bitmap3, 140, 140, 10, 4);
		no = new AudioClip(context, R.raw.bantrungruoi);
		rand = new Random();
		switch (getRandom()) {
			case 0: say = "Chết nè!"; break;
			case 1: say = "Xông lên!"; break;
			case 2: say = "Bắt lấy nó!"; break;
			case 3: say = "Go go go!";
		}
	}
	
	public void draw(Canvas c, float move) {
		CurrentTime = System.currentTimeMillis();
		if (CurrentTime > EnemyTime + 10) {
			move();
			EnemyTime = CurrentTime;
		}
		if (isBombed) drawBomb(c);
		if (isShooted) drawBullet(c);
		if (step == 0 && state) {
			enemy.setXPosCenter(x);
			enemy.setYPosCenter(y);
			enemy.drawRotatedCenter(c, degrees1);
		}
		if (step == 1 && state) {
			enemy1.loop(System.currentTimeMillis());
			x = xOriginal + move;
			enemy1.setXPosCenter(x);
			enemy1.setYPosCenter(y);
			enemy1.drawCenter(c, 255);
		}
		if (state && step >= 2) {
			enemy.gotoAndStop(1);
			enemy.setXPosCenter(x);
			enemy.setYPosCenter(y);
			enemy.drawRotatedCenter(c, degrees2);
			if(say != null) drawSay(c);
		}
		if (!state && y < h && !destroyed) {
			if (Setting.getMusic_Sound(context)) no.play();
			enemy2.playToFrame(System.currentTimeMillis(), 4);
			enemy2.setXPosCenter(x);
			enemy2.setYPosCenter(y);
			enemy2.drawCenter(c, 255);
			if (enemy2.currentFrame == 3) {
				no.stop();
				destroyed = true;				
			}
		}
	}
	
	public void tinhGoc() {
		a = 16 * (rowPos * 60 + 160) / (280 - 80 * colPos + (2 * colPos - 7) * w);
		b = -30 - a * w / 2;
		degrees1 = (float)Math.toDegrees(Math.atan(a));
		if (degrees1 > 0) degrees1 = degrees1 - 90;
		else degrees1 = degrees1 + 90;		
	}
	
	public void move() {
		//�?i tới vị trí trên hàng
		if (step == 0 && state) {
			if (y < (130 + rowPos * 60)) {
				y += 12;
				x = (y - b) / a;
				if (y >= (130 + rowPos * 60)) {
					y = 130 + rowPos * 60;
					xOriginal = x;
					step = 1;
				}
			}
			if (y > (130 + rowPos * 60) - 30) enemy.playToFrame(System.currentTimeMillis(), 9);
		}
		
		//Lượn
		//Giai đoạn 1
		if (step == 2 && state) {
			if (colPos >= 4) glideRight(System.currentTimeMillis());
			else glideLeft(System.currentTimeMillis());
		}
		
		//Giai đoạn 2
		if (step == 3 && state) {
			
			if (ID == 0) {
				if (y < h + 30) {
					y += 6;
					if (xVelocity == 0) {
						if (colPos >= 4) {
							xVelocity = (float)-2.6;
							degrees2 = 30;
						}
						else {
							xVelocity = (float)2.6;
							degrees2 = -30;
						}
					}
					x += xVelocity;
					if (y >= h + 30) setState(false);
				}
			}
			
			if (ID == 1) {
				if (y < h + 30) {
					y += 4;
					//Khởi tạo tốc độ di chuyển ban đầu
					if (xVelocity == 0) {
						if (colPos >= 4) {
							xVelocity = 4;
							degrees2 = -45;
						}
						else {
							xVelocity = - 4;
							degrees2 = 45;
						}
					}
					x += xVelocity;
					if (x >= w + 30 && xVelocity > 0) x = -30;
					if (x <= -30 && xVelocity < 0) x = w + 30;
				}
			}
			
			if (ID == 2) {
				if (y < h + 30) {
					y += 9 + GameSurfaceView.level;
					//Khởi tạo tốc độ di chuyển ban đầu
					if (xVelocity == 0) {
						if (colPos >= 4) {
							xVelocity = (float)-4.5;
							degrees2 = (float)26.6;
						}
						else {
							xVelocity = (float)4.5;
							degrees2 = (float)-26.6;
						}
					}
					x += xVelocity;
					if (x >= w + 30 && xVelocity > 0) x = -30;;
					if (x <= -30 && xVelocity < 0) x = w + 30;
				}
			}
			
			if (ID == 3) {
				if (y < h + 30) {
					y += 8;
				}
			}
			
			if (ID == 4) {
				degrees2 = 0;
				if (y < h + 30) {
					y += 12;
				}
			}
			
			if (!isShooted) {
				xBullet = x;
				yBullet = y + 20;
			}
			if (getRandom() == 0) {
				if (ID == 4 || ID == 0 || (GameSurfaceView.boss && ID == 1)) {
					if (numOfBomb < GameSurfaceView.level) {
						if (isBombed == false) {
							numOfBomb += 1;
							isBombed = true;
						}
					}
				}
				else {
					if (numOfBullet < GameSurfaceView.level) {
						if (isShooted == false) {
							numOfBullet += 1;
							isShooted = true;
						}
					}
				}
			}
		}
		if (isBombed) {
			if (bomb.size() == 0) {
				bomb.add(new Bomb(x, y));
				bomb.add(new Bomb(x, y));
				bomb.add(new Bomb(x, y));
				bomb.get(0).setTarget();
				bomb.get(1).setTarget();
				bomb.get(2).setTarget();
			}
			if (bomb.get(0).getY() >= h + 30) {
				isBombed = false;
				for (int i = 0; i < bomb.size(); i++) bomb.remove(i);
			}
		}
		if (isShooted) {
			yBullet += 10 + GameSurfaceView.level * 1.5;
			if (yBullet >= h + 30) isShooted = false;
		}
		if (y >= h + 30) destroyed = true;	 
	}
	
	public boolean checkCollision() {
		//Bomb trúng phi thuyền
		for (int i = 0; i < bomb.size(); i++) {
			Bomb b = bomb.get(i);
			if (Math.abs(b.getX() - AirCraft.getX()) <= 35 && Math.abs(b.getY() - AirCraft.getY()) <= 35 && AirCraft.state) {
				AirCraft.setState(false);
				b.state = false;
				bomb.remove(i);
				return true;
			}
		}
		return false;
	}
	
	private void glideLeft(long currentTime) {
		if (currentTime > GlideTime + 50) {
			x -= 4;
			y -= glideSpeed;
			degrees2 = 135;
			if (x < (2*colPos + 3)*w/20 - 20) {
				glideSpeed = -4;
				degrees2 = 45;
			}
			if (y > 140 + rowPos * 60) step = 3;
			EnemyTime = currentTime;
		}
	}
	
	private void glideRight(long currentTime) {
		if (currentTime > GlideTime + 50) {
			x += 4;
			y -= glideSpeed;
			degrees2 = -135;
			if (x > (2*colPos + 3)*w/20 + 20) {
				glideSpeed = -4;
				degrees2 = -45;
			}
			if (y > 140 + rowPos * 60) step = 3;
			EnemyTime = currentTime;
		}
	}
	
	private void drawBomb(Canvas c) {	
		bomb.get(0).draw(c);
		if (bomb.size() >= 2 && bomb.get(0).getY() - bomb.get(1).getY() >= 50) bomb.get(1).draw(c);
		if (bomb.size() >= 3 && bomb.get(1).getY() - bomb.get(2).getY() >= 50) bomb.get(2).draw(c);
	}
	
	private void drawBullet(Canvas c) {
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.RED);
		c.drawRect(xBullet - 2, yBullet - 10, xBullet + 2, yBullet + 10, paint);
	}
	
	private void drawSay(Canvas c) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		c.drawText(say, x, y, paint);
	}
	
	private int getRandom() {
		return (rand.nextInt() % 10);
	}
	
	public int getID() {
		return this.ID;
	}

	public void setX(float x) {
		this.x = x;	
		xOriginal = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setRowPos(byte row) {
		this.rowPos = row;
	}
	
	public void setColPos(byte col) {
		this.colPos = col;
	}
	
	public void setState(boolean b) {
		this.state = b;
	}	
	
	public void setAttack() {
		if (step == 1) step = 2;
	}
	
	public void setStep(byte st) {
		step = st;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public int getRowPos() {
		return this.rowPos;
	}
	
	public int getColPos() {
		return this.colPos;
	}
	
	public float getXBullet() {
		return this.xBullet;
	}
	
	public float getYBullet() {
		return this.yBullet;
	}
	
	public boolean getState() {
		return this.state;
	}
	
	public void free() {
		enemy.remove();
		enemy1.remove();
		enemy2.remove();
		no.release();
	}
	
}
