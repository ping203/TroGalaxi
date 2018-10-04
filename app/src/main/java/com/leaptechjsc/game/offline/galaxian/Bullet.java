package com.leaptechjsc.game.offline.galaxian;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Bullet {
	
	private float x, y;
	private float speed = -15;
	private long BulletTime;
	private long CurrentTime;
	public boolean state = true;
	private Sprite2D bullet;
	
	public Bullet(float x, float y) {
		this.x = x;
		this.y = y - 30;
		bullet = new Sprite2D();
		bullet.setXPosCenter(x);
		bullet.setYPosCenter(y);
		BulletTime = System.currentTimeMillis();
	}
	
	public void draw(Canvas c) {
		move();
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		c.drawRect(x - 2, y - 10, x + 2, y + 10, paint);
	}
	
	public void move() {
		CurrentTime = System.currentTimeMillis();
		if (CurrentTime > BulletTime + 20) {
			if (speed < 0) this.y += speed;
			if (speed > 0) this.y += speed + GameSurfaceView.level * 1.5;
			BulletTime = CurrentTime;
		}
		if (speed < 0) {
			if (y < 0) state  = false;
		} else if (y > 860) state = false;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setState(boolean state) {
		this.state = state;
	}
	
	public float getX() {
		return this.x;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getY() {
		return this.y;
	}
	
	public void destroy() {
		bullet = null;
	}
}
