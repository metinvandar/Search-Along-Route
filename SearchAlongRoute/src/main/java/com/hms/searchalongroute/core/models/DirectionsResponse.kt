package com.hms.searchalongroute.core.models

import com.google.gson.annotations.SerializedName

data class DirectionsResponse(
    @SerializedName("routes") val routes: List<Route>,
    @SerializedName("returnCode") val returnCode: String,
    @SerializedName("returnDesc") val returnDesc: String
)
