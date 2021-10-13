package ru.ktsstudio.core_data_verification_impl.data.db.cleaner

import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.common.data.db.DatabaseCleaner
import ru.ktsstudio.core_data_verification_impl.data.db.VerificationDb
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 27.10.2020.
 */
class DatabaseCleanerImpl @Inject constructor(
    private val database: VerificationDb
) : DatabaseCleaner {

    override fun clearDatabase(): Completable {
        return Completable.fromCallable {
            database.clearAllTables()
        }
    }
}