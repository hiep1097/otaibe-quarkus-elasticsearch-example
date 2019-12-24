package org.otaibe.quarkus.elasticsearch.example.service;

import io.quarkus.test.junit.QuarkusTest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.otaibe.commons.quarkus.core.utils.JsonUtils;
import org.otaibe.quarkus.elasticsearch.example.domain.Fruit;
import org.otaibe.quarkus.elasticsearch.example.utils.TestUtils;

import javax.inject.Inject;
import java.util.List;

@QuarkusTest
@Getter
@Setter
@Slf4j
public class FruitServiceTests {

    @Inject
    FruitService fruitService;
    @Inject
    TestUtils testUtils;
    @Inject
    JsonUtils jsonUtils;

    @BeforeEach
    public void before() {
        getTestUtils().deleteAllWithTestExtRef().block();
    }

    @AfterEach
    public void after() {
        getTestUtils().deleteAllWithTestExtRef().block();
    }

    @Test
    public void manageFruitTest() {
        Fruit apple = getTestUtils().createApple();

        Fruit apple1 = getFruitService().save(apple).block();
        Assertions.assertNotNull(apple1.getId());
        Assertions.assertTrue(apple1.getVersion() > 0);
        log.info("saved result: {}", getJsonUtils().toStringLazy(apple1));

        List<Fruit> fruitList = getFruitService().findByExternalRefId(TestUtils.EXT_REF_ID).collectList().block();
        Assertions.assertTrue(fruitList.size() > 0);

        List<Fruit> fruitList1 = getFruitService().findByNameOrDescription("bulgaria").collectList().block();
        Assertions.assertTrue(fruitList1.size() > 0);

        //Ensure that the full text search is working - it is 'Apples' in description
        List<Fruit> fruitList2 = getFruitService().findByDescription("apple").collectList().block();
        Assertions.assertTrue(fruitList2.size() > 0);

        //Ensure that the full text search is working - it is 'Apple' in name
        List<Fruit> fruitList3 = getFruitService().findByName("apples").collectList().block();
        Assertions.assertTrue(fruitList3.size() > 0);

        Boolean deleteAppleResult = getFruitService().getDao().deleteById(apple1).block();
        Assertions.assertTrue(deleteAppleResult);
    }

}
