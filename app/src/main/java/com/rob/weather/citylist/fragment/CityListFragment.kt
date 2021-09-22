package com.rob.weather.citylist.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rob.weather.App
import com.rob.weather.citylist.CityAdapter
import com.rob.weather.citylist.DragAndDropCallback
import com.rob.weather.citylist.ShowDialogForChangingCity
import com.rob.weather.citylist.viewmodel.CityListViewModel
import com.rob.weather.databinding.CityListFragmentBinding
import com.rob.weather.generaldaytoday.fragment.CityListViewModelFactory
import com.rob.weather.utils.BaseFragment
import javax.inject.Inject

class CityListFragment : BaseFragment<CityListFragmentBinding>(CityListFragmentBinding::inflate) {

    @Inject
    lateinit var cityListViewModelFactory: CityListViewModelFactory
    val viewModel: CityListViewModel by viewModels { cityListViewModelFactory }
    private val dialog = ShowDialogForChangingCity()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cityAdapter = CityAdapter()
        val measureRecyclerView = binding.recyclerview
        measureRecyclerView.adapter = cityAdapter
        measureRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.weatherCityList.observe(viewLifecycleOwner) { weatherInCities ->
            cityAdapter.setData(weatherInCities)
        }

        val actionListCallback = object : DragAndDropCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                viewModel.deleteCity(pos)
                cityAdapter.notifyItemRemoved(pos)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               // Collections.swap(viewModel.weatherCityList, )
                cityAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
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
    }
}