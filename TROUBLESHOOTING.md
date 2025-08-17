# Eventor Troubleshooting Guide

## 🚨 Common Issues and Solutions

### 1. **Build Failures**

#### Error: "Java 17+ required"
```
[ERROR] Source option 11 is no longer supported. Use 17 or later.
```
**Solution**: Update your Java version:
```bash
# Check current version
java -version

# Install Java 17+ (using SDKMAN example)
sdk install java 17.0.8-tem
sdk use java 17.0.8-tem
```

#### Error: "Plugin not found"
```
[ERROR] Plugin io.eventor:eventor:0.1.0-SNAPSHOT not found
```
**Solutions**:
1. Install Eventor to local repository:
```bash
git clone <repo>
cd eventor
./quick-setup.sh
```

2. Or add to settings.xml if using custom repository

### 2. **JSON Definition Errors**

#### Error: "Unknown type in JSON"
```
❌ Failed to process user-event.json:
   Error: Unknown type: dta
```
**Solution**: Fix the `type` field - must be exactly: `dto`, `producer`, or `consumer`

#### Error: "Invalid field type"
```
com.fasterxml.jackson.databind.JsonMappingException
```
**Solution**: Use only supported field types:
- `String`, `UUID`, `BigDecimal`, `LocalDateTime`
- `Integer`, `Long`, `Double`, `Boolean`
- `List<String>`, `Map<String,String>`

### 3. **Generated Code Issues**

#### Error: "Cannot resolve symbol 'UserService'"
```java
// Generated consumer tries to inject non-existent service
@Autowired
public UserEventListener(UserService userService) { // <- Error here
```
**Solution**: Create the service class referenced in your JSON:
```java
@Service
public class UserService {
    public void processUser(UserEvent event) {
        // Your business logic
    }
}
```

#### Error: "Package does not exist"
```
import com.example.dto.UserEvent; // <- Package not found
```
**Solution**: Ensure generated sources are added to build path:
```xml
<!-- Add this plugin to your pom.xml -->
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>build-helper-maven-plugin</artifactId>
    <version>3.4.0</version>
    <!-- configuration as shown in docs -->
</plugin>
```

### 4. **IDE Integration Issues**

#### IntelliJ IDEA: Generated sources not recognized
**Solution**:
1. Run `mvn clean compile` first
2. Right-click `target/generated-sources/eventor` → "Mark Directory as" → "Generated Sources Root"
3. Refresh project (Ctrl+Shift+F5)

#### VS Code: Import errors
**Solution**:
1. Install "Extension Pack for Java"
2. Run `mvn clean compile`
3. Reload window (Ctrl+Shift+P → "Developer: Reload Window")

### 5. **Spring Boot Integration Issues**

#### Error: "No qualifying bean of type KafkaTemplate"
```
No qualifying bean of type 'org.springframework.kafka.core.KafkaTemplate'
```
**Solution**: Add Kafka configuration:
```java
@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        // Configuration as shown in integration guide
    }
}
```

#### Error: "Consumer not receiving messages"
**Checklist**:
- ✅ Kafka is running (`docker run -p 9092:9092 confluentinc/cp-kafka`)
- ✅ Topic exists and matches JSON definition
- ✅ Consumer group ID is unique or reset
- ✅ Message serialization format matches

### 6. **Performance Issues**

#### Slow generation with many JSON files
**Solution**: Use file filtering in Maven:
```xml
<configuration>
    <jsonDir>${project.basedir}/src/main/resources/eventor</jsonDir>
    <includes>
        <include>**/*-event.json</include>
        <include>**/*-producer.json</include>
        <include>**/*-consumer.json</include>
    </includes>
</configuration>
```

## 🔧 Debugging Tips

### Enable Debug Mode
```bash
# Maven plugin debug
mvn eventor:generate -X

# CLI debug
java -jar eventor-cli.jar --jsonDir ./events --outputDir ./gen --verbose
```

### Validate JSON Before Generation
```bash
# Use jsonlint to validate syntax
cat user-event.json | jsonlint

# Validate against Eventor schema
ajv validate -s eventor-schema.json -d user-event.json
```

### Common File Structure Issues
```
❌ Wrong structure:
src/main/resources/
  events/           # Wrong directory name
    user.json       # Wrong naming

✅ Correct structure:  
src/main/resources/
  eventor/          # Must be named 'eventor'
    user-event.json # Descriptive names recommended
```

## 📞 Getting Help

1. **Check Examples**: Review `examples/` directory for working JSON files
2. **Integration Guide**: Follow `SPRING_BOOT_3_INTEGRATION.md` step-by-step
3. **Schema Validation**: Use `eventor-schema.json` to validate your JSON files
4. **Test Suite**: Run integration tests to verify setup

## 🎯 Quick Health Check

Run this script to verify your setup:

```bash
#!/bin/bash
echo "🔍 Eventor Health Check"

# Check Java version
echo "Java version:"
java -version 2>&1 | head -1

# Check Maven
echo "Maven version:"
mvn -version | head -1

# Check if Eventor is installed
echo "Eventor installation:"
mvn help:describe -DgroupId=io.eventor -DartifactId=eventor -Dversion=0.1.0-SNAPSHOT

# Validate JSON files (if ajv is installed)
if command -v ajv &> /dev/null; then
    echo "Validating JSON files:"
    find src/main/resources/eventor -name "*.json" -exec ajv validate -s eventor-schema.json -d {} \;
fi

echo "✅ Health check complete"
```

Save as `health-check.sh` and run to diagnose common issues.
