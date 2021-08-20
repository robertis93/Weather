package com.rob.weather.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rob.weather.R
import com.rob.weather.Utils.DateUtil.changeDateFormat
import com.rob.weather.databinding.FragmentGeneralDayTodayBinding
import com.rob.weather.databinding.SearchCityDialogBinding
import com.rob.weather.model.ForecastResponse
import com.rob.weather.model.SortedByDateWeatherForecastResult
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.view.adapters.GeneralDayTodayAdapter
import com.rob.weather.viewmodel.Retrofit.Common
import com.rob.weather.viewmodel.Retrofit.RemoteDataSource
import com.rob.weather.viewmodel.repository.Repository
import com.rob.weather.viewmodel.viewmodels.GeneralDayTodayViewModel
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GeneralDayTodayFragment : BaseFragment<FragmentGeneralDayTodayBinding>() {
    lateinit var repository: Repository
    lateinit var service: RemoteDataSource.RetrofitServices
    lateinit var layoutManager: LinearLayoutManager
    val allDaysWeatherListAdapter = GeneralDayTodayAdapter()

    //val measureGroup: Map<String, List<WeatherForecastResult>>()
    var valuesMap: Map<String, List<ForecastResponse>> = HashMap()

    companion object {
        var AppId = "2e65127e909e178d0af311a81f39948c"
        var city = "ufa"
    }

    private val viewModel: GeneralDayTodayViewModel by lazy {
        ViewModelProvider(this).get(GeneralDayTodayViewModel::class.java)
    }

    override fun inflate(inflater: LayoutInflater): FragmentGeneralDayTodayBinding =
        FragmentGeneralDayTodayBinding.inflate(inflater)

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // val collapsingToolbar = binding.toolbarLayout
        // val appBar = binding.appBar
        val toolbar = binding.toolbar
        activity?.actionBar?.subtitle = "Vova"

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    changeCity()
                    true
                }
                R.id.action_loader -> {
                    // TODO : action
                    true
                }
                else -> onOptionsItemSelected(it)
            }

        }

//        repository = Repository(RemoteDataSource())
//        repository.refreshAll()


        service = Common.retrofitService

        getWeatherForecastList()

        val recyclerView = binding.recyclerView
        recyclerView.adapter = allDaysWeatherListAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // binding.blueRectangleView.setOnClickListener
//        {
//            findNavController().navigate(R.id.action_weatherInformationByDayFragment_to_chooseDayFragment)
//        }

    }

    private fun changeCity() {
        val builder = AlertDialog.Builder(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val dialogFragment = SearchCityDialogBinding.inflate(layoutInflater)
        builder.setView(dialogFragment.root)

        val alertDialog = builder.show()

        dialogFragment.addBtn.setOnClickListener {

        }
        dialogFragment.crossImageButton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
    }

    private fun getWeatherForecastList() {
//        dialog.show()
        service.geWeatherForecastResponse(city, AppId)
            .enqueue(object : Callback<WeatherForecastResult> {
                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<WeatherForecastResult>, t: Throwable) {
                    binding.currentTemperatureTextView.text = "Error"
                }

                override fun onResponse(
                    call: Call<WeatherForecastResult>,
                    response: Response<WeatherForecastResult>
                ) {

                    binding.currentDateTextView.text =
                        "${" Сегодня, "}" + response.body()?.list?.first()?.date?.let {
                            changeDateFormat(
                                it
                            )
                        }
                    binding.currentTemperatureTextView.text =
                        response.body()?.list?.first()?.main?.temp?.toInt()
                            .toString() + "${"°"}"
                    binding.currentWeatherDescriptionTextView.text =
                        response.body()?.list?.first()?.weather?.first()?.description.toString()
                            .capitalize() + "${", ощущается как  "}" + response.body()?.list?.first()?.main?.temp_max?.toInt()
                            .toString() + "${"°"}"

                    val iconCode = response.body()?.list?.first()?.weather?.first()?.icon
                    val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
                    Picasso.get().load(iconUrl).into(binding.weatherIcon)

                    fun geWeatherForecastResponseGroupByDate(): List<SortedByDateWeatherForecastResult> {
                        val weatherForecastGroup =
                            response.body()!!.list.groupBy { changeDateFormat(it.date) }
                        return (weatherForecastGroup.keys).map { date ->
                            val forecasts = weatherForecastGroup[date] ?: emptyList()
                            SortedByDateWeatherForecastResult(date, forecasts)
                        }
                    }

                    val sortedByDateForecastResponseList = geWeatherForecastResponseGroupByDate()
                    val withoutFirstElementSortedByDateForecastResponseList =
                        sortedByDateForecastResponseList.toMutableList()
                            .subList(1, sortedByDateForecastResponseList.size)
                    allDaysWeatherListAdapter.setData(
                        withoutFirstElementSortedByDateForecastResponseList
                    )
                }
            })
    }

//    private fun showOption(id: Int) {
//        val item: MenuItem? = menu?.findItem(id)
//        if (item != null) {
//            item.isVisible = false
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                changeCity()
                Log.i("myLogs", "clicked menu")
                return true
            }
            R.id.action_loader -> binding.currentDateTextView.text = "Пиздетс"

        }
        return super.onOptionsItemSelected(item)

    }
}