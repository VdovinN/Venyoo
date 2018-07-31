package com.app.venyoo.extension

import android.content.res.Resources
import android.graphics.Color

fun Int.pxToDp(): Int {
    val metrics = Resources.getSystem().displayMetrics
    val dp = this / (metrics.densityDpi / 160f)
    return Math.round(dp)
}

fun Int.dpToPx(): Float {
    val metrics = Resources.getSystem().displayMetrics
    return this * (metrics.densityDpi / 160f)
}

fun Int.intToRGB(): Int {
    return Color.rgb((this shr 16 and 0xFF), (this shr 8 and 0xFF), (this and 0xFF))
}