package com.rob.weather.selectedday

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rob.weather.R
import com.rob.weather.utils.BaseFragment
import com.rob.weather.databinding.FragmetChooseDayBinding
import com.squareup.picasso.Picasso

class SelectedDayFragment :
    BaseFragment<FragmetChooseDayBinding>(FragmetChooseDayBinding::inflate) {
    private val args by navArgs<SelectedDayFragmentArgs>()

    private val menu: Menu? = null

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

            arrowBackImageView.setOnClickListener {
                 findNavController().navigate(R.id.action_chooseDayFragment_to_weatherInformationByDayFragment4)

            }

            //binding.aaChartView.aa_drawChartWithChartOptions(configureChartOptions1())
        }
    }

//    private fun configureChartOptions1(): AAOptions {
//        val aaChartModel: AAChartModel = AAChartModel()
//            .chartType(AAChartType.Line)
//            .markerRadius(0f)
//            .tooltipEnabled(false)
//            .zoomType(AAChartZoomType.XY)
//            .yAxisVisible(false)
//            .backgroundColor("#4D63780D")
//            .series(
//                arrayOf(
//                    AASeriesElement()
//                        .color("#45A2FF")
//                        .data(
//                            arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9)
//                        ),
//                )
//            )
//
//        val aaOptions: AAOptions =
//            aaChartModel.aa_toAAOptions()
//        aaOptions.plotOptions?.column?.groupPadding = 0f
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
