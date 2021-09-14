package com.rob.weather.citylist.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.App
import com.rob.weather.citylist.CityAdapter
import com.rob.weather.citylist.SwipeToDeleteCallback
import com.rob.weather.citylist.database.CityDao
import com.rob.weather.citylist.database.WeatherDataBase
import com.rob.weather.citylist.database.WeatherRepository
import com.rob.weather.citylist.viewmodel.CityListViewModel
import com.rob.weather.citylist.viewmodel.CityListViewModelFactory
import com.rob.weather.databinding.CityListFragmentBinding
import com.rob.weather.datasource.retrofit.WeatherDataFromRemoteSource
import com.rob.weather.generaldaytoday.fragment.ShowDialogForChangingCity
import com.rob.weather.utils.BaseFragment


class CityListFragment :
    BaseFragment<CityListFragmentBinding>(CityListFragmentBinding::inflate) {


    lateinit var db: WeatherDataBase
    private var genderDao: CityDao? = null
    val dataSource = WeatherDataFromRemoteSource()

   // val app = App()

    private val dialog = ShowDialogForChangingCity()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = WeatherDataBase.getDataBase(context = requireContext())
        genderDao = db?.cityDao()
        val cityListViewModelFactory =
            CityListViewModelFactory(WeatherRepository(db.cityDao(), dataSource))

        val viewModel: CityListViewModel by lazy {
            ViewModelProvider(this, cityListViewModelFactory).get(CityListViewModel::class.java)
        }
        val cityAdapter =
            CityAdapter()
        val measureRecyclerView = binding.recyclerview
        measureRecyclerView.adapter = cityAdapter
        measureRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                viewModel.deleteCity(pos)
                //  modelList.removeAt(pos)
                cityAdapter.notifyItemRemoved(pos)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)

        viewModel.weatherCityList.observe(viewLifecycleOwner) { weatherInCities ->
            cityAdapter.setData(weatherInCities)
        }

        binding.arrowBackImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addCityButton.setOnClickListener {
            dialog.showDialog(requireContext(), viewModel)
            //  dialog.showDialog(requireContext(), viewModel)
        }
    }
}