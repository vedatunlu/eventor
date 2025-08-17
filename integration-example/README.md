# Example Spring Boot Integration

This directory contains an example of how to integrate Eventor into a Spring Boot project.

## Project Structure

```
spring-boot-example/
├── pom.xml                          # Spring Boot project with Eventor plugin
├── src/main/resources/Eventor/      # JSON definitions directory
│   ├── user-event.json             # DTO definition
│   ├── user-producer.json          # Producer definition
│   └── user-consumer.json          # Consumer definition
└── target/generated-sources/Eventor/ # Generated classes (after build)
    ├── UserEvent.java
    ├── UserEventProducer.java
    └── UserEventListener.java
```

## Usage Steps

1. **Add Eventor plugin to your pom.xml**:
```xml
<plugin>
    <groupId>io.eventor</groupId>
    <artifactId>eventor-maven-plugin</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

2. **Create JSON definitions** in `src/main/resources/Eventor/`

3. **Run Maven build**:
```bash
mvn clean compile
```

4. **Use generated classes** in your Spring Boot application

## Generated Sources Integration

Add the generated sources to your build path by adding this to your pom.xml:

```xml
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
                    <source>${project.build.directory}/generated-sources/Eventor</source>
                </sources>
            </configuration>
        </execution>
    </executions>
</plugin>
```
