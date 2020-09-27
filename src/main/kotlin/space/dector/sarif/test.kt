package space.dector.sarif

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import java.net.URI
import java.util.Date
import java.util.UUID

class UriAdapter : JsonAdapter<URI>() {

    @FromJson
    override fun fromJson(reader: JsonReader): URI {
        return URI.create(reader.readJsonValue() as String)
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: URI?) {
        if (value != null) {
            writer.value(value.toASCIIString())
        }
    }
}

fun main() {
    val detektVersion = "1.14.1"

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
                        downloadUri = URI.create("https://github.com/detekt/detekt/releases/download/v${detektVersion}/detekt")
                        informationUri = URI.create("detekt.github.io/detekt")
                    }

                    results = mutableListOf()
                }
            }
        )
    }

    val moshi = Moshi.Builder()
        .add(URI::class.java, UriAdapter())
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build()
    val adapter = moshi.adapter(SarifSchema210::class.java)
    val result = adapter.toJson(report)

    println(result)
}
