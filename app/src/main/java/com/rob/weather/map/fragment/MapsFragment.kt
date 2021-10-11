package com.rob.weather.map.fragment

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rob.weather.databinding.FragmentMapsBinding
import com.rob.weather.map.DisplayShortInfoWeather
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider
import kotlin.properties.Delegates


//: TODO при добавлении города который рядом, иконка с информацией закрывает собой другую иконку

class MapsFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var currentLatitude by Delegates.notNull<Double>()
    var currentLongitude by Delegates.notNull<Double>()
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
        val latitudeOfCity = args.weatherCity.latitude
        val longitudeOfCity = args.weatherCity.longitude
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        locationDetermination(latitudeOfCity, longitudeOfCity, binding, args)
        binding.findCityGeoBtn.setOnClickListener {
            getLastKnownLocation(args, binding)
        }

        val listener = object : InputListener {
            override fun onMapTap(p0: Map, point: Point) {
            val latitudeSelectedCity= point.latitude//do something...
            val longitudeSelectedCity= point.longitude//do something...
            }

            override fun onMapLongTap(p0: Map, p1: Point) {
                TODO("Not yet implemented")
            }
        }
        mapview?.map?.addInputListener(listener)
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
                locationDetermination(currentLatitude, currentLongitude, binding, args)
            }
        }
    }

    @SuppressLint("ResourceType")
    fun locationDetermination(
        latitudeOfCity: Double,
        longitudeOfCity: Double,
        binding: FragmentMapsBinding,
        args: MapsFragmentArgs
    ) {
        MapKitFactory.initialize(requireContext())
        mapview = binding.mapCity
        mapview?.map?.move(
            CameraPosition(
                Point(latitudeOfCity, longitudeOfCity), 7.0f, 0.0f,
                0.0f
            ),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
        val displayShortInfoWeather =
            DisplayShortInfoWeather(requireContext())

        displayShortInfoWeather.setTemperature(
            args.weatherCity!!.temperatureMin,
            args.weatherCity!!.temperatureMax
        )
        displayShortInfoWeather.setIconWeather(args.weatherCity.icon)

        mapview?.map?.mapObjects?.addPlacemark(
            Point((latitudeOfCity + 0.4), longitudeOfCity),
            ViewProvider(displayShortInfoWeather)
        )
        val weatherInfoInCityList = args.cityWeatherList
        for (weatherInfoInCity in weatherInfoInCityList) {
            val latitude = weatherInfoInCity.latitude
            val longitude = weatherInfoInCity.longitude
            val minTemperature = weatherInfoInCity.temperatureMin
            val maxTemperature = weatherInfoInCity.temperatureMax
            val displayInfoWeather =
                DisplayShortInfoWeather(requireContext())
            displayInfoWeather.setTemperature(minTemperature, maxTemperature)
            displayInfoWeather.setIconWeather(weatherInfoInCity.icon)
            mapview?.map?.mapObjects?.addPlacemark(
                Point((latitude + 0.4), longitude),
                ViewProvider(displayInfoWeather)
            )
        }
    }
}