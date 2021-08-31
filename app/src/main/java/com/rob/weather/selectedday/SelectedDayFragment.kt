package com.rob.weather.selectedday

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AATooltip
import com.google.android.play.core.internal.br
import com.google.gson.internal.LinkedTreeMap
import com.rob.weather.R
import com.rob.weather.databinding.FragmetChooseDayBinding
import com.rob.weather.utils.BaseFragment
import com.squareup.picasso.Picasso

class SelectedDayFragment :
    BaseFragment<FragmetChooseDayBinding>(FragmetChooseDayBinding::inflate) {



    private val args by navArgs<SelectedDayFragmentArgs>()

    private val menu: Menu? = null

    interface AAChartViewCallBack {
        fun chartViewMoveOverEventMessage(aaChartView: AAChartView, messageModel: AAMoveOverEventMessageModel)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            val list = (args.todayWeather.forecastResponseList)

            val z = args.todayWeather

val array = args.todayWeather.forecastResponseList.first().main.temp
val array1 = args.todayWeather.forecastResponseList.last().main.temp
          val listTime = mutableListOf<Int>()
            for (element in args.todayWeather.forecastResponseList)
                listTime.add(element.main.temp.toInt())

            currentWeatherDescriptionTextview.text = listTime.size.toString()

            for (element in args.todayWeather.forecastResponseList)


            arrowBackImageView.setOnClickListener {
                 findNavController().navigate(R.id.action_chooseDayFragment_to_weatherInformationByDayFragment4)
            }

            binding.aaChartView.aa_drawChartWithChartOptions(configureChartOptions1())
//            binding.aaChartView.touchDelegate.onTouchEvent(MotionEvent.obtain())
//            aaChartView!.delegate = self as AAChartViewDelegate
            //Set AAChartView events delegate
//            aaChartView!!.touchDelegate = self as AAChartViewDelegate
//            //set AAChartModel support user touch event
//            aaChartModel = aaChartModel!.touchEventEnabled(true)
        }

    }

    private fun configureChartOptions1(): AAOptions {
        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .markerRadius(0f)
         //   .dataLabelsEnabled(false)
            .tooltipEnabled(false)
            .zoomType(AAChartZoomType.XY)
            .touchEventEnabled(true)
            .yAxisVisible(false)
            .legendEnabled(false)
            .xAxisVisible(true)
            .backgroundColor("#4D63780D")
            .borderRadius(24f)
          //  .yAxisTitle("")
            .series(
                arrayOf(
                    AASeriesElement()
                        .color("#45A2FF")
                        .data(
                            arrayOf(
                                arrayOf(9, 25),
                                arrayOf(12, 20),
                                arrayOf(15, 25),
                                arrayOf(18, 20),
                                arrayOf(21, 35)
                            )
                        ),
                )
            )

//        val aaOptions: AAOptions =
//            aaChartModel.aa_toAAOptions()
//        aaOptions.plotOptions?.column?.groupPadding = 0f
//        return aaOptions

        val aaTooltip = AATooltip()
            .useHTML(true)
            .formatter("""
function () {
        return ' 🌑  ${"https://openweathermap.org/img/w/" + args.todayWeather.icon + ".png" } < br /> '
        + ' 25 ° ' 
        }
""")
            .valueDecimals(2)//设置取值精确到小数点后几位//设置取值精确到小数点后几位
            .backgroundColor("#FFFFFF")
            .borderColor("#FFFFFF")
            .style(AAStyle()
                .color("#2A2D33")
                .fontSize(16f))
        val aaOptions = aaChartModel.aa_toAAOptions()
        aaOptions.tooltip = aaTooltip
        return aaOptions


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
class AAMoveOverEventMessageModel {
    var name: String? = null
    var x: Double? = null
    var y: Double? = null
    var category: String? = null
    var offset: LinkedTreeMap<*, *>? = null
    var index: Double? = null
}

