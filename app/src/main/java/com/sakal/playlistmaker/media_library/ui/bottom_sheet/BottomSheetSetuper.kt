package com.sakal.playlistmaker.media_library.ui.bottom_sheet

import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class BottomSheetSetuper(private val activity: Activity?) {

    fun setupRatio(bottomSheetDialog: BottomSheetDialog, percentage: Float) {

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<FrameLayout> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getWindowHeight()
        bottomSheet.layoutParams = layoutParams
        val peekHeight = (layoutParams.height * percentage).toInt()
        behavior.peekHeight = peekHeight

        behavior.isFitToContents = false
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun setupRatio(
        container: LinearLayout,
        percentage: Float,
    ) {
        val bottomSheetBehavior = BottomSheetBehavior
            .from(container)
            .apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

        val screenHeight = getWindowHeight()
        val peekHeight = (screenHeight * percentage).toInt()

        bottomSheetBehavior.peekHeight = peekHeight
        bottomSheetBehavior.isFitToContents = false

    }

    private fun getWindowHeight(): Int {

        val displayMetrics = DisplayMetrics()

        @Suppress("DEPRECATION") activity?.windowManager?.defaultDisplay?.getMetrics(
            displayMetrics
        )

        return displayMetrics.heightPixels
    }
}