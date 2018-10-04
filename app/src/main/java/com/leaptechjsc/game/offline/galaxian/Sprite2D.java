package com.leaptechjsc.game.offline.galaxian;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sprite2D 
{
	//private static String Tag="Sprite2D";
	private Bitmap src;	//áº¢nh chá»©a sprite sheet
    private int xPos;	//Vá»‹ trĂ­ left cá»§a sprite trĂªn screen
    private int yPos;	//Vá»‹ trĂ­ top cá»§a sprite trĂªn screen
    private float xPosCenter;	// Vá»‹ trĂ­ chĂ­nh giá»¯a tĂ¢m cá»§a sprite trĂªn screen
    private float yPosCenter;	// Vá»‹ trĂ­ chĂ­nh giá»¯a tĂ¢m cá»§a sprite trĂªn screen
    private Rect rect;	//HĂ¬nh chá»¯ nháº­t bao quanh má»™t Ă´ trong sheet
    private int fps;	//Frame per second
    private int totalFrame;	//Tá»•ng sá»‘ frame
    /**Frame hiá»‡n thá»�i, báº¯t Ä‘áº§u tá»« 0*/
    public int currentFrame;	
    private long frameTime;	//Thá»�i gian Ä‘á»ƒ cháº¡y má»™t frame, thÆ°á»�ng báº±ng 0 Ä‘á»ƒ láº¥y thá»�i gian lĂ  fps
    private int spriteHeight;	//Chiá»�u cao cá»§a sprite
    private int spriteWidth;	// Chiá»�u rá»™ng cá»§a sprite
    
    
    public Sprite2D() {
        rect = new Rect(0,0,0,0);
        frameTime =0;
        currentFrame =0;
        xPos = 0;
        yPos = 0;
    }
    
    /**Khá»Ÿi táº¡o sprite vá»›i vá»‹ trĂ­ ban Ä‘áº§u (left,top) trĂªn screen*/
    public Sprite2D(int xPos,int yPos)
    {
    	rect = new Rect(0,0,0,0);
        frameTime =0;
        currentFrame =0;
    	this.xPos=xPos;
    	this.yPos=yPos;
    }
    
    /**Khá»Ÿi táº¡o sprite vá»›i vá»‹ trĂ­ ban Ä‘áº§u á»Ÿ chĂ­nh giá»¯a tĂ¢m sprite trĂªn screen*/
    public Sprite2D(float xPosCenter,float yPosCenter,boolean b)
    {
    	rect = new Rect(0,0,0,0);
        frameTime =0;
        currentFrame =0;
    	this.xPosCenter=xPosCenter;
    	this.yPosCenter=yPosCenter;
    }
 
    /**Khá»Ÿi táº¡o cĂ¡c giĂ¡ trá»‹ nhÆ° áº£nh, chiá»�u cao, rá»™ng, fps, tá»•ng sá»‘ frame*/
    public void init(Bitmap theBitmap, int Width, int Height, int theFPS, int theFrameCount) {
        src = theBitmap;
//        spriteHeight = Height;
//        spriteWidth = Width;
        spriteHeight = src.getHeight();
        spriteWidth = src.getWidth()/theFrameCount;
        rect.top = 0;
        rect.bottom = spriteHeight;
        rect.left = 0;
        rect.right = spriteWidth;
        fps = 1000 /theFPS;
        totalFrame = theFrameCount;
        
        //Log.d(Tag,"mSpriteHeight="+spriteHeight+" "+"mSpriteWidth="+spriteWidth);
    }
 
    /**Cháº¡y sprite liĂªn tá»¥c*/
    public void loop(long GameTime) {
        if(GameTime > frameTime + fps ) {
            frameTime = GameTime;
            currentFrame +=1;
 
            if(currentFrame >= totalFrame) {
                currentFrame = 0;
            }
        }
        
        //Log.d(Tag,"currentFrame="+currentFrame);
 
        rect.left = currentFrame * spriteWidth;
        rect.right = rect.left + spriteWidth;
    }
    
    /**Cháº¡y sprite liĂªn tá»¥c tá»« frame 1 Ä‘áº¿n frame Ä‘Æ°á»£c chá»‰ ra, rá»“i tiáº¿p tá»¥c quay láº¡i tá»« frame 1*/
    public void loopToFrame(long GameTime, int frame) {
        if(GameTime > frameTime + fps ) {
            frameTime = GameTime;
            currentFrame +=1;
 
            if(currentFrame >= frame) {
                currentFrame = 0;
            }
        }
        
        //Log.d(Tag,"currentFrame="+currentFrame);
 
        rect.left = currentFrame * spriteWidth;
        rect.right = rect.left + spriteWidth;
    }
    
    /**Cháº¡y sprite má»™t láº§n cho Ä‘áº¿n frame Ä‘Æ°á»£c chá»‰ ra
     * .Thá»© tá»± frame báº¯t Ä‘áº§u tá»« 1*/
    public void playToFrame(long GameTime, int frame)
    {
    	if ((currentFrame<totalFrame)&&(GameTime > frameTime + fps)) 
    	{
            frameTime = GameTime;
            currentFrame +=1;   
            
            if(currentFrame >= frame) {
                currentFrame = frame-1;
            }
        }                
    	
    	//Log.d(Tag,"currentFrame="+currentFrame);
    	
    	rect.left = currentFrame * spriteWidth;
        rect.right = rect.left + spriteWidth;    	
    }
    
    /**Cháº¡y sprite má»™t láº§n vĂ  quay trá»Ÿ láº¡i frame Ä‘áº§u tiĂªn*/
    public void playToBegin(long GameTime)
    {
    	boolean isPlaying=true;
    	if ((currentFrame<totalFrame)&&(GameTime > frameTime + fps)&&isPlaying) 
    	{
            frameTime = GameTime;
            currentFrame +=1;   
            
            if(currentFrame >= totalFrame) {
                currentFrame = 0;
                isPlaying=false;
            }
        }                
    	
    	//Log.d(Tag,"currentFrame="+currentFrame);
    	
    	rect.left = currentFrame * spriteWidth;
        rect.right = rect.left + spriteWidth;    	
    }
    
    /**Chuyá»ƒn Ä‘áº¿n frame Ä‘Æ°á»£c Ä‘Ă¡nh sá»‘ báº¯t Ä‘áº§u tá»« 1*/
    public void gotoAndStop(int frame)
    {
    	currentFrame=frame-1;
    	
    	rect.left = currentFrame * spriteWidth;
        rect.right = rect.left + spriteWidth; 
    }
 
    /**Váº½ sprite*/
    public void draw(Canvas canvas) {
        Rect dest = new Rect(getXPos(), getYPos(), getXPos() + spriteWidth,
                        getYPos() + spriteHeight);
 
        canvas.drawBitmap(src, rect, dest, null);
    }
    
    /**Váº½ sprite vá»›i biáº¿n trans Ä‘á»ƒ lĂ m má»�*/
    public void draw(Canvas canvas, int trans) 
    {
    	Paint paint=new Paint();
    	paint.setAlpha(trans);
        Rect dest = new Rect(getXPos(), getYPos(), getXPos() + spriteWidth,
                        getYPos() + spriteHeight);
 
        canvas.drawBitmap(src, rect, dest, paint);
    }
    
    /**Váº½ sprite tĂ­nh tá»« Ä‘iá»ƒm trung tĂ¢m vá»›i biáº¿n trans Ä‘á»ƒ lĂ m má»�*/
    public void drawCenter(Canvas canvas, int trans) 
    {
    	Paint paint=new Paint();
    	paint.setAlpha(trans);
        Rect dest = new Rect((int)(getXPosCenter()-spriteWidth/2), (int)(getYPosCenter()-spriteHeight/2), 
        		(int)(getXPosCenter() + spriteWidth/2), (int)(getYPosCenter() + spriteHeight/2));
 
        canvas.drawBitmap(src, rect, dest, paint);
    } 
    
    public void drawRotatedCenter(Canvas canvas, float degrees) 
    {
    	Paint paint=new Paint();
        Matrix m = new Matrix();
        m.postRotate(degrees);
        Bitmap rotatedBMP = Bitmap.createBitmap(src, rect.left, 0, src.getWidth()/totalFrame, src.getHeight(), m, true);
        canvas.drawBitmap(rotatedBMP, (int)(getXPosCenter() - spriteWidth/2), (int)(getYPosCenter() - spriteHeight/2), paint);
    }   
    
    /**DĂ¹ng Ä‘á»ƒ xĂ³a Sprite khá»�i screen*/
    public void remove()
    {
    	src.recycle();
    }
    
    public int getXPos()
    {
    	return xPos;
    }
    
    public int getYPos()
    {
    	return yPos;
    }
    
    public float getXPosCenter()
    {
    	return xPosCenter;
    }    
    
    public float getYPosCenter()
    {
    	return yPosCenter;
    }
    
    public void setXPos(int xPos)
    {
    	this.xPos=xPos;
    }
    
    public void setYPos(int yPos)
    {
    	this.yPos=yPos;
    }
    
    public void setXPosCenter(float xPosCenter)
    {
    	this.xPosCenter=xPosCenter;
    }
    
    public void setYPosCenter(float yPosCenter)
    {
    	this.yPosCenter=yPosCenter;
    }
}
	

