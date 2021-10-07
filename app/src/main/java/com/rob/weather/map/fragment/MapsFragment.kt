package com.rob.weather.map.fragment

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.MapCustomView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rob.weather.R
import com.rob.weather.databinding.FragmentMapsBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import kotlin.properties.Delegates

class MapsFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var currentLatitude by Delegates.notNull<Double>()
    var currentLongitude by Delegates.notNull<Double>()
    lateinit var locationManager: LocationManager
    lateinit var binding: FragmentMapsBinding
    private val args by navArgs<MapsFragmentArgs>()
    private var mapview: MapView? = null
    override fun onStart() {
        super.onStart()
        mapview?.onStart()
        MapKitFactory.getInstance().onStart()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val latitudeOfCity = args.latitude.toDouble()
        val longitudeOfCity = args.longitude.toDouble()
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        locationDetermination(latitudeOfCity, longitudeOfCity, binding)
        binding.findCityGeoBtn.setOnClickListener {
            getLastKnownLocation(args, binding)
        }
        val view = binding.root
        return view
    }

    override fun onStop() {
        super.onStop()
        mapview?.onStop()
        MapKitFactory.getInstance().onStop()
    }

    fun getLastKnownLocation(args: MapsFragmentArgs, binding: FragmentMapsBinding) {
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            var location: Location? = task.result
            if (location == null) {
            } else {
                currentLatitude = location.latitude
                currentLongitude = location.longitude
                locationDetermination(currentLatitude, currentLongitude, binding)
            }

        }

    }

    @SuppressLint("ResourceType")
    fun locationDetermination(
        latitudeOfCity: Double,
        longitudeOfCity: Double,
        binding: FragmentMapsBinding
    ) {
        MapKitFactory.initialize(requireContext())
        mapview = binding.mapCity
        mapview!!.map.move(
            CameraPosition(Point(latitudeOfCity, longitudeOfCity), 9.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
        val vieww = View(requireContext()).apply {
            background = requireContext().getDrawable(R.drawable.ic_location)
        }

        val textView =
            MapCustomView(requireContext())
        textView.iconWeather.setImageResource(R.drawable.ic_location)
        textView.temperatureWeather.text = "23"

        val z = mapview?.map?.mapObjects?.addPlacemark(
            Point(latitudeOfCity, longitudeOfCity),
            ViewProvider(textView)
        )
//        mapview?.map?.mapObjects?.addPlacemark(
//            Point(latitudeOfCity, longitudeOfCity),
//            ImageProvider.fromResource(context, R.drawable.ic_location)
//        )
    }
//    private fun drawMyLocationMark() {
//        val view = View(requireContext()).apply {
//            background = requireContext().getDrawable(R.drawable.ic_place_24px)
//        }
//
//        mapview?.map?.mapObjects?.addPlacemark(
//            Point(it.latitude, it.longitude),
//            ViewProvider(view)
//        )
//    }
}