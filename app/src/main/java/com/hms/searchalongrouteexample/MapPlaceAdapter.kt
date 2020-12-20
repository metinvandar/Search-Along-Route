package com.hms.searchalongrouteexample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hms.searchalongrouteexample.databinding.MapPlaceListItemBinding
import com.huawei.hms.site.api.model.Site

class MapPlaceAdapter(private var items: List<Site>) :
    RecyclerView.Adapter<MapPlaceItemViewHolder>() {

    var itemClick: ((placeInfo: Site, addressSubText: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapPlaceItemViewHolder {
        val itemViewBinding = MapPlaceListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MapPlaceItemViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: MapPlaceItemViewHolder, position: Int) {
        holder.bind(items[position], itemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(items: List<Site>) {
        this.items = items
        notifyDataSetChanged()
    }
}
