package com.artveflina.naiveclock.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.artveflina.naiveclock.util.DatetimeGetter
import com.artveflina.naiveclock.util.degreeToRadian
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class Dial : View {
    companion object {
        private const val FULL_ANGLE = 360

        private const val TRANSPARENT_ALPHA = 0x5f
        private const val FULL_ALPHA = 0xff

        private val FOREGROUND_COLOR = Color.parseColor("#673AB7")

        private const val SECONDARY_DEGREE_STROKE_WIDTH_RATIO = 0.006
        private const val PRIMARY_DEGREE_STROKE_WIDTH_RATIO = 0.008

        private val HAND_LENGTH_DELTAS = listOf(300, 200, 10)

        private val HAND_PAINTS by lazy {
            listOf(Paint().apply {
                color = Color.parseColor("#673AB7")
                isAntiAlias = true
                strokeCap = Paint.Cap.ROUND
                strokeWidth = 0.015f * mWidth
                style = Paint.Style.FILL_AND_STROKE
            }, Paint().apply {
                color = Color.parseColor("#673AB7")
                alpha = 0xaf
                isAntiAlias = true
                strokeCap = Paint.Cap.ROUND
                strokeWidth = 0.01f * mWidth
                style = Paint.Style.FILL_AND_STROKE
            }, Paint().apply {
                color = Color.parseColor("#673AB7")
                alpha = 0x5f
                isAntiAlias = true
                strokeCap = Paint.Cap.ROUND
                strokeWidth = 0.005f * mWidth
                style = Paint.Style.FILL_AND_STROKE
            })
        }

        private var mWidth = 0
        private var mCenterX = 0
        private var mCenterY = 0
        private var mRadius = 0
        private var degreePaint = Paint()
        private var hourValuePaint = Paint()

        private val timeGetter = DatetimeGetter()
    }


    private fun handLength(index: Int) = mRadius - HAND_LENGTH_DELTAS[index]

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val horizontalPadding = paddingLeft + paddingRight
        val verticalPadding = paddingTop + paddingBottom
        val size = min(measuredWidth - horizontalPadding, measuredHeight - verticalPadding)
        setMeasuredDimension(size + horizontalPadding, size + verticalPadding)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mWidth = min(width, height)
        mCenterX = mWidth / 2
        mCenterY = mWidth / 2
        mRadius = mWidth / 2

        degreePaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
            strokeCap = Paint.Cap.ROUND
            color = FOREGROUND_COLOR
        }

        hourValuePaint.apply {
            isAntiAlias = true
            color = FOREGROUND_COLOR
            textSize = mWidth * 0.1f
            textAlign = Paint.Align.CENTER
        }

        drawHands(canvas)
        drawHourValues(canvas)
        drawDegrees(canvas)

        postInvalidateDelayed(40)
//        invalidate()
    }

    private fun getDegreePos(degree: Double, r: Double): Pair<Double, Double> {
        val x = mCenterX + r * cos(degreeToRadian(degree))
        val y = mCenterY - r * sin(degreeToRadian(degree))
        return Pair(x, y)
    }

    private fun drawDegrees(canvas: Canvas) {
        val rPadded = mCenterX - mWidth * 0.01
        val rEnd = mCenterX - mWidth * 0.05

        for (i in 0 until FULL_ANGLE step 6) {
            val (start, end) = if (i % (360 / 12) == 0) {
                degreePaint.alpha = FULL_ALPHA
                degreePaint.strokeWidth = (mWidth * PRIMARY_DEGREE_STROKE_WIDTH_RATIO).toFloat()
                Pair(getDegreePos(i.toDouble(), rEnd * .99), getDegreePos(i.toDouble(), rPadded))
            } else {
                degreePaint.alpha = TRANSPARENT_ALPHA
                degreePaint.strokeWidth = (mWidth * SECONDARY_DEGREE_STROKE_WIDTH_RATIO).toFloat()
                Pair(getDegreePos(i.toDouble(), rEnd), getDegreePos(i.toDouble(), rPadded))
            }

            canvas.drawLine(
                start.first.toFloat(),
                start.second.toFloat(),
                end.first.toFloat(),
                end.second.toFloat(),
                degreePaint
            )
        }
    }

    private fun drawHourValues(canvas: Canvas) {
        val stretch = 0.75
        for (i in 1..12) {
            val x = mCenterX + mRadius * stretch * sin(degreeToRadian(i * 30.0))
            val y = mCenterY - mRadius * stretch * cos(degreeToRadian(i * 30.0))
            canvas.drawText(
                i.toString(), x.toFloat(), y.toFloat() + hourValuePaint.textSize / 3, hourValuePaint
            )
        }
    }

    private fun drawHands(canvas: Canvas) {
        val (hour, minute, second) = timeGetter.currentTimeWithMillis()

        // (hour + minute / 60.0 + second_ / 3600.0) * 30.0
        val hourDegree = (hour % 12) * 30.0 + minute / 2.0 + second / 120.0

        // (minute + second_ / 60.0) * 6.0
        val minuteDegree = minute * 6.0 + second / 10.0

        val secondDegree = second * 6.0

        drawHand(canvas, secondDegree, 2)
        drawHand(canvas, minuteDegree, 1)
        drawHand(canvas, hourDegree, 0)
    }

    private fun drawHand(canvas: Canvas, degree: Double, index: Int) {
        val stopX = mCenterX + handLength(index) * sin(degreeToRadian(degree))
        val stopY = mCenterY - handLength(index) * cos(degreeToRadian(degree))
        canvas.drawLine(
            mCenterX.toFloat(),
            mCenterY.toFloat(),
            stopX.toFloat(),
            stopY.toFloat(),
            HAND_PAINTS[index],
        )
    }
}
