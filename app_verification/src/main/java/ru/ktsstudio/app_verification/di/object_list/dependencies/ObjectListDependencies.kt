package ru.ktsstudio.app_verification.di.object_list.dependencies

import ru.ktsstudio.common.domain.filter.FilterProvider
import ru.ktsstudio.common.domain.filter.FilterUpdater

/**
 * @author Maxim Ovchinnikov on 19.11.2020.
 */
internal interface ObjectListDependencies {
    fun filterProvider(): FilterProvider
    fun filterUpdater(): FilterUpdater
}
