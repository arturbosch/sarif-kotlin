package io.github.detekt.sarif4j;

import java.io.IOException;

public interface SarifJsonWriter {

    String toMinifiedJson(final SarifSchema210 sarif);

    String toJson(final SarifSchema210 sarif) throws IOException;
}
