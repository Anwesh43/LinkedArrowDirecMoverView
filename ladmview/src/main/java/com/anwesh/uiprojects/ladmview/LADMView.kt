package com.anwesh.uiprojects.ladmview

/**
 * Created by anweshmishra on 06/08/18.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color

val nodes : Int = 5
val speed : Float = 0.05f
fun Canvas.drawLADMNode(i : Int, scale : Float , paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / nodes
    val sc1 : Float = Math.min(0.5f, scale) * 2
    val sc2 : Float = Math.min(0.5f, Math.max(0f, scale - 0.5f)) * 2
    val size : Float = gap / 3
    val index : Int = i % 2
    val factor : Int = ((i) / 2)
    paint.strokeWidth = Math.min(w, h) / 50
    paint.strokeCap = Paint.Cap.ROUND
    paint.color = Color.parseColor("#4CAF50")
    save()
    translate(gap * factor + gap / 2 + gap * sc2 * index, gap * factor + gap * index + gap/2 + gap * sc2 * (1 - index))
    rotate(90f * (sc1 * (1 - index) + (1 - sc1) * index ))
    for (j in 0..1) {
        save()
        scale(1f, 1f - 2 * j)
        drawLine(0f, 0f, -size, -size, paint)
        restore()
    }
    restore()
}
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

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            this.scale += speed * this.dir
            if (Math.abs(this.scale - this.prevScale) > 1) {
                this.scale = this.prevScale + this.dir
                this.dir = 0f
                this.prevScale = this.scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }
    }

    data class LADMNode(var i : Int, val state : State = State()) {

        private var next : LADMNode? = null

        private var prev : LADMNode? = null

        fun update(cb : (Int, Float) -> Unit) {
            state.update {
                cb(i, it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        private fun addNeighbor() {
            if (i < nodes - 1) {
                this.next = LADMNode(i + 1)
                this.next?.prev = this
            }
        }

        init {
            addNeighbor()
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawLADMNode(i, state.scale, paint)
            next?.draw(canvas, paint)
        }

        fun getNext(dir : Int, cb : () -> Unit) : LADMNode {
            var curr : LADMNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class LinkedLADM(var i : Int) {

        private var curr : LADMNode = LADMNode(0)

        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            curr.update {i, scl ->
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(i, scl)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : LADMView) {

        private val ladm : LinkedLADM = LinkedLADM(0)

        private val animator : Animator = Animator(view)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            ladm.draw(canvas, paint)
            animator.animate {
                ladm.update {i, scl ->
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            ladm.startUpdating {
                animator.start()
            }
        }
    }
}