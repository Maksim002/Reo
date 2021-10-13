package ru.ktsstudio.reo.di.measurement.details

import dagger.BindsInstance
import dagger.Component
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_measurement_api.di.CoreMeasurementDataApi
import ru.ktsstudio.reo.di.measurement.MeasurementCommonModule
import ru.ktsstudio.reo.di.measurement.MeasurementNavigationApi
import ru.ktsstudio.reo.domain.measurement.common.MeasurementDraftHolder
import ru.ktsstudio.reo.navigation.measurement.MeasurementReturnTag
import ru.ktsstudio.reo.ui.measurement.details.MeasurementDetailsFragment
import javax.inject.Named

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */

@Component(
    dependencies = [CoreApi::class, CoreMeasurementDataApi::class, MeasurementNavigationApi::class],
    modules = [MeasurementCommonModule::class, MeasurementDetailsModule::class]
)
internal interface MeasurementDetailsComponent {
    fun inject(fragment: MeasurementDetailsFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Id measurementId: Long,
            @BindsInstance @Named("isPreviewMode") isPreviewMode: Boolean,
            @BindsInstance @Named("returnTag") returnTag: MeasurementReturnTag?,
            @BindsInstance draftHolder: MeasurementDraftHolder?,
            coreApi: CoreApi,
            measurementNavigationApi: MeasurementNavigationApi,
            coreDataApi: CoreMeasurementDataApi
        ): MeasurementDetailsComponent
    }

    companion object {
        fun create(
            measurementId: Long,
            isPreviewMode: Boolean,
            returnTag: MeasurementReturnTag?,
            draftHolder: MeasurementDraftHolder?
        ): MeasurementDetailsComponent {
            return DaggerMeasurementDetailsComponent.factory().create(
                measurementId = measurementId,
                isPreviewMode = isPreviewMode,
                returnTag = returnTag,
                coreApi = ComponentRegistry.get(),
                coreDataApi = ComponentRegistry.get(),
                measurementNavigationApi = ComponentRegistry.get(),
                draftHolder = draftHolder
            )
        }
    }
}
