package io.eventor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ConsumerDefinition {
    @JsonProperty("type")
    private String type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("methods")
    private List<ConsumerMethod> methods;

    public ConsumerDefinition() {}

    public ConsumerDefinition(String type, String name, List<ConsumerMethod> methods) {
        this.type = type;
        this.name = name;
        this.methods = methods;
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

    public List<ConsumerMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<ConsumerMethod> methods) {
        this.methods = methods;
    }

    public static class ConsumerMethod {
        @JsonProperty("methodName")
        private String methodName;

        @JsonProperty("dto")
        private String dto;

        @JsonProperty("topic")
        private String topic;

        @JsonProperty("groupId")
        private String groupId;

        @JsonProperty("listenerFactory")
        private String listenerFactory;

        @JsonProperty("dependencies")
        private List<Dependency> dependencies;

        public ConsumerMethod() {}

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
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

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getListenerFactory() {
            return listenerFactory;
        }

        public void setListenerFactory(String listenerFactory) {
            this.listenerFactory = listenerFactory;
        }

        public List<Dependency> getDependencies() {
            return dependencies;
        }

        public void setDependencies(List<Dependency> dependencies) {
            this.dependencies = dependencies;
        }
    }

    public static class Dependency {
        @JsonProperty("beanName")
        private String beanName;

        @JsonProperty("type")
        private String type;

        @JsonProperty("methodCalls")
        private List<String> methodCalls;

        public Dependency() {}

        public String getBeanName() {
            return beanName;
        }

        public void setBeanName(String beanName) {
            this.beanName = beanName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getMethodCalls() {
            return methodCalls;
        }

        public void setMethodCalls(List<String> methodCalls) {
            this.methodCalls = methodCalls;
        }
    }
}
