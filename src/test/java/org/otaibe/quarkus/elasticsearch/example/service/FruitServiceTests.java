package org.otaibe.quarkus.elasticsearch.example.service;

import io.quarkus.test.junit.QuarkusTest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.otaibe.quarkus.elasticsearch.example.domain.Fruit;
import org.otaibe.quarkus.elasticsearch.example.utils.TestUtils;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

@QuarkusTest
@Getter
@Setter
@Slf4j
public class FruitServiceTests {
    @Inject
    FruitService fruitService;

    @Test
    public void manageFruitTest() {
        Fruit apple = Fruit.of(null, TestUtils.EXT_REF_ID, "Apple", "Golden Apples from Bulgaria", 0l);

        Fruit apple1 = getFruitService().save(apple).block();
        Assertions.assertNotNull(apple1.getId());
        Assertions.assertTrue(apple1.getVersion() > 0);

        Boolean deleteAppleResult = getFruitService().getDao().deleteById(apple1).block();
        Assertions.assertTrue(deleteAppleResult);
    }
}
