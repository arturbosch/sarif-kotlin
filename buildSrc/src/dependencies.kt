object Deps {
    const val `moshi-adapters` = "com.squareup.moshi:moshi-adapters:${Versions.moshi}"
    const val moshi: String = "com.squareup.moshi:moshi:${Versions.moshi}"
    const val json_scheme_generator = "com.github.eirnym.js2p"

    object kotest {
        const val runner = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
        const val assertions = "io.kotest:kotest-assertions-core:${Versions.kotest}"
    }
}

object Versions {
    const val kotlin = "1.4.0"
    const val js2p = "1.0"
    const val moshi = "1.9.3"

    const val kotest = "4.2.3"
}
