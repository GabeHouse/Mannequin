package com.example.mannequin;


import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * A simple demo of how to create a rectangular sprite.
 *
 * Michael Terry & Jeff Avery
 */
public class RectangleSprite extends Sprite {

	private Rect rect = null;
	private String imgPath;
	Drawable img;
	int width,height;

	/**
	 * Creates a rectangle based at the origin with the specified
	 * width and height
	 */
	public RectangleSprite(int width, int height) {
		super();
		this.initialize(width, height, imgPath);
	}
	/**
	 * Creates a rectangle based at the origin with the specified
	 * width, height, and parent
	 */
	public RectangleSprite(int width, int height, Sprite parentSprite) {
		super(parentSprite);
		this.initialize(width, height, imgPath);
	}

	private void initialize(int width, int height, String imgPath)
	{
		rect = new Rect(0, 0, width, height);
		this.imgPath = imgPath;
		this.width = width;
		this.height = height;
	}

	/**
	 * Test if our rectangle contains the point specified.
	 */
	public boolean pointInside(PointF p) {
		Matrix inverse = new Matrix(this.getFullTransform());
		inverse.invert(inverse);
		float [] mousepoint = {p.x,p.y};
		Log.d("DEBUG", "pointinside " + mousepoint[0] + ", " + mousepoint[1]);

		inverse.mapPoints(mousepoint);
		if (rect.contains((int)mousepoint[0],(int)mousepoint[1])) {
			return true;
		}
		return false;
	}

	protected void drawSprite(Canvas c) {
		Paint p = new Paint();
		p.setColor(Color.WHITE);
	//	c.drawRect(rect,p);
		if (img != null) {
			Rect imageBounds = c.getClipBounds();  // Adjust this for where you want it
			imageBounds.set(0, 0, width, height);
			img.setBounds(imageBounds);
			img.draw(c);
		}


	}

	public String toString() {
		return "RectangleSprite: " + rect;
	}
}
