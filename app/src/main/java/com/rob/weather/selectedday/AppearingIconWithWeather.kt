package com.rob.weather.selectedday

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.rob.weather.R
import com.rob.weather.utils.BASE_URL_IMAGE
import com.squareup.picasso.Picasso

class PopupWeatherInCity (context: Context, layoutResource: Int) :MarkerView(context, layoutResource) {
    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        val weatherIcon = findViewById<ImageView>(R.id.weather_icon)
        val iconCode = entry?.data
        val iconUrl = BASE_URL_IMAGE + iconCode + ".png"
        Picasso.get().load(iconUrl).into(weatherIcon)
        val value = entry?.y?.toDouble() ?: 0.0
        var resText = value.toInt().toString() + "°"
        findViewById<TextView>(R.id.temperature_textview).text = resText
        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return if (ypos < 200) {
            MPPointF(-width + 150f, -height + 200f)
        } else MPPointF(-width - 20f, -height + 40f)
    }
}