package ru.ktsstudio.app_verification.presentation.object_inspection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.feature.Feature
import io.reactivex.functions.Consumer
import ru.ktsstudio.app_verification.domain.object_inspection.ObjectInspectionFeature
import ru.ktsstudio.common.di.qualifiers.Id
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.mvi.toConsumer
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 20.11.2020.
 */
class ObjectInspectionViewModel @Inject constructor(
    @Id private val objectId: String,
    private val objectInspectionFeature: Feature<
        ObjectInspectionFeature.Wish,
        ObjectInspectionFeature.State,
        ObjectInspectionFeature.News
        >,
    private val binder: Binder,
    private val resources: ResourceManager
) : RxViewModel(), Consumer<ObjectInspectionUiState> {

    private val actionSubject = Rx2PublishSubject.create<ObjectInspectionFeature.Wish>()

    private val stateLiveData = MutableLiveData<ObjectInspectionUiState>()
    private val errorLiveData = SingleLiveEvent<String>()
    private val navigateLiveData = SingleLiveEvent<Unit>()

    val state: LiveData<ObjectInspectionUiState>
        get() = stateLiveData
    val error: LiveData<String>
        get() = errorLiveData
    val navigate: LiveData<Unit>
        get() = navigateLiveData

    init {
        setupBindings()
        actionSubject.onNext(ObjectInspectionFeature.Wish.Load(objectId))
    }

    override fun onCleared() {
        super.onCleared()
        binder.dispose()
    }

    override fun accept(state: ObjectInspectionUiState) {
        stateLiveData.postValue(state)
    }

    fun retry() {
        actionSubject.onNext(ObjectInspectionFeature.Wish.Load(objectId))
    }

    private fun setupBindings() {
        binder.bind(actionSubject to objectInspectionFeature)
        binder.bind(
            objectInspectionFeature to this
                using ObjectInspectionUiStateTransformer(resources)
        )
        binder.bind(objectInspectionFeature.news to ::reportNews.toConsumer())
    }

    private fun reportNews(news: ObjectInspectionFeature.News) {
        when (news) {
            is ObjectInspectionFeature.News.SendFailed -> {
                errorLiveData.postValue(news.throwable.localizedMessage)
            }
            is ObjectInspectionFeature.News.SendComplete -> {
                navigateLiveData.postValue(Unit)
            }
        }.exhaustive
    }
}
