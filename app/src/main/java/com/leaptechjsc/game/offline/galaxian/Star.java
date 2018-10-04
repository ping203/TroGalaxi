package com.leaptechjsc.game.offline.galaxian;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Star {
	
	private Random rand;
	private float w;
	private float h;
	private float x;
	private float y = 0;
	private int speed = 2;
	private Sprite2D sprite;
	private long Time;
	
	public Star(Bitmap bitmap) {
		rand = new Random();
		sprite = new Sprite2D();
		sprite.init(bitmap, 60, 60, 2, 4);
		this.w = GameSurfaceView.w;
		this.h = GameSurfaceView.h;
		x = getRandom();
		speed = getRandom()%3 + 1;
	}
	
	public void draw(Canvas c) {
		move(System.currentTimeMillis());
		sprite.loop(System.currentTimeMillis());
		sprite.drawCenter(c, 255);
	}
	
	private void move(long CurrentTime) {
		if (CurrentTime > Time + 200) {
			if (y < h) y += speed;
			else {
				y = 0;
				x = getRandom();
			}
			sprite.setXPosCenter(x);
			sprite.setYPosCenter(y);
			Time = CurrentTime;
		}
	}
	
	private int getRandom() {
		return Math.abs(rand.nextInt())%(int)w + 1;
	}
	
	public float getPos() {
		return this.y;
	}
	
}
