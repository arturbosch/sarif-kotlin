package io.github.detekt.sarif4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.UncheckedIOException;

public class JacksonSarifWriter implements SarifJsonWriter {

    private final ObjectMapper mapper;

    public JacksonSarifWriter() {
        this(new ObjectMapper());
    }

    public JacksonSarifWriter(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String toMinifiedJson(SarifSchema210 sarif) {
        try {
            mapper.disable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(sarif);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String toJson(SarifSchema210 sarif) {
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(sarif);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
