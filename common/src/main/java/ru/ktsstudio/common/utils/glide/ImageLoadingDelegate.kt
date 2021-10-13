package ru.ktsstudio.common.utils.glide

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import ru.ktsstudio.common.R
import java.io.File

class ImageLoadingDelegate(private val view: ImageView) {

    private val cornerRadius = view.resources.getDimension(R.dimen.default_corner_radius).toInt()

    fun loadImageIntoView(
        url: String? = null,
        file: File? = null,
        @DrawableRes errorDrawable: Int = R.drawable.ic_image_placeholder,
        transformation: Transformation<Bitmap>? = MultiTransformation(
            CenterCrop(),
            RoundedCorners(cornerRadius)
        )
    ) {
        val loadImageFunc = when {
            url != null -> GlideApp.with(view).load(url)
            file != null -> GlideApp.with(view).load(file)
            else -> {
                view.setImageDrawable(null)
                return
            }
        }
        loadImageFunc
            .apply {
                transformation?.let(::transform)
            }
            .error(errorDrawable)
            .transition(
                DrawableTransitionOptions.withCrossFade(
                    DrawableCrossFadeFactory
                        .Builder()
                        .setCrossFadeEnabled(true)
                        .build()
                )
            )
            .into(view)
    }
}
