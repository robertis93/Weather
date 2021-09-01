package com.rob.weather.selectedday

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.google.gson.internal.LinkedTreeMap
import com.rob.weather.R
import com.rob.weather.databinding.FragmetChooseDayBinding
import com.rob.weather.model.FullWeatherToday
import com.rob.weather.utils.BaseFragment
import com.rob.weather.utils.Utils
import com.squareup.picasso.Picasso

class SelectedDayFragment :
    BaseFragment<FragmetChooseDayBinding>(FragmetChooseDayBinding::inflate) {

    private val args by navArgs<SelectedDayFragmentArgs>()
    private val menu: Menu? = null

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { initSecChart(it, args.todayWeather) }
        with(binding) {
            toolbarToday.text = args.todayWeather.city
            currentDateTextview.text = args.todayWeather.date
            humidityValueTextview.text = args.todayWeather.humidity.toString()
            currentTemperatureTextview.text = args.todayWeather.temperature
            windValueTextview.text = args.todayWeather.windSpeed.toString()
            preciptationValueTextview.text = args.todayWeather.clouds.toString()
            currentWeatherDescriptionTextview.text = args.todayWeather.description
            val iconCode = args.todayWeather.icon
            val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
            Picasso.get().load(iconUrl).into(weatherIcon)

            arrowBackImageView.setOnClickListener {
                findNavController().navigate(R.id.action_chooseDayFragment_to_weatherInformationByDayFragment4)
            }
        }
    }

    private fun initSecChart(context: Context, todayWeather: FullWeatherToday) {
        val entries = ArrayList<Entry>()

        val listTime = mutableListOf<Int>()
        for (element in todayWeather.forecastResponseList) {
            listTime.add(element.main.temp.toInt())
            entries.add(Entry((element.date).returnHour().toFloat(), element.main.temp.toFloat()))
        }

        val vl = LineDataSet(entries, "")

        vl.mode = LineDataSet.Mode.CUBIC_BEZIER
        vl.fillColor = R.color.design_default_color_on_secondary
        //  vl.color = R.color.line_blue
        vl.setDrawValues(false)
        //  vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.line_back
        //vl.fillAlpha = R.color.material_on_background_emphasis_medium

        // draw points as solid circles
        // vl.setCircleRadius(3f)
        val lineChart = binding.linecharttt!!
        lineChart.baseline

        //   lineChart!!.xAxis.labelRotationAngle = 0f

        lineChart.data = LineData(vl)

        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.isEnabled = false
        lineChart.setDrawGridBackground(false)
        val j = 10f
        //  lineChart.xAxis.axisMaximum = j+0.1f

        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)


        // if disabled, scaling can be done on x- and y-axis separately
//        lineChart.setPinchZoom(false)
//        lineChart.getLegend().setEnabled(false)
//
//        lineChart.setDrawGridBackground(false)
//        val y: YAxis = lineChart.axisLeft
//        y.setLabelCount(6, false)
//        y.textColor = Color.WHITE
//        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
//        y.setDrawGridLines(false)
//        y.axisLineColor = Color.WHITE

        // no description text
        lineChart.description.isEnabled = false
        lineChart.setNoDataText("No forex yet!")

//Part10
        //  lineChart.animateX(1800, Easing.EaseInExpo)

//Part11
        val markerView = CustomMarker(context, R.layout.marker_view)
        lineChart.marker = markerView
    }

    //

//    private fun configureChartOptions1(): AAOptions {
//        val aaChartModel: AAChartModel = AAChartModel()
//            .chartType(AAChartType.Spline)
//            .markerRadius(0f)
//         //   .dataLabelsEnabled(false)
//            .tooltipEnabled(false)
//            .zoomType(AAChartZoomType.XY)
//            .touchEventEnabled(true)
//            .yAxisVisible(false)
//            .legendEnabled(false)
//            .xAxisVisible(true)
//            .backgroundColor("#4D63780D")
//            .borderRadius(24f)
//          //  .yAxisTitle("")
//            .series(
//                arrayOf(
//                    AASeriesElement()
//                        .color("#45A2FF")
//                        .data(
//                            arrayOf(
//                                arrayOf(9, 25),
//                                arrayOf(12, 20),
//                                arrayOf(15, 25),
//                                arrayOf(18, 20),
//                                arrayOf(21, 35)
//                            )
//                        ),
//                )
//            )
//
////        val aaOptions: AAOptions =
////            aaChartModel.aa_toAAOptions()
////        aaOptions.plotOptions?.column?.groupPadding = 0f
////        return aaOptions
//
//        val aaTooltip = AATooltip()
//            .useHTML(true)
//            .formatter("""
//function () {
//        return ' 🌑  ${"https://openweathermap.org/img/w/" + args.todayWeather.icon + ".png" } < br /> '
//        + ' 25 ° '
//        }
//""")
//            .valueDecimals(2)//设置取值精确到小数点后几位//设置取值精确到小数点后几位
//            .backgroundColor("#FFFFFF")
//            .borderColor("#FFFFFF")
//            .style(AAStyle()
//                .color("#2A2D33")
//                .fontSize(16f))
//        val aaOptions = aaChartModel.aa_toAAOptions()
//        aaOptions.tooltip = aaTooltip
//        return aaOptions
//    }


    private fun showOption(id: Int) {
        val item: MenuItem? = menu?.findItem(id)
        if (item != null) {
            item.isVisible = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> Toast.makeText(
                context, "Clicked search button",
                Toast.LENGTH_SHORT
            ).show()
        }
        return true
    }
}

class AAMoveOverEventMessageModel {
    var name: String? = null
    var x: Double? = null
    var y: Double? = null
    var category: String? = null
    var offset: LinkedTreeMap<*, *>? = null
    var index: Double? = null
}

class CustomMarker(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        val value = entry?.y?.toDouble() ?: 0.0
        var resText = ""
        if (value.toString().length > 8) {
            resText = "Val: " + value.toString().substring(0, 7)
        } else {
            resText = "Val: " + value.toString()
        }
        //tvPrice.text = resText
        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return MPPointF(-width / 2f, -height - 10f)
    }
}

fun String.returnHour(): String {
    val changedDate = Utils.fullDateFormat.parse(this)
    return Utils.hourFormat.format(changedDate)
}
