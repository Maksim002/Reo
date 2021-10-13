package ru.ktsstudio.common.data.db

import io.reactivex.rxjava3.core.Completable

/**
 * @author Maxim Ovchinnikov on 27.10.2020.
 */
interface DatabaseCleaner {
    fun clearDatabase(): Completable
}