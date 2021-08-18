package com.rob.weather.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.rob.weather.R
import com.rob.weather.databinding.FragmetChooseDayBinding
import com.rob.weather.viewmodel.viewmodels.AllDaysWeatherInformationViewModel
import org.w3c.dom.Entity
import java.security.KeyStore


class ChooseDayFragment : BaseFragment<FragmetChooseDayBinding>() {

//    private val viewModel: AllDaysWeatherInformationViewModel by lazy {
//        ViewModelProvider(this).get(AllDaysWeatherInformationViewModel::class.java)
//    }

    private val menu: Menu? = null
    override fun inflate(inflater: LayoutInflater): FragmetChooseDayBinding =
        FragmetChooseDayBinding.inflate(inflater)

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("title")
            .subtitle("subtitle")
            .backgroundColor("#4b2b7f")
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("Tokyo")
                    .data(arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)),
                AASeriesElement()
                    .name("NewYork")
                    .data(arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)),
                AASeriesElement()
                    .name("London")
                    .data(arrayOf(0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0)),
                AASeriesElement()
                    .name("Berlin")
                    .data(arrayOf(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8))
            )
            )
        binding.aaChartView.aa_drawChartWithChartModel(aaChartModel)

//        fun setLineChart(){
//            val xValue = ArrayList<String>()
//            xValue.add("9:00")
//            xValue.add("12:00")
//            xValue.add("15:00")
//            xValue.add("18:00")
//
//            val lineetry = ArrayList<Entry>()
//            lineetry.add(Entry(20f, 0F))
//            lineetry.add(Entry(50f, 1F))
//            lineetry.add(Entry(60f, 2F))
//            lineetry.add(Entry(70f, 3F))
//            lineetry.add(Entry(10f, 4F))
//
//            val lineDataSet = LineDataSet(lineetry, "First")
//            lineDataSet.color = resources.getColor(R.color.grey_lineral)
//
//            val data = LineData(xValue, lineDataSet)
//
//            setLineChart()
//        }


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
                context,
                "Clicked search button",
                Toast.LENGTH_SHORT
            ).show()
        }
        return true
    }
}