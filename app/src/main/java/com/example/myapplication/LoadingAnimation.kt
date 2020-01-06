package com.example.myapplication

import android.app.Activity
import android.widget.RelativeLayout
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class LoadingAnimation constructor(private val context: Activity, animationName: String) {
    private var lottieAnimationView = LottieAnimationView(context)

    private var rLayout : RelativeLayout = RelativeLayout(context)
    private var rLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)

    private val lLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)

    init {
        lottieAnimationView.setAnimation(animationName)
        lottieAnimationView.layoutParams = lLayoutParams

        rLayout.addView(lottieAnimationView)
    }

    fun playAnimation(loop: Boolean) {
        lottieAnimationView.repeatCount = LottieDrawable.INFINITE
        context.setContentView(rLayout, rLayoutParams)
        lottieAnimationView.playAnimation()
    }

    fun stopAnimation(originalView: Int) {
        lottieAnimationView.cancelAnimation()
        context.setContentView(originalView)
    }
}