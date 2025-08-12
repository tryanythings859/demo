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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InstantBSTConverter {

    public static class serializer extends JsonSerializer<Instant> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter
                .ofPattern("MMM d, yyyy 'at' HH:mm z", Locale.ENGLISH)
                .withZone(ZoneId.of("Europe/London")); // 英國時間

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
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter
                .ofPattern("MMM d, yyyy 'at' HH:mm z", Locale.ENGLISH)
                .withZone(ZoneId.of("Europe/London"));

        @Override
        public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String text = jsonParser.getText();
            if (ObjectUtils.isEmpty(text)) {
                return null;
            }
            ZonedDateTime zdt = ZonedDateTime.parse(text, FORMATTER);
            return zdt.toInstant();
        }
    }
}

