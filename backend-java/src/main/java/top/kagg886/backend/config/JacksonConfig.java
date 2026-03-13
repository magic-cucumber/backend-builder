package top.kagg886.backend.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import top.kagg886.backend.util.LocalDateTimeAsStringDeSerializer;
import top.kagg886.backend.util.LocalDateTimeAsStringSerializer;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;

@Configuration
public class JacksonConfig {
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false)
                .featuresToEnable(ALLOW_COMMENTS)
                .build();

        objectMapper.registerModule(new SimpleModule() {{
            addSerializer(Long.class, new JsonSerializer<>() {
                @SneakyThrows
                @Override
                public void serialize(Long aLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
                    jsonGenerator.writeString(aLong.toString());
                }
            });

            addDeserializer(Long.class, new JsonDeserializer<>() {

                @Override
                public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return Long.parseLong(p.readValueAs(String.class));
                }
            });

            addSerializer(LocalDateTime.class,new LocalDateTimeAsStringSerializer());
            addDeserializer(LocalDateTime.class,new LocalDateTimeAsStringDeSerializer());
        }});
        return objectMapper;
    }
}

