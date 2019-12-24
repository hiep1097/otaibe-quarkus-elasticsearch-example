package org.otaibe.quarkus.elasticsearch.example.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.quarkus.runtime.StartupEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.otaibe.commons.quarkus.core.converter.json.DateTimeDeserializer;
import org.otaibe.commons.quarkus.core.converter.json.DateTimeSerializer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
@Getter
@Setter
@Slf4j
public class JsonConfig {

    @Inject
    ObjectMapper objectMapper;

    AtomicBoolean isInitialized = new AtomicBoolean(false);

    public void init(@Observes StartupEvent event) {
        getObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        ;
        getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SimpleModule module = new SimpleModule();
        module.addSerializer(DateTime.class, new DateTimeSerializer());
        module.addDeserializer(DateTime.class, new DateTimeDeserializer());
        getObjectMapper().registerModule(module);

        getIsInitialized().set(true);
    }

}
