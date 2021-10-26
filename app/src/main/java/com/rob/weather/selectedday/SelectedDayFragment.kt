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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.rob.weather.R
import com.rob.weather.databinding.FragmetChooseDayBinding
import com.rob.weather.model.SortedByDateWeatherForecastResult
import com.rob.weather.utils.BaseFragment
import com.rob.weather.utils.Utils
import com.rob.weather.utils.Utils.BASE_URL_IMAGE
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class SelectedDayFragment :
    BaseFragment<FragmetChooseDayBinding>(FragmetChooseDayBinding::inflate) {
    private val args by navArgs<SelectedDayFragmentArgs>()

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawingGraph(requireContext(), args.weatherForecastList)
        setWeatherData()
    }

    private fun setWeatherData() {
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
                    } + requireContext().getString(R.string.feels_like) + Math.round(
                    args.weatherForecastList
                        .forecastResponseList.first().main.temp_max
                )
                    .toString() + "°"
            val iconCode =
                args.weatherForecastList.forecastResponseList.first().weather.first().icon
            val iconUrl = BASE_URL_IMAGE + iconCode + ".png"
            Picasso.get().load(iconUrl).into(weatherIcon)

            arrowBackImageView.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun drawingGraph(context: Context, todayWeather: SortedByDateWeatherForecastResult) {
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
        lineDataSet.setValueTextColor(R.color.grey_storm)
        lineDataSet.lineWidth = 3f
        lineDataSet.fillColor = R.color.line_blue
        val lineChart = binding.linecharttt
        lineChart.baseline
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
        xAxis.valueFormatter = XAxisTimeDisplay()
        xAxis.axisMaximum = 21f
        lineChart.description.isEnabled = false
        lineChart.setNoDataText("No forex yet!")
        lineChart.invalidate()
        lineChart.centerOffsets
        lineChart.legend.xOffset = -60f
        lineChart.solidColor
        lineChart.highlighter.getHighlight(22f, 10f)
        val markerView = AppearingIconWithWeather(context, R.layout.marker_view)
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

fun String.returnHour(): String {
    val changedDate = Utils.fullDateFormat.parse(this)
    return Utils.hourFormat.format(changedDate)
}


