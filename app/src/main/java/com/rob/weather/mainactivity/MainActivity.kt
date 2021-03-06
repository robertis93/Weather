package com.rob.weather.mainactivity

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.rob.weather.R
import com.rob.weather.databinding.ActivityMainBinding
import com.rob.weather.generaldaytoday.fragment.GeneralDayTodayFragmentDirections
import com.rob.weather.selectedday.SelectedDayFragmentDirections
import kotlinx.coroutines.flow.collect
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    lateinit var appSettingsPrefs: SharedPreferences
    lateinit var sharedPrefEdit: SharedPreferences.Editor
    var isNightModeOn by Delegates.notNull<Boolean>()
    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        val view = binding.getRoot()
        setContentView(view)

        appSettingsPrefs = getSharedPreferences("AppSettingsPref", 0)
        sharedPrefEdit = appSettingsPrefs.edit()
        isNightModeOn = appSettingsPrefs.getBoolean("NightMode", false)
        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.cityListFragment -> configureToolbarForCityList()
                R.id.weatherInformationByDayFragment -> configureToolbarForGeneralDayToday()
                R.id.chooseDayFragment -> configureToolbarForSelectedDay()
                R.id.mapsFragment -> configureToolbarForMap()
            }
        }

        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.bundle
                .collect { bundle ->
                    navController.navigate(R.id.weatherInformationByDayFragment, bundle)
                }
        }
        binding.imageMenuLeftBtn.setOnClickListener {
            nightModeCheck(isNightModeOn, sharedPrefEdit)
        }
    }

    private fun nightModeCheck(isNightModeOn: Boolean, sharedPrefEdit: SharedPreferences.Editor) {
        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPrefEdit.putBoolean("NightMode", false)
            sharedPrefEdit.apply()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPrefEdit.putBoolean("NightMode", true)
            sharedPrefEdit.apply()
        }
    }

    private fun configureToolbarForSelectedDay() {
        binding.imageBtn.setImageDrawable(getDrawable(R.drawable.ic_chevron_left))
        binding.imageBtn.isEnabled = true
        binding.imageBtn.setOnClickListener {
            navController.popBackStack()
        }
        binding.imageMenuRightBtn.setOnClickListener {
            val action =
                SelectedDayFragmentDirections
                    .actionChooseDayFragmentToCityListFragment()
            navController.navigate(action)
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
        binding.imageMenuRightBtn.setOnClickListener {
            nightModeCheck(isNightModeOn, sharedPrefEdit)
        }
    }

    private fun configureToolbarForGeneralDayToday() {
        binding.imageBtn.setImageDrawable(getDrawable(R.drawable.ic_location))
        binding.imageBtn.setOnClickListener {
            mainActivityViewModel.putToBundle()
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