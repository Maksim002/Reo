/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.09.2020.
 */
object Projects {

    object App {
        const val measurement = ":app_measurement"
        const val verification = ":app_verification"
    }

    const val common = ":common"

    const val network = ":core_network"

    object Network {
        const val api = "$network:api"
        const val impl = "$network:impl"
    }

    const val data_measurement = ":core_data_measurement"

    object DataMeasurement {
        const val api = "$data_measurement:api"
        const val impl = "$data_measurement:impl"
    }

    const val data_verification = ":core_data_verification"

    object DataVerification {
        const val api = "$data_verification:api"
        const val impl = "$data_verification:impl"
    }

    const val sync = ":feature_sync"

    object Sync {
        const val api = "$sync:api"
        const val impl = "$sync:impl"
    }

    object Feature {
        const val auth = ":feature_auth"
        const val map = ":feature_map"
        const val mno_list = ":feature_mno_list"
        const val settings = ":feature_settings"
    }
}
