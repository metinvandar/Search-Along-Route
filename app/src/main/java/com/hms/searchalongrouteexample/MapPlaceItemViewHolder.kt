package com.hms.searchalongrouteexample

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hms.searchalongrouteexample.databinding.MapPlaceListItemBinding
import com.huawei.hms.site.api.model.AddressDetail
import com.huawei.hms.site.api.model.Site

class MapPlaceItemViewHolder(private val binding: MapPlaceListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(site: Site, itemClick: ((site: Site, addressSubText: String) -> Unit)?) {
        val addressSubText = getAddressSubText(site.address)
        binding.textViewMapPlaceItemName.text = site.name

        binding.root.setOnClickListener {
            itemClick?.invoke(site, addressSubText)
        }
        if (site.address == null) {
            binding.textViewMapPlaceItemSecondary.visibility = View.GONE
        } else {
            binding.textViewMapPlaceItemSecondary.text = getAddressSubText(site.address)
        }
    }

    private fun getAddressSubText(addressDetail: AddressDetail?): String {
        val result = StringBuilder()
        var count = 0
        if (addressDetail == null) {
            return "null"
        } else {
            addressDetail.thoroughfare?.let {
                result.append("$it, ")
                count++
            }
            addressDetail.locality?.let {
                result.append("$it, ")
                count++
            }
            addressDetail.subAdminArea?.let {
                result.append("$it, ")
                count++
            }
            addressDetail.adminArea?.let {
                result.append(it)
                count++
            }
            return if (count == 1) {
                val resultString = result.toString()

                resultString.replace(",", "")
            } else {
                result.toString()
            }
        }

    }
}