package com.charles.btviz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class TreeVisualizer extends View {

    // Padding for each node circle
    private static final float NODE_PADDING = 10f;
    private Node root;
    private Paint paint;
    // For pinch-to-zoom
    private float scaleFactor = 1.0f;
    private ScaleGestureDetector scaleDetector;
    // Variables for panning
    private float translateX = 0f, translateY = 0f;
    private float lastTouchX = 0f, lastTouchY = 0f;
    private boolean isDragging = false;

    public TreeVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setStrokeWidth(5);

        scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();
                // Limit zoom level
                scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 3.0f));
                invalidate();
                return true;
            }
        });
    }

    public void setTree(Node root) {
        this.root = root;
        // Redraw the view
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Save canvas state
        canvas.save();
        // Add padding to the left and top
        canvas.translate(20f, 20f);
        // Apply zoom
        canvas.scale(scaleFactor, scaleFactor);
        // Apply pan
        canvas.translate(translateX / scaleFactor, translateY / scaleFactor);

        if (root != null) {
            drawNode(canvas, root, getWidth() / 2, 100, getWidth() / 4);
        }

        // Restore canvas state
        canvas.restore();
    }

    private void drawNode(Canvas canvas, Node node, int x, int y, int offset) {
        if (node.left != null) {
            canvas.drawLine(x, y, x - offset, y + 150, paint);
            drawNode(canvas, node.left, x - offset, y + 150, offset / 2);
        }
        if (node.right != null) {
            canvas.drawLine(x, y, x + offset, y + 150, paint);
            drawNode(canvas, node.right, x + offset, y + 150, offset / 2);
        }

        // Draw node circle with padding
        // Base radius
        float radius = 50f;
        // Increase radius by padding
        canvas.drawCircle(x, y, radius + NODE_PADDING, paint);

        // Draw the value text in the center of the circle
        // Text color is white
        paint.setColor(Color.WHITE);
        // Center text horizontally
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(node.value), x, y + 15, paint);

        // Restore original color for the next node
        paint.setColor(Color.BLACK);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDragging = true;
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    float dx = event.getX() - lastTouchX;
                    float dy = event.getY() - lastTouchY;

                    translateX += dx;
                    translateY += dy;

                    lastTouchX = event.getX();
                    lastTouchY = event.getY();

                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                break;
        }

        return true;
    }

    public void resetView() {
        scaleFactor = 1.0f;
        translateX = 0f;
        translateY = 0f;
        // Redraw the view
        invalidate();
    }

}
