package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "logging.level.io.eventor=DEBUG"
})
@DisplayName("Eventor Generated Files Integration Tests")
class EventorGeneratedFilesTest {

    private static final String GENERATED_SOURCES_PATH = "target/generated-sources/Eventor";
    private Path generatedSourcesDir;

    @BeforeEach
    void setUp() {
        generatedSourcesDir = Paths.get(GENERATED_SOURCES_PATH);
    }

    @Test
    @DisplayName("Should verify generated sources directory exists")
    void shouldVerifyGeneratedSourcesDirectoryExists() {
        assertTrue(Files.exists(generatedSourcesDir),
            "Generated sources directory should exist at: " + generatedSourcesDir);
        assertTrue(Files.isDirectory(generatedSourcesDir),
            "Generated sources path should be a directory");
    }

    @Test
    @DisplayName("Should verify UserRegisteredEvent DTO is generated")
    void shouldVerifyUserRegisteredEventDtoIsGenerated() {
        Path dtoFile = generatedSourcesDir.resolve("UserRegisteredEvent.java");

        assertTrue(Files.exists(dtoFile),
            "UserRegisteredEvent.java should be generated");
        assertTrue(Files.isRegularFile(dtoFile),
            "UserRegisteredEvent.java should be a regular file");
        assertTrue(dtoFile.toFile().length() > 0,
            "UserRegisteredEvent.java should not be empty");
    }

    @Test
    @DisplayName("Should verify UserEventProducer is generated")
    void shouldVerifyUserEventProducerIsGenerated() {
        Path producerFile = generatedSourcesDir.resolve("UserEventProducer.java");

        assertTrue(Files.exists(producerFile),
            "UserEventProducer.java should be generated");
        assertTrue(Files.isRegularFile(producerFile),
            "UserEventProducer.java should be a regular file");
        assertTrue(producerFile.toFile().length() > 0,
            "UserEventProducer.java should not be empty");
    }

    @Test
    @DisplayName("Should verify UserEventListener consumer is generated")
    void shouldVerifyUserEventListenerConsumerIsGenerated() {
        Path consumerFile = generatedSourcesDir.resolve("UserEventListener.java");

        assertTrue(Files.exists(consumerFile),
            "UserEventListener.java should be generated");
        assertTrue(Files.isRegularFile(consumerFile),
            "UserEventListener.java should be a regular file");
        assertTrue(consumerFile.toFile().length() > 0,
            "UserEventListener.java should not be empty");
    }

    @Test
    @DisplayName("Should verify all three expected files are generated")
    void shouldVerifyAllThreeExpectedFilesAreGenerated() {
        String[] expectedFiles = {
            "UserRegisteredEvent.java",
            "UserEventProducer.java",
            "UserEventListener.java"
        };

        for (String expectedFile : expectedFiles) {
            Path filePath = generatedSourcesDir.resolve(expectedFile);
            assertTrue(Files.exists(filePath),
                String.format("Expected file %s should exist", expectedFile));
        }
    }

    @Test
    @DisplayName("Should verify generated files have correct Java syntax structure")
    void shouldVerifyGeneratedFilesHaveCorrectJavaSyntaxStructure() throws Exception {
        // Check DTO file content structure
        Path dtoFile = generatedSourcesDir.resolve("UserRegisteredEvent.java");
        String dtoContent = Files.readString(dtoFile);

        assertAll("DTO content validation",
            () -> assertTrue(dtoContent.contains("package com.example.dto;"),
                "DTO should have correct package declaration"),
            () -> assertTrue(dtoContent.contains("public class UserRegisteredEvent"),
                "DTO should have correct class declaration"),
            () -> assertTrue(dtoContent.contains("private UUID userId;"),
                "DTO should contain userId field"),
            () -> assertTrue(dtoContent.contains("private String username;"),
                "DTO should contain username field"),
            () -> assertTrue(dtoContent.contains("private String email;"),
                "DTO should contain email field"),
            () -> assertTrue(dtoContent.contains("public UUID getUserId()"),
                "DTO should have getUserId getter"),
            () -> assertTrue(dtoContent.contains("public void setUserId("),
                "DTO should have setUserId setter")
        );

        // Check Producer file content structure
        Path producerFile = generatedSourcesDir.resolve("UserEventProducer.java");
        String producerContent = Files.readString(producerFile);

        assertAll("Producer content validation",
            () -> assertTrue(producerContent.contains("package com.example.producer;"),
                "Producer should have correct package declaration"),
            () -> assertTrue(producerContent.contains("@Component"),
                "Producer should have @Component annotation"),
            () -> assertTrue(producerContent.contains("public class UserEventProducer"),
                "Producer should have correct class declaration"),
            () -> assertTrue(producerContent.contains("KafkaTemplate"),
                "Producer should use KafkaTemplate"),
            () -> assertTrue(producerContent.contains("@Autowired"),
                "Producer should have @Autowired annotation")
        );

        // Check Consumer file content structure
        Path consumerFile = generatedSourcesDir.resolve("UserEventListener.java");
        String consumerContent = Files.readString(consumerFile);

        assertAll("Consumer content validation",
            () -> assertTrue(consumerContent.contains("package com.example.consumer;"),
                "Consumer should have correct package declaration"),
            () -> assertTrue(consumerContent.contains("@Component"),
                "Consumer should have @Component annotation"),
            () -> assertTrue(consumerContent.contains("@KafkaListener"),
                "Consumer should have @KafkaListener annotation"),
            () -> assertTrue(consumerContent.contains("public class UserEventListener"),
                "Consumer should have correct class declaration"),
            () -> assertTrue(consumerContent.contains("handleUserRegisteredEvent"),
                "Consumer should have the expected method name")
        );
    }

    @Test
    @DisplayName("Should verify generated files compile successfully")
    void shouldVerifyGeneratedFilesCompileSuccessfully() {
        // This test verifies that the build was successful, meaning generated files compiled
        // The fact that we can run this test means compilation was successful
        File classesDir = new File("target/classes");
        assertTrue(classesDir.exists(), "Classes directory should exist after successful compilation");

        // Check that our hand-written classes are compiled
        assertTrue(new File("target/classes/com/example/SpringBootEventorDemoApplication.class").exists(),
            "Main application class should be compiled");
        assertTrue(new File("target/classes/com/example/service/UserService.class").exists(),
            "UserService should be compiled");
        assertTrue(new File("target/classes/com/example/service/NotificationService.class").exists(),
            "NotificationService should be compiled");
    }

    @Test
    @DisplayName("Should verify JSON source files exist in resources")
    void shouldVerifyJsonSourceFilesExistInResources() {
        String[] expectedJsonFiles = {
            "src/main/resources/Eventor/user-registered-event.json",
            "src/main/resources/Eventor/user-event-producer.json",
            "src/main/resources/Eventor/user-event-consumer.json"
        };

        for (String jsonFile : expectedJsonFiles) {
            Path filePath = Paths.get(jsonFile);
            assertTrue(Files.exists(filePath),
                String.format("JSON source file %s should exist", jsonFile));
        }
    }

    @Test
    @DisplayName("Should verify generated file timestamps are recent")
    void shouldVerifyGeneratedFileTimestampsAreRecent() throws Exception {
        long currentTime = System.currentTimeMillis();
        long oneHourAgo = currentTime - (60 * 60 * 1000); // 1 hour ago

        String[] generatedFiles = {
            "UserRegisteredEvent.java",
            "UserEventProducer.java",
            "UserEventListener.java"
        };

        for (String fileName : generatedFiles) {
            Path filePath = generatedSourcesDir.resolve(fileName);
            long lastModified = Files.getLastModifiedTime(filePath).toMillis();

            assertTrue(lastModified > oneHourAgo,
                String.format("Generated file %s should have been modified recently. " +
                    "Last modified: %d, One hour ago: %d", fileName, lastModified, oneHourAgo));
        }
    }
}
