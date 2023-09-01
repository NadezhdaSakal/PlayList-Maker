package com.sakal.playlistmaker.media_library.ui.bottom_sheet

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetPlaylist : BottomSheetDialogFragment() {

    override fun onStart() {
        super.onStart()
        val bottomSheetSetuper = BottomSheetSetuper(activity)
        bottomSheetSetuper.setupRatio(dialog as BottomSheetDialog, PERCENT_OCCUPIED_BY_BOTTOM_SHEET)
    }

    companion object {
        private const val PERCENT_OCCUPIED_BY_BOTTOM_SHEET = 0.45f

    }
}