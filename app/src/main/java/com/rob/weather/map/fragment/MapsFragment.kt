package com.rob.weather.map.fragment

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rob.weather.map.DisplayShortInfoWeather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rob.weather.databinding.FragmentMapsBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider
import java.nio.charset.Charset
import kotlin.properties.Delegates

class MapsFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var currentLatitude by Delegates.notNull<Double>()
    var currentLongitude by Delegates.notNull<Double>()
    lateinit var binding: FragmentMapsBinding
    private val args by navArgs<MapsFragmentArgs>()
    private var mapview: MapView? = null
    val style = "[" +
            "        {" +
            "            \"types\": \"point\"," +
            "            \"tags\": {" +
            "                \"all\": [" +
            "                    \"poi\"" +
            "                ]" +
            "            }," +
            "            \"stylers\": {" +
            "                \"color\": \"#RGBA\"" +
            "            }" +
            "        }" +
            "    ]"
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
        locationDetermination(latitudeOfCity, longitudeOfCity, binding)
        binding.findCityGeoBtn.setOnClickListener {
            getLastKnownLocation(args, binding)
        }
        mapview!!.map.setMapStyle(style)
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

        val inputStream = requireContext().assets.open("style_map.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        val charset: Charset = Charsets.UTF_8
        inputStream.read(buffer)
        inputStream.close()
       val json = String(buffer, charset)


        mapview?.map?.move(
            CameraPosition(Point(latitudeOfCity, longitudeOfCity), 9.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
        //mapview.map.setMapStyle(style)
        val displayShortInfoWeather =
            DisplayShortInfoWeather(requireContext())

        displayShortInfoWeather.setTemperature(
            args.weatherCity!!.temperatureMin,
            args.weatherCity!!.temperatureMax
        )
        displayShortInfoWeather.setIconWeather(args.weatherCity.icon)

        mapview?.map?.mapObjects?.addPlacemark(
            Point((latitudeOfCity + 0.1), longitudeOfCity),
            ViewProvider(displayShortInfoWeather)
        )

        mapview!!.map.setMapStyle(style)
    }


}