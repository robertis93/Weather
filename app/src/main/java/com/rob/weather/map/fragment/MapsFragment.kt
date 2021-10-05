package com.rob.weather.map.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rob.weather.R
import com.rob.weather.databinding.FragmentMapsBinding
import com.rob.weather.databinding.FragmetChooseDayBinding
import com.rob.weather.utils.BaseFragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView


class MapsFragment : Fragment(){
    lateinit var binding: FragmentMapsBinding

    private var mapview: MapView? = null
    override fun onStart() {
        super.onStart()
        mapview?.onStart();
        MapKitFactory.getInstance().onStart();
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MapKitFactory.setApiKey("040a037b-1287-4381-bb2c-5b20912a208e");
        MapKitFactory.initialize(requireContext());
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        mapview = binding.mapCity
        mapview!!.getMap().move(
            CameraPosition(Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
        val view = binding.root
        return view    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//        mapFragment?.getMapAsync(callback)
        Log.i("myLogs", "MapsFragment")
    }

    override fun onStop() {
        super.onStop()
        mapview?.onStop()
        MapKitFactory.getInstance().onStop()
    }
}