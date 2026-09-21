package com.example.climbingcanvas.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.example.climbingcanvas.data.Hold;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import com.example.climbingcanvas.R;

public class HoldDrawingView extends AppCompatImageView {
    private List<Hold> holds = new ArrayList<>();
    private Paint startPaint, topPaint, handPaint, footPaint, dimPaint, selectedPaint;
    private Path maskPath;
    private boolean dimmingEnabled = false;
    private Hold selectedHold;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private OnHoldPlacedListener listener;
    private boolean isDraggingHold = false;
    private float lastTouchX, lastTouchY;

    public interface OnHoldPlacedListener {
        void onHoldPlaced(float x, float y);
        void onHoldEdited(Hold hold);
    }

    public HoldDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        startPaint = createPaint(ContextCompat.getColor(getContext(), R.color.hold_start));
        topPaint = createPaint(ContextCompat.getColor(getContext(), R.color.hold_top));
        handPaint = createPaint(ContextCompat.getColor(getContext(), R.color.hold_hand));
        footPaint = createPaint(ContextCompat.getColor(getContext(), R.color.hold_foot));

        selectedPaint = new Paint(handPaint);
        selectedPaint.setColor(Color.WHITE);
        selectedPaint.setStrokeWidth(4f);
        selectedPaint.setStyle(Paint.Style.STROKE);

        dimPaint = new Paint();
        dimPaint.setColor(Color.parseColor("#99000000")); // semi-transparent black
        dimPaint.setStyle(Paint.Style.FILL);
        maskPath = new Path();

        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        gestureDetector = new GestureDetector(getContext(), new GestureListener());
        setScaleType(ScaleType.MATRIX);
    }

    private Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6f); // Thinner outline as requested
        paint.setAntiAlias(true);
        // Add a small shadow to make it pop
        paint.setShadowLayer(3f, 1f, 1f, Color.BLACK);
        return paint;
    }

    public void setHolds(List<Hold> holds) {
        this.holds = holds;
        invalidate();
    }

    public void setSelectedHold(Hold hold) {
        this.selectedHold = hold;
        invalidate();
    }

    public Hold getSelectedHold() {
        return selectedHold;
    }

    public void increaseRadius() {
        if (selectedHold != null) {
            selectedHold.radius += 0.005f;
            invalidate();
        }
    }

    public void decreaseRadius() {
        if (selectedHold != null && selectedHold.radius > 0.01f) {
            selectedHold.radius -= 0.005f;
            invalidate();
        }
    }

    public void fitImageToWidth() {
        if (getDrawable() == null || getWidth() == 0) return;

        float viewWidth = getWidth();
        float viewHeight = getHeight();
        float drawableWidth = getDrawable().getIntrinsicWidth();
        float drawableHeight = getDrawable().getIntrinsicHeight();

        float scale = viewWidth / drawableWidth;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        
        // Center vertically if there's extra space
        float scaledHeight = drawableHeight * scale;
        if (scaledHeight < viewHeight) {
            matrix.postTranslate(0, (viewHeight - scaledHeight) / 2f);
        }
        
        setImageMatrix(matrix);
    }

    public void setDimmingEnabled(boolean enabled) {
        this.dimmingEnabled = enabled;
        invalidate();
    }

    public void setOnHoldPlacedListener(OnHoldPlacedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getDrawable() == null) return;

        canvas.save();
        canvas.concat(getImageMatrix());

        float imageWidth = getDrawable().getIntrinsicWidth();
        float imageHeight = getDrawable().getIntrinsicHeight();

        if (dimmingEnabled && !holds.isEmpty()) {
            maskPath.reset();
            maskPath.addRect(0, 0, imageWidth, imageHeight, Path.Direction.CW);
            for (Hold hold : holds) {
                maskPath.addCircle(hold.x * imageWidth, hold.y * imageHeight, hold.radius * imageWidth, Path.Direction.CCW);
            }
            canvas.drawPath(maskPath, dimPaint);
        }

        for (Hold hold : holds) {
            Paint paint = getPaintForType(hold.type);
            float cx = hold.x * imageWidth;
            float cy = hold.y * imageHeight;
            float r = hold.radius * imageWidth;
            canvas.drawCircle(cx, cy, r, paint);

            if (hold == selectedHold) {
                canvas.drawCircle(cx, cy, r + 4, selectedPaint);
            }
        }
        canvas.restore();
    }

    private Paint getPaintForType(String type) {
        if (type == null) return handPaint;
        switch (type) {
            case "START": return startPaint;
            case "TOP": return topPaint;
            case "FOOT": return footPaint;
            default: return handPaint;
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getDrawable() == null) return super.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Hold touched = findHoldAt(x, y);
                if (touched != null && touched == selectedHold) {
                    isDraggingHold = true;
                    lastTouchX = x;
                    lastTouchY = y;
                    return true;
                }
                isDraggingHold = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDraggingHold && selectedHold != null) {
                    Matrix inverse = new Matrix();
                    getImageMatrix().invert(inverse);
                    float[] last = {lastTouchX, lastTouchY};
                    float[] curr = {x, y};
                    inverse.mapPoints(last);
                    inverse.mapPoints(curr);

                    float dx = (curr[0] - last[0]) / getDrawable().getIntrinsicWidth();
                    float dy = (curr[1] - last[1]) / getDrawable().getIntrinsicHeight();

                    selectedHold.x += dx;
                    selectedHold.y += dy;

                    lastTouchX = x;
                    lastTouchY = y;
                    invalidate();
                    if (listener != null) listener.onHoldEdited(selectedHold);
                    return true;
                }
                break;
        }

        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            performClick();
        }
        return true;
    }

    private Hold findHoldAt(float x, float y) {
        if (getDrawable() == null) return null;
        Matrix inverse = new Matrix();
        getImageMatrix().invert(inverse);
        float[] pts = {x, y};
        inverse.mapPoints(pts);

        float iw = getDrawable().getIntrinsicWidth();
        float ih = getDrawable().getIntrinsicHeight();

        for (Hold h : holds) {
            float dx = (pts[0] / iw) - h.x;
            float dy = (pts[1] / ih) - h.y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist < h.radius * 1.5f) return h;
        }
        return null;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Matrix matrix = new Matrix(getImageMatrix());
            matrix.postScale(detector.getScaleFactor(), detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY());
            setImageMatrix(matrix);
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Matrix matrix = new Matrix(getImageMatrix());
            matrix.postTranslate(-distanceX, -distanceY);
            setImageMatrix(matrix);
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (listener != null && getDrawable() != null) {
                Matrix inverse = new Matrix();
                getImageMatrix().invert(inverse);
                float[] pts = {e.getX(), e.getY()};
                inverse.mapPoints(pts);

                float imageWidth = getDrawable().getIntrinsicWidth();
                float imageHeight = getDrawable().getIntrinsicHeight();

                float normalizedX = pts[0] / imageWidth;
                float normalizedY = pts[1] / imageHeight;

                if (normalizedX >= 0 && normalizedX <= 1 && normalizedY >= 0 && normalizedY <= 1) {
                    listener.onHoldPlaced(normalizedX, normalizedY);
                }
            }
            return true;
        }
    }
}
