package com.example.sharedcard.ui.view

import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint

class PFCLineChartModel(
    var percentOfLine: Float = 0F,
    var percentToStartAt: Float = 0F,
    var colorOfLine: Int = 0,
    stroke: Float = 0F,
    var paint: Paint = Paint(),
    var paintRound: Paint = Paint()
) {
    var stroke = stroke
        set(value) {
            field = value
//            paint.strokeWidth = value
//            paintRound.strokeWidth = value
        }

    init {
        if (percentOfLine < 0 || percentOfLine > 100) {
            percentOfLine = 0F
        }


        if (percentToStartAt < 0 || percentToStartAt > 100) {
            percentToStartAt = 0F
        }


        if (colorOfLine == 0) {
            colorOfLine = Color.parseColor("#000000")
        }
        paint = Paint()
        paint.run {
            color = colorOfLine
            isAntiAlias = true
            style = Paint.Style.FILL
//            strokeWidth = stroke
        }
        paintRound = Paint()
        paintRound.run {
            color = colorOfLine
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = stroke
            isDither = true
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            pathEffect = CornerPathEffect(8F)
        }
    }
}
