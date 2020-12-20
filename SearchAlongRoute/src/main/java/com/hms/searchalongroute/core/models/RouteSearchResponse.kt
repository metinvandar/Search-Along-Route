package com.hms.searchalongroute.core.models

import com.huawei.hms.site.api.model.Site

data class RouteSearchResponse(
        var startPoint: LatLngData? = null,
        var endPoint: LatLngData? = null,
        var siteList: List<Site>? = null,
)