package com.rob.weather.citylist.fragment

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.App
import com.rob.weather.citylist.CityAdapter
import com.rob.weather.citylist.SwipeToDeleteCallback
import com.rob.weather.citylist.viewmodel.CityListViewModel
import com.rob.weather.databinding.CityListFragmentBinding
import com.rob.weather.datasource.retrofit.WeatherDataSource
import com.rob.weather.generaldaytoday.fragment.ShowDialogForChangingCity
import com.rob.weather.utils.BaseFragment
import javax.inject.Inject

class CityListFragment :
    BaseFragment<CityListFragmentBinding>(CityListFragmentBinding::inflate) {


    private val viewModel: CityListViewModel by viewModels()
  //  @Inject
//   lateinit var cityListViewModelFactory: CityListModelFactory
//   private val viewModel: CityListViewModel by viewModels { cityListViewModelFactory}
    private val dialog = ShowDialogForChangingCity()

   // lateinit var viewModel: CityListViewModel

 //   private val retrofitService: WeatherDataSource

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cityAdapter =
            CityAdapter()
        val measureRecyclerView = binding.recyclerview
        measureRecyclerView.adapter = cityAdapter
        measureRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                    //viewModel.deleteCity(pos)
                //  modelList.removeAt(pos)
                cityAdapter.notifyItemRemoved(pos)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)

        viewModel.cityList.observe(viewLifecycleOwner) { weatherInCities ->
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