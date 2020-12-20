package com.hms.searchalongroute.core

import com.hms.searchalongroute.core.models.RouteSearchResponse
import com.huawei.hms.site.api.model.SearchStatus

interface RouteSearchListener {

    fun onSuccess(routeSearchResponse: RouteSearchResponse)

    fun onFailure(error: SearchStatus?)
}
