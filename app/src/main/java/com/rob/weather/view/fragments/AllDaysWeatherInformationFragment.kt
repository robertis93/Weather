package com.rob.weather.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.rob.weather.R
import com.rob.weather.databinding.FragmentWeatherInformationAllDaysBinding

class AllDaysWeatherInformationFragment : BaseFragment<FragmentWeatherInformationAllDaysBinding>() {

    private val menu: Menu? = null
    override fun inflate(inflater: LayoutInflater): FragmentWeatherInformationAllDaysBinding =
        FragmentWeatherInformationAllDaysBinding.inflate(inflater)

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val collapsingToolbar = binding.toolbarLayout
        val appBar = binding.appBar
        val toolbar = binding.toolbar
        activity?.actionBar?.subtitle = "Vova"



        //collapsingToolbar.title = "This is the title"
//        val allDaysWeatherListAdapter = AllDaysWeatherListAdapter()
//        val recyclerView = binding.recyclerview
//        recyclerView.adapter = allDaysWeatherListAdapter
//        recyclerView.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //binding.text.setOnClickListener{Navigation.findNavController(view).navigate(R.id.action_weatherInformationByDayFragment_to_oneDayWeatherInformationFragment)}
//        binding.appBar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
//                //  Collapsed
//                Toast.makeText(context, "full", Toast.LENGTH_SHORT).show()
//            } else {
//                //Expanded
//                Toast.makeText(context, "full", Toast.LENGTH_SHORT).show()
//            }
//        })

//        binding.appBar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            if (collapsingToolbar.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(
//                    collapsingToolbar
//                )
//            ) {
//                toolbar.setBackgroundColor(R.color.purple_700)
//            } else {
//                toolbar.setBackgroundColor(R.color.white)
//                   // .setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
//            }
//        })
        appBar.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    binding.toolbarToday.visibility = View.GONE
                   // binding.toolbar.setBackgroundColor(R.color.purple_700)
                } else if (isShow) {
                    isShow = false
                    binding.toolbarToday.visibility = View.VISIBLE
                   // binding.toolbar.setBackgroundColor(R.color.white)
                }
            }
        })
    }

    private fun showOption(id : Int) {
        val item: MenuItem? = menu?.findItem(id)
        if (item != null) {
            item.isVisible = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> Toast.makeText(
                context,
                "Clicked search button",
                Toast.LENGTH_SHORT
            ).show()
        }
        return true
    }
}