package com.rob.weather.selectedday

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.rob.weather.R
import com.rob.weather.utils.BaseFragment
import com.rob.weather.databinding.FragmetChooseDayBinding

class SelectedDayFragment :
    BaseFragment<FragmetChooseDayBinding>(FragmetChooseDayBinding::inflate) {

    private val menu: Menu? = null

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBackImageView.setOnClickListener {
            findNavController().navigate(R.id.action_chooseDayFragment_to_weatherInformationByDayFragment)

        }

            //binding.aaChartView.aa_drawChartWithChartOptions(configureChartOptions1())
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
