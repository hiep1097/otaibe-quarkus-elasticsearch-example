package org.otaibe.quarkus.elasticsearch.example.web.controller;

import io.quarkus.test.junit.QuarkusTest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.otaibe.commons.quarkus.core.utils.JsonUtils;
import org.otaibe.quarkus.elasticsearch.example.domain.Fruit;
import org.otaibe.quarkus.elasticsearch.example.service.FruitService;
import org.otaibe.quarkus.elasticsearch.example.utils.TestUtils;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Getter(value = AccessLevel.PROTECTED)
@Slf4j
public class FruitResourceTest {

    @Inject
    TestUtils testUtils;
    @Inject
    JsonUtils jsonUtils;
    @Inject
    FruitService service;

    @Test
    public void restEndpointsTest() {
        log.info("restEndpointsTest start");
        Fruit apple = getTestUtils().createApple();

        Fruit savedApple = given()
                .when()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(apple)
                .post(FruitResource.ROOT_PATH)
                .then()
                .statusCode(200)
                .extract()
                .as(Fruit.class);
        String id = savedApple.getId();
        Assertions.assertTrue(StringUtils.isNotBlank(id));

        URI findByIdPath = UriBuilder.fromPath(FruitResource.ROOT_PATH)
                .path(id)
                .build();

        Fruit foundApple = given()
                .when().get(findByIdPath.getPath())
                .then()
                .statusCode(200)
                .extract()
                .as(Fruit.class);

        Assertions.assertEquals(savedApple, foundApple);

        Boolean deleteResult = getService().delete(foundApple).block();
        Assertions.assertTrue(deleteResult);

        given()
                .when().get(findByIdPath.getPath())
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                ;

        log.info("restEndpointsTest end");
    }

}