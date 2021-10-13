package ru.ktsstudio.common.utils.id

import org.threeten.bp.Instant
import java.util.UUID
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.03.2021.
 */
class IdGeneratorImpl @Inject constructor(): IdGenerator {

    override fun generateStringId(): String {
        return UUID.randomUUID().toString() + "-${Instant.now().nano}"
    }
}