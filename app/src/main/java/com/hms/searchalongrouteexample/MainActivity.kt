package com.hms.searchalongrouteexample

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hms.searchalongroute.core.RouteSearchListener
import com.hms.searchalongroute.core.RouteSearchService
import com.hms.searchalongroute.core.models.DirectionsResponse
import com.hms.searchalongroute.core.utils.readJsonFileFromAssets
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.*
import com.huawei.hms.site.api.model.LocationType
import com.huawei.hms.site.api.model.SearchStatus

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var routeSearchService: RouteSearchService
    private var map: HuaweiMap? = null
    private lateinit var mapFragment: MapFragment
    private lateinit var mapPlaceAdapter: MapPlaceAdapter
    private lateinit var recyclerViewPlaces: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var bottomSheet: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        routeSearchService = RouteSearchService(this, API_KEY)
        mapPlaceAdapter = MapPlaceAdapter(emptyList())
        recyclerViewPlaces = findViewById(R.id.map_places_list)
        recyclerViewPlaces.adapter = mapPlaceAdapter
        bottomSheet = findViewById(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        progressBar = findViewById(R.id.progress_bar_main)
        initMap()

        val directionResponse = getDirectionResponse()
        directionResponse?.let { response ->
            //ToDo Implement multiple route and path
            val route = response.routes[0]
            val path = route.paths[0]
            val steps = path.steps
            val polylineOptions = PolylineOptions()
            val latLngBounds = LatLngBounds.builder()
            polylineOptions.add(LatLng(path.startLocation.lat, path.startLocation.lng))
            latLngBounds.include(LatLng(path.startLocation.lat, path.startLocation.lng))
            steps.forEach {
                it.polyline.forEach { latLngData ->
                    polylineOptions.add(LatLng(latLngData.lat, latLngData.lng))
                    latLngBounds.include(LatLng(latLngData.lat, latLngData.lng))
                }
            }

            val routeSearchListener = object : RouteSearchListener {
                override fun onSuccess(routeSearchResponse: com.hms.searchalongroute.core.models.RouteSearchResponse) {
                    progressBar.visibility = View.GONE
                    val sites = routeSearchResponse.siteList
                    sites?.run {
                        mapPlaceAdapter.updateList(this)
                        drawMap(routeSearchResponse)
                        addPolyline(polylineOptions)
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 200)
                        map?.animateCamera(cameraUpdate)
                        halfExpandBottomSheet()
                    }
                }

                override fun onFailure(error: SearchStatus?) {
                    progressBar.visibility = View.GONE
                }
            }
            progressBar.visibility = View.VISIBLE
            routeSearchService.searchAlongRoute(route, LocationType.GAS_STATION, routeSearchListener)
        }
    }

    private fun initMap() {
        mapFragment = fragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
        MapsInitializer.setApiKey(API_KEY)
        mapFragment.getMapAsync(this)
    }

    private fun getDirectionResponse(): com.hms.searchalongroute.core.models.DirectionsResponse? {
        val responseString = this.readJsonFileFromAssets("direction_response.json")
        val directionResponseType = object : TypeToken<DirectionsResponse>() {}.type
        return Gson().fromJson<com.hms.searchalongroute.core.models.DirectionsResponse>(responseString, directionResponseType)
    }


    override fun onMapReady(p0: HuaweiMap?) {
        map = p0
        map?.apply {
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isCompassEnabled = false
            setMarkersClustering(true)
        }
    }

    private fun drawMap(routeSearchResponse: com.hms.searchalongroute.core.models.RouteSearchResponse) {
        val siteList = routeSearchResponse.siteList
        val startLatLng = routeSearchResponse.startPoint
        val endLatLng = routeSearchResponse.endPoint

        addMarker(
                startLatLng!!.lat,
                startLatLng.lng,
                "Start Point",
                false,
                R.drawable.ic_location
        )
        addMarker(
                endLatLng!!.lat,
                endLatLng.lng,
                "Destination Point",
                false,
                R.drawable.ic_location_green
        )

        siteList?.let { sites ->
            sites.forEach {
                addMarker(it.location.lat, it.location.lng, it.name, true)
            }
        }
    }

    private fun addPolyline(polylineOptions: PolylineOptions) {
        polylineOptions.color(Color.BLUE)
        polylineOptions.width(3f)
        map?.addPolyline(polylineOptions)
    }

    private fun addMarker(
            lat: Double,
            lng: Double,
            title: String,
            clusterable: Boolean,
            @DrawableRes customIconId: Int? = null
    ) {
        val markerOptions = MarkerOptions()
                .position(LatLng(lat, lng))
                .clusterable(clusterable)
                .title(title)

        customIconId?.let {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(it))
        }

        map?.addMarker(markerOptions)
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }

    private fun halfExpandBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    companion object {
        private const val API_KEY = "CgB6e3x9KOlQ3qA/9ZpKRI8WUdZay4DK5stQEThMzSzCuHE/rIkhAavGeTl+x9QrgDEd7CR7OnTT2hKoMSGp9Fxr"
        private const val TAG = "MainActivity"
    }
}