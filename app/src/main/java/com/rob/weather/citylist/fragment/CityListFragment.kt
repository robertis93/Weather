package com.rob.weather.citylist.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.drag.ItemTouchCallback
import com.mikepenz.fastadapter.drag.SimpleDragCallback
import com.mikepenz.fastadapter.utils.DragDropUtil
import com.rob.weather.App
import com.rob.weather.citylist.BindingWeatherInCityItem
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

class CityListFragment : BaseFragment<CityListFragmentBinding>(CityListFragmentBinding::inflate),
    ItemTouchCallback {

    @Inject
    lateinit var cityListViewModelFactory: CityListViewModelFactory
    val viewModel: CityListViewModel by viewModels { cityListViewModelFactory }
    private val dialog = ShowDialogForChangingCity()
    var сityList = listOf<WeatherCity>()
    val cityWeatherItemAdapter = ItemAdapter<BindingWeatherInCityItem>()
    val cityInWeatherFastAdapter =
        FastAdapter.with(cityWeatherItemAdapter)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        cityListViewModelFactory = (activity?.application as App).component.getDependencyCityList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cityListRecyclerview.adapter = cityInWeatherFastAdapter
        // BindingWeatherInCityItem
//        val cityAdapter = CityAdapter()
//        val measureRecyclerView = binding.recyclerview
//        measureRecyclerView.adapter = cityAdapter
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
                    for (element in cityList) {
                        cityWeatherItemAdapter.add(BindingWeatherInCityItem(element))
                    }
                    // cityAdapter.setData(cityList)
                    сityList = cityList
                    binding.mapIcon.setOnClickListener {
                        val action =
                            CityListFragmentDirections.actionCityListFragmentToMapsFragment(
                                cityList.last(),
                                cityList.toTypedArray()
                            )
                        binding.root.findNavController().navigate(action)
                    }
                }
        }

        binding.addCityButton.setOnClickListener {
            dialog.showDialog(requireContext(), viewModel)
        }

        val actionListCallback = object : DragAndDropCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                viewModel.deleteTheCity(pos)
                //   cityAdapter.notifyItemRemoved(pos)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                Collections.swap(сityList, from, to)
                // cityAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                viewModel.updateCity(сityList)
                return true
            }
        }

//        val itemTouchHelper = ItemTouchHelper(actionListCallback)
//        itemTouchHelper.attachToRecyclerView(binding.cityListRecyclerview)
        val dragCallback = SimpleDragCallback()
        val touchHelper = ItemTouchHelper(dragCallback)
        touchHelper.attachToRecyclerView(binding.cityListRecyclerview)
    }

    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int): Boolean {
        DragDropUtil.onMove(
            cityWeatherItemAdapter,
            oldPosition,
            newPosition
        ) // change position
        return true
    }

//    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int): Boolean {
////        DragDropUtil.onMove(cityWeatherItemAdapter.itemAdapter, oldPosition, newPosition) // change position
////        return true
//    }
}