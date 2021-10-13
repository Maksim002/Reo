include(":core_data_verification:impl")
include(":core_data_verification:api")
include(":core_data_verification")
include(":core_data_measurement:impl")
include(":core_data_measurement:api")
include(":core_data_measurement")
include(":feature_sync:impl")
include(":feature_sync:api")
include(":feature_sync")
include(":feature_auth")
include(
    ":app_verification",
    ":app_measurement",
    ":common",
    ":core_network",
    ":core_network:api",
    ":core_network:impl",
    ":feature_map",
    ":feature_auth",
    ":feature_mno_list",
    ":feature_settings"
)
rootProject.name = "Reo"
apply(from = "./buildSrc/buildCacheSettings.gradle")