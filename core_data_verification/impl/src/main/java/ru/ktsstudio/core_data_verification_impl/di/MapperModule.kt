package ru.ktsstudio.core_data_verification_impl.di

import dagger.Binds
import dagger.Module
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.FiasAddress
import ru.ktsstudio.core_data_verfication_api.data.model.GeneralInformation
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Progress
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.ScheduleSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.EnvironmentMonitoringEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SewagePlantEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WeightControlEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.Schedule
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourcesSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.WastePlacementMap
import ru.ktsstudio.core_data_verification_impl.data.db.converter.SurveyProgressFetcher
import ru.ktsstudio.core_data_verification_impl.data.db.converter.SurveyProgressFetcherImpl
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.CheckedSurveyDbMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.FiasAddressMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.FiasAddressToRemoteMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.GeneralInformationNetworkMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.GeneralInformationSerializableMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.SerializableCheckedSurveyDbMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.SerializableSurveyDbMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.SurveyDbMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.SurveyProgressMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.VerificationObjectDbMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.domain_to_local.MediaDbMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.domain_to_local.ReferenceDbMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.domain_to_local.SerializableInfrastructureCheckedSurveyMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.domain_to_local.SerializableInfrastructureSurveyMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.domain_to_local.VerificationObjectFromDbMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.local_to_domain.InfrastructureCheckedSurveyMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.local_to_domain.InfrastructureSurveyMapper
import ru.ktsstudio.core_data_verification_impl.data.db.mapper.local_to_domain.ReferenceMapper
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalReference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObject
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObjectWithRelation
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.EnvironmentMonitoringEquipmentMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.InfrastructureEquipmentMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.InfrastructureSurveyNetworkMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.MediaMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.ObjectNetworkMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.ProductionSurveyMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.ReferenceRemoteMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.ScheduleDbMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.ScheduleSurveyNetworkMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.SewagePlantEquipmentMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.VerifiedFieldsMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.WastePlacementMapMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.WeightControlEquipmentMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.send.GpsPointToSendMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.send.MediaToSendMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.send.ProductionSurveySendMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.send.ReferenceSendRemoteMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.send.ScheduleSendNetworkMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.send.SecondaryResourceMapper
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.send.WastePlacementMapToSendMapper
import ru.ktsstudio.core_data_verification_impl.data.network.model.GpsPointToSend
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteFiasAddress
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteInfrastructureObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteProductsInfo
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteSecondaryResources
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteVerificationObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteWastePlacementMap
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableCheckedSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableGeneralInformation
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableInfrastructureSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableObjectWorkSchedule
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableSchedule
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.infrastructure.SerializableInfrastructureCheckedSurvey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.11.2020.
 */
@Module
interface MapperModule {
    @Binds
    fun bindMediaMapper(impl: MediaMapper): Mapper<SerializableMedia, Media>

    @Binds
    fun bindWeightControlEquipmentMapper(impl: WeightControlEquipmentMapper): Mapper<RemoteInfrastructureObject, WeightControlEquipment>

    @Binds
    fun bindInfrastructureEquipmentMapper(impl: InfrastructureEquipmentMapper): Mapper<RemoteInfrastructureObject, InfrastructureEquipment>

    @Binds
    fun bindSewageEquipmentMapper(impl: SewagePlantEquipmentMapper): Mapper<RemoteInfrastructureObject, SewagePlantEquipment>

    @Binds
    fun bindEnvironmentMonitoringEquipmentMapper(
        impl: EnvironmentMonitoringEquipmentMapper
    ): Mapper<RemoteInfrastructureObject, EnvironmentMonitoringEquipment>

    @Binds
    fun bindReferenceMapper(impl: ReferenceMapper): Mapper<LocalReference, Reference>

    @Binds
    fun bindReferenceRemoteMapper(impl: ReferenceRemoteMapper): Mapper<RemoteReference, Reference>

    @Binds
    fun bindProductionSurveyMapper(impl: ProductionSurveyMapper): Mapper<RemoteProductsInfo?, ProductionSurvey>

    @Binds
    fun bindProductionSurveySendMapper(impl: ProductionSurveySendMapper): Mapper2<ProductionSurvey, Map<String, LocalMedia>, RemoteProductsInfo>

    @Binds
    fun bindReferenceDbMapper(impl: ReferenceDbMapper): Mapper<Reference, LocalReference>

    @Binds
    fun bindObjectNetworkMapper(impl: ObjectNetworkMapper): Mapper<RemoteVerificationObject, VerificationObject?>

    @Binds
    fun bindObjectFromLocalMapper(impl: VerificationObjectFromDbMapper): Mapper<LocalVerificationObjectWithRelation, VerificationObject>

    @Binds
    fun bindObjectDbMapper(impl: VerificationObjectDbMapper): Mapper<VerificationObject, LocalVerificationObject>

    @Binds
    fun bindScheduleSurveyNetworkMapper(impl: ScheduleSurveyNetworkMapper): Mapper<SerializableObjectWorkSchedule, ScheduleSurvey>

    @Binds
    fun bindScheduleSbMapper(impl: ScheduleDbMapper): Mapper<Schedule, SerializableSchedule>

    @Binds
    fun bindVerifiedFieldsMapper(impl: VerifiedFieldsMapper): Mapper2<RemoteVerificationObject, VerificationObjectType, CheckedSurvey>

    @Binds
    fun bindSerializableSurveyDbMapper(impl: SerializableSurveyDbMapper): Mapper<Survey, SerializableSurvey>

    @Binds
    fun bindSurveyDbMapper(impl: SurveyDbMapper): Mapper<SerializableSurvey, Survey>

    @Binds
    fun bindSerializableCheckedSurveyDbMapper(impl: SerializableCheckedSurveyDbMapper): Mapper<CheckedSurvey, SerializableCheckedSurvey>

    @Binds
    fun bindCheckedSurveyDbMapper(impl: CheckedSurveyDbMapper): Mapper<SerializableCheckedSurvey, CheckedSurvey>

    @Binds
    fun bindSurveyProgressMapper(impl: SurveyProgressMapper): Mapper<LocalCheckedSurvey, Map<SurveySubtype, Progress>>

    @Binds
    fun bindSurveyProgressFetcher(impl: SurveyProgressFetcherImpl): SurveyProgressFetcher

    @Binds
    fun bindFiasAddressMapper(impl: FiasAddressMapper): Mapper<RemoteFiasAddress, FiasAddress>

    @Binds
    fun bindFiasAddressToRemoteMapper(impl: FiasAddressToRemoteMapper): Mapper<FiasAddress, RemoteFiasAddress>

    @Binds
    fun bindGeneralInformationNetworkMapper(impl: GeneralInformationNetworkMapper): Mapper<RemoteVerificationObject, GeneralInformation>

    @Binds
    fun bindGeneralInformationSerializableMapper(impl: GeneralInformationSerializableMapper): Mapper<SerializableGeneralInformation, GeneralInformation>

    @Binds
    fun bindMediaDbMapper(impl: MediaDbMapper): Mapper<Media, LocalMedia>

    @Binds
    fun bindScheduleSendNetworkMapper(impl: ScheduleSendNetworkMapper): Mapper<ScheduleSurvey, SerializableObjectWorkSchedule>

    @Binds
    fun bindReferenceSendRemoteMapper(impl: ReferenceSendRemoteMapper): Mapper<Reference, RemoteReference>

    @Binds
    fun bindSecondaryResourceMapper(impl: SecondaryResourceMapper): Mapper<SecondaryResourcesSurvey, RemoteSecondaryResources>

    @Binds
    fun bindMediaToSerializableMapper(impl: MediaToSendMapper): Mapper2<Media, Map<String, LocalMedia>, SerializableMedia>

    @Binds
    fun bindWastePlacementMapMapper(impl: WastePlacementMapMapper): Mapper<RemoteWastePlacementMap, WastePlacementMap>

    @Binds
    fun bindWastePlacementMapToSendMapper(impl: WastePlacementMapToSendMapper): Mapper<WastePlacementMap, RemoteWastePlacementMap>

    @Binds
    fun bindInfrastructureSurveyNetworkMapper(impl: InfrastructureSurveyNetworkMapper): Mapper2<
        RemoteVerificationObject,
        VerificationObjectType,
        InfrastructureSurvey
        >

    @Binds
    fun bindInfrastructureCheckedSurveyMapper(impl: InfrastructureCheckedSurveyMapper): Mapper<
        SerializableInfrastructureCheckedSurvey,
        InfrastructureCheckedSurvey
        >

    @Binds
    fun bindSerializableInfrastructureCheckedSurveyMapper(impl: SerializableInfrastructureCheckedSurveyMapper): Mapper<
        InfrastructureCheckedSurvey,
        SerializableInfrastructureCheckedSurvey
        >

    @Binds
    fun bindInfrastructureSurveyMapper(impl: InfrastructureSurveyMapper): Mapper<
        SerializableInfrastructureSurvey,
        InfrastructureSurvey
        >

    @Binds
    fun bindSerializableInfrastructureSurveyMapper(impl: SerializableInfrastructureSurveyMapper): Mapper<
        InfrastructureSurvey,
        SerializableInfrastructureSurvey
        >

    @Binds
    fun bindGpsPointToSendMapper(impl: GpsPointToSendMapper): Mapper<GpsPoint, GpsPointToSend>
}