package ru.ktsstudio.core_data_measurement_impl.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.ktsstudio.common.data.db.DatabaseCleaner
import ru.ktsstudio.common.data.media.FileManager
import ru.ktsstudio.common.data.media.FileManagerImpl
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.data.settings.SettingsRepository
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.data.CategoryRepository
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.data.RegisterRepository
import ru.ktsstudio.core_data_measurement_api.data.SyncRepository
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementStatus
import ru.ktsstudio.core_data_measurement_api.data.model.MixedWasteContainer
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer
import ru.ktsstudio.core_data_measurement_api.data.model.Register
import ru.ktsstudio.core_data_measurement_api.data.model.SeparateWasteContainer
import ru.ktsstudio.core_data_measurement_api.data.model.WasteCategory
import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementComposite
import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.core_data_measurement_api.domain.SeparateContainerComposite
import ru.ktsstudio.core_data_measurement_api.domain.WasteGroupComposite
import ru.ktsstudio.core_data_measurement_impl.CategoryRepositoryImpl
import ru.ktsstudio.core_data_measurement_impl.data.MeasurementRepositoryImpl
import ru.ktsstudio.core_data_measurement_impl.data.MnoRepositoryImpl
import ru.ktsstudio.core_data_measurement_impl.data.RegisterRepositoryImpl
import ru.ktsstudio.core_data_measurement_impl.data.SettingsRepositoryImpl
import ru.ktsstudio.core_data_measurement_impl.data.SyncRepositoryImpl
import ru.ktsstudio.core_data_measurement_impl.data.db.MeasurementDb
import ru.ktsstudio.core_data_measurement_impl.data.db.cleaner.DatabaseCleanerImpl
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.CategoryDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.ContainerDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementContainerDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementDraftDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MediaDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MnoDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MorphologyDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.WasteGroupDao
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.ContainerTypeDbMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.ContainerWasteTypeDbMerger
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.MeasurementDbMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.MeasurementStatusMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.MixedContainerCompositeDbMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.MixedContainerCompositeDbMerger
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.MixedWasteContainerDbMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.MnoContainersDbMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.MnoDbMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.MorphologyDbMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.SeparateContainerDbMerger
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.SeparateContainerWasteTypeDbMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.SeparateWasteContainerDbMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.WasteCategoryDbMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.WasteGroupDbMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local.WasteSubgroupDbMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain.ContainerTypeMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain.ContainerWasteTypeMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain.MeasurementCompositeMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain.MeasurementWithRelationsMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain.MixedContainerCompositeMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain.MnoContainerMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain.MnoWithRelationsMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain.MorphologyMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain.SeparateContainerCompositeMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain.WasteCategoryMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain.WasteGroupCompositeMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain.WasteSubgroupMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_remote.MeasurementToSendMapper
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteTypeWithRelation
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurement
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementStatus
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMno
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphology
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphologyWithRelation
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroup
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroupWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteSubgroup
import ru.ktsstudio.core_data_measurement_impl.data.db.roomconverter.EnumConverters
import ru.ktsstudio.core_data_measurement_impl.data.media.MediaRepositoryImpl
import ru.ktsstudio.core_data_measurement_impl.data.network.MeasurementNetworkApi
import ru.ktsstudio.core_data_measurement_impl.data.network.MediaApi
import ru.ktsstudio.core_data_measurement_impl.data.network.MnoNetworkApi
import ru.ktsstudio.core_data_measurement_impl.data.network.RegisterNetworkApi
import ru.ktsstudio.core_data_measurement_impl.data.network.SettingsNetworkApi
import ru.ktsstudio.core_data_measurement_impl.data.network.mapper.MeasurementNetworkMapper
import ru.ktsstudio.core_data_measurement_impl.data.network.mapper.MnoNetworkMapper
import ru.ktsstudio.core_data_measurement_impl.data.network.mapper.RegisterNetworkMapper
import ru.ktsstudio.core_data_measurement_impl.data.network.model.MeasurementToSend
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteMeasurement
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteMno
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteRegister
import ru.ktsstudio.core_network_api.qualifiers.DefaultRetrofit
import ru.ktsstudio.core_network_api.qualifiers.MediaRetrofit

@Module
interface CoreMeasurementDataModule {

    @Binds
    fun bindFileManager(impl: FileManagerImpl): FileManager

    @Binds
    fun bindRegisterRepository(repo: RegisterRepositoryImpl): RegisterRepository

    @Binds
    fun bindSyncRepository(repo: SyncRepositoryImpl): SyncRepository

    @Binds
    fun bindMnoRepository(repo: MnoRepositoryImpl): MnoRepository

    @Binds
    fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    fun bindMeasurementRepository(impl: MeasurementRepositoryImpl): MeasurementRepository

    @Binds
    fun bindSettingsRepository(repo: SettingsRepositoryImpl): SettingsRepository

    @Binds
    fun bindMediaRepository(repo: MediaRepositoryImpl): MediaRepository

    @Binds
    fun bindMnoNetworkMapper(impl: MnoNetworkMapper): Mapper<RemoteMno, Mno>

    @Binds
    fun bindMeasurementNetworkMapper(impl: MeasurementNetworkMapper): Mapper<RemoteMeasurement, Measurement>

    @Binds
    fun bindMeasurementToSendMapper(impl: MeasurementToSendMapper): Mapper<LocalMeasurementWithRelations, MeasurementToSend>

    @Binds
    fun bindRegisterNetworkMapper(impl: RegisterNetworkMapper): Mapper<RemoteRegister, Register>

    @Binds
    fun bindMnoDbMapper(impl: MnoDbMapper): Mapper<Mno, LocalMno>

    @Binds
    fun bindWasteGroupDbMapper(impl: WasteGroupDbMapper): Mapper<WasteGroup, LocalWasteGroup>

    @Binds
    fun bindWasteSubgroupDbMapper(impl: WasteSubgroupDbMapper): Mapper<WasteSubgroup, LocalWasteSubgroup>

    @Binds
    fun bindContainerDbMapper(impl: MnoContainersDbMapper): Mapper<Mno, List<LocalMnoContainer>>

    @Binds
    fun bindMeasurementWithRelationsMapper(impl: MeasurementWithRelationsMapper): Mapper<LocalMeasurementWithRelations, Measurement>

    @Binds
    fun bindMeasurementCompositeMapper(impl: MeasurementCompositeMapper): Mapper<LocalMeasurementWithRelations, MeasurementComposite>

    @Binds
    fun bindMixedContainerCompositeMapper(impl: MixedContainerCompositeMapper): Mapper<LocalMixedWasteContainerWithRelations, MixedWasteContainerComposite>

    @Binds
    fun bindMixedContainerCompositeDbMapper(impl: MixedContainerCompositeDbMapper): Mapper2<MixedWasteContainerComposite, Long, LocalMixedWasteContainer>

    @Binds
    fun bindMixedContainerCompositeDbMerger(impl: MixedContainerCompositeDbMerger): Mapper2<MixedWasteContainerComposite, LocalMixedWasteContainer, LocalMixedWasteContainer>

    @Binds
    fun bindWasteTypeDbMerger(impl: ContainerWasteTypeDbMerger): Mapper2<ContainerWasteType, LocalContainerWasteType, LocalContainerWasteType>

    @Binds
    fun bindMnoMapper(impl: MnoWithRelationsMapper): Mapper<LocalMnoWithRelations, Mno>

    @Binds
    fun bindMeasurementStatusMapper(impl: MeasurementStatusMapper): Mapper<MeasurementStatus, LocalMeasurementStatus>

    @Binds
    fun bindContainerTypeDbMapper(impl: ContainerTypeDbMapper): Mapper<ContainerType, LocalContainerType>

    @Binds
    fun bindWasteCategoryDbMapper(impl: WasteCategoryDbMapper): Mapper<WasteCategory, LocalWasteCategory>

    @Binds
    fun bindWasteCategoryMapper(impl: WasteCategoryMapper): Mapper<LocalWasteCategory, WasteCategory>

    @Binds
    fun bindContainerTypeMapper(impl: ContainerTypeMapper): Mapper<LocalContainerType, ContainerType>

    @Binds
    fun bindMnoContainerMapper(impl: MnoContainerMapper): Mapper<LocalMnoContainerWithRelations, MnoContainer>

    @Binds
    fun bindSeparateWasteContainerDbMapper(impl: SeparateWasteContainerDbMapper): Mapper2<SeparateWasteContainer, Long, LocalSeparateWasteContainer>

    @Binds
    fun bindSeparateContainerDbMerger(impl: SeparateContainerDbMerger): Mapper2<SeparateContainerComposite, LocalSeparateWasteContainer, LocalSeparateWasteContainer>

    @Binds
    fun bindMeasurementMapper(impl: MeasurementDbMapper): Mapper<Measurement, LocalMeasurement>

    @Binds
    fun bindSeparateContainerCompositeMapper(impl: SeparateContainerCompositeMapper): Mapper<LocalSeparateWasteContainerWithRelations, SeparateContainerComposite>

    @Binds
    fun bindContainerWasteTypeMapper(impl: ContainerWasteTypeMapper): Mapper<LocalContainerWasteTypeWithRelation, ContainerWasteType>

    @Binds
    fun bindMixedContainerDbMapper(impl: MixedWasteContainerDbMapper): Mapper2<MixedWasteContainer, Long, LocalMixedWasteContainer>

    @Binds
    fun bindSeparateContainerWasteTypeDbMapper(impl: SeparateContainerWasteTypeDbMapper): Mapper2<ContainerWasteType, Long, LocalContainerWasteType>

    @Binds
    fun bindMorphologyDbMapper(impl: MorphologyDbMapper): Mapper2<MorphologyItem, Long, LocalMorphology>

    @Binds
    fun bindMorphologyMapper(impl: MorphologyMapper): Mapper<LocalMorphologyWithRelation, MorphologyItem>

    @Binds
    fun bindWasteSubgroupMapper(impl: WasteSubgroupMapper): Mapper<LocalWasteSubgroup, WasteSubgroup>

    @Binds
    fun bindWasteGroupCompositeMapper(impl: WasteGroupCompositeMapper): Mapper<LocalWasteGroupWithRelations, WasteGroupComposite>

    @Binds
    fun bindDatabaseCleaner(impl: DatabaseCleanerImpl): DatabaseCleaner

    companion object {
        @Provides
        fun provideMorphologyDao(db: MeasurementDb): MorphologyDao =
            db.morphologyDao()

        @Provides
        @FeatureScope
        fun provideDb(
            context: Context,
            enumConverters: EnumConverters
        ) = Room.databaseBuilder(
            context,
            MeasurementDb::class.java,
            MeasurementDb.dbName
        )
            .addTypeConverter(enumConverters)
            .fallbackToDestructiveMigration()
            .build()

        @Provides
        fun provideContainerDao(db: MeasurementDb): ContainerDao = db.containerDao()

        @Provides
        fun provideMnoDao(db: MeasurementDb): MnoDao = db.mnoDao()

        @Provides
        fun provideCategoryDao(db: MeasurementDb): CategoryDao = db.categoryDao()

        @Provides
        fun provideMeasurementDao(db: MeasurementDb): MeasurementDao = db.measurementDao()

        @Provides
        fun provideMeasurementContainerDao(db: MeasurementDb): MeasurementContainerDao =
            db.measurementContainerDao()

        @Provides
        fun provideMeasurementDraftDao(db: MeasurementDb): MeasurementDraftDao =
            db.measurementDraftDao()

        @Provides
        fun provideMediaDao(db: MeasurementDb): MediaDao = db.mediaDao()

        @Provides
        fun provideWasteGroupDao(db: MeasurementDb): WasteGroupDao = db.wasteGroupDao()

        @Provides
        @FeatureScope
        fun provideRegisterNetworkService(@DefaultRetrofit retrofit: Retrofit): RegisterNetworkApi =
            retrofit.create()

        @Provides
        @FeatureScope
        fun provideMnoNetworkService(@DefaultRetrofit retrofit: Retrofit): MnoNetworkApi =
            retrofit.create()

        @Provides
        @FeatureScope
        fun provideMeasurementNetworkService(
            @DefaultRetrofit retrofit: Retrofit
        ): MeasurementNetworkApi = retrofit.create()

        @Provides
        @FeatureScope
        fun provideMediaNetworkService(
            @MediaRetrofit retrofit: Retrofit
        ): MediaApi = retrofit.create()

        @Provides
        @FeatureScope
        fun provideSettingsNetworkService(
            @DefaultRetrofit retrofit: Retrofit
        ): SettingsNetworkApi = retrofit.create()
    }
}