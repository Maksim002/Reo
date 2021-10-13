package ru.ktsstudio.common.domain.models

import org.threeten.bp.Month
import ru.ktsstudio.common.R
import ru.ktsstudio.common.ui.resource_manager.ResourceManager

/**
 * Created by Igor Park on 14/10/2020.
 */
sealed class Season {
    abstract fun getValue(resources: ResourceManager): String

    object Summer : Season() {
        override fun getValue(resources: ResourceManager): String {
            return resources.getString(R.string.season_summer)
        }
    }

    object Winter : Season() {
        override fun getValue(resources: ResourceManager): String {
            return resources.getString(R.string.season_winter)
        }
    }

    object Spring : Season() {
        override fun getValue(resources: ResourceManager): String {
            return resources.getString(R.string.season_spring)
        }
    }

    object Autumn : Season() {
        override fun getValue(resources: ResourceManager): String {
            return resources.getString(R.string.season_autumn)
        }
    }

    companion object {
        fun getSeason(month: Month): Season {
            return when (month) {
                Month.DECEMBER,
                Month.JANUARY,
                Month.FEBRUARY -> Winter

                Month.APRIL,
                Month.MARCH,
                Month.MAY -> Spring

                Month.JUNE,
                Month.JULY,
                Month.AUGUST -> Summer

                Month.SEPTEMBER,
                Month.OCTOBER,
                Month.NOVEMBER -> Autumn
            }
        }
    }
}
