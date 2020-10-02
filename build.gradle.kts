plugins {
    `java-library`
    kotlin("jvm") version Versions.kotlin
    id(Deps.json_scheme_generator) version Versions.js2p
}

dependencies {
    implementation(Deps.moshi)
    implementation(Deps.moshi_adapters)

    testImplementation(kotlin("stdlib"))
    testImplementation(Deps.junit)
    testImplementation(Deps.jsonpath)
    testImplementation(Deps.assertj)
}

sourceSets["main"].java {
    srcDir(buildDir.resolve("generated-sources/js2p"))
}

allprojects {
    group = "io.github.detekt.${rootProject.name}"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}

jsonSchema2Pojo {
    targetPackage = "io.github.detekt.sarif4j"
    setAnnotationStyle(org.jsonschema2pojo.AnnotationStyle.MOSHI1.toString())
}
