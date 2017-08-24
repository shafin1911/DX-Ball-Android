package com.shafin.sdxball;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("DrawAllocation")
public class GameCanvas extends View implements Runnable
{
	Paint paint;
    Circle ballObject;
    Bar barObject;
    int BrickColor1, BrickColor2;
	public static boolean gameOver;
    public static boolean newLife;
    public static int life, canvasHeight, canvasWidth;
    float barWidth = 200;
    float brickX = 0, brickY = 200;
    int score = 0;
    float barLeft, barRight, barTop, barBottom;
    float clicked, unClicked;
    int level = 1, row = 0, ballSpeed = 6;
    boolean start = true;
    float xDelta = 0, a=0;
    Bitmap b,c;
    boolean giveLife = false;
    float lx = 0, ly = 0;
    int lrandom = 0, incRandom = 0;
    boolean increased;
    
	ArrayList<Objects> bricks = new ArrayList<Objects>();

    public GameCanvas(Context context) 
    {
        super(context);
        paint = new Paint();
        life = 5;
        gameOver = false;
        newLife = false;
    }


    @Override
    protected void onDraw(Canvas canvas) 
    {
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();

        if(start == true)
        {
        	Random r = new Random();
        	lrandom = r.nextInt(14);
        	lx = 0; ly = 0; giveLife = false;
        	incRandom = 0;
        	increased = false;
        	
            start = false;
            switch(level)
            {
            	case 1: row = 3; BrickColor1 = Color.rgb(140, 12, 121); BrickColor2 = Color.rgb(239, 35, 236);
                break;
            	case 2: row = 5; BrickColor1 = Color.rgb(150, 148, 27); BrickColor2 = Color.rgb(214, 210, 8);
                break;
            	case 3: row = 7; BrickColor1 = Color.rgb(53, 117, 10); BrickColor2 = Color.rgb(91, 168, 3);
                break;
            	default:row = 9; BrickColor1 = Color.rgb(7, 127, 107); BrickColor2 = Color.rgb(8, 217, 224);
            	break;
            }
            
            for(int j = 0; j < row; j++)
            {
            	int color;
	            for(int i = 0; i < 5 ; i++)
	            {
	                if(brickX >= canvas.getWidth()) 
	                {
	                    brickX = 0;
	                    brickY += canvas.getWidth()/20;
	                }
	                
	                if(j % 2 == 0)
	                    color = BrickColor1; 
	                else
	                	color = BrickColor2; 

	                bricks.add(new Objects(brickX, brickY, brickX+(canvas.getWidth()/5)-2, brickY+(canvas.getWidth()/20)-5,color));
	                brickX += canvas.getWidth() / 5;
	            }
            }
            
          
            ballObject = new Circle( canvas.getWidth()/2, canvas.getHeight()-100, Color.GRAY, 22);
            ballObject.setDx(ballSpeed);
            ballObject.setDy(-ballSpeed);
            
            barLeft = getWidth() / 2 - (barWidth / 2);
            barTop = getHeight() - 20;
            barRight = getWidth() / 2 + (barWidth / 2);
            barBottom = getHeight();
            barObject = new Bar(barLeft, barTop, barRight, barBottom, Color.WHITE);
            
            b = BitmapFactory.decodeResource(getResources(), R.raw.heart);
            c = getResizedBitmap(b, canvas.getWidth()/8, canvas.getHeight()/20);
        }

        if(bricks.size() <= 0)
        {
        	level++;
        	life = 5;
            gameOver = false;
            newLife = false;
            start = true;
            ballSpeed++;
            
            barObject.setBottom(barBottom);
            barObject.setLeft(barLeft);
            barObject.setRight(barRight);
            barObject.setTop(barTop);
        }
        
        if(newLife && !start)
        {
            newLife = false;
            if(life >= 4)
            {
            	ballObject = new Circle(canvas.getWidth()/2,canvas.getHeight()-50, Color.rgb(39, 151, 165), 20);
            }
            
            if(life == 2 || life == 3)
            {
            	ballObject = new Circle(canvas.getWidth()/2,canvas.getHeight()-50, Color.rgb(106, 38, 165), 15);
            }
            
            else if(life == 1)
            {
            	ballObject = new Circle(canvas.getWidth()/2,canvas.getHeight()-50, Color.rgb(173, 10, 37), 10);
            }
            
            ballObject.setDx(ballSpeed);
            ballObject.setDy(-ballSpeed);
        }
        
        if(gameOver)
        {
            paint.setColor(Color.BLACK);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            paint.setFakeBoldText(true);
            
            canvas.drawText("GAME OVER",canvas.getWidth()/2 - 200, canvas.getHeight()/2, paint);
            canvas.drawText("SCORE: " + score, canvas.getWidth()/2 - 200 , canvas.getHeight()/2+60, paint);
            gameOver = false;

            try 
            {
                Thread.sleep(1000);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
            ((MainActivity)getContext()).finish();
        }
        
        paint.setTextSize(30);
        paint.setFakeBoldText(true);
        paint.setColor(Color.WHITE);
        canvas.drawText("Score: "+score, 10, 30, paint);

        paint.setTextSize(30);
        paint.setFakeBoldText(true);
        paint.setColor(Color.WHITE);
        canvas.drawText("Life: "+life, 180, 30, paint);
        
        paint.setTextSize(30);
        paint.setFakeBoldText(true);
        paint.setColor(Color.WHITE);
		canvas.drawText("LEVEL: "+level , 300 , 30, paint);
		
		canvas.drawCircle(ballObject.getX(), ballObject.getY(), ballObject.getRadius(), ballObject.getPaint());
		
        canvas.drawRect(barObject.getLeft(), barObject.getTop(), barObject.getRight(), barObject.getBottom(), barObject.getPaint());

        for(int i=0; i < bricks.size(); i++)
        {
            canvas.drawRect(bricks.get(i).getLeft(),bricks.get(i).getTop(),bricks.get(i).getRight(),bricks.get(i).getBottom(),bricks.get(i).getPaint());
        }
        
        this.ballBrickCollision(canvas);
        this.ballBarCollision(canvas);
        this.ballBoundaryCollision(canvas);
        this.lifeBarCollision(canvas);

        ballObject.move();
        
		if(giveLife)
        {
			paint.setColor(Color.RED);
        	canvas.drawBitmap(c, lx, ly++, paint);
        	if(ly >= canvasHeight)
        	{
        		giveLife = false;
        	}
        }
        this.run();
    }
    
    public void ballBoundaryCollision(Canvas canvas) 
    {
        if((ballObject.getY() - ballObject.getRadius()) >= canvas.getHeight())
        {
            life -= 1;
            newLife = true;
        }

        if(life == 0)
        	gameOver = true;
        else
        {
	        if((ballObject.getX() + ballObject.getRadius()) >= canvas.getWidth() || (ballObject.getX() - ballObject.getRadius()) <= 0)
	        {
	        	ballObject.setDx(-ballObject.getDx());
	        }
	        
	        if( (ballObject.getY() - ballObject.getRadius()) <= 0)
	        {
	        	ballObject.setDy(-ballObject.getDy());
	        }
        }
    }
    
    public void ballBarCollision(Canvas canvas)
    {
        if(((ballObject.getY() + ballObject.getRadius()) >= barObject.getTop()) && ((ballObject.getY()+ballObject.getRadius()) <= barObject.getBottom()) && ((ballObject.getX()) >= barObject.getLeft()) && ((ballObject.getX()) <= barObject.getRight())) 
        {
        	ballObject.setDy(-(ballObject.getDy()));
        }

    }
    
    public void ballBrickCollision(Canvas canvas)
    {
        for(int i=0; i < bricks.size(); i++) 
        {
            if (((ballObject.getY() - ballObject.getRadius()) <= bricks.get(i).getBottom()) && ((ballObject.getY() + ballObject.getRadius()) >= bricks.get(i).getTop()) && ((ballObject.getX()) >= bricks.get(i).getLeft()) && ((ballObject.getX()) <= bricks.get(i).getRight())) 
            {
            	if(bricks.get(i).getColor() == BrickColor2)
            	{
            		bricks.get(i).setColor(BrickColor1);
            		bricks.get(i).getPaint().setColor(BrickColor1);
            	}
            	else
            	{
            		if(incRandom++ == lrandom)
            		{
            			lx = bricks.get(i).getLeft();
            			ly = bricks.get(i).getTop();
            			giveLife = true;
            		}
	                score += 1;
	                bricks.remove(i);
            	}
            	ballObject.setDy(-(ballObject.getDy()));
            }
        }
    }
    
    public void lifeBarCollision(Canvas canvas)
    {
        if((ly  > barObject.getTop()) && (ly < barObject.getBottom()) && (lx > barObject.getLeft()) && (lx < barObject.getRight())) 
        {
        	if(!increased && life < 5)
        	{
        		increased = true;
        		life++;
        	}
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK) 
        {
            case MotionEvent.ACTION_DOWN:
	            	clicked = ev.getX();
	            	xDelta = barObject.getLeft();
	                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                	unClicked = ev.getX();
                	a = unClicked - clicked;
                	if(canvasWidth+10 >= xDelta + a + barWidth && xDelta + a >= -10) 
                    {
                		barObject.setLeft(xDelta + a);
                        barObject.setRight(xDelta + a + barWidth); 
                    }
                	
                break;
            default:
                break;
        }
        return true;
    }
    
    
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) 
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
    
    
    @Override
    public void run()
    {
        invalidate();
    }
}
