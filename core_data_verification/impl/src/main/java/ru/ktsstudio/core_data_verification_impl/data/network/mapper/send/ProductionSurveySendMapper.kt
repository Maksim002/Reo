package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionSurvey
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteProduct
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteProductsInfo
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteService
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 28.12.2020.
 */
class ProductionSurveySendMapper @Inject constructor(
    private val sendMediaMapper: Mapper2<Media, Map<String, LocalMedia>, SerializableMedia>
) : Mapper2<ProductionSurvey, @JvmSuppressWildcards Map<String, LocalMedia>, RemoteProductsInfo> {
    override fun map(item1: ProductionSurvey, item2: Map<String, LocalMedia>): RemoteProductsInfo {
        return RemoteProductsInfo(
            id = item1.id,
            totalCountPerYear = item1.productCapacity,
            products = item1.products.values.map {
                RemoteProduct(
                    id = it.id,
                    name = it.name,
                    volume = it.output,
                    photos = sendMediaMapper.map(it.photos, item2)
                )
            },
            providedServices = item1.services.values.map {
                RemoteService(
                    id = it.id,
                    name = it.name,
                    volume = it.output
                )
            }
        )
    }
}