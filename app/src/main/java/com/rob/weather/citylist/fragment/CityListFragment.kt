package com.rob.weather.citylist.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.App
import com.rob.weather.citylist.CityAdapter
import com.rob.weather.citylist.DragAndDropCallback
import com.rob.weather.citylist.ShowDialogForChangingCity
import com.rob.weather.citylist.model.WeatherCity
import com.rob.weather.citylist.viewmodel.CityListViewModel
import com.rob.weather.databinding.CityListFragmentBinding
import com.rob.weather.generaldaytoday.fragment.CityListViewModelFactory
import com.rob.weather.utils.BaseFragment
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject

class CityListFragment : BaseFragment<CityListFragmentBinding>(CityListFragmentBinding::inflate) {

    @Inject
    lateinit var cityListViewModelFactory: CityListViewModelFactory
    val viewModel: CityListViewModel by viewModels { cityListViewModelFactory }
    private val dialog = ShowDialogForChangingCity()
    var сityList = listOf<WeatherCity>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cityListViewModelFactory = (activity?.application as App).component.getDependencyCityList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cityAdapter = CityAdapter()
        val measureRecyclerView = binding.recyclerview
        measureRecyclerView.adapter = cityAdapter

        viewModel.getCityList()

        lifecycleScope.launchWhenStarted {
            viewModel.cityList
                .collect { listCity ->
                    viewModel.getWeatherByCity(listCity)
                }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.cityListWithWeather
                .collect { cityList ->
                    cityAdapter.setData(cityList)
                    сityList = cityList
                }
        }

        val actionListCallback = object : DragAndDropCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                viewModel.deleteTheCity(pos)
                cityAdapter.notifyItemRemoved(pos)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                Collections.swap(сityList, from, to)
                cityAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                viewModel.updateCity(сityList)
                return true
            }
        }

        val itemTouchHelper = ItemTouchHelper(actionListCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)

        binding.arrowBackImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addCityButton.setOnClickListener {
            dialog.showDialog(requireContext(), viewModel)
        }

        binding.mapIcon.setOnClickListener {
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("myLogs", "onResume CityListFragment")
    }
}