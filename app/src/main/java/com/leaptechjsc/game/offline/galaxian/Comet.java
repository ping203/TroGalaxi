package com.leaptechjsc.game.offline.galaxian;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Comet {
	
	public boolean state = true;
	boolean updown;
	
	private float x, y;
	private float w, h;
	private Random rand;
	private Sprite2D sprite;
	
	public Comet(Bitmap bitmap) {
		w = GameSurfaceView.w;
		h = GameSurfaceView.h;		
		rand = new Random();
		sprite = new Sprite2D();
		sprite.init(bitmap, 100, 94, 3, 3);
		if (Math.abs(rand.nextInt()) % 2 == 0) {
			updown = true;
			x = w + 40;
			y = h + 40;
		} else {
			x = w + 40;
			y = 40;
		}
	}
	
	public void draw(Canvas c) {
		move();
		sprite.loop(System.currentTimeMillis());
		if (updown) sprite.drawRotatedCenter(c, 90);
		else sprite.drawCenter(c, 255);
	}
	
	private void move() {
		if (updown) {
			x -= 1;
			y -= 2;
		} else {
			x -= 1;
			y += 2;
		}
		if (x <= -40) state = false;
		sprite.setXPosCenter(x);
		sprite.setYPosCenter(y);
	}

	public void free() {
		sprite.remove();
	}
}
