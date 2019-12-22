package org.otaibe.quarkus.elasticsearch.example.dao;

import io.quarkus.runtime.StartupEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.otaibe.commons.quarkus.core.utils.JsonUtils;
import org.otaibe.commons.quarkus.elasticsearch.client.dao.AbstractElasticsearchReactiveDaoImplementation;
import org.otaibe.quarkus.elasticsearch.example.domain.Fruit;
import org.otaibe.quarkus.elasticsearch.example.service.ElasticsearchService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
@Getter
@Setter
@Slf4j
public class FruitDaoImpl extends AbstractElasticsearchReactiveDaoImplementation<Fruit> {

    public static final String TABLE_NAME_PREFFIX = "fruit_";
    public static final String ENGLISH = "english";

    @Inject
    JsonUtils jsonUtils;
    @Inject
    ElasticsearchService elasticsearchService; //used as dependency

    private String tableName;

    public void init(@Observes StartupEvent event) {
        log.info("init started");
        super.init();
        setTableName(TABLE_NAME_PREFFIX);
        ensureIndex()
                .doOnNext(aBoolean -> log.info("index {} exists={}", getTableName(), aBoolean))
                .filter(aBoolean -> !aBoolean)
                .flatMap(aBoolean -> createIndex())
                .doOnNext(aBoolean -> log.info("index {} created={}", getTableName(), aBoolean))
                .doOnTerminate(() -> log.info("init completed"))
                .subscribe() //you can use subscribe here ...
        ;
    }

    @Override
    protected Long getVersion(Fruit entity) {
        return entity.getVersion();
    }

    @Override
    protected void setVersion(Fruit entity, Long version) {
        entity.setVersion(version);
    }

    public Flux<Fruit> findByExternalRefId(String value) {
        return findByMatch(Fruit.EXT_REF_ID, value);
    }

    public Flux<Fruit> findByName(String value) {
        return findByMatch(Fruit.NAME, value);
    }

    public Flux<Fruit> findByDescription(String value) {
        return findByMatch(Fruit.NAME, value);
    }

    public Flux<Fruit> findByNameAndDescription(String value) {
        Map<String, Object> query = new HashMap<>();
        query.put(Fruit.NAME, value);
        query.put(Fruit.DESCRIPTION, value);
        return findByMatch(query);
    }

    @Override
    protected String getId(Fruit entity) {
        if (entity == null) {
            return null;
        }
        return entity.getId();
    }

    @Override
    protected void setId(Fruit entity, String id) {
        if (entity == null) {
            return;
        }

        entity.setId(id);
    }

    @Override
    protected Class<Fruit> getEntityClass() {
        return Fruit.class;
    }

    @Override
    protected Mono<Boolean> createIndex() {
        CreateIndexRequest request = new CreateIndexRequest(getTableName());
        Map<String, Object> mapping = new HashMap();
        Map<String, Object> propsMapping = new HashMap<>();
        propsMapping.put(Fruit.ID, getKeywordTextAnalizer());
        propsMapping.put(Fruit.EXT_REF_ID, getKeywordTextAnalizer());
        propsMapping.put(Fruit.NAME, getTextAnalizer(ENGLISH));
        propsMapping.put(Fruit.DESCRIPTION, getTextAnalizer(ENGLISH));
        propsMapping.put(Fruit.VERSION, getLongFieldType());
        mapping.put(PROPERTIES, propsMapping);
        request.mapping(mapping);

        return createIndex(request);
    }

}
