package com.hms.searchalongrouteexample

import androidx.annotation.StringRes
import com.huawei.hms.site.api.model.LocationType

enum class PoiType(val locationType: LocationType?, @StringRes val nameResourceId: Int) {
    CAFE(LocationType.CAFE, R.string.poi_cafe),
    RESTAURANT(LocationType.RESTAURANT, R.string.poi_restaurant),
    SHOPPING_MALL(LocationType.SHOPPING_MALL, R.string.poi_shopping_mall),
    ADDRESS(LocationType.ADDRESS, R.string.poi_address),
    DRUG_STORE(LocationType.DRUGSTORE, R.string.poi_drug_store),
    HOSPITAL(LocationType.HOSPITAL, R.string.poi_type_hospital),
    BANK(LocationType.BANK, R.string.poi_type_bank),
    GAS_STATION(LocationType.GAS_STATION, R.string.poi_type_gas_station)
}
