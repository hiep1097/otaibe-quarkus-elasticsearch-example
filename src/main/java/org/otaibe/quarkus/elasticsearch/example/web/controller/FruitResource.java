package org.otaibe.quarkus.elasticsearch.example.web.controller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.otaibe.commons.quarkus.rest.utils.ControllerUtils;
import org.otaibe.quarkus.elasticsearch.example.domain.Fruit;
import org.otaibe.quarkus.elasticsearch.example.service.FruitService;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path(FruitResource.ROOT_PATH)
@Getter(value = AccessLevel.PROTECTED)
@Slf4j
public class FruitResource {

    public static final String ROOT_PATH = "/fruits";
    public static final String ID = "id";

    @Inject
    FruitService service;
    @Inject
    ControllerUtils controllerUtils;

    @GET
    @Path("{" + ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> findById(@PathParam(ID) @NotBlank String id) {
        CompletableFuture<Response> result = new CompletableFuture<>();
        getControllerUtils().processResult(result,
                getService().findById(id),
                new Fruit(),
                fruit1 -> StringUtils.isNotBlank(fruit1.getId())
        )
                .subscriberContext(context -> context.put(ControllerUtils.CLIENT_ERROR_KEY, Response.Status.NOT_FOUND))
                .subscribe()
        ;
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> save(@NotNull Fruit data) {
        CompletableFuture<Response> result = new CompletableFuture<>();
        getControllerUtils().processResult(result,
                getService().save(data),
                new Fruit(),
                fruit1 -> StringUtils.isNotBlank(fruit1.getId())
        )
                .subscribe()
        ;
        return result;
    }


}