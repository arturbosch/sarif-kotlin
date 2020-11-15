import com.github.eirnym.js2p.GenerateJsonSchemaJavaTask
import java.net.URL
import java.nio.file.Files

plugins {
    `java-library`
    `maven-publish`
    signing
    kotlin("jvm") version Versions.kotlin
    id(Deps.json_scheme_generator) version Versions.js2p
    id("io.codearte.nexus-staging") version Versions.nexus
}

dependencies {
    implementation(Deps.jackson)

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
    val customArgs = projectDir.resolve("javadoc-silence.txt")
    customArgs.writeText("""
        -Xdoclint:none
    """.trimIndent())
    options.optionFiles?.add(customArgs)
}

tasks.withType(Test::class).configureEach {
    useJUnitPlatform()
}

val getSchema by tasks.registering {
    val schemaFile = projectDir.resolve("src/main/resources/json/sarif-schema-2.1.0.json").toPath()

    outputs.file(schemaFile)

    doLast {
        val schema = URL("https://raw.githubusercontent.com/oasis-tcs/sarif-spec/master/Schemata/sarif-schema-2.1.0.json")
            .openStream()
            .readBytes()
        Files.write(schemaFile, schema)
    }
}

tasks.withType<GenerateJsonSchemaJavaTask>().configureEach {
    dependsOn(getSchema)
}

jsonSchema2Pojo {
    targetPackage = "io.github.detekt.sarif4j"
    initializeCollections = false
    isGenerateBuilders = true
    setInclusionLevel("NON_DEFAULT")
    setAnnotationStyle(org.jsonschema2pojo.AnnotationStyle.JACKSON.toString())
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
            developers {
                developer {
                    id.set("Artur Bosch")
                    name.set("Artur Bosch")
                    email.set("arturbosch@gmx.de")
                }
            }
            scm {
                url.set("https://github.com/detekt/sarif4j")
            }
        }
    }
}

if (findProperty("signing.keyId") != null) {
    signing {
        sign(publishing.publications[rootProject.name])
    }
}

nexusStaging {
    packageGroup = "io.github.detekt"
    stagingProfileId = "117c7a00a4d531"
    username = sonatypeUsername
    password = sonatypePassword
}
