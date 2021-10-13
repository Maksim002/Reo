package ru.ktsstudio.app_verification.di.object_list.dependencies

import dagger.Subcomponent
import ru.ktsstudio.common.di.FeatureScope

/**
 * @author Maxim Ovchinnikov on 19.11.2020.
 */

@Subcomponent
@FeatureScope
internal interface ObjectListDependencyComponent : ObjectListDependencies
