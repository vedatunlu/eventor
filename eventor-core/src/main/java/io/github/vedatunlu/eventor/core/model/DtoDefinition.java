package io.github.vedatunlu.eventor.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class DtoDefinition {
    @JsonProperty("type")
    private String type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("fields")
    private Map<String, String> fields;

    public DtoDefinition() {}

    public DtoDefinition(String type, String name, Map<String, String> fields) {
        this.type = type;
        this.name = name;
        this.fields = fields;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
