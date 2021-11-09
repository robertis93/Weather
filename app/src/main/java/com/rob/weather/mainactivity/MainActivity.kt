package com.rob.weather.mainactivity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.rob.weather.R
import com.rob.weather.databinding.ActivityMainBinding
import com.rob.weather.generaldaytoday.fragment.GeneralDayTodayFragmentDirections
import com.rob.weather.utils.extensions.getAppComponent

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModelFactory: MainActivityViewModelFactory
    private val mainActivityViewModel:
            MainActivityViewModel by viewModels { mainActivityViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mainActivityViewModelFactory = getAppComponent().getDependencyMainActivity()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        val view = binding.getRoot()
        setContentView(view)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.cityListFragment -> configureToolbarForCityList()
                R.id.weatherInformationByDayFragment -> configureToolbarForGeneralDayToday()
                R.id.chooseDayFragment -> configureToolbarForSelectedDay()
                R.id.mapsFragment -> configureToolbarForMap()
            }
        }
    }

    private fun configureToolbarForSelectedDay() {
        binding.imageBtn.setImageDrawable(getDrawable(R.drawable.ic_arrow_back))
        binding.imageBtn.isEnabled = true
        binding.imageBtn.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun configureToolbarForCityList() {
        binding.imageBtn.setImageDrawable(getDrawable(R.drawable.ic_chevron_left))
        binding.imageBtn.isEnabled = true
        binding.toolbar.visibility = View.VISIBLE
        binding.imageMenuLeftBtn.visibility = View.VISIBLE
        binding.imageBtn.visibility = View.VISIBLE
        binding.imageMenuRightBtn.visibility = View.VISIBLE
        binding.imageMenuRightBtn.setImageDrawable(getDrawable(R.drawable.ic_loader_1))
        binding.imageMenuLeftBtn.visibility = View.GONE
        binding.imageBtn.setOnClickListener {
            navController.popBackStack()
        }

    }

    private fun configureToolbarForGeneralDayToday() {
        binding.imageBtn.setImageDrawable(getDrawable(R.drawable.ic_location))
        binding.imageBtn.isEnabled = true
        binding.imageBtn.setOnClickListener {
            mainActivityViewModel.checkDataBase()
        }
        binding.imageMenuRightBtn.visibility = View.VISIBLE
        binding.imageMenuLeftBtn.visibility = View.VISIBLE
        binding.imageMenuLeftBtn.setImageDrawable(getDrawable(R.drawable.ic_loader_1))
        binding.imageMenuRightBtn.setImageDrawable(getDrawable(R.drawable.ic_search_city))
        binding.imageMenuRightBtn.setOnClickListener {
            val action =
                GeneralDayTodayFragmentDirections
                    .actionWeatherInformationByDayFragmentToCityListFragment()
            navController.navigate(action)
        }
    }

    private fun configureToolbarForMap() {
        binding.imageBtn.setImageDrawable(getDrawable(R.drawable.ic_chevron_left))
        binding.imageBtn.isEnabled = true
        binding.toolbar.visibility = View.GONE
        binding.imageMenuRightBtn.visibility = View.GONE
        binding.imageMenuLeftBtn.visibility = View.GONE
        binding.imageBtn.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainer)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}