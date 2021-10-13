package ru.ktsstudio.reo.navigation

import android.os.Bundle
import ru.ktsstudio.common.navigation.BaseNavigator
import ru.ktsstudio.common.navigation.api.AuthFailedNavigator
import ru.ktsstudio.common.navigation.api.AuthNavigator
import ru.ktsstudio.common.navigation.api.RecycleMapNavigator
import ru.ktsstudio.common.navigation.api.SyncNavigator
import ru.ktsstudio.common.ui.dialog.GpsRequestDialogFragment
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.navigateSafe
import ru.ktsstudio.common.utils.resetNavGraph
import ru.ktsstudio.feature_auth.ui.CheckAuthFragmentDirections
import ru.ktsstudio.feature_auth.ui.LoginFragmentDirections
import ru.ktsstudio.feature_mno_list.navigation.MnoNavigator
import ru.ktsstudio.feature_mno_list.ui.details.MnoDetailsFragmentDirections
import ru.ktsstudio.feature_sync_impl.ui.SyncFragmentDirections
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.navigation.measurement.EditMeasurementNavigator
import ru.ktsstudio.reo.navigation.measurement.MeasurementReturnTag
import ru.ktsstudio.reo.ui.measurement.add_container.AddContainerFragmentDirections
import ru.ktsstudio.reo.ui.measurement.create.CreateMeasurementFragmentDirections
import ru.ktsstudio.reo.ui.measurement.details.MeasurementDetailsFragmentDirections
import ru.ktsstudio.reo.ui.measurement.edit_separate_container.EditSeparateContainerFragmentDirections
import ru.ktsstudio.reo.ui.measurement.morphology.section.EditMorphologyFragmentDirections
import ru.ktsstudio.reo.ui.measurement.start.StartMeasurementFragmentDirections
import ru.ktsstudio.reo.ui.tabs.TabsFragmentDirections
import ru.ktsstudio.settings.navigation.SettingsNavigator
import javax.inject.Inject

class Navigator @Inject constructor() : BaseNavigator(),
    AuthNavigator,
    RecycleMapNavigator,
    MnoNavigator,
    MeasurementNavigator,
    SyncNavigator,
    SettingsNavigator,
    AuthFailedNavigator,
    EditMeasurementNavigator {

    override fun authFeatureCheckAuthToLogin() {
        navController?.navigateSafe(CheckAuthFragmentDirections.actionCheckAuthToLogin())
    }

    override fun authFeatureCheckAuthToFinishAuth() {
        navController?.navigateSafe(CheckAuthFragmentDirections.actionCheckAuthToSyncFragment())
    }

    override fun authFeatureLoginToFinishAuth() {
        navController?.navigateSafe(LoginFragmentDirections.actionLoginFragmentToSyncFragment())
    }

    override fun openMnoDetails(id: String) {
        navController?.navigateSafe(TabsFragmentDirections.actionTabsToMnoDetails(id))
    }

    override fun openMnoFilter() {
        navController?.navigateSafe(TabsFragmentDirections.actionTabsFragmentToObjectFilter())
    }

    override fun openMeasurementDetails(localId: Long) {
        navController?.navigateSafe(
            TabsFragmentDirections.actionTabsToMeasurementDetails(
                localId,
                isPreviewMode = false,
                returnTag = null
            )
        )
    }

    override fun measurementDetailsEditMeasurement(mnoId: String, measurementId: Long) {
        val editMeasurementDirection =
            MeasurementDetailsFragmentDirections.actionMeasurementDetailsFragmentToCreateMeasurement(
                mnoId = mnoId,
                measurementLocalId = measurementId.toString()
            )
        navController?.navigateSafe(editMeasurementDirection)
    }

    override fun measurementDetailsEditComplete(returnTag: MeasurementReturnTag) {
        when (returnTag) {
            MeasurementReturnTag.MEASUREMENT_DETAILS -> {
//                navController?.popBackStack() //todo change destination
                navController?.popBackStack(R.id.tabsFragment, false)
            }
            MeasurementReturnTag.MNO_DETAILS -> {
                navController?.popBackStack(R.id.mnoDetailsFragment, false)
            }
        }.exhaustive
    }

    override fun startMeasurement(mnoId: String) {
        navController?.navigateSafe(
            MnoDetailsFragmentDirections.actionMnoDetailsToStartMeasurement(mnoId)
        )
    }

    override fun mapFeatureToObjectFilter() {
        navController?.navigateSafe(TabsFragmentDirections.actionTabsFragmentToObjectFilter())
    }

    override fun settingsLogout() {
        navController?.resetNavGraph(R.navigation.nav_graph)
    }

    override fun finishSync() {
        navController?.navigateSafe(SyncFragmentDirections.actionSyncFragmentToTabsFragment())
    }

    override fun navigateToAuthScreen() {
        navController?.resetNavGraph(R.navigation.nav_graph)
    }

    override fun measurementSkipped() {
        navController?.popBackStack()
    }

    override fun measurementCreateCard(mnoId: String) {
        StartMeasurementFragmentDirections.actionStartMeasurementToCreateMeasurement(
            mnoId = mnoId,
            measurementLocalId = null
        )
        navController?.navigateSafe(
            StartMeasurementFragmentDirections.actionStartMeasurementToCreateMeasurement(
                mnoId = mnoId,
                measurementLocalId = null
            )
        )
    }

    override fun measurementPreviewCard(measurementId: Long, returnTag: MeasurementReturnTag) {
        navController?.navigateSafe(
            CreateMeasurementFragmentDirections.actionCreateMeasurementToMeasurementDetailsFragment(
                measurementId,
                isPreviewMode = true,
                returnTag = returnTag.name
            )
        )
    }

    override fun measurementAddContainer(mnoId: String, measurementId: Long) {
        navController?.navigateSafe(
            CreateMeasurementFragmentDirections.actionCreateMeasurementToAddContainer(
                measurementId,
                mnoId
            )
        )
    }

    override fun measurementEditMorphology(measurementId: Long) {
        navController?.navigateSafe(
            CreateMeasurementFragmentDirections.actionCreateMeasurementToEditMorphologyFragment(
                measurementId
            )
        )
    }

    override fun measurementEditMorphologyItem(measurementId: Long, morphologyItemId: Long?) {
        navController?.navigateSafe(
            EditMorphologyFragmentDirections.actionEditMorphologyFragmentToEditMorphologyItemFragment(
                measurementId = measurementId,
                morphologyId = morphologyItemId?.toString()
            )
        )
    }

    override fun measurementEditMorphologyCompleted() {
        navController?.popBackStack(R.id.createMeasurement, false)
    }

    override fun measurementEditMorphologyItemCompleted() {
        navController?.popBackStack(R.id.editMorphologyFragment, false)
    }

    override fun measurementAddSeparateContainer(
        navigationData: EditMeasurementNavigator.EditContainerNavigationData
    ) {
        val directions = with(navigationData) {
            AddContainerFragmentDirections.actionAddContainerToEditSeparateContainer(
                measurementId = measurementId,
                mnoContainerId = mnoContainerId,
                containerTypeId = containerTypeId,
                containerId = null
            )
        }
        navController?.navigateSafe(directions)
    }

    override fun measurementAddMixedContainer(
        navigationData: EditMeasurementNavigator.EditContainerNavigationData
    ) {
        val directions = with(navigationData) {
            AddContainerFragmentDirections.actionAddContainerToEditMixedContainer(
                measurementId = measurementId,
                mnoContainerId = mnoContainerId,
                containerTypeId = containerTypeId,
                containerId = null
            )
        }
        navController?.navigateSafe(directions)
    }

    override fun measurementEditSeparateContainer(measurementId: Long, containerId: Long) {
        val directions =
            CreateMeasurementFragmentDirections.actionCreateMeasurementToEditSeparateContainer(
                measurementId = measurementId,
                containerId = "$containerId",
                mnoContainerId = null,
                containerTypeId = null
            )
        navController?.navigateSafe(directions)
    }

    override fun measurementEditMixedContainer(measurementId: Long, containerId: Long) {
        val directions =
            CreateMeasurementFragmentDirections.actionCreateMeasurementToEditMixedContainer(
                measurementId = measurementId,
                containerId = "$containerId",
                mnoContainerId = null,
                containerTypeId = null
            )
        navController?.navigateSafe(directions)
    }

    override fun measurementEditWasteType(separateContainerId: Long, wasteTypeId: String?) {
        val directions =
            EditSeparateContainerFragmentDirections.actionEditSeparateContainerToEditWasteType(
                containerId = separateContainerId,
                wasteTypeId = wasteTypeId
            )
        navController?.navigateSafe(directions)
    }

    override fun measurementContainerUpdated() {
        navController?.popBackStack(R.id.createMeasurement, false)
    }

    override fun measurementWasteTypeUpdated() {
        navController?.popBackStack(R.id.editSeparateContainer, false)
    }

    override fun measurementConfirmDataClear(actionKey: String) {
        navController?.navigateSafe(
            CreateMeasurementFragmentDirections
                .actionCreateMeasurementToDisposeMeasurementCardDialogFragment(actionKey)
        )
    }

    override fun measurementGpsEnableRequest(message: String) {
        val bundle = Bundle().apply {
            putString(
                GpsRequestDialogFragment.MESSAGE_ARG_KEY,
                message
            )
        }
        navController?.navigate(R.id.gpsRequestDialogFragment, bundle)
    }
}
