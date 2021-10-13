package ru.ktsstudio.common.presentation

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.facebook.shimmer.ShimmerFrameLayout
import ru.ktsstudio.common.R
import ru.ktsstudio.common.utils.glide.GlideApp

class ImageLoadingDelegate(private val view: ImageView) {

    private val cornerRadius =
        view.resources.getDimension(R.dimen.default_corner_radius).toInt()

    fun loadImageIntoView(
        imageUrl: String?,
        transformation: Transformation<Bitmap>? = MultiTransformation(
            CenterCrop(),
            RoundedCorners(cornerRadius)
        ),
        @DrawableRes errorDrawable: Int = R.drawable.ic_image_placeholder,
        onStartLoading: (() -> Unit)? = null,
        onLoadComplete: (() -> Unit)? = null
    ) {
        onStartLoading?.invoke()
        GlideApp.with(view)
            .load(imageUrl)
            .apply {
                transformation?.let(::transform)
            }
            .error(errorDrawable)
            .transition(
                DrawableTransitionOptions.withCrossFade(
                    DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
                )
            )
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    onLoadComplete?.invoke()
                    (view.parent as? ShimmerFrameLayout)?.apply {
                        stopShimmer()
                        hideShimmer()
                    }
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    onLoadComplete?.invoke()
                    (view.parent as? ShimmerFrameLayout)?.apply {
                        stopShimmer()
                        hideShimmer()
                    }
                    return false
                }
            })
            .into(view)
    }
}
