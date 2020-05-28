### Build and Play
- You have to start elasticsearch:

```
docker run -it --rm=true --name elasticsearch_quarkus_test \
    -p 11027:9200 -p 11028:9300 \
    -e "discovery.type=single-node" \
    docker.elastic.co/elasticsearch/elasticsearch:7.4.0
```
- Open the file `src/test/resources/shell/docker_build_and_run.sh` and adjust the path for your `JAVA_HOME` variable
- Open the terminal window in your project directory and start the native build:
```
bash src/test/resources/shell/docker_build_and_run.sh 
```
- This should build and start your native build through docker
- Now you can run the tests against native build:
```
mvn package -D%test.service.http.host=http://localhost:11025
```
