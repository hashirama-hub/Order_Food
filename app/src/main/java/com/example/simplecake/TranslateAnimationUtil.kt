package com.example.simplecake

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

class TranslateAnimationUtil(context: Context, viewAnimation:View): View.OnTouchListener {
    private var gestureDetector: GestureDetector = GestureDetector(context,SimpleGestureDetector(viewAnimation))
    open class SimpleGestureDetector(viewAnimation: View) : GestureDetector.SimpleOnGestureListener() {
        private  var viewAnimation:View = viewAnimation
        private var isFinishAnimation= true
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if(distanceY>0){
                hiddenView()
            }
            else{
                showView()
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        private fun showView() {
            if(viewAnimation ==null || viewAnimation.visibility==View.VISIBLE){
                return
            }
            var animationUp:Animation = AnimationUtils.loadAnimation(viewAnimation.context,R.anim.move_up)
            animationUp.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {
                    viewAnimation.visibility = View.VISIBLE
                    isFinishAnimation = false
                }

                override fun onAnimationEnd(p0: Animation?) {

                    isFinishAnimation = true
                }

                override fun onAnimationRepeat(p0: Animation?) {

                }

            })
            if (isFinishAnimation){
                viewAnimation.startAnimation(animationUp)
            }
        }

        private fun hiddenView() {
            if(viewAnimation ==null || viewAnimation.visibility==View.GONE){
                return
            }
            var animationDown:Animation = AnimationUtils.loadAnimation(viewAnimation.context,R.anim.move_down)
            animationDown.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {
                    viewAnimation.visibility = View.VISIBLE
                    isFinishAnimation = false
                }

                override fun onAnimationEnd(p0: Animation?) {
                    viewAnimation.visibility = View.GONE
                    isFinishAnimation = true
                }

                override fun onAnimationRepeat(p0: Animation?) {

                }

            })
            if (isFinishAnimation){
                viewAnimation.startAnimation(animationDown)
            }
        }
    }

    override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(motionEvent)
    }


}