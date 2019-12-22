package org.otaibe.quarkus.elasticsearch.example.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Fruit {

    public static final String ID = "id";
    public static final String EXT_REF_ID = "ext_ref_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String VERSION = "version";

    @JsonProperty(ID)
    public String id;
    @JsonProperty(EXT_REF_ID)
    public String extRefId;
    @JsonProperty(NAME)
    public String name;
    @JsonProperty(DESCRIPTION)
    public String description;
    @JsonProperty(VERSION)
    public Long version;
}