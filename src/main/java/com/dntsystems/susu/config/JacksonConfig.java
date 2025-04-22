package com.dntsystems.susu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    ObjectMapper objectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // Custom LocalDateTime de-serializer that supports both formats
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm") // First try "yyyy-MM-dd HH:mm"
                .optionalStart().appendPattern(":ss").optionalEnd() // If seconds are included, accept them
                .toFormatter()));

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return new ObjectMapper()
                .registerModule(javaTimeModule)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
