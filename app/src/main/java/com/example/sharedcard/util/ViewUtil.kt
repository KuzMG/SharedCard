package com.example.sharedcard.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Region
import android.graphics.RegionIterator
import android.text.StaticLayout
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.graphics.withTranslation
import com.example.sharedcard.R
import com.google.android.material.chip.Chip
import java.util.Calendar


fun View.isVisible(flag: Boolean) {
    visibility = when (flag) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}

fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}


fun Context.spToPx(sp: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        this.resources.displayMetrics
    );
}


fun Context.getPrimaryColor(): Int {
    val typedValue = TypedValue()
    val theme = theme
    theme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true)
    return typedValue.data
}

fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {
    canvas.withTranslation(x, y) {
        draw(this)
    }
}

fun Canvas.drawRegion(region: Region, paint: Paint) {
    val iterator = RegionIterator(region)
    val r = Rect()
    while (iterator.next(r)) {
        drawRect(r, paint)
    }
}

fun Long.getDaysInMonthFromTimestamp(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun Long.getWeekDaysForDate(): List<Int> {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@getWeekDaysForDate
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    return (0..6).map { offset ->
        (calendar.clone() as Calendar).apply {
            add(Calendar.DATE, offset)
        }.get(Calendar.DAY_OF_MONTH)
    }
}


fun createChipAction(chipGroup: ViewGroup, text: String, click: (View) -> Unit) =
    createChip(R.layout.chip_action, chipGroup, text, click)


fun createChipChose(chipGroup: ViewGroup, text: String, click: (View) -> Unit) =
    createChip(R.layout.chip_chose, chipGroup, text, click)


private fun createChip(
    @LayoutRes style: Int,
    chipGroup: ViewGroup,
    text: String,
    click: (View) -> Unit
): Chip {
    val chip = LayoutInflater.from(chipGroup.context).inflate(style, chipGroup, false) as Chip
    chip.text = text
    chip.setEnsureMinTouchTargetSize(false)
    chip.setOnClickListener(click)
    return chip
}