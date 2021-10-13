package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ConveyorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.EquipmentKind
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteEquipment

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.12.2020.
 */

val objectTypeToIdMap = mapOf(
    VerificationObjectType.WASTE_TREATMENT to "4d8cd9ad-5e0f-4277-b15c-c1ff111c2e41",
    VerificationObjectType.WASTE_RECYCLING to "d2a15470-525d-45eb-acdd-ad2259bc6a4f",
    VerificationObjectType.WASTE_DISPOSAL to "94c47925-8add-482a-8818-f0970bf5ad51",
    VerificationObjectType.WASTE_PLACEMENT to "202cd4d3-d56d-4043-9801-b66bf29275eb"
)

val pressTypeToIdMap = mapOf(
    PressType.HORIZONTAL to "eeff8127-e87e-48f7-8cbb-13153b2afeab",
    PressType.VERTICAL to "a623d89a-2f6b-4f80-8605-d43d6dd52c77",
    PressType.COMPACTOR to "286135ab-2f13-48ba-ad22-3d9d3fbf70d9",
    PressType.PACKING_MACHINES to "c2d75472-ff87-46eb-929a-edf2dcfb7986"
)

val separatorTypeToIdMap = mapOf(
    SeparatorType.METAL to "1721d085-848f-4f5d-8a73-ba1e28381f37",
    SeparatorType.IRON to "7c2ec1e4-7872-4e72-b974-70aa6fb581a9",
    SeparatorType.OPTIC to "875f9452-f6f0-420d-bc5b-42323c6d5086",
    SeparatorType.EDDY_CURRENT to "2d9e5088-d9a1-4203-9a92-eb801c3e5e8e",
    SeparatorType.BALLISTIC to "c6907759-839d-469e-b678-31971ea0c4e9",
    SeparatorType.FRACTION to "434d2837-d7cb-4e63-b0a5-47acd63e3fe0",
    SeparatorType.VIBRATION to "76de5a52-84ad-4b3a-b8c8-7297c3cf7e80",
    SeparatorType.PLASTIC to "61daa90f-0546-4cfe-8b63-3c2bfd2bfe34"
)

val conveyorTypeToIdMap = mapOf(
    ConveyorType.SERVING to "4b7d6e92-f732-4973-a913-2f04135c2381",
    ConveyorType.SORT to "ea846ac8-6d56-46ce-8c89-db73d00038df",
    ConveyorType.REVERSE to "28c3c74b-3e7e-417f-bded-9487cfd73485",
    ConveyorType.PRESS to "b91fe3ea-9890-481f-8f74-57eb426c005f",
    ConveyorType.BAG_BREAKER to "a2e1cd6d-4f98-4055-8dbd-18e9017b2df4"
)

val equipmentKinds = mapOf(
    EquipmentKind.CONVEYOR to "ec0828ef-73e0-4a51-b814-bf6903ee34de",
    EquipmentKind.PRESS to "e16a0d6a-9446-4997-9a84-ff8b371f5542",
    EquipmentKind.SEPARATOR to "bbcef6b5-75ce-4770-bd00-3872a17773c3",
    EquipmentKind.ADDITIONAL to "57016882-eeb1-4ed1-bf78-a43f8c68e351",
    EquipmentKind.TECHNICAL_EQUIPMENT_MAIN to "309a641b-6aca-415a-bac9-d79ace1ed381",
    EquipmentKind.TECHNICAL_EQUIPMENT_SECONDARY to "d0c65af6-3a2b-4c82-b493-618b6be1e360",
)

fun RemoteReference?.isConveyor(): Boolean {
    val referenceType = this?.type?.name ?: return false
    return referenceType == ReferenceType.CONVEYOR
}

fun RemoteReference?.isAdditionalEquipment(): Boolean {
    val referenceType = this?.type?.name ?: return false
    return referenceType == ReferenceType.ADDITIONAL_EQUIPMENT
}

fun RemoteReference?.isSeparator(): Boolean {
    val referenceType = this?.type?.name ?: return false
    return referenceType == ReferenceType.SEPARATOR
}

fun RemoteReference?.isPress(): Boolean {
    val referenceType = this?.type?.name ?: return false
    return referenceType == ReferenceType.PRESS
}

fun RemoteEquipment.isOtherConveyorType(): Boolean {
    return type == null &&
        otherEquipmentName.isNullOrBlank().not() &&
        kind?.id == equipmentKinds[EquipmentKind.CONVEYOR]
}

fun RemoteEquipment.isOtherPress(): Boolean {
    return type == null && otherEquipmentName.isNullOrBlank().not() && kind.isPressKind()
}

fun RemoteEquipment.isOtherSeparator(): Boolean {
    return type == null && otherEquipmentName.isNullOrBlank().not() && kind.isSeparatorKind()
}

fun RemoteEquipment.isOtherAdditionalEquipment(): Boolean {
    return type == null && otherEquipmentName.isNullOrBlank().not() && kind.isAdditionalKind()
}

fun RemoteReference?.getSeparatorType(): SeparatorType? {
    if (isSeparator().not()) return null
    return separatorTypeToIdMap.filterValues { it == this?.id.orEmpty() }
        .keys
        .firstOrNull()
}

fun RemoteReference?.getPressType(): PressType? {
    if (isPress().not()) return null
    return pressTypeToIdMap.filterValues { it == this?.id.orEmpty() }
        .keys
        .firstOrNull()
}

fun RemoteReference?.getConveyorType(): ConveyorType? {
    if (isConveyor().not()) return null
    return conveyorTypeToIdMap.filterValues { it == this?.id.orEmpty() }
        .keys
        .firstOrNull()
}

fun RemoteReference?.isEquipmentKind(): Boolean {
    val referenceType = this?.type?.name ?: return false
    return referenceType == ReferenceType.EQUIPMENT_KIND
}

fun RemoteReference?.isConveyorKind(): Boolean {
    if (isEquipmentKind().not()) return false
    return this?.id == equipmentKinds[EquipmentKind.CONVEYOR]
}

fun RemoteReference?.isSeparatorKind(): Boolean {
    if (isEquipmentKind().not()) return false
    return this?.id == equipmentKinds[EquipmentKind.SEPARATOR]
}

fun RemoteReference?.isPressKind(): Boolean {
    if (isEquipmentKind().not()) return false
    return this?.id == equipmentKinds[EquipmentKind.PRESS]
}

fun RemoteReference?.isAdditionalKind(): Boolean {
    if (isEquipmentKind().not()) return false
    return this?.id == equipmentKinds[EquipmentKind.ADDITIONAL]
}

fun RemoteReference?.isEquipmentKind(kind: EquipmentKind): Boolean {
    return this?.id == equipmentKinds[kind]
}