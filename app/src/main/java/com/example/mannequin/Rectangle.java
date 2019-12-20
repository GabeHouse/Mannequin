package com.example.mannequin;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

public class Rectangle {
    float x, y;
    float width, height;
    Matrix matrix = new Matrix(); // identity matrix

    // Construct a rectangle with the given dimensions
    // The matrix will be used to determine location (defaults to identity matrix)
    // By default, is drawn with the upper-left corner at the origin
    // Assumes: width and height are positive numbers
    Rectangle(float _width, float _height) {
        x = 0;
        y = 0;
        width = _width;
        height = _height;
    }

    // Translate by dx, dy
    // Appends to the current matrix, so operations are cumulative
    void translate(float dx, float dy) {
        matrix.postTranslate(dx, dy);
    }

    // Scale by sx, sy
    // Appends to the current matrix, so operations are cumulative
    void scale(float sx, float sy) {
        matrix.postScale(sx, sy);
    }

    // Draw using the current matrix
    void draw(Canvas canvas, Paint paint) {
        Matrix oldMatrix = canvas.getMatrix();
        canvas.setMatrix(matrix);
        canvas.drawRect(x, y, x+width, y+height, paint);
        canvas.setMatrix(oldMatrix);
    }
	public boolean pointInside(float x, float y) {

		Matrix inverse = new Matrix(matrix);
		matrix.invert(inverse);
		float [] mousepoint = {x,y};
		Log.d("DEBUG", "pointinside " + mousepoint[0] + ", " + mousepoint[1]);

		inverse.mapPoints(mousepoint);
		Log.d("DEBUG", "pointinside " + mousepoint[0] + ", " + mousepoint[1] + ", " + this.x + ", " + width + ", " + this.y + ", " + height);
		if (mousepoint[0] >= this.x && mousepoint[0] <= this.x + width && mousepoint[1] >= this.y && mousepoint[1] <= this.y + height) {
			return true;
		}
		return false;

	}
    boolean contains(float x, float y){
        Log.d("DEBUG", "contains " + x + ", " + y + ", " + this.x + ", " + this.y);
        if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
            Log.d("DEBUG", "contains ");
            return true;
        }
        return false;
    }
}
