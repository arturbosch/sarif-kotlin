package space.dector.sarif

import java.net.URI

typealias PropertyBag = Map<String, String>

data class Sarif(
    val runs: List<Run>,
    val version: Version = Version.V2_1_0,
) {

    enum class Version(val value: String) {
        V2_1_0("2.1.0")
    }
}

data class Run(
    val tool: Tool,
    val results: List<Result>?,
)

data class Tool(
    val driver: ToolComponent,
    val extensions: Set<ToolComponent>?,
)

data class ToolComponent(
    val name: String,
    val organization: String,
    val product: String?,
    val fullName: String?,
    val version: String?,
    val semanticVersion: String?,
    val releaseDateUtc: String?,
    val downloadUri: URI?,
    val informationUri: URI?,
    val rules: List<ReportingDescriptor>,
)

data class ReportingDescriptor(
    val id: String,
    val name: String,
    val shortDescription: String,
    val fullDescription: String,
    val defaultConfiguration: ReportingConfiguration,
)

data class ReportingConfiguration(
    val enabled: Boolean = true,
    val level: Level = Level.Warning,
    val rank: Int = 1,
    val parameters: PropertyBag,
)

enum class Level(val value: String) {
    None("none"),
    Note("note"),
    Warning("warning"),
    Error("error"),
}

data class Result(
    val ruleId: String?,
    val kind: Kind?,
    val level: Level?,
    val message: Message,
    val locations: List<Location>?,
) {

    enum class Kind(val value: String) {
        NotApplicable("notApplicable"),
        Pass("pass"),
        Fail("fail"),
        Review("review"),
        Open("open"),
        Informational("informational"),
    }
}

data class Message(
    val text: String,
    val markdown: String?,
)

data class Location(
    val physicalLocation: PhysicalLocation,
)

data class PhysicalLocation(
    val artifactLocation: ArtifactLocation,
)

data class ArtifactLocation(
    val uri: URI,
    val index: Int,
    val uriBaseId: URI?,
)
