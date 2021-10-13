package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMno
import javax.inject.Inject

class MnoDbMapper @Inject constructor() : Mapper<Mno, LocalMno> {

    override fun map(item: Mno): LocalMno = with(item) {
        LocalMno(
            id = objectInfo.mnoId,
            taskIds = objectInfo.taskIds,
            location = objectInfo.gpsPoint,
            nameSource = source.name,
            typeSource = source.type,
            categoryId = source.category.id,
            subcategory = source.subcategory,
            typeUnit = source.unit.type,
            quantityUnit = source.unit.quantity,
            typeUnitAlt = source.altUnit.type,
            quantityUnitAlt = source.altUnit.quantity,
            federalDistrict = sourceAddress.federalDistrict,
            region = sourceAddress.region,
            municipalDistrict = sourceAddress.municipalDistrict,
            address = sourceAddress.address
        )
    }
}