package ru.ktsstudio.app_verification.presentation.exit

import androidx.lifecycle.LiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 10.02.2021.
 */
internal class ExitSurveyViewModel<DraftType> @Inject constructor(
    private val feature: Feature<
        ObjectSurveyFeature.Wish<DraftType>,
        ObjectSurveyFeature.State<DraftType>,
        ObjectSurveyFeature.News<DraftType>
        >,
    private val binder: Binder
) : RxViewModel() {

    private val exitMutableLiveData = SingleLiveEvent<Unit>()
    private val exitDialogMutableLiveData = SingleLiveEvent<Unit>()

    private val actionSubject = Rx2PublishSubject.create<ObjectSurveyFeature.Wish<DraftType>>()

    val exit: LiveData<Unit>
        get() = exitMutableLiveData
    val exitDialog: LiveData<Unit>
        get() = exitDialogMutableLiveData

    init {
        setupBindings()
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    fun exit() {
        actionSubject.onNext(ObjectSurveyFeature.Wish.Exit())
    }

    private fun setupBindings() {
        binder.bind(actionSubject to feature)
        binder.bind(feature.news to ::reportNews.toConsumer())
    }

    private fun reportNews(news: ObjectSurveyFeature.News<DraftType>) {
        when (news) {
            is ObjectSurveyFeature.News.Exit -> exitMutableLiveData.postValue(Unit)
            is ObjectSurveyFeature.News.ShowExitDialog -> exitDialogMutableLiveData.postValue(Unit)
            is ObjectSurveyFeature.News.GpsAvailabilityState,
            is ObjectSurveyFeature.News.CaptureMedia,
            is ObjectSurveyFeature.News.MediaUpdateIsNotCompleted,
            is ObjectSurveyFeature.News.CancelPhotoWithoutLocation,
            is ObjectSurveyFeature.News.CheckMediaPermission,
            is ObjectSurveyFeature.News.DraftIsInvalid -> {
            }
        }.exhaustive
    }
}
