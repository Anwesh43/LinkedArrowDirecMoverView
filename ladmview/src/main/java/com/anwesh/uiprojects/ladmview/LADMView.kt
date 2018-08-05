package com.anwesh.uiprojects.ladmview

/**
 * Created by anweshmishra on 06/08/18.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas

val nodes : Int = 5

class LADMView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }
    
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}