plugins {
    `java-library`
    `maven-publish`
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

group = "io.github.detekt.${rootProject.name}"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

tasks.withType(Javadoc::class).configureEach {
    val customArgs = buildDir.resolve("javadoc-silence.txt")
    customArgs.writeText("""
        -Xdoclint:none
    """.trimIndent())
    options.optionFiles?.add(customArgs)
}

tasks.withType(Test::class).configureEach {
    useJUnitPlatform()
}

jsonSchema2Pojo {
    targetPackage = "io.github.detekt.sarif4j"
    setAnnotationStyle(org.jsonschema2pojo.AnnotationStyle.MOSHI1.toString())
}

val sonatypeUsername: String? =
    findProperty("sonatypeUsername")?.toString()
        ?: System.getenv("MAVEN_CENTRAL_USER")

val sonatypePassword: String? =
    findProperty("sonatypePassword")?.toString()
        ?: System.getenv("MAVEN_CENTRAL_PW")

publishing {
    repositories {
        maven {
            name = "mavenCentral"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                username = sonatypeUsername
                password = sonatypePassword
            }
        }
        maven {
            name = "mavenSnapshot"
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
            credentials {
                username = sonatypeUsername
                password = sonatypePassword
            }
        }
    }
    publications.register<MavenPublication>(rootProject.name) {
        groupId = project.group as? String
        artifactId = project.name
        version = project.version as? String
        from(components["java"])
        pom {
            description.set("SARIF models for the JVM")
            name.set(rootProject.name)
            url.set("https://detekt.github.io/detekt")
            licenses {
                license {
                    name.set("The Apache Software License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("repo")
                }
            }
            scm {
                url.set("https://github.com/detekt/sarif4j")
            }
        }
    }
}
