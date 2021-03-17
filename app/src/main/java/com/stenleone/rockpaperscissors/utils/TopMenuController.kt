package com.stenleone.rockpaperscissors.utils

import android.view.MotionEvent
import android.view.View
import com.stenleone.rockpaperscissors.databinding.ToolBarBinding

class TopMenuController(private val binding: ToolBarBinding) {

    init {
        binding.apply {

            root.y = (binding.root.height/4).toFloat()

            var listener = View.OnTouchListener(function = { view, motionEvent ->

                    if (motionEvent.action == MotionEvent.ACTION_MOVE) {

                        if (((motionEvent.rawY - binding.root.height) < 0) && (motionEvent.rawY - binding.root.height / 4) > 0) {
                            view.y = motionEvent.rawY - view.height
                        }
                    }

                true
            })

            binding.root.setOnTouchListener(listener)
        }
    }

}