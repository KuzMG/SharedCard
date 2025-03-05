package com.example.sharedcard.ui.view


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.example.sharedcard.R
import com.example.sharedcard.util.dpToPx
import com.example.sharedcard.util.drawRegion


class PFCLineChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), PFCLinetInterface {
    enum class PFC(val color: Int, val text: Int) {
        FAT(R.color.fat, R.string.pie_chart_fat),
        PROTEIN(R.color.protein, R.string.pie_chart_protein),
        CARB(R.color.carb, R.string.pie_chart_carb),
    }

    companion object {
        private const val DEFAULT_PERCENT_LINE = 0.9
        const val DEFAULT_VIEW_SIZE_HEIGHT = 10
        const val DEFAULT_VIEW_SIZE_WIDTH = 400
    }

    private var percentageCircleList: MutableMap<PFC, PFCLineChartModel> = mutableMapOf()
    private var textRowList: MutableList<StaticLayout> = mutableListOf()
    private var lineStartX = 0F
    private var lineStartY = 0F
    private var lineSize = 0
    private var strokeLine = context.dpToPx(10)
    private var click: (Int, String) -> Unit = { _, _ -> }

    private var data = mapOf<PFC, Float>()


    private var selectedItem: PFC? = null
    private var animationSweepAngle: Int = 0


    override fun setDataChart(protein: Float, fat: Float, carb: Float) {
        data = mapOf(PFC.PROTEIN to protein, PFC.FAT to fat, PFC.CARB to carb)
        calculatePercentageOfData()
    }

    /**
     * Имплиментируемый метод интерфейса взаимодействия [PFCLinetInterface].
     * Запуск анимации отрисовки View.
     */
    override fun startAnimation() {
        // Проход значений от 0 до 360 (целый круг), с длительностью - 1.5 секунды
        val animator = ValueAnimator.ofInt(0, 100).apply {
            duration = 1500 // длительность анимации в миллисекундах
            interpolator = FastOutSlowInInterpolator() // интерпретатор анимации
            addUpdateListener { valueAnimator ->
                // Обновляем значение для отрисовки диаграммы
                animationSweepAngle = valueAnimator.animatedValue as Int
                // Принудительная перерисовка
                invalidate()
            }
        }
        val bmOriginal = BitmapFactory.decodeResource(
            resources,
            com.example.sharedcard.R.drawable.tab_background_selected,
        )
        animator.start()
    }

    override fun addOnClickListener(click: (Int, String) -> Unit) {
        this.click = click
    }


    /**
     * Метод получения размера View по переданному Mode.
     */
    private fun resolveDefaultSize(spec: Int, defValue: Int): Int {
        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.UNSPECIFIED -> context.dpToPx(defValue)
                .toInt() // Размер не определен parent layout
            else -> MeasureSpec.getSize(spec) // Слушаемся parent layout
        }
    }


    /**
     * Метод заполнения поля [percentageCircleList]
     */
    private fun calculatePercentageOfData() {
        val totalAmount = data.values.sum()
        var startAt = 0f
        percentageCircleList = mutableMapOf()
        data.forEach {
            val percent = it.value * 100 / totalAmount
            val resultModel = PFCLineChartModel(
                percentOfLine = percent,
                percentToStartAt = startAt,
                colorOfLine = ContextCompat.getColor(context, it.key.color),
                stroke = strokeLine
            )
            if (percent != 0F) startAt += percent
            percentageCircleList[it.key] = resultModel
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textRowList.clear()
        val initSizeWidth = resolveDefaultSize(widthMeasureSpec, DEFAULT_VIEW_SIZE_WIDTH)

        var initSizeHeight = resolveDefaultSize(heightMeasureSpec, DEFAULT_VIEW_SIZE_HEIGHT)
        initSizeHeight += context.dpToPx(10).toInt()/2
        lineStartX = strokeLine
        lineStartY = initSizeHeight.toFloat() - context.dpToPx(15).toInt()/2
        lineSize = initSizeWidth -  context.dpToPx(10).toInt()*2

        setMeasuredDimension(initSizeWidth, initSizeHeight)
    }


    /**
     * Метод отрисовки круговой диаграммы на Canvas.
     */
    private fun drawLines(canvas: Canvas) {
        for (line in percentageCircleList) {
            val startX = line.value.percentToStartAt / 100 * lineSize + lineStartX

            if (animationSweepAngle > line.value.percentToStartAt + line.value.percentOfLine) {
                val stopX =
                    (line.value.percentToStartAt + line.value.percentOfLine) / 100 * lineSize + lineStartX

                drawLine(canvas, startX, stopX, line)
            } else if (animationSweepAngle > line.value.percentToStartAt) {
                val stopX = animationSweepAngle.toFloat() / 100 * lineSize + lineStartX

                drawLine(canvas, startX, stopX, line)
            }
        }
    }

    private fun drawLine(
        canvas: Canvas,
        startX: Float,
        stopX: Float,
        line: MutableMap.MutableEntry<PFC, PFCLineChartModel>
    ) {
        when (line.key) {
            PFC.PROTEIN -> {
                if (line.value.percentOfLine == 0F)
                    return
                drawLeftCorner(canvas, startX, line.value.stroke, line.value.paint)
                drawCenterLine(canvas, startX, stopX, line.value.stroke, line.value.paint)
                if (percentageCircleList[PFC.FAT]!!.percentOfLine == 0F && percentageCircleList[PFC.CARB]!!.percentOfLine == 0F) {
                    drawRightCorner(canvas, stopX, line.value.stroke, line.value.paint)
                }
            }

            PFC.FAT -> {
                if (percentageCircleList[PFC.PROTEIN]!!.percentOfLine == 0F)
                    drawLeftCorner(canvas, startX, line.value.stroke, line.value.paint)

                if (percentageCircleList[PFC.CARB]!!.percentOfLine == 0F)
                    drawRightCorner(canvas, stopX, line.value.stroke, line.value.paint)

                drawCenterLine(canvas, startX, stopX, line.value.stroke, line.value.paint)
            }

            PFC.CARB -> {
                if (line.value.percentOfLine == 0F)
                    return
                drawCenterLine(canvas, startX, stopX, line.value.stroke, line.value.paint)
                drawRightCorner(canvas, stopX, line.value.stroke, line.value.paint)
                if (percentageCircleList[PFC.FAT]!!.percentOfLine == 0F && percentageCircleList[PFC.PROTEIN]!!.percentOfLine == 0F) {
                    drawLeftCorner(canvas, startX, line.value.stroke, line.value.paint)
                }
            }
        }
    }

    private fun drawCenterLine(
        canvas: Canvas,
        startX: Float,
        stopX: Float,
        stroke: Float,
        paint: Paint
    ) {
        val reg = Region(
            startX.toInt(),
            (lineStartY - stroke / 2).toInt(),
            stopX.toInt(),
            (lineStartY + stroke / 2).toInt()
        )
        canvas.drawRegion(reg, paint)
    }

    private fun drawLeftCorner(canvas: Canvas, startX: Float, stroke: Float, paint: Paint) {
        val path = Path()
        val rectF = RectF(
            startX - stroke / 2,
            lineStartY - stroke / 2,
            startX + stroke / 2,
            (lineStartY + stroke / 2)
        )
        path.addOval(rectF, Path.Direction.CCW)
        val regRounding = Region()
        val clip = Region(
            (startX - stroke / 2).toInt(),
            (lineStartY - stroke / 2).toInt(),
            startX.toInt(),
            (lineStartY + stroke / 2).toInt()
        )
        regRounding.setPath(path, clip)
        canvas.drawRegion(regRounding, paint)
    }

    private fun drawRightCorner(canvas: Canvas, stopX: Float, stroke: Float, paint: Paint) {
        val path = Path()
        val rectF = RectF(
            stopX - stroke / 2,
            lineStartY - stroke / 2,
            stopX + stroke / 2,
            lineStartY + stroke / 2
        )
        path.addOval(rectF, Path.Direction.CCW)
        val regRounding = Region()
        val clip = Region(
            stopX.toInt(),
            (lineStartY - stroke / 2).toInt(),
            (stopX + stroke / 2).toInt(),
            (lineStartY + stroke / 2).toInt()
        )
        regRounding.setPath(path, clip)
        canvas.drawRegion(regRounding, paint)
    }

    /**
     * Метод жизненного цикла View.
     * Отрисовка всех необходимых компонентов на Canvas.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawLines(canvas)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (selectedItem != null) {
                    percentageCircleList[selectedItem]!!.stroke = context.dpToPx(10)
                    strokeLine = context.dpToPx(10)
                }
                for (k in percentageCircleList.keys) {
                    if (percentageCircleList[k]!!.percentOfLine == 0F)
                        continue
                    var startX =
                        percentageCircleList[k]!!.percentToStartAt / 100 * lineSize + lineStartX
                    var stopX =
                        (percentageCircleList[k]!!.percentToStartAt + percentageCircleList[k]!!.percentOfLine) / 100 * lineSize + lineStartX
                    when (k) {
                        PFC.PROTEIN -> {
                            startX -= strokeLine

                            if (percentageCircleList[PFC.FAT]!!.percentOfLine == 0F && percentageCircleList[PFC.CARB]!!.percentOfLine == 0F)
                                stopX += strokeLine

                        }

                        PFC.FAT -> {
                            if (percentageCircleList[PFC.PROTEIN]!!.percentOfLine == 0F)
                                startX -= strokeLine

                            if (percentageCircleList[PFC.CARB]!!.percentOfLine == 0F)
                                stopX += strokeLine
                        }

                        PFC.CARB -> {
                            stopX += strokeLine

                            if (percentageCircleList[PFC.FAT]!!.percentOfLine == 0F && percentageCircleList[PFC.PROTEIN]!!.percentOfLine == 0F)
                                startX -= strokeLine

                        }
                    }
                    if (event.x in startX..stopX) {
                        if (selectedItem == k) {
                            selectedItem = null
                            click(R.color.white, "")
                            invalidate()
                            return true
                        }
                        selectedItem = k
                        percentageCircleList[k]!!.stroke = context.dpToPx(15)
                        strokeLine = context.dpToPx(15)
                        val idStr = when (k) {
                            PFC.PROTEIN -> R.string.pie_chart_protein
                            PFC.FAT -> R.string.pie_chart_fat
                            PFC.CARB -> R.string.pie_chart_carb
                        }
                        click(k.color, context.getString(idStr, data[k].toString()))
                        invalidate()
                    }
                }


            }
        }
        return super.onTouchEvent(event)

    }
}