package com.rob.weather.citylist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rob.weather.citylist.model.City
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [City::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDataBase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null

        fun getDataBase(context: Context): WeatherDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDataBase::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}