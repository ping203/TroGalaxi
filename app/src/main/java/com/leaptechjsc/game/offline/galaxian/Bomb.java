package com.leaptechjsc.game.offline.galaxian;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Bomb {
	
	public boolean state = true;
	
	private float x, y;
	private float dx = 0, dy = 0;
	private float targetX, targetY;
	
	private boolean setTarget = false;
	
	public Bomb(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void draw(Canvas c) {
		move();
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.FILL);
		c.drawCircle(x, y, 5, paint);
	}
	
	public void setTarget() {
		if (!setTarget) {
			targetX = AirCraft.getX();
			targetY = AirCraft.getY();
			setTarget = true;
		}
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	private void move() {
		if (dy == 0) dy = 10 + GameSurfaceView.level * (float)1.5;
		if (dx == 0) dx = dy *(targetX - x)/Math.abs(targetY - y);
		x += dx;
		y += dy;
		if (y >= GameSurfaceView.h + 30) state = false;
	}
	
}
