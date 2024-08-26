package com.artveflina.naiveclock.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.artveflina.naiveclock.util.DatetimeGetter
import kotlin.math.min

class Digital : View {
    companion object {
        private const val FULL_ANGLE = 360

        private const val TRANSPARENT_ALPHA = 0x5f
        private const val FULL_ALPHA = 0xff

        private val FOREGROUND_COLOR = Color.parseColor("#673AB7")

        private const val DEFAULT_DEGREE_STROKE_WIDTH_RATIO = 0.008

        private val HAND_LENGTH_DELTAS = listOf(300, 200, 10)

        private val TYPEFACE = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)

        private var mWidth = 0
        private var mCenterX = 0
        private var mCenterY = 0
        private var textPaint = Paint()

        private val datetimeGetter = DatetimeGetter()
    }

    private fun formatString(blink: Boolean) = if (blink) {
        "%02d %02d %02d"
    } else {
        "%02d:%02d:%02d"
    }

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
        mWidth = width
        mCenterX = mWidth / 2
        mCenterY = mWidth / 2

        textPaint.apply {
            color = FOREGROUND_COLOR
            isAntiAlias = true
            textSize = 0.1f * mWidth
            textAlign = Paint.Align.CENTER
            typeface = TYPEFACE
        }

        val datetime = datetimeGetter.currentDatetime()
        canvas.drawText(
            String.format(
                formatString(datetime[5] % 2 != 0), datetime[3], datetime[4], datetime[5]
            ),
            mCenterX.toFloat(),
            mCenterY.toFloat() - 0.075f * mWidth,
            textPaint
        )
        canvas.drawText(
            "${datetime[0]}/${datetime[1]}/${datetime[2]}",
            mCenterX.toFloat(),
            mCenterY.toFloat() + 0.075f * mWidth,
            textPaint
        )

        postInvalidateDelayed(1000)
    }
}