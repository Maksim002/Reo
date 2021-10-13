package ru.ktsstudio.feature_map.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import ru.ktsstudio.feature_map.R
import ru.ktsstudio.feature_map.presentation.RecycleObject
import ru.ktsstudio.utilities.extensions.getColorDrawable
import ru.ktsstudio.utilities.extensions.orFalse

/**
 * Created by Igor Park on 2019-10-29.
 */
typealias CommonColorRes = ru.ktsstudio.common.R.color

internal class RecycleMarkerRenderer(
    private val context: Context,
    clusterManager: ClusterManager<RecycleObject>,
    map: GoogleMap
) : DefaultClusterRenderer<RecycleObject>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(
        item: RecycleObject?,
        markerOptions: MarkerOptions?
    ) {
        val isChecked = item?.isSelected.orFalse()

        val colorRes = if (isChecked) CommonColorRes.primary_normal else CommonColorRes.secondary_normal
        val markerDescriptor = getBitmapFromVector(
            context,
            R.drawable.ic_recycle_marker,
            colorRes
        )
        markerOptions?.icon(markerDescriptor)
    }

    private fun getBitmapFromVector(
        context: Context,
        @DrawableRes vectorIcon: Int,
        @ColorRes tint: Int
    ): BitmapDescriptor? {
        val vectorDrawable = context.getColorDrawable(vectorIcon, tint)
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun shouldRenderAsCluster(cluster: Cluster<RecycleObject>?): Boolean {
        return cluster?.size?.let { it > 1 } ?: false
    }

    override fun getColor(clusterSize: Int): Int {
        return ContextCompat.getColor(context, CommonColorRes.secondary_normal)
    }
}