package org.otaibe.quarkus.elasticsearch.example.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.otaibe.quarkus.elasticsearch.example.dao.FruitDaoImpl;
import org.otaibe.quarkus.elasticsearch.example.domain.Fruit;
import reactor.core.publisher.Mono;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Getter
@Setter
@Slf4j
public class TestUtils {
    public static final String EXT_REF_ID = "ad8dd55d-d618-4c9f-ae43-2c007f3fc64b";

    @Inject
    FruitDaoImpl fruitDao;

    public Fruit createApple() {
        return Fruit.of(null, TestUtils.EXT_REF_ID, "Apple", "Golden Apples from Bulgaria", 0l);
    }

    public Mono<Void> deleteAllWithTestExtRef() {
        return getFruitDao().findByExternalRefId(EXT_REF_ID)
                .flatMap(fruit -> getFruitDao().deleteById(fruit))
                .then();
    }

}
