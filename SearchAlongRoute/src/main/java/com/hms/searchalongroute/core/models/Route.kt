package com.hms.searchalongroute.core.models

import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("paths") val paths: List<Path>,
    @SerializedName("bounds") val bounds: Bounds
)
