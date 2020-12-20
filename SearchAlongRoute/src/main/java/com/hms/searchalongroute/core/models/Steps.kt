package com.hms.searchalongroute.core.models

import com.google.gson.annotations.SerializedName

data class Steps(
    @SerializedName("duration") val duration: Double,
    @SerializedName("orientation") val orientation: Double,
    @SerializedName("durationText") val durationText: String,
    @SerializedName("distance") val distance: Double,
    @SerializedName("action") val action: String,
    @SerializedName("instruction") val instruction: String,
    @SerializedName("distanceText") val distanceText: String,
    @SerializedName("startLocation") val startLocation: LatLngData,
    @SerializedName("endLocation") val endLocation: LatLngData,
    @SerializedName("polyline") val polyline: List<LatLngData>,
    @SerializedName("roadName") val roadName: String
)
