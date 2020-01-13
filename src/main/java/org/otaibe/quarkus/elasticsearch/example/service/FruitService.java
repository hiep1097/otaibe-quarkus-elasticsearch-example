package org.otaibe.quarkus.elasticsearch.example.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.otaibe.quarkus.elasticsearch.example.dao.FruitDaoImpl;
import org.otaibe.quarkus.elasticsearch.example.domain.Fruit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Getter
@Setter
@Slf4j
public class FruitService {

    @Inject
    FruitDaoImpl dao;

    public Mono<Fruit> save(Fruit entity) {
        return getDao().save(entity);
    }

    public Mono<Fruit> findById(Fruit entity) {
        return getDao().findById(entity);
    }

    public Mono<Fruit> findById(String id) {
        return Mono.just(Fruit.of(id, null, null, null, null))
                .flatMap(entity -> findById(entity))
                ;
    }

    public Flux<Fruit> findByExternalRefId(String value) {
        return getDao().findByExternalRefId(value);
    }

    public Flux<Fruit> findByName(String value) {
        return getDao().findByName(value);
    }

    public Flux<Fruit> findByDescription(String value) {
        return getDao().findByDescription(value);
    }

    public Flux<Fruit> findByNameOrDescription(String value) {
        return getDao().findByNameOrDescription(value);
    }

    public Mono<Boolean> delete(Fruit entity) {
        return Mono.just(entity.getId())
                .filter(s -> StringUtils.isNotBlank(s))
                .flatMap(s -> getDao().deleteById(entity))
                .defaultIfEmpty(false);
    }
}
