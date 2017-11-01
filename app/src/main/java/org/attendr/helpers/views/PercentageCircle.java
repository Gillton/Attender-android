package org.attendr.helpers.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import org.attendr.R;

public class PercentageCircle extends View {

    private ValueAnimator animator;
    private Paint paint;
    private Paint centerPaint;
    private RectF bounds;
    private RectF centerBounds;

    private int width;
    private int height;

    private int angle = 1;
    private String text = "";

    public PercentageCircle(Context context) {
        super(context);
        init();
    }

    public PercentageCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PercentageCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint.setStyle(Paint.Style.FILL);
        centerPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setProgress(55);
            }
        }, 100);
    }

    public void recalcSize() {
        int strokeWidth = getHeight() / 15;
        int diameter = Math.min(width, height);
        int top = strokeWidth / 2;
        int bottom = diameter - strokeWidth / 2;
        paint.setStrokeWidth(strokeWidth);
        bounds = new RectF(top, top, bottom, bottom);
        int newTop = top + strokeWidth;
        int newBottom = bottom - strokeWidth;
        centerBounds = new RectF(newTop, newTop, newBottom, newBottom);
        paint.setTextSize(getHeight() / 4);
    }

    public int getProgress() {
        return angle;
    }

    public void setProgress(int percentage) {
        int angle = (int) (360 * (percentage / 100.0f));
        if (0 <= percentage && percentage <= 100) {
            text = Integer.toString(percentage) + "%";
            if (animator != null) {
                animator.cancel();
                animator.setIntValues(getProgress(), angle);
            } else {
                animator = ValueAnimator.ofInt(getProgress(), angle);
                animator.setInterpolator(new AccelerateInterpolator());
                animator.setDuration(800);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        PercentageCircle.this.angle = (Integer) animation.getAnimatedValue();
                        invalidate();
                    }
                });
            }
            animator.start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        recalcSize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
        canvas.drawArc(bounds, 0, 360, true, paint);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorVividBlue));
        canvas.drawArc(bounds, -91, angle, true, paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(centerBounds, 0, 360, true, centerPaint);
        int xPos = (canvas.getWidth() / 2);
        int yPos = xPos - (int) ((paint.descent() + paint.ascent()) / 2);
        canvas.drawText(text, xPos - (int) paint.measureText(text) / 2, yPos, paint);
    }
}
