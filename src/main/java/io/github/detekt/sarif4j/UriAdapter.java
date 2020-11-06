package io.github.detekt.sarif4j;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.ToJson;

import java.io.IOException;
import java.net.URI;

public class UriAdapter extends JsonAdapter<URI> {

    @Override
    @FromJson
    public URI fromJson(JsonReader reader) throws IOException {
        Object value = reader.readJsonValue();
        if (value instanceof String) {
            return URI.create(String.valueOf(value));
        }
        throw new IllegalStateException("String type expected to convert to an URI.");
    }

    @Override
    @ToJson
    public void toJson(JsonWriter writer, URI value) throws IOException {
        writer.value(value == null ? "null" : value.toString());
    }
}
