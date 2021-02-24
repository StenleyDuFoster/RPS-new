package com.stenleone.rockpaperscissors.ui.activitys

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.ActivityRegisterBinding
import com.stenleone.rockpaperscissors.ui.activitys.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import soup.neumorphism.NeumorphButton
import soup.neumorphism.ShapeType

@AndroidEntryPoint
class RegisterActivity(override var layId: Int = R.layout.activity_register) :
    BaseActivity<ActivityRegisterBinding>() {

    override fun setup(savedInstanceState: Bundle?) {

        binding.apply {

//            button.setOnClickListener {
//
//                if (button.getShapeType() == ShapeType.BASIN) {
//                    button.setShapeType(ShapeType.PRESSED)
//                } else {
//                    button.setShapeType(ShapeType.BASIN)
//                }
//
//            }

        }

    }


}