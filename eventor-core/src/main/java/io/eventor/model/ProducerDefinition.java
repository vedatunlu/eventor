package io.eventor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProducerDefinition {
    @JsonProperty("type")
    private String type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("dto")
    private String dto;

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("factoryBean")
    private String factoryBean;

    public ProducerDefinition() {}

    public ProducerDefinition(String type, String name, String dto, String topic, String factoryBean) {
        this.type = type;
        this.name = name;
        this.dto = dto;
        this.topic = topic;
        this.factoryBean = factoryBean;
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

    public String getDto() {
        return dto;
    }

    public void setDto(String dto) {
        this.dto = dto;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getFactoryBean() {
        return factoryBean;
    }

    public void setFactoryBean(String factoryBean) {
        this.factoryBean = factoryBean;
    }
}
