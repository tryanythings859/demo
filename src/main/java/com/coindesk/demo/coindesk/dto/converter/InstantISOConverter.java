package com.coindesk.demo.coindesk.dto.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class InstantISOConverter {

    public static class serializer extends JsonSerializer<Instant> {
        private static final DateTimeFormatter FORMATTER =
                DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneOffset.UTC);

        @Override
        public void serialize(Instant instant, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (instant == null) {
                gen.writeNull();
            } else {
                gen.writeString(FORMATTER.format(instant));
            }
        }
    }

    public static class deserializer extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String text = jsonParser.getText();
            if (ObjectUtils.isEmpty(text)) {
                return null;
            }
            return OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant();
        }
    }
}

