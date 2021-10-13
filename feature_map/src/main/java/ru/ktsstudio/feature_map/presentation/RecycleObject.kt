package ru.ktsstudio.feature_map.presentation

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import ru.ktsstudio.common.domain.models.MapObject

/**
 * Created by Igor Park on 03/10/2020.
 */
internal data class RecycleObject(
    val mapObject: MapObject,
    val isSelected: Boolean
) : ClusterItem {
    private val latLng: LatLng = LatLng(mapObject.latitude, mapObject.longitude)
    override fun getPosition() = latLng
    override fun getSnippet(): String? = null
    override fun getTitle(): String? = null
}