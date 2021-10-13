package ru.ktsstudio.app_verification.domain.object_filter

import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.domain.filter.data.FilterDataProvider
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.RegisterRepository
import ru.ktsstudio.core_data_verfication_api.data.WasteManagementTypeRepository
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectFilterApplier
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 19.11.2020.
 */
class ObjectFilterDataProvider @Inject constructor(
    private val registerRepository: RegisterRepository,
    private val objectRepository: ObjectRepository,
    private val wasteManagementTypeRepository: WasteManagementTypeRepository
) : FilterDataProvider<ObjectFilterItem> {

    override fun observeData(): Observable<Pair<FilterKey, List<ObjectFilterItem>>> {
        return Observable.merge(
            observeWasteManagementTypeFilter(),
            observeRegions(),
            observeSurveyStatuses()
        )
    }

    private fun observeWasteManagementTypeFilter(): Observable<Pair<FilterKey, List<ObjectFilterItem>>> {
        return wasteManagementTypeRepository.observeAllWasteManagementTypes()
            .map {
                VerificationObjectFilterApplier.WASTE_MANAGEMENT_TYPE_KEY to it.map(
                    ObjectFilterItem::WasteManagementTypeItem
                )
            }
    }

    private fun observeRegions(): Observable<Pair<FilterKey, List<ObjectFilterItem>>> {
        return objectRepository.getAllReferences()
            .map<List<ObjectFilterItem>> {
                it.filter { it.type == ReferenceType.REGION }
                    .map(ObjectFilterItem::RegionItem)
            }
            .map { regions -> VerificationObjectFilterApplier.REGION_KEY to regions }
            .toObservable()
    }

    private fun observeSurveyStatuses(): Observable<Pair<FilterKey, List<ObjectFilterItem>>> {
        return objectRepository.observeAllObjects()
            .map<List<ObjectFilterItem>> {
                it.mapNotNull {
                    it.status?.let { status ->
                        ObjectFilterItem.SurveyStatusItem(
                            type = status.type,
                            name = status.name
                        )
                    }
                }
                    .distinct()
            }
            .map { statuses -> VerificationObjectFilterApplier.SURVEY_STATUS_KEY to statuses }
    }
}
