package com.rob.weather.citylist.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.drag.ItemTouchCallback
import com.mikepenz.fastadapter.drag.SimpleDragCallback
import com.mikepenz.fastadapter.swipe.SimpleSwipeCallback
import com.mikepenz.fastadapter.swipe.SimpleSwipeDrawerCallback
import com.mikepenz.fastadapter.swipe_drag.SimpleSwipeDrawerDragCallback
import com.mikepenz.fastadapter.utils.DragDropUtil
import com.rob.weather.App
import com.rob.weather.citylist.IDraggableViewHolder
import com.rob.weather.citylist.ShowDialogForChangingCity
import com.rob.weather.citylist.model.WeatherCity
import com.rob.weather.citylist.viewmodel.CityListViewModel
import com.rob.weather.databinding.CityListFragmentBinding
import com.rob.weather.generaldaytoday.fragment.CityListViewModelFactory
import com.rob.weather.utils.BaseFragment
import kotlinx.coroutines.flow.collect
import java.util.*
import java.util.function.Consumer
import javax.inject.Inject

class CityListFragment : BaseFragment<CityListFragmentBinding>(CityListFragmentBinding::inflate),
    ItemTouchCallback, SimpleSwipeCallback.ItemSwipeCallback,
    SimpleSwipeDrawerCallback.ItemSwipeCallback {
    var сityList = listOf<WeatherCity>()
    private lateinit var cityWeatherItemFastAdapter: FastItemAdapter<CityWeatherItem>
    private lateinit var touchHelper: ItemTouchHelper
    private lateinit var touchCallback: SimpleDragCallback
    @Inject
    lateinit var cityListViewModelFactory: CityListViewModelFactory
    val viewModel: CityListViewModel by viewModels { cityListViewModelFactory }
    private val dialog = ShowDialogForChangingCity()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cityListViewModelFactory = (activity?.application as App).component.getDependencyCityList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cityWeatherItemFastAdapter = FastItemAdapter()
        viewModel.getCityList()
        lifecycleScope.launchWhenStarted {
            viewModel.cityList
                .collect { listCity ->
                    viewModel.getWeatherByCity(listCity)
                }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.cityListWithWeather
                .collect { weatherCityList ->
                    val items = ArrayList<CityWeatherItem>()
                    for (city in weatherCityList) {
                        val swipeableItem = CityWeatherItem().addWeatherCity(city)
                        swipeableItem.deleteAction = Consumer { item -> delete(item) }
                        items.add(swipeableItem)
                    }
                    cityWeatherItemFastAdapter.add(items)
                    сityList = weatherCityList
                    binding.mapIcon.setOnClickListener {
                        val action =
                            CityListFragmentDirections.actionCityListFragmentToMapsFragment(
                                weatherCityList.last(),
                            )
                        binding.root.findNavController().navigate(action)
                    }
                }
        }

        binding.addCityButton.setOnClickListener {
            dialog.showDialog(requireContext(), viewModel)
        }
        binding.cityListRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.cityListRecyclerview.itemAnimator = DefaultItemAnimator()
        binding.cityListRecyclerview.adapter = cityWeatherItemFastAdapter

        touchCallback = SimpleSwipeDrawerDragCallback(
            this,
            ItemTouchHelper.LEFT,
            this
        )
            .withNotifyAllDrops(true)
            .withSwipeLeft(80) // Width of delete button
            .withSensitivity(10f)
            .withSurfaceThreshold(0.3f)

        touchHelper =
            ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(binding.cityListRecyclerview)
        cityWeatherItemFastAdapter.withSavedInstanceState(savedInstanceState)
    }

    private fun delete(item: CityWeatherItem) {
        item.deleteAction = null
        val position = cityWeatherItemFastAdapter.getAdapterPosition(item)
        if (position != RecyclerView.NO_POSITION) {
            cityWeatherItemFastAdapter.itemFilter.remove(position)
            viewModel.deleteTheCity(position)
            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int): Boolean {
        DragDropUtil.onMove(
            cityWeatherItemFastAdapter.itemAdapter,
            oldPosition,
            newPosition
        )
        Collections.swap(сityList, oldPosition, newPosition)
        viewModel.updateCity(сityList)
        return true
    }

    override fun itemTouchDropped(oldPosition: Int, newPosition: Int) {

        val vh: RecyclerView.ViewHolder? =
            binding.cityListRecyclerview.findViewHolderForAdapterPosition(newPosition)
        if (vh is IDraggableViewHolder) {
            (vh as IDraggableViewHolder).onDropped()
        }
    }

    override fun itemTouchStartDrag(viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is IDraggableViewHolder) {
            (viewHolder as IDraggableViewHolder).onDragged()
        }
    }


    override fun itemSwiped(position: Int, direction: Int) {
        var directionStr = ""
        if (ItemTouchHelper.LEFT == direction) directionStr = "left"
        else if (ItemTouchHelper.RIGHT == direction) directionStr = "right"
        println("Item $position swiped $directionStr")
    }

    override fun itemUnswiped(position: Int) {
        println("Item $position unswiped")
    }
}
