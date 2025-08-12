package com.coindesk.demo.coindesk.dto.converter;

import com.coindesk.demo.config.CurrencyMappingLoader;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class CurrencyNameConverter {

    @Component
    public static class serializer extends JsonSerializer<String> {
        private final CurrencyMappingLoader loader;

        public serializer(CurrencyMappingLoader loader) {
            this.loader = loader;
        }

        @Override
        public void serialize(String code, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            String display = loader.getDisplayName(code);
            gen.writeString(display);
        }
    }

}

