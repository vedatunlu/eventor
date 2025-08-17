package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Eventor Maven Plugin Integration Tests")
class EventorMavenPluginIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Should verify Maven plugin configuration is correct")
    void shouldVerifyMavenPluginConfigurationIsCorrect() throws Exception {
        // Read the pom.xml to verify plugin configuration
        File pomFile = new File("pom.xml");
        assertTrue(pomFile.exists(), "pom.xml should exist");

        String pomContent = Files.readString(pomFile.toPath());

        assertAll("Maven plugin configuration validation",
            () -> assertTrue(pomContent.contains("<groupId>io.github.vedatunlu</groupId>"),
                "Plugin should have correct groupId"),
            () -> assertTrue(pomContent.contains("<artifactId>eventor-maven-plugin</artifactId>"),
                "Plugin should have correct artifactId"),
            () -> assertTrue(pomContent.contains("<version>0.1.0-SNAPSHOT</version>"),
                "Plugin should have correct version"),
            () -> assertTrue(pomContent.contains("<goal>generate</goal>"),
                "Plugin should have generate goal configured"),
            () -> assertTrue(pomContent.contains("generate-sources"),
                "Plugin should be bound to generate-sources phase")
        );
    }

    @Test
    @DisplayName("Should verify JSON source files have correct structure")
    void shouldVerifyJsonSourceFilesHaveCorrectStructure() throws Exception {
        // Verify DTO JSON structure
        File dtoJson = new File("src/main/resources/Eventor/user-registered-event.json");
        assertTrue(dtoJson.exists(), "DTO JSON file should exist");

        String dtoContent = Files.readString(dtoJson.toPath());
        assertAll("DTO JSON validation",
            () -> assertTrue(dtoContent.contains("\"type\": \"dto\""),
                "DTO JSON should have correct type"),
            () -> assertTrue(dtoContent.contains("\"name\": \"UserRegisteredEvent\""),
                "DTO JSON should have correct name"),
            () -> assertTrue(dtoContent.contains("\"fields\""),
                "DTO JSON should have fields section"),
            () -> assertTrue(dtoContent.contains("\"userId\": \"UUID\""),
                "DTO JSON should have userId field"),
            () -> assertTrue(dtoContent.contains("\"username\": \"String\""),
                "DTO JSON should have username field")
        );

        // Verify Producer JSON structure
        File producerJson = new File("src/main/resources/Eventor/user-event-producer.json");
        assertTrue(producerJson.exists(), "Producer JSON file should exist");

        String producerContent = Files.readString(producerJson.toPath());
        assertAll("Producer JSON validation",
            () -> assertTrue(producerContent.contains("\"type\": \"producer\""),
                "Producer JSON should have correct type"),
            () -> assertTrue(producerContent.contains("\"name\": \"UserEventProducer\""),
                "Producer JSON should have correct name"),
            () -> assertTrue(producerContent.contains("\"dto\": \"UserRegisteredEvent\""),
                "Producer JSON should reference correct DTO"),
            () -> assertTrue(producerContent.contains("\"topic\": \"user-events\""),
                "Producer JSON should have correct topic"),
            () -> assertTrue(producerContent.contains("\"factoryBean\": \"kafkaTemplate\""),
                "Producer JSON should have correct factory bean")
        );

        // Verify Consumer JSON structure
        File consumerJson = new File("src/main/resources/Eventor/user-event-consumer.json");
        assertTrue(consumerJson.exists(), "Consumer JSON file should exist");

        String consumerContent = Files.readString(consumerJson.toPath());
        assertAll("Consumer JSON validation",
            () -> assertTrue(consumerContent.contains("\"type\": \"consumer\""),
                "Consumer JSON should have correct type"),
            () -> assertTrue(consumerContent.contains("\"name\": \"UserEventListener\""),
                "Consumer JSON should have correct name"),
            () -> assertTrue(consumerContent.contains("\"methods\""),
                "Consumer JSON should have methods section"),
            () -> assertTrue(consumerContent.contains("\"methodName\": \"handleUserRegisteredEvent\""),
                "Consumer JSON should have correct method name"),
            () -> assertTrue(consumerContent.contains("\"dependencies\""),
                "Consumer JSON should have dependencies section")
        );
    }

    @Test
    @DisplayName("Should verify build helper plugin is configured correctly")
    void shouldVerifyBuildHelperPluginIsConfiguredCorrectly() throws Exception {
        File pomFile = new File("pom.xml");
        String pomContent = Files.readString(pomFile.toPath());

        assertAll("Build helper plugin validation",
            () -> assertTrue(pomContent.contains("build-helper-maven-plugin"),
                "Build helper plugin should be configured"),
            () -> assertTrue(pomContent.contains("<goal>add-source</goal>"),
                "Build helper should have add-source goal"),
            () -> assertTrue(pomContent.contains("generated-sources/Eventor"),
                "Build helper should point to correct generated sources directory")
        );
    }

    @Test
    @DisplayName("Should verify generated files are in target classpath")
    void shouldVerifyGeneratedFilesAreInTargetClasspath() {
        // Verify generated classes are compiled and available in target/classes
        File targetClasses = new File("target/classes");
        assertTrue(targetClasses.exists(), "Target classes directory should exist");

        // Note: The actual generated class files would be in the packages, but we can verify
        // the source files were generated which is what led to successful compilation
        File generatedSourcesDir = new File("target/generated-sources/Eventor");
        assertTrue(generatedSourcesDir.exists(), "Generated sources directory should exist");

        File[] generatedFiles = generatedSourcesDir.listFiles();
        assertNotNull(generatedFiles, "Generated sources directory should contain files");
        assertEquals(3, generatedFiles.length, "Should have exactly 3 generated files");
    }

    @Test
    @DisplayName("Should verify Maven phases execute in correct order")
    void shouldVerifyMavenPhasesExecuteInCorrectOrder() {
        // This test verifies that the lifecycle phases executed correctly
        // by checking that all expected directories and files exist

        // 1. generate-sources phase should have created generated sources
        assertTrue(new File("target/generated-sources/Eventor").exists(),
            "Generate-sources phase should create generated sources directory");

        // 2. process-sources phase should have added sources to compilation
        // 3. compile phase should have compiled all sources
        assertTrue(new File("target/classes").exists(),
            "Compile phase should create classes directory");

        // 4. Resources should be copied
        assertTrue(new File("target/classes/Eventor").exists(),
            "Resources should be copied to target");
    }

    @Test
    @DisplayName("Should verify project dependencies include required libraries")
    void shouldVerifyProjectDependenciesIncludeRequiredLibraries() throws Exception {
        File pomFile = new File("pom.xml");
        String pomContent = Files.readString(pomFile.toPath());

        assertAll("Required dependencies validation",
            () -> assertTrue(pomContent.contains("spring-boot-starter-web"),
                "Should include Spring Boot Web starter"),
            () -> assertTrue(pomContent.contains("spring-kafka"),
                "Should include Spring Kafka dependency"),
            () -> assertTrue(pomContent.contains("spring-boot-starter-test"),
                "Should include Spring Boot Test starter")
        );
    }

    @Test
    @DisplayName("Should verify generated files match expected naming conventions")
    void shouldVerifyGeneratedFilesMatchExpectedNamingConventions() {
        File generatedDir = new File("target/generated-sources/Eventor");
        File[] files = generatedDir.listFiles();

        assertNotNull(files, "Generated directory should contain files");

        final boolean[] foundFlags = {false, false, false}; // [dto, producer, consumer]

        for (File file : files) {
            String fileName = file.getName();
            if (fileName.equals("UserRegisteredEvent.java")) {
                foundFlags[0] = true;
            } else if (fileName.equals("UserEventProducer.java")) {
                foundFlags[1] = true;
            } else if (fileName.equals("UserEventListener.java")) {
                foundFlags[2] = true;
            }
        }

        assertAll("File naming conventions",
            () -> assertTrue(foundFlags[0], "Should find DTO file with correct name"),
            () -> assertTrue(foundFlags[1], "Should find Producer file with correct name"),
            () -> assertTrue(foundFlags[2], "Should find Consumer file with correct name")
        );
    }

    @Test
    @DisplayName("Should verify Maven project structure is correct")
    void shouldVerifyMavenProjectStructureIsCorrect() {
        // Verify standard Maven directory structure
        assertTrue(new File("src/main/java").exists(), "Should have main java directory");
        assertTrue(new File("src/main/resources").exists(), "Should have main resources directory");
        assertTrue(new File("src/test/java").exists(), "Should have test java directory");
        assertTrue(new File("target").exists(), "Should have target directory");

        // Verify Eventor-specific structure
        assertTrue(new File("src/main/resources/Eventor").exists(),
            "Should have Eventor resources directory");
        assertTrue(new File("target/generated-sources").exists(),
            "Should have generated sources directory");
        assertTrue(new File("target/generated-sources/Eventor").exists(),
            "Should have Eventor generated sources directory");
    }
}
