package com.hms.searchalongroute.core.models

import com.google.gson.annotations.SerializedName

data class Path(
    @SerializedName("duration") val duration: Double,
    @SerializedName("durationText") val durationText: String,
    @SerializedName("durationInTraffic") val durationInTraffic: Double,
    @SerializedName("distance") val distance: Double,
    @SerializedName("startLocation") val startLocation: LatLngData,
    @SerializedName("endLocation") val endLocation: LatLngData,
    @SerializedName("startAddress") val startAddress: String,
    @SerializedName("distanceText") val distanceText: String,
    @SerializedName("steps") val steps: List<Steps>
)
