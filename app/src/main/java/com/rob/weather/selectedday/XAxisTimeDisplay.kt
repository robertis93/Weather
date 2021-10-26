package com.rob.weather.selectedday

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class XAxisTimeDisplay() : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        if (value.toInt() == 0 || value.toInt() == 3 || value.toInt() == 6 || value.toInt() == 9) {
            return "0" + value.toInt().toString() + ":00"
        } else {
            return value.toInt().toString() + ":00"
        }
    }
}