### Dependencies
You have to start elasticsearch:

```
docker run -it --rm=true --name elasticsearch_quarkus_test \
    -p 11027:9200 -p 11028:9300 \
    -e "discovery.type=single-node" \
    docker.elastic.co/elasticsearch/elasticsearch:7.4.0
```

