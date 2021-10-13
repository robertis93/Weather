package com.rob.weather.selectedday

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.rob.weather.R
import com.rob.weather.databinding.FragmetChooseDayBinding
import com.rob.weather.model.SortedByDateWeatherForecastResult
import com.rob.weather.utils.BaseFragment
import com.rob.weather.utils.Utils
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class SelectedDayFragment :
    BaseFragment<FragmetChooseDayBinding>(FragmetChooseDayBinding::inflate) {
    private val args by navArgs<SelectedDayFragmentArgs>()

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { initSecChart(it, args.weatherForecastList) }
        with(binding) {
            toolbarToday.text = args.weatherForecastList.city
            currentDateTextview.text = args.weatherForecastList.date
            humidityValueTextview.text =
                args.weatherForecastList.forecastResponseList.first().main.humidity.toString()
            currentTemperatureTextview.text =
                args.weatherForecastList.forecastResponseList.first().main.temp.toInt()
                    .toString() + "°"
            windValueTextview.text =
                args.weatherForecastList.forecastResponseList.first().wind.speed.toInt().toString()
            preciptationValueTextview.text =
                args.weatherForecastList.forecastResponseList.first().clouds.all.toString()
            currentWeatherDescriptionTextview.text =
                args.weatherForecastList.forecastResponseList.first().weather.first().description
                    .replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                        else "$it"
                    } + ", ощущается как  " + Math.round(args.weatherForecastList.forecastResponseList.first().main.temp_max)
                    .toString() + "°"
            val iconCode =
                args.weatherForecastList.forecastResponseList.first().weather.first().icon
            val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
            Picasso.get().load(iconUrl).into(weatherIcon)

            arrowBackImageView.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun initSecChart(context: Context, todayWeather: SortedByDateWeatherForecastResult) {
        val entries = ArrayList<Entry>()
        val xValsOriginalMillis = ArrayList<Long>()

        for (element in todayWeather.forecastResponseList) {
            xValsOriginalMillis.add(element.dt)
        }

        val listTime = mutableListOf<Int>()
        for (element in todayWeather.forecastResponseList) {
            val iconCode = element.weather.first().icon
            listTime.add(element.main.temp.toInt())
            entries.add(
                Entry(
                    (((element.date).returnHour())).toFloat(),
                    element.main.temp.toInt().toFloat(),
                    iconCode
                )
            )
        }

        val lineDataSet = LineDataSet(entries, "")
        lineDataSet.setDrawIcons(false)
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(false)

        lineDataSet.setDrawHighlightIndicators(true)
        lineDataSet.setDrawVerticalHighlightIndicator(true)
        lineDataSet.setDrawHorizontalHighlightIndicator(true)
       // lineDataSet.setupCircularHighlightIndicator
       // lineDataSet.setDrawHorizontalHighlightIndicator(true)
        lineDataSet.setValueTextColor(R.color.grey_storm)
        lineDataSet.lineWidth = 3f
        lineDataSet.fillColor = R.color.line_blue
        val lineChart = binding.linecharttt
        lineChart.baseline
        //   lineChart!!.xAxis.labelRotationAngle = 0f
        lineChart.data = LineData(lineDataSet)
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.isEnabled = false
        lineChart.setDrawGridBackground(false)
        lineChart.setBackgroundResource(R.drawable.chart_rounded_corners)
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(false)
        lineChart.isScaleXEnabled = true
        lineChart.isScaleYEnabled = true

        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 12f
        xAxis.setDrawAxisLine(false)
        xAxis.gridDashPathEffect
        xAxis.setLabelCount(entries.size, true)
        xAxis.valueFormatter = MyValueFormatter()
        xAxis.axisMaximum = 21f

         lineChart.description.isEnabled = false
        lineChart.setNoDataText("No forex yet!")
        lineChart.invalidate()
        lineChart.centerOffsets
        lineChart.legend.xOffset = -60f
         lineChart.solidColor

        lineChart.highlighter.getHighlight(22f, 10f)

        val markerView = CustomMarker(context, R.layout.marker_view)
        lineChart.marker = markerView
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

class CustomMarker(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        val weatherIcon = findViewById<ImageView>(R.id.weather_icon)
        val iconCode = entry?.data
        val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
        Picasso.get().load(iconUrl).into(weatherIcon)
        val value = entry?.y?.toDouble() ?: 0.0
        var resText = value.toInt().toString() + "°"
        findViewById<TextView>(R.id.temperature_textview).text = resText
        super.refreshContent(entry, highlight)
    }


    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return if (ypos < 200) {
            MPPointF(-width + 150f, -height + 200f)
        } else MPPointF(-width - 20f, -height + 40f)
    }
}

fun String.returnHour(): String {
    val changedDate = Utils.fullDateFormat.parse(this)
    return Utils.hourFormat.format(changedDate)
}

class MyValueFormatter() : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        if (value.toInt() == 0 || value.toInt() == 3 || value.toInt() == 6 || value.toInt() == 9) {
            return "0" + value.toInt().toString() + ":00"
        } else {
            return value.toInt().toString() + ":00"
        }
    }
}
