package ru.ktsstudio.reo.domain.measurement.create_measurement

import java.lang.RuntimeException

/**
 * @author Maxim Myalkin (MaxMyalkin) on 07.12.2020.
 */
class IncorrectGainException : RuntimeException("incorrect volume or weight gain")
