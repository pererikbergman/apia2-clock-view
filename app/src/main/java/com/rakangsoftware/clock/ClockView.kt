package com.rakangsoftware.clock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*


class ClockView : View {

    private val paint = Paint()
    private val hourPaint = Paint()
    private val minutesPaint = Paint()
    private val secondPaint = Paint()

    private var contentSize = 0
    private var offsetX = 0f
    private var offsetY = 0f

    private var hours = 0f
    private var minutes = 0f
    private var seconds = 0f

    private var hourHand = 0
    private var minuteHand = 0
    private var secondHand = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.ClockView,
            0, 0
        )

        try {
            hourHand = typedArray.getDimensionPixelSize(R.styleable.ClockView_hourHand, 1)
            minuteHand = typedArray.getDimensionPixelSize(R.styleable.ClockView_minutHand, 1)
            secondHand = typedArray.getDimensionPixelSize(R.styleable.ClockView_secondHand, 1)

        } finally {
            typedArray.recycle()
        }

        paint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 3f
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
        }

        hourPaint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = hourHand.toFloat()
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
        }

        minutesPaint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = minuteHand.toFloat()
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
        }

        secondPaint.apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = secondHand.toFloat()
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
        }

        updateTime()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom
        contentSize = Math.min(contentWidth, contentHeight)

        offsetX = (contentWidth - contentSize) / 2f + paddingLeft
        offsetY = (contentHeight - contentSize) / 2f + paddingTop
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()

        canvas.translate(offsetX, offsetY)

        // Clock board
        val halfClockSize = contentSize / 2f
        canvas.drawCircle(halfClockSize, halfClockSize, halfClockSize, paint)

        for (i in 0..59) {
            val inner = if (i % 5 == 0) 0.85f else 0.9f
            val outer = 0.95f
            val angle = Math.PI * 2.0f * (i / 60f)

            canvas.drawLine(
                halfClockSize - Math.cos(angle).toFloat() * halfClockSize * inner,
                halfClockSize - Math.sin(angle).toFloat() * halfClockSize * inner,
                halfClockSize - Math.cos(angle).toFloat() * halfClockSize * outer,
                halfClockSize - Math.sin(angle).toFloat() * halfClockSize * outer,
                paint
            )
        }

        // Hour hand
        val hour = Math.PI / 2 - Math.PI * 2 * (hours / 60f)
        canvas.drawLine(
            halfClockSize,
            halfClockSize,
            halfClockSize - Math.cos(hour).toFloat() * halfClockSize * 0.6f,
            halfClockSize - Math.sin(hour).toFloat() * halfClockSize * 0.6f,
            hourPaint
        )

        // Minute hand
        val minute = Math.PI / 2 + Math.PI * 2 * (minutes / 60f)
        canvas.drawLine(
            halfClockSize,
            halfClockSize,
            halfClockSize - Math.cos(minute).toFloat() * halfClockSize * 0.8f,
            halfClockSize - Math.sin(minute).toFloat() * halfClockSize * 0.8f,
            minutesPaint
        )

        // Second hand
        val second = Math.PI / 2 + Math.PI * 2 * (seconds / 60f)
        canvas.drawLine(
            halfClockSize,
            halfClockSize,
            halfClockSize - Math.cos(second).toFloat() * halfClockSize * 0.95f,
            halfClockSize - Math.sin(second).toFloat() * halfClockSize * 0.95f,
            secondPaint
        )

        canvas.restore()

        postDelayed({
            updateTime()
            invalidate()
        }, 1000)
    }



    private fun updateTime() {
        val ms = Date().time//System.currentTimeMillis()
        seconds = ((ms / 1000) % 3600).toFloat()
        minutes = ((ms / 1000 / 60) % 3600).toFloat()
        hours = ((ms / 1000 / 60 / 60) % 3600).toFloat()

//        var widthAnimator: ValueAnimator? = null
//        widthAnimator?.removeAllListeners()
//        widthAnimator = ValueAnimator.ofFloat(seconds, ((ms / 1000) % 3600).toFloat())
//        widthAnimator?.let {
//
//            it.addUpdateListener { animation ->
//                seconds = animation.animatedValue as Float
//                invalidate()
//            }
//            it.duration = 100
//            it.interpolator = BounceInterpolator()
//            it.start()
//        }

    }
}
