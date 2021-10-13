package ru.ktsstudio.core_data_verfication_api.data.model.reference

import com.google.gson.annotations.SerializedName

enum class ReferenceType {
    @SerializedName("ADDITIONAL_EQUIPMENT")
    ADDITIONAL_EQUIPMENT,

    @SerializedName("ALLOWED_USING_TYPE")
    ALLOWED_USING_TYPE,

    @SerializedName("BALLISTIC_SEPARATOR")
    BALLISTIC_SEPARATOR,

    @SerializedName("BLACK_METALS_SEPARATOR")
    BLACK_METALS_SEPARATOR,

    @SerializedName("CONVEYOR")
    CONVEYOR,

    @SerializedName("COORDINATE_SYSTEM")
    COORDINATE_SYSTEM,

    @SerializedName("DANGER_TYPE")
    DANGER_TYPE,

    @SerializedName("DATA_SOURCE_NAME")
    DATA_SOURCE_NAME,

    @SerializedName("DOT_DESCRIPTION")
    DOT_DESCRIPTION,

    @SerializedName("EDDY_CURRENT_SEPARATOR")
    EDDY_CURRENT_SEPARATOR,

    @SerializedName("EGRN")
    EGRN,

    @SerializedName("ENVIRONMENT_MONITORING_SYSTEM")
    ENVIRONMENT_MONITORING_SYSTEM,

    @SerializedName("EQUIPMENT_KIND")
    EQUIPMENT_KIND,

    @SerializedName("FEDERAL_DISTRICT")
    FEDERAL_DISTRICT,

    @SerializedName("FENCE_CANVAS")
    FENCE_CANVAS,

    @SerializedName("FENCE_FABRIC_MATERIAL")
    FENCE_FABRIC_MATERIAL,

    @SerializedName("FENCE_FOUNDATION")
    FENCE_FOUNDATION,

    @SerializedName("FENCE_FUNCTIONAL")
    FENCE_FUNCTIONAL,

    @SerializedName("FENCE_MOBILITY")
    FENCE_MOBILITY,

    @SerializedName("FENCE_SUPPORTS_MATERIAL")
    FENCE_SUPPORTS_MATERIAL,

    @SerializedName("FENCE_TYPE")
    FENCE_TYPE,

    @SerializedName("FENCE_VIEWABILITY")
    FENCE_VIEWABILITY,

    @SerializedName("FIAS")
    FIAS,

    @SerializedName("GIVING")
    GIVING,

    @SerializedName("GIVING_BMP")
    GIVING_BMP,

    @SerializedName("GRORO")
    GRORO,

    @SerializedName("HORIZONTAL_PRESS")
    HORIZONTAL_PRESS,

    @SerializedName("IMPORT_STATUS")
    IMPORT_STATUS,

    @SerializedName("LAND_CATEGORY")
    LAND_CATEGORY,

    @SerializedName("LAND_DESCRIPTION")
    LAND_DESCRIPTION,

    @SerializedName("LIGHTS_FUNCTIONAL_TYPE")
    LIGHTS_FUNCTIONAL_TYPE,

    @SerializedName("LIGHTS_SOURCE_TYPE")
    LIGHTS_SOURCE_TYPE,

    @SerializedName("LIGHT_AND_HEAVY_FRACTIONS_SEPARATOR")
    LIGHT_AND_HEAVY_FRACTIONS_SEPARATOR,

    @SerializedName("NEGATIVE_OBJECT_CATEGORY")
    NEGATIVE_OBJECT_CATEGORY,

    @SerializedName("NEUTRALIZATION_OBJECT_EQUIPMENT")
    NEUTRALIZATION_OBJECT_EQUIPMENT,

    @SerializedName("NONFERROUS_MENATLS_SEPARATOR")
    NONFERROUS_MENATLS_SEPARATOR,

    @SerializedName("OBJECT_CATEGORY")
    OBJECT_CATEGORY,

    @SerializedName("OBJECT_REQUEST_STATUS")
    OBJECT_REQUEST_STATUS,

    @SerializedName("OBJECT_STATUS")
    OBJECT_STATUS,

    @SerializedName("OBJECT_TYPE")
    OBJECT_TYPE,

    @SerializedName("OKTMO_NUMBER")
    OKTMO_NUMBER,

    @SerializedName("OPTICAL_SEPARATOR")
    OPTICAL_SEPARATOR,

    @SerializedName("OTHER")
    OTHER,

    @SerializedName("OTHER_SEPARATOR")
    OTHER_SEPARATOR,

    @SerializedName("OWNERSHIP_TYPE")
    OWNERSHIP_TYPE,

    @SerializedName("PACKAGING_MACHINE")
    PACKAGING_MACHINE,

    @SerializedName("PACKET_REAPER")
    PACKET_REAPER,

    @SerializedName("PET_PERFORATOR")
    PET_PERFORATOR,

    @SerializedName("PLACEMENT_OBJECT_EQUIPMENT")
    PLACEMENT_OBJECT_EQUIPMENT,

    @SerializedName("PRESS")
    PRESS,

    @SerializedName("PRES_COMPACTOR")
    PRES_COMPACTOR,

    @SerializedName("REGION")
    REGION,

    @SerializedName("REGION_MUNICIPAL_NAME")
    REGION_MUNICIPAL_NAME,

    @SerializedName("REGION_MUNICIPAL_OKTMO")
    REGION_MUNICIPAL_OKTMO,

    @SerializedName("REGIONAL_OPERATOR_ZONE_NAME")
    REGIONAL_OPERATOR_ZONE_NAME,

    @SerializedName("REVERSE")
    REVERSE,

    @SerializedName("ROAD_SURFACE_TYPE")
    ROAD_SURFACE_TYPE,

    @SerializedName("ROAD_TYPE")
    ROAD_TYPE,

    @SerializedName("SECONDARY_RESOURCES")
    SECONDARY_RESOURCES,

    @SerializedName("SECURITY_SOURCE")
    SECURITY_SOURCE,

    @SerializedName("SEPARATOR")
    SEPARATOR,

    @SerializedName("SORTING")
    SORTING,

    @SerializedName("SUBJECT_AREA")
    SUBJECT_AREA,

    @SerializedName("SUBJECT_NAME")
    SUBJECT_NAME,

    @SerializedName("TECHNICAL_SURVEY_RESULT")
    TECHNICAL_SURVEY_RESULT,

    @SerializedName("TECHNICAL_SURVEY_STATUS")
    TECHNICAL_SURVEY_STATUS,

    @SerializedName("TREATMENT_FACILITIES")
    TREATMENT_FACILITIES,

    @SerializedName("UTILIZATION_OBJECT_EQUIPMENT")
    UTILIZATION_OBJECT_EQUIPMENT,

    @SerializedName("UTILIZATION_TYPE")
    UTILIZATION_TYPE,

    @SerializedName("VERTICAL_PRESS")
    VERTICAL_PRESS,

    @SerializedName("VIBRATION_SEPARATOR")
    VIBRATION_SEPARATOR,

    @SerializedName("WASTE_CATEGORY")
    WASTE_CATEGORY,

    @SerializedName("WASTE_DANGER_CLASS")
    WASTE_DANGER_CLASS,

    @SerializedName("WASTE_MANAGEMENT_TYPE")
    WASTE_MANAGEMENT_TYPE,

    @SerializedName("WASTE_TYPE")
    WASTE_TYPE,

    @SerializedName("WATERPROOFING_TYPE")
    WATERPROOFING_TYPE,

    @SerializedName("ZONE")
    ZONE,

    @SerializedName("CORE_EQUIPMENT_TYPE")
    MAIN_EQUIPMENT_TYPE,

    @SerializedName("AUXILIARY_EQUIPMENT_TYPE")
    SECONDARY_EQUIPMENT_TYPE,

    @SerializedName("UTILIZATION_OBJECT_EQUIPMENT_KIND")
    UTILIZATION_OBJECT_EQUIPMENT_KIND
}