package com.rob.weather.generalDayToday.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.rob.weather.R
import com.rob.weather.Utils.DateUtil
import com.rob.weather.databinding.SearchCityDialogBinding

//class SearchCityDialog(context: Context, addBtn: MaterialButton ) {
//
//    val builder = AlertDialog.Builder(context)
//    val layoutInflater = LayoutInflater.from(context)
//    val dialogFragment = SearchCityDialogBinding.inflate(layoutInflater)
//    builder.setView(dialogFragment.root)
//
//    val alertDialog = builder.show()
//
//    dialogFragment.addBtn.setOnClickListener {
//        DateUtil.city = dialogFragment.searchCityEditText.text.toString()
//        viewModel.getAllWeatherForecast(DateUtil.city, DateUtil.AppId)
//        alertDialog.dismiss()
//    }
//    dialogFragment.crossImageBtn.setOnClickListener {
//        alertDialog.dismiss()
//    }
//    dialogFragment.cancelBtn.setOnClickListener {
//        alertDialog.dismiss()
//    }
//    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//}
//
//class AddMeasureDialog(var message: String) : DialogFragment() {
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity?.let {
//            val builder = AlertDialog.Builder(it)
//            builder.setTitle(message)
//                .setIcon(R.drawable.ic_alert_sign)
//                .setPositiveButton(R.string.ok_i_do_it) { dialog, id ->
//                    dialog.cancel()
//                }
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }
//}