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
        val appBar = binding.appBar
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

//        val toolBar: Toolbar = view.findViewById(R.id.toolbar)
//        (activity as AppCompatActivity?)!!.setSupportActionBar(toolBar)
//        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("title")
//
//        val collapsingToolbarLayout: CollapsingToolbarLayout =
//            view.findViewById(R.id.toolbar_layout)
//        collapsingToolbarLayout.title = city

        //binding.text.setOnClickListener{Navigation.findNavController(view).navigate(R.id.action_weatherInformationByDayFragment_to_oneDayWeatherInformationFragment)}
//        binding.appBar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
//                //  Collapsed
//                Toast.makeText(context, "full", Toast.LENGTH_SHORT).show()
//            } else {
//                //Expanded
//                Toast.makeText(context, "full", Toast.LENGTH_SHORT).show()
//            }
//        })

//        binding.appBar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            if (collapsingToolbar.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(
//                    collapsingToolbar
//                )
//            ) {
//                toolbar.setBackgroundColor(R.color.purple_700)
//            } else {
//                toolbar.setBackgroundColor(R.color.white)
//                   // .setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
//            }
//        })
        appBar.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    binding.toolbarToday.visibility = View.GONE
                    // binding.toolbar.setBackgroundColor(R.color.purple_700)
                } else if (isShow) {
                    isShow = false
                    binding.toolbarToday.visibility = View.VISIBLE
                    // binding.toolbar.setBackgroundColor(R.color.white)
                }
            }
        })
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
                    //  HashMap<String, ArrayList<Integer>>()
                    //  binding.todayTemperatureTextView.text = response.body()?.list.map { mainList.lastIndex.toString() }
                 //   binding.toolbarLayout.title = response.body()?.city?.name.toString()
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