package io.github.detekt.sarif4j;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;
import okio.Buffer;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Objects;

public class MoshiSarifJsonWriter implements SarifJsonWriter {

    private final String indent;

    public MoshiSarifJsonWriter() {
        this("    ");
    }

    public MoshiSarifJsonWriter(final String indent) {
        Objects.requireNonNull(indent);
        this.indent = indent;
    }

    @Override
    public String toJson(final SarifSchema210 sarif) throws IOException {
        final JsonAdapter<SarifSchema210> adapter = createAdapter();
        final Buffer buffer = new Buffer();
        JsonWriter writer = JsonWriter.of(buffer);
        writer.setIndent(indent);
        adapter.toJson(writer, sarif);
        return buffer.readUtf8();
    }

    private JsonAdapter<SarifSchema210> createAdapter() {
        final Moshi moshi = new Moshi.Builder()
                .add(URI.class, new UriAdapter())
                .add(Date.class, new Rfc3339DateJsonAdapter())
                .build();
        return moshi.adapter(SarifSchema210.class);
    }

    @Override
    public String toMinifiedJson(final SarifSchema210 sarif) {
        return createAdapter().toJson(sarif);
    }
}
