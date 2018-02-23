package org.attendr.helpers.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import org.attendr.R

/**
 * @author Pauldg7@gmail.com (Paul Gillis)
 */
class PercentageCircle(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var animator: ValueAnimator? = null

    private var paint: Paint
    private var paintGray: Paint
    private var paintText: Paint
    private var centerPaint: Paint

    private var bounds: RectF? = null
    private var boundsGray: RectF? = null
    private var centerBounds: RectF? = null

    private var progress = 1
        set(value) {
            val angle = (360 * (value / 100.0f)).toInt()
            if (value in 0..100) {
                text = Integer.toString(value) + "%"
                if (animator != null) {
                    animator?.cancel()
                    animator?.setIntValues(progress, angle)
                } else {
                    animator = ValueAnimator.ofInt(progress, angle)
                    animator?.interpolator = AccelerateInterpolator()
                    animator?.duration = 800
                    animator?.addUpdateListener { animation ->
                        field = animation.animatedValue as Int
                        invalidate()
                    }
                }
                animator?.start()
            }
        }

    private var text = ""

    private var strokeWidth: Int = 0

    init {
        attrs?.let {
            val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.PercentageCircle, 0, 0)
            strokeWidth = attributes.getDimensionPixelSize(R.styleable.PercentageCircle_stroke, 3)
            attributes.recycle()
        }

        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(getContext(), R.color.colorVividBlue)

        paintGray = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        paintGray.color = ContextCompat.getColor(getContext(), R.color.colorLightGray)

        centerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        centerPaint.style = Paint.Style.FILL
        centerPaint.color = ContextCompat.getColor(getContext(), android.R.color.white)

        paintText = Paint(Paint.ANTI_ALIAS_FLAG)
        paintText.style = Paint.Style.FILL
        paintText.color = ContextCompat.getColor(getContext(), R.color.colorVividBlue)

        resize()
    }

    private fun resize() {
        val heightOrWidth = Math.min(width, height) //ALSO diameter
        strokeWidth = heightOrWidth / 15
        val top = strokeWidth / 2
        val bottom = heightOrWidth - strokeWidth / 2
        paint.strokeWidth = strokeWidth.toFloat()
        bounds = RectF(top.toFloat(), top.toFloat(), bottom.toFloat(), bottom.toFloat())
        boundsGray = RectF(top.toFloat(), top.toFloat(), bottom.toFloat(), bottom.toFloat())
        val newTop = top + strokeWidth
        val newBottom = bottom - strokeWidth
        centerBounds = RectF(newTop.toFloat(), newTop.toFloat(), newBottom.toFloat(), newBottom.toFloat())
        paintText.textSize = (heightOrWidth / 4).toFloat()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        resize()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawArc(boundsGray, 0f, 360f, true, paintGray)
        canvas.drawArc(bounds, -91f, progress.toFloat(), true, paint)
        canvas.drawArc(centerBounds, 0f, 360f, true, centerPaint)
        val xPos = canvas.width / 2
        val yPos = xPos - ((paintText.descent() + paintText.ascent()) / 2).toInt()
        canvas.drawText(text, (xPos - paintText.measureText(text).toInt() / 2).toFloat(), yPos.toFloat(), paintText)
    }
}