package com.example.sharedcard.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Region
import android.graphics.RegionIterator
import android.text.StaticLayout
import android.util.TypedValue
import android.view.View
import androidx.core.graphics.withTranslation

fun View.isVisible(flag: Boolean) {
    visibility = when (flag) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}

fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}

/**
 * Context Extension для конвертирования значения размера шрифта в пиксели.
 * @property sp - значение scale-independent pixels
 */
fun Context.spToPx(sp: Int): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), this.resources.displayMetrics);
}
fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {
    canvas.withTranslation(x, y) {
        draw(this)
    }
}
fun Canvas.drawRegion(region: Region,paint: Paint){
    val iterator = RegionIterator(region)
    val r = Rect()
    while (iterator.next(r)){
        drawRect(r,paint)
    }
}