package com.example.mannequin;


import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Vector;

/**
 * A building block for creating your own shapes that can be
 * transformed and that can respond to input. This class is
 * provided as an example; you will likely need to modify it
 * to meet the assignment requirements.
 *
 * Michael Terry & Jeff Avery
 */
public abstract class Sprite {

	/**
	 * Tracks our current interaction mode after a mouse-down
	 */
	protected enum InteractionMode {
		IDLE,
		DRAGGING,
		SCALING,
		ROTATING
	}

	private Sprite parent = null;                                       // Pointer to our parent
	private Vector<Sprite> children = new Vector<Sprite>();             // Holds all of our children
	private Matrix transform = new Matrix();
	private Matrix translate = new Matrix();
	private Matrix rotate = new Matrix();
	private float scale = 1f;
	protected PointF lastPoint = null;// Last mouse point
	protected InteractionMode defaultInteractionMode = InteractionMode.ROTATING;
	protected InteractionMode interactionMode = defaultInteractionMode;   // current state

	boolean draggable = false;
	float localRotateDegrees;
	int maxRotationDegrees = Integer.MAX_VALUE;
	int localTranslateX = 0;
	int localTranslateY = 0;
	boolean scalable = false;
	boolean stretchable = true;

	Point rotatePoint = new Point(75,150);

	public Sprite() {
	}


	public Sprite(Sprite parent) {
		if (parent != null) {
			parent.addChild(this);
		}
	}

	public void addChild(Sprite s) {
		children.add(s);
		s.setParent(this);
	}
	public Sprite getParent() {
		return parent;
	}
	private void setParent(Sprite s) {
		this.parent = s;
	}

	/**
	 * Test whether a point, in world coordinates, is within our sprite.
	 */
	public abstract boolean pointInside(PointF p);

	/**
	 * Handles a mouse down event, assuming that the event has already
	 * been tested to ensure the mouse point is within our sprite.
	 */
	protected void handleTouchStartEvent(MotionEvent e) {
		lastPoint = new PointF(e.getX(),e.getY());
	}

	/**
	 * Handle mouse drag event, with the assumption that we have already
	 * been "selected" as the sprite to interact with.
	 * This is a very simple method that only works because we
	 * assume that the coordinate system has not been modified
	 * by scales or rotations. You will need to modify this method
	 * appropriately so it can handle arbitrary transformations.
	 */
	protected void handleTouchMoveEvent(MotionEvent e) {
		PointF oldPoint = lastPoint;
		PointF newPoint = new PointF(e.getX(),e.getY());
		float x_diff, y_diff;
		switch (interactionMode) {
			case IDLE:
				; // no-op
				break;
			case DRAGGING:
				 x_diff = newPoint.x - oldPoint.x;
				 y_diff = newPoint.y - oldPoint.y;

				this.translate(x_diff, y_diff);
				break;
			case ROTATING:
				 x_diff = newPoint.x - oldPoint.x;
				 y_diff = newPoint.y - oldPoint.y;

				Matrix inverse = new Matrix(getFullTransform());
				inverse.invert(inverse);

				float [] point = {e.getX(), e.getY()};
				float [] op = {oldPoint.x, oldPoint.y};
				inverse.mapPoints(op);
				inverse.mapPoints(point);
				float a = (float)(Math.sqrt(y_diff*y_diff + x_diff*x_diff));
				float b = (float)Math.sqrt(((point[1] - rotatePoint.y)*(point[1] - rotatePoint.y))
											+ ((point[0] - rotatePoint.x)*(point[0] - rotatePoint.x)));
				float c = (float)Math.sqrt(((op[1] - rotatePoint.y)*(op[1] - rotatePoint.y))
						+ ((op[0] - rotatePoint.x)*(op[0] - rotatePoint.x)));
				float A;
				if (-2*b*c == 0) {
					A = 0;
				} else {

					A = (float)Math.acos((a*a - b*b - c*c)/(-2*b*c));
					Log.d("DEBUG", "rotate1 " + (-2*b*c) + ", " + A);
				}
				if (Float.isNaN(A)) {
					A = 0.01f;
				}

				int dir = 1;
				float crossProduct = (op[0] - rotatePoint.x)*(point[1] - rotatePoint.y) - (op[1] - rotatePoint.y)*(point[0] - rotatePoint.x);
				if (crossProduct < 0)
					dir = -1;
				Log.d("DEBUG", "rotate " + a + ", " + b + ", " + c + ", " + Math.toDegrees(A) + "," + crossProduct);
				localRotateDegrees += (float)Math.toDegrees(A)*dir;
				if (localRotateDegrees < -maxRotationDegrees) {
					localRotateDegrees = -maxRotationDegrees;
				} else if (localRotateDegrees > maxRotationDegrees) {
					localRotateDegrees = maxRotationDegrees;
				}
				rotate.postRotate((float)Math.toDegrees(A)*dir,rotatePoint.x,rotatePoint.y);

				break;
			case SCALING:
		}
		lastPoint = newPoint;
	}

	protected void handleTouchUpEvent(MotionEvent e) {
		interactionMode = defaultInteractionMode;
	}

	/**
	 * Locates the sprite that was hit by the given event.
	 * You *may* need to modify this method, depending on
	 * how you modify other parts of the class.
	 *
	 * @return The sprite that was hit, or null if no sprite was hit
	 */

	public Sprite getSpriteHit(float x, float y) {
		for (Sprite sprite : children) {
			Sprite s = sprite.getSpriteHit(x,y);
			if (s != null) {
				return s;
			}
		}
		if (this.pointInside(new PointF(x, y))) {
			return this;
		}
		return null;
	}

	/*
	 * Important note: How transforms are handled here are only an example. You will
	 * likely need to modify this code for it to work for your assignment.
	 */

	/**
	 * Returns the full transform to this object from the root
	 */
	public Matrix getFullTransform() {
		Matrix returnTransform = new Matrix();

		if (stretchable)
			returnTransform.postConcat(getFullScale());
		returnTransform.postConcat(getFullRotate());
		returnTransform.postConcat(getFullTranslate());

		return returnTransform;
	}
	public Matrix getFullScale() {
		Matrix returnScale = new Matrix();
		Sprite curSprite = this;
		while (curSprite != null) {
			returnScale.preConcat(curSprite.getLocalScale());
			curSprite = curSprite.getParent();
		}
		return returnScale;
	}
	public Matrix getFullTranslate() {
		Matrix returnTranslate = new Matrix();
		Sprite curSprite = this;
		while (curSprite != null) {
			float parentScale = 1;
			if (curSprite.parent != null) {
				parentScale = curSprite.parent.scale;
			}
			returnTranslate.preConcat(curSprite.getLocalTranslate());
			curSprite = curSprite.getParent();
		}
		return returnTranslate;
	}
	public Matrix getFullRotate() {

		Matrix returnRotate = new Matrix();
		Sprite curSprite = this;
		while (curSprite != null) {

			Matrix relativeTranslate = new Matrix();
			Sprite curCurSprite = this;
			while (curCurSprite != curSprite) {
				relativeTranslate.preConcat(curCurSprite.getLocalTranslate());
				curCurSprite = curCurSprite.getParent();
			}
			Matrix relativeTranslateInverse = new Matrix(relativeTranslate);
			relativeTranslateInverse.invert(relativeTranslateInverse);
			float [] relativeCurSpriteRP = {curSprite.rotatePoint.x, curSprite.rotatePoint.y};
			relativeTranslateInverse.mapPoints(relativeCurSpriteRP);
			returnRotate.postRotate(curSprite.localRotateDegrees, relativeCurSpriteRP[0], relativeCurSpriteRP[1]);
			curSprite = curSprite.getParent();
		}
		return returnRotate;
	}

	/**
	 * Returns our local transform
	 */
	public Matrix getLocalTransform() {
		return new Matrix(transform);
	}
	public Matrix getLocalTranslate() {
		Matrix returnTranslate = new Matrix();
		float parentScale = 1;
			Sprite curSprite = parent;
			while (curSprite != null) {
				parentScale *= curSprite.scale;
				curSprite = curSprite.getParent();
			}


		returnTranslate.postTranslate(localTranslateX, localTranslateY*parentScale);
		return returnTranslate;
	}
	public Matrix getLocalRotate() {
		return new Matrix(rotate);
	}
	public Matrix getLocalScale() {
		Matrix returnScale = new Matrix();
		returnScale.setScale(1,scale);
		return returnScale;
	}
	/**
	 * Performs an arbitrary transform on this sprite
	 */
	public void transform(Matrix t) {
		transform.postConcat(t);
	}
	public void translate(float dx, float dy) {
		translate.postTranslate(dx,dy);
		localTranslateX += dx;
		localTranslateY += dy;

	}
	public void scale(float scaleFactor) {
		if (scalable) {
			scale *= scaleFactor;
		}
	}

	/**
	 * Draws the sprite. This method will call drawSprite after
	 * the transform has been set up for this sprite.
	 */
	public void draw(Canvas c) {
		Matrix oldTransform = c.getMatrix();
		// Set to our transform
		Matrix currentAT = c.getMatrix();
		currentAT.postConcat(getFullTransform());
		c.setMatrix(currentAT);
		// Draw the sprite (delegated to sub-classes)
		this.drawSprite(c);
		// Restore original transform
		c.setMatrix(oldTransform);
		// Draw children
		for (Sprite sprite : children) {
			sprite.draw(c);
		}
	}

	/**
	 * The method that actually does the sprite drawing. This method
	 * is called after the transform has been set up in the draw() method.
	 * Sub-classes should override this method to perform the drawing.
	 */
	protected abstract void drawSprite(Canvas c);
}
