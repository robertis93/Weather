package com.rob.weather.selectedday

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.rob.weather.R
import com.rob.weather.databinding.FragmetChooseDayBinding
import com.rob.weather.generaldaytoday.model.WeatherForecastForNextDays
import com.rob.weather.utils.BASE_URL_IMAGE
import com.rob.weather.utils.BaseFragment
import com.rob.weather.utils.Utils.fullDateFormat
import com.rob.weather.utils.Utils.hourFormat
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collect

class SelectedDayFragment :
    BaseFragment<FragmetChooseDayBinding>(FragmetChooseDayBinding::inflate) {
    private val args by navArgs<SelectedDayFragmentArgs>()
    private val selectedDayViewModel: SelectedDayViewModel by lazy {
        ViewModelProvider(this).get(SelectedDayViewModel::class.java)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            selectedDayViewModel.isSunRise.collect {
                binding.backgroundForWeatherIndicatorsView.setBackgroundResource(R.drawable.rectangle_sunrise_)
                binding.intersectView?.setBackgroundResource(R.drawable.intersect_sunrise)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            selectedDayViewModel.isDay.collect {
                binding.backgroundForWeatherIndicatorsView.setBackgroundResource(R.drawable.rectangle_day)
                binding.intersectView?.setBackgroundResource(R.drawable.intersect_day)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            selectedDayViewModel.isNight.collect {
                binding.backgroundForWeatherIndicatorsView.setBackgroundResource(R.drawable.rectangle_night)
                binding.intersectView?.setBackgroundResource(R.drawable.intersect_night)
            }
        }
        setWeatherData()
        drawingGraph(requireContext(), args.weatherForecastList)
        selectedDayViewModel.checkTime()
    }

    private fun setWeatherData() {
        with(binding) {
            nameCityToolbar.text = args.weatherForecastList.city
            currentDateTextview.text = args.weatherForecastList.date
            humidityValueTextview.text =
                args.weatherForecastList.humidity
            currentTemperatureTextview.text =
                args.weatherForecastList.averageTemperature + "°"
            windValueTextview.text = args.weatherForecastList.windSpeed
            preciptationValueTextview.text =
                args.weatherForecastList.preciptation
            currentWeatherDescriptionTextview.text =
                args.weatherForecastList.descriptionWeather +
                        requireContext().getString(R.string.feels_like) +
                        args.weatherForecastList.maxTemperatureForDay + "°"
            val iconCode =
                args.weatherForecastList.forecastResponseList.first().weather.first().icon
            val iconUrl = BASE_URL_IMAGE + iconCode + ".png"
            Picasso.get().load(iconUrl).into(weatherIcon)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun drawingGraph(context: Context, todayWeather: WeatherForecastForNextDays) {
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
        with(lineDataSet) {
            setDrawIcons(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawValues(false)
            setDrawCircles(false)
            setDrawHighlightIndicators(true)
            setDrawVerticalHighlightIndicator(false)
            setDrawHorizontalHighlightIndicator(false)
            lineWidth = 3f
            color = ContextCompat.getColor(requireContext(), R.color.blue)
        }

        val lineChart = binding.linecharttt
        with(lineChart) {
            baseline
            data = LineData(lineDataSet)
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            setDrawGridBackground(false)
            setBackgroundResource(R.drawable.chart_rounded_corners)
            setTouchEnabled(true)
            setPinchZoom(false)
            isScaleXEnabled = true
            isScaleYEnabled = true
            description.isEnabled = false
            setNoDataText("No forex yet!")
            invalidate()
            centerOffsets
            legend.xOffset = -60f
            highlighter.getHighlight(22f, 10f)
        }

        val xAxis: XAxis = lineChart.xAxis
        with(xAxis) {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            textSize = 12f
            setDrawAxisLine(false)
            setLabelCount(entries.size, true)
            valueFormatter = XAxisTimeDisplay()
            axisMaximum = 21f
        }

        val markerView = PopupWeatherInCity(context, R.layout.marker_view)
        lineChart.marker = markerView
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.text_chart)
    }
}

fun String.returnHour(): String {
    val changedDate = fullDateFormat.parse(this)
    return hourFormat.format(changedDate)
}


