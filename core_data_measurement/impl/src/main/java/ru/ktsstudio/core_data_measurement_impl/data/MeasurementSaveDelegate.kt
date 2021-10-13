package ru.ktsstudio.core_data_measurement_impl.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.LocalModelState
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementStatus
import ru.ktsstudio.core_data_measurement_api.data.model.MixedWasteContainer
import ru.ktsstudio.core_data_measurement_api.data.model.SeparateWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.ContainerStore
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.core_data_measurement_impl.data.db.EntityToId
import ru.ktsstudio.core_data_measurement_impl.data.db.MeasurementStore
import ru.ktsstudio.core_data_measurement_impl.data.db.model.CompositeMedia
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurement
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementStatus
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphology
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.MediaType
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 19.11.2020.
 */
class MeasurementSaveDelegate @Inject constructor(
    private val measurementStore: MeasurementStore,
    private val morphologyStore: MorphologyStore,
    private val containerStore: ContainerStore,
    private val measurementStatusMapper: Mapper<MeasurementStatus, LocalMeasurementStatus>,
    private val measurementDbMapper: Mapper<Measurement, LocalMeasurement>,
    private val separateWasteContainerDbMapper: Mapper2<SeparateWasteContainer, MeasurementLocalId, LocalSeparateWasteContainer>,
    private val mixedWasteContainerDbMapper: Mapper2<MixedWasteContainer, MeasurementLocalId, LocalMixedWasteContainer>,
    private val separateWasteTypeDbMapper: Mapper2<ContainerWasteType, SeparateWasteContainerLocalId, LocalContainerWasteType>,
    private val morphologyDbMapper: Mapper2<MorphologyItem, MeasurementLocalId, LocalMorphology>,
    private val containerTypeDbMapper: Mapper<ContainerType, LocalContainerType>
) {

    fun saveMeasurements(measurements: List<Measurement>): Completable {
        return measurementStore.saveMeasurementStatuses(
            measurements.map { it.status }
                .distinctBy { it.id }
                .let(measurementStatusMapper::map)
        )
            .andThen(
                measurementStore.saveMeasurements(measurementDbMapper.map(measurements))
                    .map {
                        measurements.zip(it) { measurement: Measurement, localId: Long ->
                            EntityToId(measurement, localId)
                        }
                    }
            )
            .flatMapCompletable {
                Completable.merge(
                    listOf(
                        saveContainerTypes(it)
                            .andThen(saveMeasurementContainers(it))
                            .andThen(saveMeasurementMedias(it)),
                        saveMorphology(it)
                    )
                )
            }
    }

    private fun saveMorphology(measurements: List<EntityToId<Measurement>>): Completable {
        return measurements.flatMap { measurement: EntityToId<Measurement> ->
            measurement.entity.morphologyList.map { morphology ->
                morphologyDbMapper.map(morphology, measurement.id)
            }
        }
            .let(morphologyStore::saveMorphologies)
    }

    private fun saveMeasurementContainers(measurements: List<EntityToId<Measurement>>): Completable {
        return with(measurementStore) {
            measurements.flatMap { measurement ->
                measurement.entity.separateWasteContainers.map { container ->
                    separateWasteContainerDbMapper.map(container, measurement.id)
                }
            }
                .let(::saveSeparateContainers)
                .flatMap { ids ->
                    Single.just(
                        measurements.flatMap { it.entity.separateWasteContainers }
                            .zip(ids) { container: SeparateWasteContainer, localId: Long ->
                                EntityToId(
                                    container,
                                    localId
                                )
                            }
                    )
                }
                .flatMapCompletable { containers ->
                    containers.flatMap { container ->
                        container.entity.wasteTypes.orEmpty().map { wasteType ->
                            separateWasteTypeDbMapper.map(wasteType, container.id)
                        }
                    }
                        .let(::saveContainerWasteTypes)
                }
                .andThen(
                    measurements.flatMap { measurement ->
                        measurement.entity.mixedWasteContainers.map { container ->
                            mixedWasteContainerDbMapper.map(container, measurement.id)
                        }
                    }
                        .let(::saveMixedContainers)
                )
        }
    }

    private fun saveContainerTypes(measurements: List<EntityToId<Measurement>>): Completable {
        return measurements.flatMap { measurement ->
            val separateContainerTypes = measurement.entity
                .separateWasteContainers
                .map { it.containerType }
            val mixedContainerTypes = measurement.entity
                .mixedWasteContainers
                .map { it.containerType }

            separateContainerTypes + mixedContainerTypes
        }
            .map(containerTypeDbMapper::map)
            .let(containerStore::saveContainerTypes)
    }

    private fun saveMeasurementMedias(measurements: List<EntityToId<Measurement>>): Completable {
        return measurements.mapNotNull { measurement ->
            measurement.entity.media?.let { media ->
                CompositeMedia(
                    photos = media.photos.map {
                        createLocalMedia(
                            remoteId = it.id,
                            url = it.url,
                            mediaType = MediaType.PHOTO,
                            date = it.date,
                            gpsPoint = it.gpsPoint
                        )
                    },
                    videos = media.videos.map {
                        createLocalMedia(
                            remoteId = it.id,
                            url = it.url,
                            mediaType = MediaType.VIDEO,
                            date = it.date,
                            gpsPoint = it.gpsPoint
                        )
                    },
                    actPhotos = media.measurementActPhotos.map {
                        createLocalMedia(
                            remoteId = it.id,
                            url = it.url,
                            mediaType = MediaType.PHOTO,
                            date = null,
                            gpsPoint = null
                        )
                    },
                    measurementId = measurement.id
                )
            }
        }
            .let(measurementStore::saveMedia)
    }

    private fun createLocalMedia(
        remoteId: String,
        url: String?,
        mediaType: MediaType,
        date: Instant?,
        gpsPoint: GpsPoint?
    ): LocalMedia {
        return LocalMedia(
            remoteId = remoteId,
            url = url,
            mediaType = mediaType,
            cachedFilePath = null,
            gpsPoint = gpsPoint,
            date = date,
            draftState = DraftState.IDLE,
            localState = LocalModelState.SUCCESS
        )
    }
}