package ru.ktsstudio.reo.domain.measurement.common

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.ktsstudio.common.utils.replace
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.core_data_measurement_api.domain.SeparateContainerComposite
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Created by Igor Park on 18/11/2020.
 */
class DraftHolder @Inject constructor() :
    MeasurementDraftHolder,
    SeparateContainerDraftHolder,
    MixedContainerDraftHolder,
    MorphologyDraftHolder,
    WasteTypeDraftHolder {

    private val separateContainers = mutableMapOf<Long, SeparateContainerComposite>()
    private val mixedContainers = mutableMapOf<Long, MixedWasteContainerComposite>()
    private val morphologyItems = mutableMapOf<Long, MorphologyItem>()
    private val containerIdToWasteTypes = mutableMapOf<Long, Set<ContainerWasteType>>()
    private var comment: String by Delegates.observable("") { _, _, _ ->
        draftSubject.onNext(draft)
    }
    private val draft: MeasurementDraft
        get() = MeasurementDraft(
            separateContainers = separateContainers.values.toSet(),
            mixedContainers = mixedContainers.values.toSet(),
            morphologyItems = morphologyItems.values.toSet(),
            comment = comment
        )

    private val draftSubject = BehaviorSubject.createDefault(draft)
    private val containerWasteTypesSubject = BehaviorSubject.createDefault(containerIdToWasteTypes)

    override fun saveSeparateContainerDraft(container: SeparateContainerComposite) {
        separateContainers.updateDraftAndNotify {
            put(container.localId, container)
        }
    }

    override fun saveMixedContainerDraft(mixedContainer: MixedWasteContainerComposite) {
        mixedContainers.updateDraftAndNotify {
            put(mixedContainer.localId, mixedContainer)
        }
    }

    override fun saveContainerWasteTypeDraft(containerId: Long, wasteType: ContainerWasteType) {
        val updatedWasteTypes = containerIdToWasteTypes[containerId]
            .orEmpty()
            .replace(wasteType) { it.localId == wasteType.localId }
        containerIdToWasteTypes.updateWasteTypesAndNotify {
            put(containerId, updatedWasteTypes)
        }
    }

    override fun deleteSeparateContainerDraft(containerId: Long) {
        separateContainers.updateDraftAndNotify {
            remove(containerId)
        }
    }

    override fun deleteContainerWasteTypeDraft(containerId: Long, wasteTypeId: String) {
        val updatedWasteTypes = containerIdToWasteTypes[containerId]
            .orEmpty()
            .toMutableSet()
            .apply {
                removeAll { it.localId == wasteTypeId }
            }
        containerIdToWasteTypes.updateWasteTypesAndNotify {
            put(containerId, updatedWasteTypes)
        }
    }

    override fun saveComment(comment: String) {
        this.comment = comment
    }

    override fun isEmpty(): Boolean {
        return draft == MeasurementDraft.EMPTY
    }

    override fun isContainerAddedToMeasurement(containerId: Long): Boolean {
        return separateContainers[containerId] != null
    }

    override fun getMixedContainerDraft(mixedContainerId: Long): MixedWasteContainerComposite? {
        return mixedContainers[mixedContainerId]
    }

    override fun getMorphologyItemDraft(categoryId: Long): MorphologyItem? {
        return morphologyItems[categoryId]
    }

    override fun getSeparateContainerDraft(containerId: Long): SeparateContainerComposite? {
        return separateContainers[containerId]
    }

    override fun getContainerWasteTypeDraft(wasteTypeId: String): ContainerWasteType? {
        return containerIdToWasteTypes.values
            .flatten()
            .find { it.localId == wasteTypeId }
    }

    override fun saveMorphologyItem(item: MorphologyItem) {
        morphologyItems.updateDraftAndNotify {
            put(item.localId, item)
        }
    }

    override fun observeMeasurementDraft(): Observable<MeasurementDraft> {
        return draftSubject.distinctUntilChanged()
    }

    override fun getSessionMeasurementDraft(): MeasurementDraft {
        return draft
    }

    override fun observeMorphologyDraft(): Observable<Set<MorphologyItem>> {
        return draftSubject.map { it.morphologyItems }
            .distinctUntilChanged()
    }

    override fun observeContainerWasteTypeDrafts(containerId: Long): Observable<List<ContainerWasteType>> {
        return containerWasteTypesSubject.map { containerToWasteTypesMap ->
            containerToWasteTypesMap[containerId]
                .orEmpty()
                .toList()
        }
            .distinctUntilChanged()
    }

    override fun clearContainerWasteTypeDrafts(containerId: Long) {
        containerIdToWasteTypes.updateWasteTypesAndNotify {
            remove(containerId)
        }
    }

    override fun initWasteTypeList(containerToWasteTypesMap: Map<Long, Set<ContainerWasteType>>) {
        containerIdToWasteTypes.putAll(containerToWasteTypesMap)
    }

    private fun <K, V> MutableMap<K, V>.updateDraftAndNotify(
        update: MutableMap<K, V>.() -> Unit
    ) {
        this.update()
        draftSubject.onNext(draft)
    }

    private fun MutableMap<Long, Set<ContainerWasteType>>.updateWasteTypesAndNotify(
        update: MutableMap<Long, Set<ContainerWasteType>>.() -> Unit
    ) {
        this.update()
        containerWasteTypesSubject.onNext(containerIdToWasteTypes)
    }
}
