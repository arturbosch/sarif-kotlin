package io.github.detekt.sarif4j;

public interface SarifJsonWriter {

    String toJson(SarifSchema210 sarif);
}
