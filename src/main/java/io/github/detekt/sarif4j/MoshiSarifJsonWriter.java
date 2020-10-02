package io.github.detekt.sarif4j;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;

import java.net.URI;
import java.util.Date;

public class MoshiSarifJsonWriter implements SarifJsonWriter {

    @Override
    public String toJson(SarifSchema210 sarif) {
        Moshi moshi = new Moshi.Builder()
                .add(URI.class, new UriAdapter())
                .add(Date.class, new Rfc3339DateJsonAdapter())
                .build();
        JsonAdapter<SarifSchema210> adapter = moshi.adapter(SarifSchema210.class);
        return adapter.toJson(sarif);
    }
}
