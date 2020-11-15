import io.github.detekt.sarif4j.JacksonSarifWriter
import io.github.detekt.sarif4j.Run
import io.github.detekt.sarif4j.SarifSchema210
import io.github.detekt.sarif4j.Tool
import io.github.detekt.sarif4j.ToolComponent
import io.restassured.path.json.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.URI
import java.util.UUID

class DetektToolTest {

    @Test
    fun `render a minimal detekt based tool report`() {
        val report = SarifSchema210().apply {
            `$schema` = URI.create("https://raw.githubusercontent.com/oasis-tcs/sarif-spec/master/Schemata/sarif-schema-2.1.0.json")
            version = SarifSchema210.Version._2_1_0
            runs = mutableListOf(
                Run().apply {
                    tool = Tool().apply {
                        driver = ToolComponent().apply {
                            guid = UUID.randomUUID().toString()
                            name = "detekt"
                            fullName = "detekt"
                            organization = "detekt"
                            language = "Kotlin"
                            downloadUri = URI.create("https://github.com/detekt/detekt/releases/download/v1.14.1/detekt")
                            informationUri = URI.create("detekt.github.io/detekt")
                        }

                        results = mutableListOf()
                    }
                }
            )
        }

        val result = JacksonSarifWriter().toJson(report)
        val jsonPath = JsonPath.from(result)

        assertThat(jsonPath.getString("runs[0].tool.driver.name")).isEqualTo("detekt")
    }
}
