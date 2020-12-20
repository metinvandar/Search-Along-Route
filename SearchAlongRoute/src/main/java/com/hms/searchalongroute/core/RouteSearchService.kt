package com.hms.searchalongroute.core

import android.app.Activity
import android.location.Location
import com.hms.searchalongroute.core.Constants.API_KEY
import com.hms.searchalongroute.core.Constants.UTF
import com.hms.searchalongroute.core.models.LatLngData
import com.hms.searchalongroute.core.models.Route
import com.hms.searchalongroute.core.models.RouteSearchResponse
import com.hms.searchalongroute.core.models.Steps
import com.huawei.hms.site.api.SearchResultListener
import com.huawei.hms.site.api.SearchService
import com.huawei.hms.site.api.SearchServiceFactory
import com.huawei.hms.site.api.model.*
import java.net.URLEncoder

class RouteSearchService constructor(private val activity: Activity, private val apiKey: String) {

    private val searchService: SearchService = SearchServiceFactory.create(
        activity, URLEncoder.encode(
            apiKey, UTF
        )
    )
    private val totalPlacesMap = mutableMapOf<String, Site>()
    private var routeSearchListener: RouteSearchListener? = null
    private lateinit var routeSearchResponse: RouteSearchResponse

    fun searchAlongRoute(
        route: Route,
        poiType: LocationType,
        routeSearchListener: RouteSearchListener
    ) {
        totalPlacesMap.clear()
        this.routeSearchResponse = RouteSearchResponse()
        this.routeSearchListener = routeSearchListener
        if (route.paths.isNullOrEmpty()) {
            return
        } else {
            //ToDo Implement multiple path search
            val path = route.paths[0]
            val searchPoints = getSearchPointsFromSteps(path.steps)
            this.routeSearchResponse.startPoint = path.startLocation
            this.routeSearchResponse.endPoint = path.endLocation
            searchPoints.forEachIndexed { index, latLngData ->
                searchNearby(
                    latitude = latLngData.lat,
                    longitude = latLngData.lng,
                    poiType = poiType,
                    isLastLocation = index == searchPoints.size - 1
                )
            }
        }

    }

    private fun getSearchPointsFromSteps(steps: List<Steps>): List<LatLngData> {
        val result = mutableListOf<LatLngData>()
        var lastUsedStep = steps[0]
        steps.forEachIndexed { index, step ->
            if (index == 0) {
                result.add(LatLngData(step.startLocation.lat, step.endLocation.lng))
            } else {
                val previousStep = steps[index - 1]
                val distanceToPrevious = distanceTwoPoints(
                    LatLngData(
                        previousStep.startLocation.lat,
                        previousStep.startLocation.lng
                    ), LatLngData(step.startLocation.lat, step.startLocation.lng)
                )

                if (distanceToPrevious >= 1000) {
                    lastUsedStep = step
                    result.add(
                        LatLngData(
                            lastUsedStep.startLocation.lat,
                            lastUsedStep.startLocation.lng
                        )
                    )
                }
            }
        }

        return result
    }

    private fun distanceTwoPoints(startPoint: LatLngData, endPoint: LatLngData): Float {
        val resultArray = FloatArray(1)
        Location.distanceBetween(
            startPoint.lat, startPoint.lng,
            endPoint.lat, endPoint.lng, resultArray
        )
        return resultArray[0]
    }

    private fun searchNearby(
        latitude: Double,
        longitude: Double,
        poiType: LocationType,
        isLastLocation: Boolean
    ) {
        val request = NearbySearchRequest()
        val coordinate = Coordinate(latitude, longitude)
        request.location = coordinate
        request.poiType = poiType

        val searchResultListener = object : SearchResultListener<NearbySearchResponse> {

            override fun onSearchResult(searchResponse: NearbySearchResponse?) {
                searchResponse?.let { response ->
                    val sites = response.sites
                    sites?.forEach {
                        if (totalPlacesMap.containsKey(it.siteId).not()) {
                            totalPlacesMap[it.siteId] = it
                        }
                    }
                }
                if (isLastLocation) {
                    routeSearchListener?.let {
                        val siteList = totalPlacesMap.toList().map { siteMap ->
                            siteMap.second
                        }
                        routeSearchResponse.siteList = siteList
                        it.onSuccess(routeSearchResponse)
                    }
                }
            }

            override fun onSearchError(searchStatus: SearchStatus?) {
                if (isLastLocation) {
                    routeSearchListener?.onFailure(searchStatus)
                    totalPlacesMap.clear()
                }
            }

        }
        searchService.nearbySearch(request, searchResultListener)
    }
}