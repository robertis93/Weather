package com.rob.weather.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rob.weather.databinding.FragmentOneDayWeatherInformationBinding

class OneDayWeatherInformationFragment : BaseFragment<FragmentOneDayWeatherInformationBinding>() {
    override fun inflate(inflater: LayoutInflater): FragmentOneDayWeatherInformationBinding =
        FragmentOneDayWeatherInformationBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val measureListAdapter = MedicamentAnalysesAdapter(informationListViewModel)
//        val recyclerView = binding.measureListRecyclerView
//        recyclerView.adapter = measureListAdapter
//        recyclerView.layoutManager =
//            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
    }
}
