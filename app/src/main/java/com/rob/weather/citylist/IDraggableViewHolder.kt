package com.rob.weather.citylist

interface IDraggableViewHolder {
    /**
     * Called when an item enters drag state
     */
    fun onDragged()

    /**
     * Called when an item has been dropped
     */
    fun onDropped()
}
