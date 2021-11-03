package com.rob.weather

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.rob.weather.databinding.ActivityMainBinding
import com.rob.weather.generaldaytoday.fragment.GeneralDayTodayFragmentDirections

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        val view = binding.getRoot()
        setContentView(view)
        val toolbar = binding.toolbar
        toolbar.setOnMenuItemClickListener(this::clickOnMenu)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.cityListFragment -> configureToolbarForCityList()
                R.id.weatherInformationByDayFragment -> configureToolbarForGeneralDayToday()
                R.id.chooseDayFragment -> configureToolbarForSelectedDay()
                R.id.mapsFragment -> configureToolbarForMap()
            }
        }
        binding.imageBtn.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun configureToolbarForSelectedDay() {
        binding.imageBtn.setImageDrawable(getDrawable(R.drawable.ic_arrow_back))
        binding.imageBtn.isEnabled = true
    }

    private fun configureToolbarForCityList() {
        binding.imageBtn.setImageDrawable(getDrawable(R.drawable.ic_arrow_back))
        binding.imageBtn.isEnabled = true
        binding.toolbarToday.text = "Мои города"
        binding.toolbar.menu.clear()
        binding.toolbar.inflateMenu(R.menu.light_menu)
    }

    private fun configureToolbarForGeneralDayToday() {
        binding.imageBtn.setImageDrawable(getDrawable(R.drawable.ic_navigation))
        binding.imageBtn.isEnabled = false
        binding.toolbarToday.text = ""
        binding.toolbar.menu.clear()
        binding.toolbar.inflateMenu(R.menu.toolbar_menu)
    }

    private fun configureToolbarForMap() {
        binding.toolbar.visibility = View.GONE
//        binding.imageBtn.setImageDrawable(getDrawable(R.drawable.ic_arrow_back))
//        binding.imageBtn.isEnabled = true
//        binding.toolbarToday.text = "Тамбов"
//        binding.toolbar.menu.clear()
//        binding.toolbar.inflateMenu(R.menu.map_menu)
//        binding.toolbar.setBackgroundResource(R.color.line_back)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainer)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun clickOnMenu(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_search -> {
                val action =
                    GeneralDayTodayFragmentDirections
                        .actionWeatherInformationByDayFragmentToCityListFragment()
                navController.navigate(action)
                true
            }
            R.id.switch_mode -> {
                // generalDayTodayViewModel.changeMode()
                true
            }
        }
        return true
    }
}