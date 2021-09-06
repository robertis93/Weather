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
import androidx.core.view.marginLeft
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.rob.weather.R
import com.rob.weather.databinding.FragmetChooseDayBinding
import com.rob.weather.model.FullWeatherToday
import com.rob.weather.utils.BaseFragment
import com.rob.weather.utils.Utils
import com.rob.weather.utils.Utils.fullDateFormat
import com.rob.weather.utils.Utils.timeFormat
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
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

    @SuppressLint("ResourceAsColor")
    private fun initSecChart(context: Context, todayWeather: FullWeatherToday) {
        val entries = ArrayList<Entry>()
        val image: ImageView
        val someResource = resources.getDrawable(R.drawable.ic_sun)
        val iconCode = args.todayWeather.icon
        val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"

//        val bitmap: Bitmap = Glide
//            .with(context)
//            .asBitmap()
//            .load("https://openweathermap.org/img/w/" + iconCode + ".png")
//            .submit()
//            .get()
        // val k =  Picasso.get().load(iconUrl).into(bitmap)
        val picasso = Picasso.get()


        //val mTarget : Target()

        val xValsDateLabel = ArrayList<String>()
        val xValsOriginalMillis = ArrayList<Long>()
        //test list
//        xValsOriginalMillis.add(1554875423736L)
//        xValsOriginalMillis.add(1555275494836L)
//        xValsOriginalMillis.add(1585578525900L)
//        xValsOriginalMillis.add(1596679626245L)
//        xValsOriginalMillis.add(1609990727820L)

        //  val listTime = mutableListOf<Int>()
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
        //testlist
//        entries.add(Entry(0f, 5f))
//        entries.add(Entry(1f, 10f))
//        entries.add(Entry(2f, 15f))
//        entries.add(Entry(3f, 20f))
//        entries.add(Entry(4f, 5f))
//        entries.add(Entry(5f, 10f))

        val vl = LineDataSet(entries, "")
        vl.setDrawIcons(false)

        vl.mode = LineDataSet.Mode.CUBIC_BEZIER
        //  vl.color = R.color.line_blue
        vl.setDrawValues(false)
        vl.setDrawCircles(false)
        vl.setDrawHighlightIndicators(false)
        vl.setDrawHorizontalHighlightIndicator(false)

        // vl.setValueTextColors()
        //  vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.line_blue


        // draw points as solid circles
        // vl.setCircleRadius(3f)
        val lineChart = binding.linecharttt
        lineChart.baseline

        //   lineChart!!.xAxis.labelRotationAngle = 0f

        lineChart.data = LineData(vl)

        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.isEnabled = false
        lineChart.setDrawGridBackground(false)
        lineChart.setBackgroundResource(R.drawable.chart_rounded_corners)

        val j = 10f
        //  lineChart.xAxis.axisMaximum = j+0.1f

        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(false)

         lineChart.setScaleXEnabled(true);
         lineChart.setScaleYEnabled(true);
        //  val xAxisFormatter: AxisValueFormatter = HourAxisValueFormatter()
        //  val xAxis: XAxis = lineChart.getXAxis()


//        val xAxisFormatter: HourAxisValueFormatter = HourAxisValueFormatter()
//        val xAxisss: XAxis = lineChart.xAxis
        // xAxisss.valueFormatter = xAxisFormatter

        // chart.setDrawYLabels(false);
        // val xAxisFormatter: IAxisValueFormatter = DayAxisValueFormatter(lineChart)

        // if disabled, scaling can be done on x- and y-axis separately
//        lineChart.setPinchZoom(false)
//        lineChart.getLegend().setEnabled(false)
//
//        lineChart.setDrawGridBackground(false)
        // chart.setDrawYLabels(false);
        // val xAxisFormatter: ValueFormatter = CustomYAxisValueFormatter()
        val xAxis: XAxis = lineChart.xAxis
       // xAxis.xOffset = +700f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 12f
        xAxis.setDrawAxisLine(false)
        xAxis.gridDashPathEffect



        for (i in xValsOriginalMillis.indices) {
            val mm = xValsOriginalMillis[i] / 60 % 60
            val hh = xValsOriginalMillis[i] / (60 * 60) % 24
            val mDateTime = "$hh:$mm" + "0"
            xValsDateLabel.add(mDateTime)
        }
        xAxis.valueFormatter = (MyValueFormatter(xValsDateLabel))


        // xAxis.enableGridDashedLine(10f, 10f, 10f)
        // CustomYAxisValueFormatter().getFormattedValue(100f, xAxis)
        //xAxis.setValueFormatter(CustomYAxisValueFormatter().getFormattedValue(100f, xAxis))


        // lineChart.setMarkerView(myMarkerView)
//        xAxis.valueFormatter = MyXAxisValueFormatter()
//        xAxis.setLabelsToSkip(0)

//        y.setLabelCount(6, false)
//        y.textColor = Color.WHITE
//        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        // x.setDrawGridLines(false)
//        y.axisLineColor = Color.WHITE

        // no description text
        lineChart.description.isEnabled = false
        lineChart.setNoDataText("No forex yet!")
        lineChart.centerOffsets
            //lineChart.legend.xOffset = +10f
        //ось поднялась наверх
        lineChart.legend.yOffset = +10f
        lineChart.solidColor
        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener
        {
            override fun onValueSelected(e: Entry, h: Highlight?) {
                val x = e.x.toString()
                val y = e.y
                val selectedXAxisCount = x.substringBefore(".") //this value is float so use substringbefore method
                // another method shown below
               // vl.setDrawCircles(true)
               // vl.circleRadius = 20f
                val nonFloat=lineChart.getXAxis().getValueFormatter().getFormattedValue(e.x)
                //if you are display any string in x axis you will get this
            }

            override fun onNothingSelected() {}
        })

//Part10
        //  lineChart.animateX(1800, Easing.EaseInExpo)

//Part11
        val markerView = CustomMarker(context, R.layout.marker_view)
        lineChart.marker = markerView
        // xAxis.valueFormatter = xAxisFormatter
    }

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
interface IAxisValueFormatter {
    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return
     */
    fun getFormattedValue(value: Float, axis: AxisBase?): String?
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

class MyValueFormatter(private val xValsDateLabel: ArrayList<String>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return value.toString()
    }

    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        if (value.toInt() == 3 || value.toInt() == 6 || value.toInt() == 9 || value.toInt() == 12 || value.toInt() == 15 || value.toInt() == 18 || value.toInt() == 21) {
            return value.toInt().toString() + ":00"
        } else {
            return ""
        }
    }
}
