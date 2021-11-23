package com.rob.weather.citylist.fragment

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.drag.IDraggable
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.swipe.IDrawerSwipeableViewHolder
import com.mikepenz.fastadapter.swipe.ISwipeable
import com.rob.weather.R
import com.rob.weather.citylist.IDraggableViewHolder
import com.rob.weather.citylist.model.WeatherCity
import com.rob.weather.utils.BASE_URL_IMAGE
import com.squareup.picasso.Picasso
import java.util.function.Consumer

class CityWeatherItem() : AbstractItem<CityWeatherItem.ViewHolder>(), ISwipeable, IDraggable {

    var weatherCity: WeatherCity? = null
    lateinit var weatherCityList: List<WeatherCity>
    var deleteAction: Consumer<CityWeatherItem>? = null
    override var isSwipeable = true
    override var isDraggable = true

    override val type: Int
        get() = R.id.fastadapter_swipable_drawer_item_id

    override val layoutRes: Int
        get() = R.layout.city_weather_item

    fun addWeatherCity(weatherCity: WeatherCity): CityWeatherItem {
        this.weatherCity = weatherCity
        return this
    }

    fun addCityWeatherList(cityWeatherList: List<WeatherCity>): CityWeatherItem {
        weatherCityList = cityWeatherList
        return this
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)
        if (weatherCity != null) {
            holder.nameCity.text = (weatherCity ?: return).name
            holder.maxTemperature.text = (weatherCity ?: return).temperatureMax.toString() + "°"
            holder.minTemperature.text = (weatherCity ?: return).temperatureMin.toString() + "°"
            val iconCode = (weatherCity ?: return).icon
            val iconUrl = BASE_URL_IMAGE + iconCode + ".png"
            Picasso.get().load(iconUrl).into(holder.icon)
            holder.deleteActionRunnable = Runnable { deleteAction?.accept(this) }
            holder.itemContent.setOnClickListener {
               val q = weatherCity!!
                val action =
                    CityListFragmentDirections.actionCityListFragmentToMapsFragment(
                        q
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        holder.nameCity.text = null
        holder.maxTemperature.text = null
        holder.minTemperature.text = null
        holder.deleteActionRunnable = null
        holder.itemContent.translationX = 0f
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), IDraggableViewHolder,
        IDrawerSwipeableViewHolder {
        var nameCity: TextView = view.findViewById(R.id.city_name_weather_city_textview)
        var maxTemperature: TextView = view.findViewById(R.id.max_temperature_weather_city_text)
        var minTemperature: TextView = view.findViewById(R.id.min_temperature_weather_city_text)
        var icon: ImageView = view.findViewById(R.id.city_weather_icon)
        var deleteBtn: View = view.findViewById(R.id.delete_btn)
        var itemContent: View = view.findViewById(R.id.item_content)
        var deleteActionRunnable: Runnable? = null

        init {
            deleteBtn.setOnClickListener {
                deleteActionRunnable?.run()
            }

            itemContent.setOnClickListener {
                (it.parent as View).performClick()
            }
        }

        override fun onDropped() {
        }

        override fun onDragged() {
        }

        override val swipeableView: View
            get() = itemContent


    }
}
