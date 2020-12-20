package com.hms.searchalongroute.core.models

import com.google.gson.annotations.SerializedName

data class LatLngData(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)
