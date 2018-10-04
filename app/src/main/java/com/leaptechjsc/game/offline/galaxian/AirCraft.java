package com.leaptechjsc.game.offline.galaxian;

import android.graphics.Canvas;

public class AirCraft {
	public Sprite2D nv;
	public Sprite2D hieuung;
	private static float x;
	private static float y;
	
	private long gametime = 0;
	
	public static boolean state = true;
	
	public AirCraft(Sprite2D sprite1, Sprite2D sprite2, float x, float y) {
		this.nv = sprite1;
		this.hieuung = sprite2;
		this.setX(x);
		this.setY(y);
	}
	
	public void draw(Canvas c) {
		if (state) {
			nv.setXPosCenter(getX());
			nv.setYPosCenter(y);
			nv.drawCenter(c, 255);
		} else {
			hieuung.setXPosCenter(getX());
			hieuung.setYPosCenter(y);
			hieuung.drawCenter(c, 255);
			if (hieuung.currentFrame == 3) {
				hieuung.currentFrame = 0;
				state = true;
			}
			hieuung.playToFrame(System.currentTimeMillis(), 4);
		}
	}
	
	public void move(long time, float pos) {
		if (time > gametime) {
			gametime = time;
			if (x <= pos) x += 8;
			else x -= 8;
		}
	}
	
	public static void setState(boolean state) {
		AirCraft.state = state;
	}
	
	public void setX(float x) {
		AirCraft.x = x;
	}
	
	public void setY(float y) {
		AirCraft.y = y;
	}
	
	public static float getX() {
		return x;
	}
	
	public static float getY() {
		return y;
	}

}
