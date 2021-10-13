package ru.ktsstudio.feature_map.data

/**
 * @author Maxim Ovchinnikov on 13.11.2020.
 */
interface MapResourceProvider {

    fun getMapTitle(): String
    fun getMapSearchHint(): String
}