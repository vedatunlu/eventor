package com.example;

import com.example.dto.UserRegisteredEvent;
import com.example.producer.UserEventProducer;
import com.example.consumer.UserEventListener;
import com.example.service.UserService;
import com.example.service.NotificationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.UUID;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=localhost:9092",
    "spring.kafka.consumer.auto-offset-reset=earliest",
    "spring.kafka.consumer.group-id=test-group"
})
@DisplayName("Eventor Generated Classes Functionality Tests")
class EventorGeneratedClassesFunctionalityTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Should verify generated DTO can be instantiated and used")
    void shouldVerifyGeneratedDtoCanBeInstantiatedAndUsed() {
        // Test default constructor
        UserRegisteredEvent event1 = new UserRegisteredEvent();
        assertNotNull(event1, "Default constructor should work");

        // Test parameterized constructor
        UUID userId = UUID.randomUUID();
        String username = "testuser";
        String email = "test@example.com";
        LocalDateTime registrationDate = LocalDateTime.now();
        Boolean isActive = true;

        UserRegisteredEvent event2 = new UserRegisteredEvent(userId, username, email, registrationDate, isActive);

        assertAll("Parameterized constructor validation",
            () -> assertEquals(userId, event2.getUserId(), "UserId should be set correctly"),
            () -> assertEquals(username, event2.getUsername(), "Username should be set correctly"),
            () -> assertEquals(email, event2.getEmail(), "Email should be set correctly"),
            () -> assertEquals(registrationDate, event2.getRegistrationDate(), "Registration date should be set correctly"),
            () -> assertEquals(isActive, event2.getIsActive(), "IsActive should be set correctly")
        );
    }

    @Test
    @DisplayName("Should verify generated DTO getters and setters work correctly")
    void shouldVerifyGeneratedDtoGettersAndSettersWorkCorrectly() {
        UserRegisteredEvent event = new UserRegisteredEvent();

        UUID userId = UUID.randomUUID();
        String username = "newuser";
        String email = "new@example.com";
        LocalDateTime registrationDate = LocalDateTime.now();
        Boolean isActive = false;

        // Test setters
        event.setUserId(userId);
        event.setUsername(username);
        event.setEmail(email);
        event.setRegistrationDate(registrationDate);
        event.setIsActive(isActive);

        // Test getters
        assertAll("Getters and setters validation",
            () -> assertEquals(userId, event.getUserId(), "getUserId should return correct value"),
            () -> assertEquals(username, event.getUsername(), "getUsername should return correct value"),
            () -> assertEquals(email, event.getEmail(), "getEmail should return correct value"),
            () -> assertEquals(registrationDate, event.getRegistrationDate(), "getRegistrationDate should return correct value"),
            () -> assertEquals(isActive, event.getIsActive(), "getIsActive should return correct value")
        );
    }

    @Test
    @DisplayName("Should verify generated DTO equals and hashCode work correctly")
    void shouldVerifyGeneratedDtoEqualsAndHashCodeWorkCorrectly() {
        UUID userId = UUID.randomUUID();
        String username = "testuser";
        String email = "test@example.com";
        LocalDateTime registrationDate = LocalDateTime.now();
        Boolean isActive = true;

        UserRegisteredEvent event1 = new UserRegisteredEvent(userId, username, email, registrationDate, isActive);
        UserRegisteredEvent event2 = new UserRegisteredEvent(userId, username, email, registrationDate, isActive);
        UserRegisteredEvent event3 = new UserRegisteredEvent(UUID.randomUUID(), "other", "other@example.com", registrationDate, false);

        assertAll("Equals and hashCode validation",
            () -> assertEquals(event1, event2, "Events with same data should be equal"),
            () -> assertNotEquals(event1, event3, "Events with different data should not be equal"),
            () -> assertEquals(event1.hashCode(), event2.hashCode(), "Equal events should have same hash code"),
            () -> assertNotEquals(event1.hashCode(), event3.hashCode(), "Different events should have different hash codes")
        );
    }

    @Test
    @DisplayName("Should verify generated DTO toString works correctly")
    void shouldVerifyGeneratedDtoToStringWorksCorrectly() {
        UUID userId = UUID.randomUUID();
        UserRegisteredEvent event = new UserRegisteredEvent(userId, "testuser", "test@example.com", LocalDateTime.now(), true);

        String toString = event.toString();

        assertAll("ToString validation",
            () -> assertNotNull(toString, "toString should not be null"),
            () -> assertTrue(toString.contains("UserRegisteredEvent"), "toString should contain class name"),
            () -> assertTrue(toString.contains(userId.toString()), "toString should contain userId"),
            () -> assertTrue(toString.contains("testuser"), "toString should contain username"),
            () -> assertTrue(toString.contains("test@example.com"), "toString should contain email")
        );
    }

    @Test
    @DisplayName("Should verify generated Producer is Spring component")
    void shouldVerifyGeneratedProducerIsSpringComponent() {
        assertTrue(applicationContext.containsBean("userEventProducer"),
            "UserEventProducer should be registered as Spring bean");

        UserEventProducer producer = applicationContext.getBean(UserEventProducer.class);
        assertNotNull(producer, "UserEventProducer bean should be available");
    }

    @Test
    @DisplayName("Should verify generated Consumer is Spring component")
    void shouldVerifyGeneratedConsumerIsSpringComponent() {
        assertTrue(applicationContext.containsBean("userEventListener"),
            "UserEventListener should be registered as Spring bean");

        UserEventListener listener = applicationContext.getBean(UserEventListener.class);
        assertNotNull(listener, "UserEventListener bean should be available");
    }

    @Test
    @DisplayName("Should verify generated Producer has required methods")
    void shouldVerifyGeneratedProducerHasRequiredMethods() throws Exception {
        Class<UserEventProducer> producerClass = UserEventProducer.class;

        // Check for send method with event parameter
        Method sendEventMethod = producerClass.getMethod("sendUserRegisteredEvent", UserRegisteredEvent.class);
        assertNotNull(sendEventMethod, "Producer should have sendUserRegisteredEvent method");

        // Check for send method with key and event parameters
        Method sendEventWithKeyMethod = producerClass.getMethod("sendUserRegisteredEvent", String.class, UserRegisteredEvent.class);
        assertNotNull(sendEventWithKeyMethod, "Producer should have sendUserRegisteredEvent method with key parameter");
    }

    @Test
    @DisplayName("Should verify generated Consumer has required methods")
    void shouldVerifyGeneratedConsumerHasRequiredMethods() throws Exception {
        Class<UserEventListener> consumerClass = UserEventListener.class;

        // Check for handler method
        Method handlerMethod = consumerClass.getMethod("handleUserRegisteredEvent", UserRegisteredEvent.class);
        assertNotNull(handlerMethod, "Consumer should have handleUserRegisteredEvent method");
    }

    @Test
    @DisplayName("Should verify generated Consumer has proper dependencies injected")
    void shouldVerifyGeneratedConsumerHasProperDependenciesInjected() {
        UserEventListener listener = applicationContext.getBean(UserEventListener.class);

        // Use reflection to verify dependencies are injected
        try {
            java.lang.reflect.Field userServiceField = UserEventListener.class.getDeclaredField("userService");
            userServiceField.setAccessible(true);
            Object userService = userServiceField.get(listener);
            assertNotNull(userService, "UserService should be injected");
            assertTrue(userService instanceof UserService, "Injected service should be UserService instance");

            java.lang.reflect.Field notificationServiceField = UserEventListener.class.getDeclaredField("notificationService");
            notificationServiceField.setAccessible(true);
            Object notificationService = notificationServiceField.get(listener);
            assertNotNull(notificationService, "NotificationService should be injected");
            assertTrue(notificationService instanceof NotificationService, "Injected service should be NotificationService instance");
        } catch (Exception e) {
            fail("Should be able to access dependency fields: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should verify service dependencies are available as Spring beans")
    void shouldVerifyServiceDependenciesAreAvailableAsSpringBeans() {
        assertTrue(applicationContext.containsBean("userService"),
            "UserService should be registered as Spring bean");
        assertTrue(applicationContext.containsBean("notificationService"),
            "NotificationService should be registered as Spring bean");

        UserService userService = applicationContext.getBean(UserService.class);
        NotificationService notificationService = applicationContext.getBean(NotificationService.class);

        assertNotNull(userService, "UserService bean should be available");
        assertNotNull(notificationService, "NotificationService bean should be available");
    }

    @Test
    @DisplayName("Should verify generated classes have proper package structure")
    void shouldVerifyGeneratedClassesHaveProperPackageStructure() {
        assertEquals("com.example.dto", UserRegisteredEvent.class.getPackage().getName(),
            "DTO should be in com.example.dto package");
        assertEquals("com.example.producer", UserEventProducer.class.getPackage().getName(),
            "Producer should be in com.example.producer package");
        assertEquals("com.example.consumer", UserEventListener.class.getPackage().getName(),
            "Consumer should be in com.example.consumer package");
    }
}
