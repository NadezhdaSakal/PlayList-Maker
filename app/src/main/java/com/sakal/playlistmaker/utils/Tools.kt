package com.sakal.playlistmaker.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale


fun ImageView.setImage(url: String, placeholder: Int, cornerRadius: Int) {
    Glide
        .with(this.context)
        .load(url)
        .placeholder(placeholder)
        .transform(CenterCrop(), RoundedCorners(cornerRadius))
        .into(this)
}

fun Long.millisConverter(): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}


