package com.example.hydroboost.ui.home

import android.view.View
import android.util.AttributeSet
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class WaterFillingView(context : Context, attrs: AttributeSet? = null, x : Int, y : Int, rectangleWidth : Int, rectangleHeight : Int) : View(context, attrs) {

    private val rectangleWidth = rectangleWidth
    private val rectangleHeight = rectangleHeight
    private val x = x
    private val y = y

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint : Paint = Paint()
        paint.color = Color.BLUE

        val waterRectangle = Rect(x, y, x + rectangleWidth, y + rectangleHeight)
        canvas.drawRect(waterRectangle, paint)
    }

}