package com.hms.searchalongroute.core.models

import com.google.gson.annotations.SerializedName

data class Bounds(
    @SerializedName("southWest") val southwest: LatLngData,
    @SerializedName("northEast") val northeast: LatLngData
)
