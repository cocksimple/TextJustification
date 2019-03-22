package com.text.library.zhang

import android.content.Context
import android.graphics.Canvas
import android.text.StaticLayout
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView

/**
 * 使用空格进行填充的分散对齐
 * 展示效果有问题，会将不满的行填充满，不符合要求，需要进行改进
 */
class JustifyTextView(context: Context, attrs: AttributeSet? = null) : TextView(context, attrs) {
    companion object {
        const val TWO_CHINESE_BLANK = "  "
    }

    private var mLineY = 0F
    private var mViewWidth = 0

    override fun onDraw(canvas: Canvas?) {
        mViewWidth = measuredWidth

        paint.color = currentTextColor
        paint.drawableState = drawableState
        val text = text.toString()
        mLineY = textSize

        if (null == layout)
            return

        val fm = paint.fontMetrics

        val textHeight = Math.ceil((fm.descent - fm.ascent).toDouble()) * layout.spacingMultiplier + layout.spacingAdd

        for (i in 0 until layout.lineCount) {
            val lineStart = layout.getLineStart(i)
            val lineEnd = layout.getLineEnd(i)
            val width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, paint)
            val line = text.substring(lineStart, lineEnd)
            if (needScale(line)) {
                drawScaledText(canvas, lineStart, line, width)
            } else {
                canvas?.drawText(line, 0F, mLineY, paint)
            }
            mLineY += textHeight.toFloat()
        }

    }

    private fun needScale(line: String): Boolean {
        if (TextUtils.isEmpty(line)) {
            return false
        } else {
            return line[line.length - 1] != '\n'
        }
    }

    private fun drawScaledText(canvas: Canvas?, lineStart: Int, line: String, lineWidth: Float) {
        var x = 0F
        var line = line
        if (isFirstLineOfParagraph(lineStart, line)) {
            canvas?.drawText(TWO_CHINESE_BLANK, x, mLineY, paint)
            val bw = StaticLayout.getDesiredWidth(TWO_CHINESE_BLANK, paint)
            x += bw
            line = line.substring(3)
        }
        val gapCount = line.length - 1
        var i = 0
        if (line.length > 2 && line[0].equals(12288) && line[1].equals(12288)) {
            val substring = line.substring(0, 2)
            val cw = StaticLayout.getDesiredWidth(substring, paint)
            canvas?.drawText(substring, x, mLineY, paint)
            x += cw
            i += 2
        }

        val d = (mViewWidth - lineWidth) / gapCount

        for (i in i until line.length) {
            val c = line[i].toString()
            val cw = StaticLayout.getDesiredWidth(c, paint)
            canvas?.drawText(c, x, mLineY, paint)
            x += cw + d
        }

    }

    private fun isFirstLineOfParagraph(lineStart: Int, line: String): Boolean {
        return line.length > 3 && line[0] == ' ' && line[1] == ' '
    }
}