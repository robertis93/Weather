package com.rob.weather.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.rob.weather.R
import com.rob.weather.Utils.changeDateFormat
import com.rob.weather.databinding.FragmentWeatherInformationAllDaysBinding
import com.rob.weather.model.ForecastResponse
import com.rob.weather.model.SortedByDateWeatherForecastResult
import com.rob.weather.model.WeatherForecastResult
import com.rob.weather.view.adapters.AllDaysWeatherListAdapter
import com.rob.weather.viewmodel.Retrofit.Common
import com.rob.weather.viewmodel.Retrofit.RemoteDataSource
import com.rob.weather.viewmodel.repository.Repository
import com.rob.weather.viewmodel.viewmodels.AllDaysWeatherInformationViewModel
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AllDaysWeatherInformationFragment : BaseFragment<FragmentWeatherInformationAllDaysBinding>() {
    lateinit var repository: Repository
    lateinit var service: RemoteDataSource.RetrofitServices
    lateinit var layoutManager: LinearLayoutManager
    val allDaysWeatherListAdapter = AllDaysWeatherListAdapter()

    //val measureGroup: Map<String, List<WeatherForecastResult>>()
    var valuesMap: Map<String, List<ForecastResponse>> = HashMap()

    companion object {
        var AppId = "2e65127e909e178d0af311a81f39948c"
        var city = "moscow"
    }

    private val viewModel: AllDaysWeatherInformationViewModel by lazy {
        ViewModelProvider(this).get(AllDaysWeatherInformationViewModel::class.java)
    }

    private val menu: Menu? = null
    override fun inflate(inflater: LayoutInflater): FragmentWeatherInformationAllDaysBinding =
        FragmentWeatherInformationAllDaysBinding.inflate(inflater)

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // val collapsingToolbar = binding.toolbarLayout
       // val appBar = binding.appBar
        val toolbar = binding.toolbar
        activity?.actionBar?.subtitle = "Vova"

//        repository = Repository(RemoteDataSource())
//        repository.refreshAll()


        service = Common.retrofitService
        // binding.recyclerMovieList.setHasFixedSize(true)
        // layoutManager = LinearLayoutManager(this)
        //  binding.recyclerMovieList.layoutManager = layoutManager
        // dialog = SpotsDialog.Builder().setCancelable(true).setContext(this).build()

        getAllMovieList()


        // allDaysWeatherListAdapter = AllDaysWeatherListAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = allDaysWeatherListAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun getAllMovieList() {
//        dialog.show()
        service.geWeatherForecastResponse(city, AppId)
            .enqueue(object : Callback<WeatherForecastResult> {
                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<WeatherForecastResult>, t: Throwable) {
//                    binding.todayTemperatureTextView.text = "Error"
//                    binding.weatherTextView.text = "Error"
                }

                override fun onResponse(
                    call: Call<WeatherForecastResult>,
                    response: Response<WeatherForecastResult>
                ) {

                    binding.currentDateTextView.text = "${" Сегодня, "}" + response.body()?.list?.first()?.date?.let {
                        changeDateFormat(
                            it
                        )
                    }
                    //  HashMap<String, ArrayList<Integer>>()
                      binding.currentTemperatureTextView.text = response.body()?.list?.first()?.main?.temp?.toInt()
                          .toString() + "${"°"}"
                   binding.currentWeatherTextView.text = response.body()?.list?.first()?.weather?.first()?.description + "${", ощущается как  "}" + response.body()?.list?.first()?.main?.temp_max?.toInt()
                       .toString() + "${"°"}"

                    val iconCode = response.body()?.list?.first()?.weather?.first()?.icon
                    val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
                    Picasso.get().load(iconUrl).into(binding.weatherIcon)

                    // response.body()?.let { allDaysWeatherListAdapter.setData(it.list) }


                    fun geWeatherForecastResponseGroupByDate(): List<SortedByDateWeatherForecastResult> {
                        val weatherForecastGroup = response.body()!!.list.groupBy { changeDateFormat(it.date) }
                        return (weatherForecastGroup.keys).map { date ->
                            val forecasts = weatherForecastGroup[date] ?: emptyList()
                            SortedByDateWeatherForecastResult(date, forecasts)
                        }
                    }
                    val z = geWeatherForecastResponseGroupByDate()

                    allDaysWeatherListAdapter.setData(z)
                }
            })
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