# Eventor - Spring Event Generator

Eventor is a powerful Maven package that generates Spring Event DTOs, Producers, and Consumers from JSON definitions. It works both as a CLI tool and as a Maven plugin for seamless integration into **Spring Boot 3+** projects.

## ✨ Spring Boot 3+ Compatibility

This package is fully compatible with Spring Boot 3.0+ and includes:
- Support for Jakarta EE (jakarta.* packages)
- Spring Framework 6.0+ compatibility
- Java 17+ support
- Modern Spring Kafka integration

## Features

- **JSON-based Configuration**: Define DTOs, Producers, and Consumers using simple JSON files
- **Dual Interface**: Use as CLI tool or Maven plugin
- **Spring Boot 3+ Integration**: Generates ready-to-use Spring components
- **Kafka Support**: Built-in Kafka producer and consumer generation
- **Dependency Injection**: Automatic wiring of service dependencies

## Maven Coordinates

```xml
<groupId>io.eventor</groupId>
<artifactId>eventor-maven-plugin</artifactId>
<version>0.1.0-SNAPSHOT</version>
```

## 🚀 Quick Start for Spring Boot 3+ Projects

### Step 1: Add the Plugin to Your Spring Boot Project

Add this to your `pom.xml`:

```xml
<build>
    <plugins>
        <!-- Eventor Plugin -->
        <plugin>
            <groupId>io.eventor</groupId>
            <artifactId>eventor-maven-plugin</artifactId>
            <version>0.1.0-SNAPSHOT</version>
            <configuration>
                <jsonDir>${project.basedir}/src/main/resources/eventor</jsonDir>
                <outputDir>${project.build.directory}/generated-sources/eventor</outputDir>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>

        <!-- Add Generated Sources to Build Path -->
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>build-helper-maven-plugin</artifactId>
            <version>3.4.0</version>
            <executions>
                <execution>
                    <id>add-source</id>
                    <phase>generate-sources</phase>
                    <goals>
                        <goal>add-source</goal>
                    </goals>
                    <configuration>
                        <sources>
                            <source>${project.build.directory}/generated-sources/eventor</source>
                        </sources>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Step 2: Ensure Spring Boot 3+ Dependencies

Make sure your project uses Spring Boot 3+ parent:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version> <!-- or any 3.x version -->
    <relativePath/>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    <!-- Other Spring Boot 3+ dependencies -->
</dependencies>
```

### Step 3: Create JSON Definitions

Create the directory `src/main/resources/eventor/` and add your JSON files:

**DTO Definition (`user-event.json`):**
```json
{
  "type": "dto",
  "name": "UserCreatedEvent",
  "fields": {
    "userId": "UUID",
    "username": "String",
    "email": "String",
    "createdAt": "LocalDateTime"
  }
}
```

**Producer Definition (`user-producer.json`):**
```json
{
  "type": "producer", 
  "name": "UserEventProducer",
  "dto": "UserCreatedEvent",
  "topic": "user-events",
  "factoryBean": "kafkaTemplate"
}
```

**Consumer Definition (`user-consumer.json`):**
```json
{
  "type": "consumer",
  "name": "UserEventListener", 
  "methods": [
    {
      "methodName": "handleUserCreatedEvent",
      "dto": "UserCreatedEvent", 
      "topic": "user-events",
      "groupId": "user-service-group",
      "listenerFactory": "kafkaListenerContainerFactory",
      "dependencies": [
        {
          "beanName": "userService",
          "type": "com.example.service.UserService", 
          "methodCalls": ["processNewUser"]
        }
      ]
    }
  ]
}
```

### Step 4: Build Your Project

```bash
mvn clean compile
```

The plugin will automatically:
1. Generate DTO, Producer, and Consumer classes
2. Add them to your compilation path
3. Make them available as Spring components

## 📋 Requirements for Spring Boot 3+ Projects

- **Java 17+** (Spring Boot 3+ requirement)
- **Spring Boot 3.0+**
- **Maven 3.6.3+**

## Usage

### As Maven Plugin

Run the generation:
```bash
mvn eventor:generate
```

### As CLI Tool

Download the CLI JAR and run:
```bash
java -jar eventor-0.1.0-SNAPSHOT-cli.jar --jsonDir ./json-definitions --outputDir ./generated-sources
```

## JSON Definition Formats

### DTO Definition

```json
{
  "type": "dto",
  "name": "OrderCreatedEvent",
  "fields": {
    "orderId": "UUID",
    "userId": "UUID",
    "amount": "BigDecimal",
    "orderDate": "LocalDateTime",
    "items": "List<String>"
  }
}
```

### Producer Definition

```json
{
  "type": "producer",
  "name": "OrderEventProducer",
  "dto": "OrderCreatedEvent",
  "topic": "orders-topic",
  "factoryBean": "kafkaTemplate"
}
```

### Consumer Definition

```json
{
  "type": "consumer",
  "name": "OrderEventListener",
  "methods": [
    {
      "methodName": "handleOrderCreatedEvent",
      "dto": "OrderCreatedEvent",
      "topic": "orders-topic",
      "groupId": "order-service-group",
      "listenerFactory": "kafkaListenerFactory",
      "dependencies": [
        {
          "beanName": "orderService",
          "type": "com.example.service.OrderService",
          "methodCalls": ["processOrder"]
        }
      ]
    }
  ]
}
```

## Generated Code Examples

### Generated DTO (Spring Boot 3+ Compatible)
```java
package com.example.dto;

import java.util.UUID;
import java.time.LocalDateTime;

/**
 * Generated DTO class for UserCreatedEvent
 * Generated by Eventry Spring Event Generator
 * Compatible with Spring Boot 3+
 */
public class UserCreatedEvent {
    private UUID userId;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    
    // Constructors, getters, setters, equals, hashCode, toString
    // ...
}
```

### Generated Producer (Spring Boot 3+ Compatible)
```java
package com.example.producer;

import com.example.dto.UserCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Generated Producer class for UserEventProducer
 * Compatible with Spring Boot 3+ and Spring Kafka
 */
@Component
public class UserEventProducer {
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;
    
    @Autowired
    public UserEventProducer(KafkaTemplate<String, UserCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void sendUserCreatedEvent(UserCreatedEvent event) {
        kafkaTemplate.send("user-events", event);
    }
}
```

### Generated Consumer (Spring Boot 3+ Compatible)
```java
package com.example.consumer;

import com.example.dto.UserCreatedEvent;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Generated Consumer class for UserEventListener
 * Compatible with Spring Boot 3+ and Spring Kafka
 */
@Component
public class UserEventListener {
    private final UserService userService;
    
    @Autowired
    public UserEventListener(UserService userService) {
        this.userService = userService;
    }
    
    @KafkaListener(
        topics = "user-events",
        groupId = "user-service-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        userService.processNewUser(event);
    }
}
```

## 🔧 Spring Boot 3+ Configuration

### Kafka Configuration Example

```java
@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "default-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
```

## Building the Project

```bash
# Build the project
mvn clean compile

# Package (creates both Maven plugin and CLI JAR)
mvn clean package

# Install to local repository
mvn clean install
```

## Configuration Parameters

### Maven Plugin Parameters
- `jsonDir`: Directory containing JSON definition files (default: `${project.basedir}/src/main/resources/eventor`)
- `outputDir`: Output directory for generated classes (default: `${project.build.directory}/generated-sources/eventor`)

### CLI Parameters
- `--jsonDir, -j`: Directory containing JSON definition files (required)
- `--outputDir, -o`: Output directory for generated classes (required)

## Supported Field Types

- `String`
- `UUID`
- `BigDecimal`
- `LocalDateTime`
- `Integer`, `Long`, `Double`
- `Boolean`
- `List<T>`, `Map<K,V>`
- Custom types (fully qualified class names)

## Integration with Spring Boot

1. Add the generated sources to your compilation path
2. Ensure Kafka dependencies are in your project
3. Configure Kafka properties in `application.yml`
4. The generated components will be automatically detected by Spring's component scanning

## License

This project is licensed under the MIT License.
