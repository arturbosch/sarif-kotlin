import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
    id(Deps.json_scheme_generator) version Versions.js2p
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(Deps.moshi)
    implementation(Deps.`moshi-adapters`)

    testImplementation(Deps.kotest.runner)
    testImplementation(Deps.kotest.assertions)
}

sourceSets["main"].java {
    srcDir(buildDir.resolve("generated-sources/js2p"))
}

allprojects {
    group = "space.dector.${rootProject.name}"
    version = "0.1-SNAPSHOT"

    repositories {
        jcenter()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}

jsonSchema2Pojo {
    targetPackage = "space.dector.sarif"
    setAnnotationStyle(org.jsonschema2pojo.AnnotationStyle.MOSHI1.toString())
}
