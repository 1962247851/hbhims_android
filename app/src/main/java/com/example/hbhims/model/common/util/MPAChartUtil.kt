package com.example.hbhims.model.common.util

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.hbhims.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.youth.xframe.XFrame
import java.util.*

class MPAChartUtil {

    companion object {

        val defaultChartColors: List<Int> = ArrayList(
            listOf(
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor1),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor2),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor3),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor4),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor5),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor6),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor7),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor8),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor9),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor10),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor11),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor12),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor13),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor14),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor15),
                ContextCompat.getColor(XFrame.getContext(), R.color.ChartColor16)
            )
        )

        /**
         * 创建一个饼状图
         *
         * @param entries     entries
         * @param colors      颜色
         * @param label       图例的标签（默认关闭）pieChart.getLegend().setEnabled(false);
         * @param description 图的描述
         * @return PieChart
         */
        @JvmStatic
        fun createPieChart(
            context: Context,
            entries: List<PieEntry>,
            colors: List<Int>,
            label: String,
            description: String
        ): PieChart {
            val pieChart = PieChart(context)
            val layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800)
            pieChart.layoutParams = layoutParams
            return updatePieChart(entries, pieChart, context, label, colors, description)
        }

        /**
         * 更新一个饼状图
         *
         * @param entries     entries
         * @param pieChart pieChart
         * @param context context
         * @param label       图例的标签（默认关闭）pieChart.getLegend().setEnabled(false);
         * @param colors      颜色
         * @param description 图的描述
         * @return PieChart
         */
        @JvmStatic
        fun updatePieChart(
            entries: List<PieEntry>,
            pieChart: PieChart,
            context: Context,
            label: String,
            colors: List<Int>,
            description: String
        ): PieChart {
            var haveSmallValue = false
            for (pieEntry in entries) {
                if (pieEntry.value - 0.05 < 1e-9) {
                    haveSmallValue = true
                    break
                }
            }
            pieChart.setUsePercentValues(true)
            pieChart.setNoDataTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.MainTextColor
                )
            )
            pieChart.setEntryLabelColor(
                ContextCompat.getColor(
                    context,
                    R.color.MainTextColor
                )
            )
            pieChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD)
            pieChart.setNoDataText(context.getString(R.string.no_data_chart))
            pieChart.legend.isEnabled = false
            if (entries.isNotEmpty()) {
                val set = PieDataSet(entries, label)
                //根据主题设置颜色
                set.valueTextColor = ContextCompat.getColor(context, R.color.MainTextColor)
                set.valueLineColor = ContextCompat.getColor(context, R.color.MainTextColor)
                set.colors = colors
                if (haveSmallValue) {
                    //数据在饼图周围显示
                    set.valueLinePart1OffsetPercentage = 80f
                    set.valueLinePart1Length = 1.2f
                    set.valueLinePart2Length = 0.5f
                    set.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                    pieChart.setExtraOffsets(10f, 5f, 10f, 5f)
                    pieChart.setDrawEntryLabels(false)
                    val legend = pieChart.legend
                    legend.textColor = ContextCompat.getColor(
                        context,
                        R.color.MainTextColor
                    )
                    legend.isEnabled = true
                    legend.orientation = Legend.LegendOrientation.HORIZONTAL
                    //顶部
                    legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                    //左对齐
                    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                }
                val data = PieData(set)
                data.setDrawValues(true)
                data.setValueFormatter(PercentFormatter(pieChart))
                data.setValueTextSize(12f)
                pieChart.data = data
                pieChart.invalidate()
                val dscp = Description()
                dscp.text = description
                pieChart.description = dscp
                pieChart.holeRadius = 0f
                pieChart.transparentCircleRadius = 0f
            } else {
                pieChart.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            return pieChart
        }

        /**
         * 创建一个柱状图
         *
         * @param entries     entries
         * @param colors      颜色
         * @param label       图例的标签
         * @param xValues     横坐标
         * @param description 图的描述
         * @return BarChart
         */
        @JvmStatic
        fun createBarChart(
            context: Context,
            entries: List<BarEntry>,
            colors: List<Int>,
            label: String,
            xValues: List<String>,
            description: String,
            yAxisValueFormatter: ValueFormatter,
            dataValueFormatter: ValueFormatter
        ): BarChart {
            val barChart = BarChart(context)
            val layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800)
            barChart.layoutParams = layoutParams
            return updateBarChart(
                barChart,
                context,
                entries,
                xValues,
                label,
                colors,
                description,
                yAxisValueFormatter,
                dataValueFormatter
            )
        }

        @JvmStatic
        fun updateBarChart(
            barChart: BarChart,
            context: Context,
            entries: List<BarEntry>,
            xValues: List<String>,
            label: String,
            colors: List<Int>,
            description: String,
            yAxisValueFormatter: ValueFormatter,
            dataValueFormatter: ValueFormatter
        ): BarChart {
            barChart.setNoDataTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.MainTextColor
                )
            )
            barChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD)
            barChart.setNoDataText(context.getString(R.string.no_data_chart))
            //是否绘制网格背景
            barChart.setDrawGridBackground(false)
            //将Y数据显示在点的上方
            barChart.setDrawValueAboveBar(true)
            //挤压缩放
            barChart.setPinchZoom(true)
            //双击缩放
            barChart.isDoubleTapToZoomEnabled = true
            barChart.axisRight.isEnabled = false
            barChart.setFitBars(true)
            //图例设置
            barChart.legend.isEnabled = false
            if (entries.isNotEmpty()) {
                //对Y轴进行设置
                val yAxis = barChart.axisLeft
                yAxis.textColor = ContextCompat.getColor(context, R.color.MainTextColor)
                //设置最小间隔，防止当放大时，出现重复标签
                yAxis.granularity = 1f
                yAxis.setDrawAxisLine(true)
                yAxis.setDrawGridLines(true)
                // this replaces setStartAtZero(true)
                yAxis.axisMinimum = 0f
                yAxis.valueFormatter = yAxisValueFormatter
                //        yl.setInverted(true);
                val xAxis = barChart.xAxis
                xAxis.textColor = ContextCompat.getColor(context, R.color.MainTextColor)
                //设置最小间隔，防止当放大时，出现重复标签
                xAxis.granularity = 1f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                //显示个数
                xAxis.labelCount = xValues.size
                //设置x轴的数据
                xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
                val set = BarDataSet(entries, label)
                set.valueTextColor = ContextCompat.getColor(context, R.color.MainTextColor)
                set.colors = colors
                val data = BarData(set)
                data.setDrawValues(true)
                data.setValueTextSize(12f)
                data.setValueFormatter(dataValueFormatter)
                barChart.data = data
                //图表数据显示动画
                barChart.animateXY(800, 800)
                //设置屏幕显示条数
                barChart.setVisibleXRangeMaximum(8f)
                barChart.invalidate()
                val dscp = Description()
                dscp.text = description
                barChart.description = dscp
            } else {
                barChart.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            return barChart
        }

        /**
         * 创建一个折线图，单条折线
         *
         * @param entries     entries
         * @param colors      颜色
         * @param label       图例的标签
         * @param xValues     横坐标
         * @param description 图的描述
         * @return LineChart
         */
        @JvmStatic
        fun createLineChart(
            context: Context,
            entries: List<Entry>,
            colors: List<Int>,
            label: String,
            xValues: List<String>,
            description: String,
            yAxisValueFormatter: ValueFormatter,
            dataValueFormatter: ValueFormatter
        ): LineChart {
            val lineChart = LineChart(context)
            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800)
            lineChart.layoutParams = layoutParams
            return updateLineChart(
                lineChart,
                context,
                entries,
                xValues,
                label,
                colors,
                description,
                yAxisValueFormatter,
                dataValueFormatter
            )
        }

        @JvmStatic
        fun updateLineChart(
            lineChart: LineChart,
            context: Context,
            entries: List<Entry>,
            xValues: List<String>,
            label: String,
            colors: List<Int>,
            description: String,
            yAxisValueFormatter: ValueFormatter,
            dataValueFormatter: ValueFormatter
        ): LineChart {
            lineChart.setNoDataTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.MainTextColor
                )
            )
            lineChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD)
            lineChart.setNoDataText(context.getString(R.string.no_data_chart))
            //是否绘制网格背景
            lineChart.setDrawGridBackground(false)
            //挤压缩放
            lineChart.setPinchZoom(true)
            //双击缩放
            lineChart.isDoubleTapToZoomEnabled = true
            lineChart.axisRight.isEnabled = false
            //图例设置
            lineChart.legend.isEnabled = false
            if (entries.isNotEmpty()) {
                //对Y轴进行设置
                val yAxis = lineChart.axisLeft
                yAxis.textColor = ContextCompat.getColor(context, R.color.MainTextColor)
                //设置最小间隔，防止当放大时，出现重复标签
                yAxis.granularity = 1f
                yAxis.setDrawAxisLine(true)
                yAxis.setDrawGridLines(true)
                // this replaces setStartAtZero(true)
                yAxis.axisMinimum = 0f
                yAxis.valueFormatter = yAxisValueFormatter
//                object :
//                    ValueFormatter() {
//                    override fun getFormattedValue(value: Float): String {
//                        return value.toInt().toString() + unitString
//                    }
//                }
                //        yl.setInverted(true);
                val xAxis = lineChart.xAxis
                xAxis.textColor = ContextCompat.getColor(context, R.color.MainTextColor)
                //设置最小间隔，防止当放大时，出现重复标签
                xAxis.granularity = 1f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.axisMinimum = -1f
                xAxis.axisMaximum = xValues.size.toFloat()
                //显示个数
                xAxis.labelCount = xValues.size + 2
                //设置x轴的数据
                xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
                val set = LineDataSet(entries, label)
                set.valueTextColor = ContextCompat.getColor(context, R.color.MainTextColor)
                set.colors = colors
                val data = LineData(set)
                data.setDrawValues(true)
                data.setValueTextSize(12f)
                data.setValueFormatter(dataValueFormatter)
//                object :
//                    ValueFormatter() {
//                    override fun getFormattedValue(value: Float): String {
//                        return if (dataIsInteger) {
//                            value.toInt().toString() + unitString
//                        } else {
//                            value.toString() + unitString
//                        }
//                    }
//                }
                lineChart.data = data
                //图表数据显示动画
                lineChart.animateXY(800, 800)
                //设置屏幕显示条数
                lineChart.setVisibleXRangeMaximum(5f)
                lineChart.invalidate()
                val dscp = Description()
                dscp.text = description
                lineChart.description = dscp
            } else {
                lineChart.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            return lineChart
        }

        /**
         * 创建一个折线图，多条折线
         *
         * @param entriesList entriesList
         * @param labels      图例的标签
         * @param xValues     横坐标
         * @param description 图的描述
         * @return LineChart
         */
        @JvmStatic
        fun createMultiLineChart(
            context: Context,
            entriesList: List<List<Entry>>,
            labels: List<String>,
            xValues: List<String>,
            description: String,
            yAxisValueFormatter: ValueFormatter,
            dataValueFormatter: ValueFormatter
        ): LineChart {
            val lineChart = LineChart(context)
            val layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800)
            lineChart.layoutParams = layoutParams
            return updateMultiLineChart(
                lineChart,
                context,
                entriesList,
                xValues,
                labels,
                description,
                yAxisValueFormatter,
                dataValueFormatter
            )
        }

        @JvmStatic
        fun updateMultiLineChart(
            lineChart: LineChart,
            context: Context,
            entriesList: List<List<Entry>>,
            xValues: List<String>,
            labels: List<String>,
            description: String,
            yAxisValueFormatter: ValueFormatter,
            dataValueFormatter: ValueFormatter
        ): LineChart {
            val lineDataSets: MutableList<ILineDataSet> =
                ArrayList()
            lineChart.setNoDataTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.MainTextColor
                )
            )
            lineChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD)
            lineChart.setNoDataText(context.getString(R.string.no_data_chart))
            //是否绘制网格背景
            lineChart.setDrawGridBackground(false)
            //挤压缩放
            lineChart.setPinchZoom(true)
            //双击缩放
            lineChart.isDoubleTapToZoomEnabled = true
            lineChart.axisRight.isEnabled = false
            //图例设置
            val legend = lineChart.legend
            legend.textColor = ContextCompat.getColor(context, R.color.MainTextColor)
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            //顶部
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            //左对齐
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            var hasData = false
            for (list in entriesList) {
                if (list.isNotEmpty()) {
                    hasData = true
                    break
                }
            }
            if (hasData) {
                //对Y轴进行设置
                val yAxis = lineChart.axisLeft
                yAxis.textColor = ContextCompat.getColor(context, R.color.MainTextColor)
                //设置最小间隔，防止当放大时，出现重复标签
                yAxis.granularity = 1f
                yAxis.setDrawAxisLine(true)
                yAxis.setDrawGridLines(true)
                // this replaces setStartAtZero(true)
                yAxis.axisMinimum = 0f
                yAxis.valueFormatter = yAxisValueFormatter
                //        yl.setInverted(true);
                val xAxis = lineChart.xAxis
                xAxis.textColor = ContextCompat.getColor(context, R.color.MainTextColor)
                //设置最小间隔，防止当放大时，出现重复标签
                xAxis.granularity = 1f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.axisMinimum = -1f
                xAxis.axisMaximum = xValues.size.toFloat()
                //显示个数
                xAxis.labelCount = xValues.size + 2
                //设置x轴的数据
                xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
                for (i in entriesList.indices) {
                    val entries =
                        entriesList[i]
                    val set = LineDataSet(entries, labels[i])
                    set.valueTextColor = ContextCompat.getColor(
                        context,
                        R.color.MainTextColor
                    )
                    set.color =
                        defaultChartColors[i]
                    set.setCircleColor(
                        defaultChartColors[i]
                    )
                    set.mode = LineDataSet.Mode.CUBIC_BEZIER
                    set.setDrawFilled(true)
                    set.fillColor = defaultChartColors[i]
                    lineDataSets.add(set)
                }
                val data = LineData(lineDataSets)
                data.setDrawValues(true)
                data.setValueTextSize(12f)
                data.setValueFormatter(dataValueFormatter)
                lineChart.data = data
                //图表数据显示动画
                lineChart.animateXY(800, 800)
                //设置屏幕显示条数
                lineChart.setVisibleXRangeMaximum(5f)
                lineChart.invalidate()
                val dscp = Description()
                dscp.text = description
                lineChart.description = dscp
            } else {
                lineChart.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            return lineChart
        }

    }
}