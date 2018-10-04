package com.leaptechjsc.game.offline.galaxian;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Boss {
	
	public boolean state = true;
//	public boolean destroyed = false;
	
	private byte bosslevel;
	private float x;
	private float y;
	private byte dx = -4;
	private byte dy = 4; 
	private byte step = 1;
	private int trans = 255;
	private float w;
	
	private Paint paint;
	
	public int enegy;
	public int bossWidth, bossHeight;
	private int _enegy;
	
	private long Time = 0;
	
	private Sprite2D sprite;
	
	private ArrayList<Bullet> bullet = new ArrayList<Bullet>();  
	private ArrayList<Bomb> bomb = new ArrayList<Bomb>();
	
	public Boss(Bitmap bitmap, byte bosslevel) {
		this.bosslevel = bosslevel;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		paint.setTextSize(30);
		_enegy = enegy = bosslevel * 1000 + (GameSurfaceView.level - 1) * 1000;
		sprite = new Sprite2D();
		switch (bosslevel) {
			case 1: {
				sprite.init(bitmap, 270, 158, 4, 2);
			}; break;
			case 2: {
				sprite.init(bitmap, 760, 331, 4, 4);
			}; break;
		}
		this.w = GameSurfaceView.w;
		bossWidth = bitmap.getWidth();
		bossHeight = bitmap.getHeight();
		x = w/2;
		y = - bossHeight;
		sprite.setXPosCenter(x);
		sprite.setYPosCenter(y);
	}
	
	public void draw(Canvas c) {
		initWeapon(System.currentTimeMillis());
		move(System.currentTimeMillis());
		switch (bosslevel) {
			case 1: {
				if (enegy > 0) sprite.loop(System.currentTimeMillis());
			}; break;
			case 2: {
				if (AirCraft.getX() >= w /2 + 20) sprite.gotoAndStop(3);
				if (AirCraft.getX() <= w /2 - 20) sprite.gotoAndStop(4);
			}; break;
		}
		sprite.drawCenter(c, trans);
		for (int i = 0; i < bullet.size(); i++) bullet.get(i).draw(c);
		if (bomb.size() > 0) {
			sprite.gotoAndStop(1);
			bomb.get(0).draw(c);
			if (bomb.size() >= 2 && bomb.get(0).getY() - bomb.get(1).getY() >= 50) bomb.get(1).draw(c);
			if (bomb.size() >= 3 && bomb.get(1).getY() - bomb.get(2).getY() >= 50) bomb.get(2).draw(c);
		}
		if (enegy > bosslevel * 100) paint.setColor(Color.BLUE);
		else paint.setColor(Color.RED);
		paint.setStyle(Style.FILL);
		c.drawRect(x - _enegy/20, y - bossHeight/2, x - _enegy/20 + enegy/10, y - bossHeight/2 + 5, paint);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.GRAY);
		c.drawRect(x - _enegy/20, y - bossHeight/2, x + _enegy/20, y - bossHeight/2 + 5, paint);
	}
	
	public boolean checkCollision() {
		float xAirCraft = AirCraft.getX();
		float yAirCraft = AirCraft.getY();
		for (int i = 0; i < bullet.size(); i++) {
			Bullet b = bullet.get(i);
			float xBullet = b.getX();
			float yBullet = b.getY();
			if (Math.abs(xBullet - xAirCraft) <= 30 && b.state && AirCraft.state)
				if (Math.abs(yBullet - yAirCraft) <= 30) {
					b.setState(false);
					AirCraft.setState(false);
					return true;
				}
		}
		for (int i = 0; i < bomb.size(); i++) {
			Bomb b = bomb.get(i);
			if (Math.abs(b.getX() - xAirCraft) <= 35 && Math.abs(b.getY() - yAirCraft) <= 35 && AirCraft.state) {
				AirCraft.setState(false);
				b.state = false;
				bomb.remove(i);
				return true;
			}
		}
		return false;
	}
	
	private void initWeapon(long CurrentTime) {
		if (CurrentTime > Time + 2000) {
			Time = CurrentTime;
			for (int i = 0; i < bullet.size(); i++) {
				if (!bullet.get(i).state) bullet.remove(i);
			}
			if (bosslevel == 1 && bullet.size() == 0 && step == 2) {
				for (int i = 0; i < 3; i++) {
					Bullet b = new Bullet((i - 1) * 100 + x, y);
					b.setSpeed(20);
					bullet.add(b);
				}
			}
			for (int i = 0; i < bomb.size(); i++) {
				if (!bomb.get(i).state) bomb.remove(i);
			}
			if ( bosslevel >= 2 && bomb.size() == 0 && step == 2) {
				bomb.add(new Bomb(x, y));
				bomb.add(new Bomb(x, y));
				bomb.add(new Bomb(x, y));
				bomb.get(0).setTarget();
				bomb.get(1).setTarget();
				bomb.get(2).setTarget();
			}
		}
	}
	
	private void move(long CurrentTime) {	
		if (step == 1) {
			if (CurrentTime > Time + 200) {
				y += 10;
				if (y >= 40 + bossHeight/2) step = 2;
				sprite.setYPosCenter(y);
				Time = CurrentTime;
			}
		}
		if (step == 2 && bosslevel == 1) {
			x += dx;
			if (x >= w + 135) x = 0;
			if ( x <= -135) x = w;
			y += dy;
			sprite.setXPosCenter(x);
			sprite.setYPosCenter(y);
			if (y >= 200) {
				if (dx < 0) dx = 4;
			}
			if (y >= 400) {
				if (dy > 0) dy = -4;
			}
			if (y <= 200) {
				if (dx > 0) dx = -4;
			}
			if (y <= 0) {
				dx = -4;
				dy = 4;
			}
		}
		if (step == 3) {
			if (CurrentTime > Time + 100) {
				if (trans > 1) trans -= 1; else state = false;
			}
		}
	}
	
	public void destroy(int enegy) {
		if (this.enegy >= enegy) this.enegy -= enegy;
		else this.enegy = 0;
		if (this.enegy == 0) step = 3;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public void free() {
		sprite.remove();
	}
}
