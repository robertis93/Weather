package com.rob.weather.selectedDay

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.rob.weather.R
import com.rob.weather.Utils.BaseFragment
import com.rob.weather.databinding.FragmetChooseDayBinding

class SelectedDayFragment : BaseFragment<FragmetChooseDayBinding>(FragmetChooseDayBinding::inflate) {

    //TODO : этот фрагемент пока что не готов
    // private val args by navArgs<ChooseDayFragmentArgs>()
//    private val viewModel: AllDaysWeatherInformationViewModel by lazy {
//        ViewModelProvider(this).get(AllDaysWeatherInformationViewModel::class.java)
//    }
    interface AAChartViewCallBack {
        fun chartViewMoveOverEventMessage(
            aaChartView: AAChartView,
            messageModel: AAMoveOverEventMessageModel
        )
    }

    private val menu: Menu? = null

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBackImageView.setOnClickListener {
            findNavController().navigate(R.id.action_chooseDayFragment_to_weatherInformationByDayFragment)

        }

        binding.aaChartView.aa_drawChartWithChartOptions(configureChartOptions1())

//        fun configureChartOptions(
//            aaChartModel: AAChartModel
//        ): AAOptions {
//
//            val aaChart = AAChart()
//                .type(aaChartModel.chartType) //绘图类型
//                .inverted(aaChartModel.inverted) //设置是否反转坐标轴，使X轴垂直，Y轴水平。 如果值为 true，则 x 轴默认是 倒置 的。 如果图表中出现条形图系列，则会自动反转
//                .backgroundColor(aaChartModel.backgroundColor) //设置图表的背景色(包含透明度的设置)
//                .pinchType(aaChartModel.zoomType) //设置手势缩放方向
//                .panning(true) //设置手势缩放后是否可平移
//                .polar(aaChartModel.polar) //是否极化图表(开启极坐标模式)
//                .margin(aaChartModel.margin)
//                .scrollablePlotArea(aaChartModel.scrollablePlotArea)
//        }


        // binding.aaChartView.eve

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

    private fun configureChartOptions1(): com.github.aachartmodel.aainfographics.aachartcreator.AAOptions {
        val aaChartModel: AAChartModel = AAChartModel()
            //.stacking(AAChartZoomType.None)
            .chartType(AAChartType.Line)
            //.chartType(AAChartType.Areaspline)
//            .yAxisGridLineWidth(0f)
            .markerRadius(0f)
            .tooltipEnabled(false)
            .zoomType(AAChartZoomType.XY)
            // .legendEnabled(false)
            .yAxisVisible(false)
            //  .touchEventEnabled(true)
            .backgroundColor("#4D63780D")
            //  .dataLabelsEnabled(true)
            .series(
                arrayOf(
                    AASeriesElement()
                        .color("#45A2FF")
                        .data(
                            arrayOf(
                                7.0,
                                6.9,
                                9.5,
                                14.5,
                                18.2,
                                21.5,
                                25.2,
                                26.5,
                                23.3,
                                18.3,
                                13.9,
                                9.6
                            )
                        ),
                )
            )


        val aaOptions: com.github.aachartmodel.aainfographics.aachartcreator.AAOptions =
            aaChartModel.aa_toAAOptions()
        aaOptions.plotOptions?.column?.groupPadding = 0f
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
                context,
                "Clicked search button",
                Toast.LENGTH_SHORT
            ).show()
        }
        return true
    }
}

//it is for AAChartCore
//class AAMoveOverEventMessageModel {
//    var name: String? = null
//    var x: Double? = null
//    var y: Double? = null
//    var category: String? = null
//    var offset: LinkedTreeMap<*, *>? = null
//    var index: Double? = null
//}


