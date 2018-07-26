package com.app.venyoo.extension

import android.content.res.Resources

fun Int.pxToDp(): Int {
    val metrics = Resources.getSystem().displayMetrics
    val dp = this / (metrics.densityDpi / 160f)
    return Math.round(dp)
}

fun Int.dpToPx(): Float {
    val metrics = Resources.getSystem().displayMetrics
    return this * (metrics.densityDpi / 160f)
}