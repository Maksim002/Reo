package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import arrow.core.k
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.production.Product
import ru.ktsstudio.core_data_verfication_api.data.model.production.Service
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteProduct
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteProductsInfo
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteService
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import java.util.UUID
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 28.12.2020.
 */
class ProductionSurveyMapper @Inject constructor(
    private val mediaMapper: Mapper<SerializableMedia, Media>
): Mapper<RemoteProductsInfo?, ProductionSurvey> {
    override fun map(item: RemoteProductsInfo?): ProductionSurvey {
        item ?: return ProductionSurvey(
            id = UUID.randomUUID().toString(),
            productCapacity = null,
            products = emptyMap<String, Product>().k(),
            services = emptyMap<String, Service>().k()
        )

        return ProductionSurvey(
            id = item.id,
            productCapacity = item.totalCountPerYear,
            products = mapProducts(item.products.orEmpty()).associateBy { it.id }.k(),
            services = mapServices(item.providedServices.orEmpty()).associateBy { it.id }.k()
        )
    }

    private fun mapProducts(remoteProducts: List<RemoteProduct>): List<Product> {
        return remoteProducts.map {
            Product(
                id = it.id,
                photos = it.photos.orEmpty().map(mediaMapper::map),
                output = it.volume,
                name = it.name.orEmpty()
            )
        }
    }

    private fun mapServices(remoteServices: List<RemoteService>): List<Service> {
        return remoteServices.map {
            Service(
                id = it.id,
                output = it.volume,
                name = it.name,
                unit = null
            )
        }
    }
}