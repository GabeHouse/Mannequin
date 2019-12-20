package com.example.mannequin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.Vector;

public class DrawingView extends View {
    private Vector<Sprite> sprites = new Vector<Sprite>(); // All sprites we're managing
    private Sprite interactiveSprite = null; // Sprite with which user is interacting
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.1f;
    boolean scaling = false;
    Drawable torsoImg;
	Drawable headImg;
	Drawable leftupperarmImg;
	Drawable leftlowerarmImg;
	Drawable lefthandImg;
	Drawable rightupperarmImg;
	Drawable rightlowerarmImg;
	Drawable righthandImg;
	Drawable leftupperlegImg;
	Drawable leftlowerlegImg;
	Drawable leftfootImg;
	Drawable rightupperlegImg;
	Drawable rightlowerlegImg;
	Drawable rightfootImg;
	boolean firstDraw = true;




	public DrawingView(Context context) {
        super(context);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        this.setBackgroundColor(Color.WHITE);


    }
    public void initDoll(Context context) {


        sprites.clear();
        int torsow = 300;
        int torsoh = 400;
        int headw = 120;
        int headh = 120;
        int uaw = 55;
        int uah = 160;
        int law = 40;
        int lah = 110;
        int handw = 40;
        int handh = 60;
        int ulw = 50;
        int ulh = 150;
        int llw = 50;
        int llh = 150;
        int fw = 100;
        int fh = 40;

        torsoImg = context.getResources().getDrawable(R.drawable.torso);
        RectangleSprite torso = new RectangleSprite(torsow,torsoh);
        torso.img = torsoImg;
        torso.translate(this.getWidth()/2 - torsow/2, (this.getHeight() - headh - torsoh - ulh - llh - fh)/2 + 200);
        torso.defaultInteractionMode = Sprite.InteractionMode.DRAGGING;
        torso.interactionMode = Sprite.InteractionMode.DRAGGING;
        sprites.add(torso);

		headImg = context.getResources().getDrawable(R.drawable.head);
        RectangleSprite head = new RectangleSprite(headw,headh,torso);
        head.img = headImg;
        head.translate((torsow - headw)/2,-headh + 5);
        head.maxRotationDegrees = 50;
        head.rotatePoint = new Point(headw/2,headh+15);
        sprites.add(head);
		leftupperarmImg = context.getResources().getDrawable(R.drawable.leftupperarm);
        RectangleSprite leftupperarm = new RectangleSprite(uaw,uah,torso);
        leftupperarm.img = leftupperarmImg;
        leftupperarm.translate(0,75);
        leftupperarm.rotatePoint = new Point(uaw/2,-10);
		leftlowerarmImg = context.getResources().getDrawable(R.drawable.leftlowerarm);
        RectangleSprite leftlowerarm = new RectangleSprite(law,lah,leftupperarm);
        leftlowerarm.img = leftlowerarmImg;
        leftlowerarm.translate(10,uah);
        leftlowerarm.rotatePoint = new Point(law/2,-10);
        leftlowerarm.maxRotationDegrees = 135;
		lefthandImg = context.getResources().getDrawable(R.drawable.lefthand);
        RectangleSprite lefthand = new RectangleSprite(handw,handh,leftlowerarm);
        lefthand.img = lefthandImg;
        lefthand.translate(0,lah);
        lefthand.rotatePoint = new Point(handw/2,-10);
        lefthand.maxRotationDegrees = 35;
        sprites.add(leftupperarm);
        sprites.add(leftlowerarm);
        sprites.add(lefthand);

        RectangleSprite rightupperarm = new RectangleSprite(uaw,uah,torso);
		rightupperarmImg = context.getResources().getDrawable(R.drawable.rightupperarm);
		rightupperarm.img = rightupperarmImg;
        rightupperarm.translate(torsow - uaw,75);
        rightupperarm.rotatePoint = new Point(uaw/2,-10);
        RectangleSprite rightlowerarm = new RectangleSprite(law,lah,rightupperarm);
		rightlowerarmImg = context.getResources().getDrawable(R.drawable.rightlowerarm);
		rightlowerarm.img = rightlowerarmImg;
        rightlowerarm.translate(0,uah);
        rightlowerarm.rotatePoint = new Point(law/2,-10);
        rightlowerarm.maxRotationDegrees = 135;
        RectangleSprite righthand = new RectangleSprite(handw,handh,rightlowerarm);
		righthandImg = context.getResources().getDrawable(R.drawable.righthand);
		righthand.img = righthandImg;
        righthand.translate(0,lah);
        righthand.rotatePoint = new Point(handw/2,-10);
        righthand.maxRotationDegrees = 35;
        sprites.add(rightupperarm);
        sprites.add(rightlowerarm);
        sprites.add(righthand);

        RectangleSprite rightupperleg = new RectangleSprite(ulw,ulh,torso);
		rightupperlegImg = context.getResources().getDrawable(R.drawable.rightupperleg);
		rightupperleg.img = rightupperlegImg;
        rightupperleg.translate(torsow - 75 - ulw-2,torsoh);
        rightupperleg.rotatePoint = new Point(ulw/2,-15);
        rightupperleg.scalable = true;
        rightupperleg.maxRotationDegrees = 90;
        RectangleSprite rightlowerleg = new RectangleSprite(llw,llh,rightupperleg);
		rightlowerlegImg = context.getResources().getDrawable(R.drawable.rightlowerleg);
		rightlowerleg.img = rightlowerlegImg;
        rightlowerleg.translate(-5,ulh);
        rightlowerleg.rotatePoint = new Point(llw/2,-10);
        rightlowerleg.maxRotationDegrees = 90;
        rightlowerleg.scalable = true;
        RectangleSprite rightfoot = new RectangleSprite(fw,fh,rightlowerleg);
		rightfootImg = context.getResources().getDrawable(R.drawable.rightfoot);
		rightfoot.img = rightfootImg;
        rightfoot.translate(0,llh-10);
        rightfoot.rotatePoint = new Point(20,-10);
        rightfoot.maxRotationDegrees = 35;
        sprites.add(rightupperleg);
        sprites.add(rightlowerleg);
        sprites.add(rightfoot);

        RectangleSprite leftupperleg = new RectangleSprite(ulw,ulh,torso);
		leftupperlegImg = context.getResources().getDrawable(R.drawable.leftupperleg);
		leftupperleg.img = leftupperlegImg;
        leftupperleg.translate(75,torsoh);
        leftupperleg.rotatePoint = new Point(ulw/2,-15);
        leftupperleg.scalable = true;
        leftupperleg.maxRotationDegrees = 90;
        RectangleSprite leftlowerleg = new RectangleSprite(llw,llh,leftupperleg);
		leftlowerlegImg = context.getResources().getDrawable(R.drawable.leftlowerleg);
		leftlowerleg.img = leftlowerlegImg;
        leftlowerleg.translate(5,ulh);
        leftlowerleg.rotatePoint = new Point(llw/2-2,-10);
        leftlowerleg.maxRotationDegrees = 90;
        leftlowerleg.scalable = true;
        RectangleSprite leftfoot = new RectangleSprite(fw,fh,leftlowerleg);
		leftfootImg = context.getResources().getDrawable(R.drawable.leftfoot);
		leftfoot.img = leftfootImg;
        leftfoot.translate(-(fw - llw),llh-10);
        leftfoot.rotatePoint = new Point(fw - 20,-10);
        leftfoot.maxRotationDegrees = 35;
        sprites.add(leftupperleg);
        sprites.add(leftlowerleg);
        sprites.add(leftfoot);
        leftfoot.stretchable = false;
        rightfoot.stretchable = false;
        this.invalidate();
    }
    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaling = true;
            if (interactiveSprite != null) {

              //  mScaleFactor *= ;
                Log.d("DEBUG", "scalefactor " + mScaleFactor);
                // Don't let the object get too small or too large.
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

                interactiveSprite.scale((1 + (detector.getScaleFactor()-1)/2));
            }
            return true;
        }

    }
    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (firstDraw) {
            initDoll(this.getContext());
            firstDraw = false;
        }
        for (Sprite sprite : sprites) {
            sprite.draw(canvas);
        }

    }
    float oy = 0, ox = 0;
    private void touch_start(MotionEvent e) {
        /*
        ox = x;
        oy = y;
         */

        float x = e.getX();
        float y = e.getY();
        Log.d("DEBUG", "pc " + e.getPointerCount());
        for (Sprite sprite : sprites) {
            interactiveSprite = sprite.getSpriteHit(x, y);
            if (interactiveSprite != null) {
                interactiveSprite.handleTouchStartEvent(e);

                break;
            }
        }
    }
    private void touch_move(MotionEvent e) {

        if (interactiveSprite != null)
            interactiveSprite.interactionMode = interactiveSprite.defaultInteractionMode;
        float x = -10000;
        float y = -100000;
        if (!scaling) {
            x = e.getX();
            y = e.getY();
            if (interactiveSprite != null) {
                interactiveSprite.handleTouchMoveEvent(e);
            }
        }
        Log.d("DEBUG", "pc " + e.getPointerCount());
        if (e.getPointerCount() == 2) {

            x = (e.getX(0) + e.getX(1))/2;
            y = (e.getY(0) + e.getY(1))/2;
            if (interactiveSprite == null || scaling == false) {
                for (Sprite sprite : sprites) {
                    interactiveSprite = sprite.getSpriteHit(x, y);
                    if (interactiveSprite != null) {

                        if (e.getPointerCount() == 2) {

                            interactiveSprite.interactionMode = Sprite.InteractionMode.SCALING;
                        }
                        interactiveSprite.handleTouchMoveEvent(e);
                        break;
                    }
                }

            }
        }



    }
    private void touch_up(MotionEvent e) {
        mScaleFactor = 1;
        if (interactiveSprite != null) {
            interactiveSprite.handleTouchUpEvent(e);

        }
        scaling = false;
        interactiveSprite = null;

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mScaleDetector.onTouchEvent(event);

        Log.d("DEBUG", "touch " + event.getX() + "," + event.getY());

        if (true) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                        touch_start(event);

                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(event);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up(event);
                    invalidate();
                    break;
            }
        }
        return true;
    }
}
