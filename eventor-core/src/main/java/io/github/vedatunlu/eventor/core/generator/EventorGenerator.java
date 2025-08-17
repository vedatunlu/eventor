package io.github.vedatunlu.eventor.core.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.github.vedatunlu.eventor.core.model.ConsumerDefinition;
import io.github.vedatunlu.eventor.core.model.DtoDefinition;
import io.github.vedatunlu.eventor.core.model.ProducerDefinition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class EventorGenerator {
    private final ObjectMapper objectMapper;
    private final Configuration freemarkerConfig;

    public EventorGenerator() {
        this.objectMapper = new ObjectMapper();
        this.freemarkerConfig = new Configuration(Configuration.VERSION_2_3_32);
        this.freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
        this.freemarkerConfig.setDefaultEncoding("UTF-8");
    }

    public void generateFromJsonDirectory(String jsonDir, String outputDir) throws IOException, TemplateException {
        Path jsonPath = Paths.get(jsonDir);
        Path outputPath = Paths.get(outputDir);

        // Validate input directory exists
        if (!Files.exists(jsonPath)) {
            throw new IllegalArgumentException(
                String.format("JSON directory does not exist: %s%n" +
                "Please create the directory and add your JSON definitions.", jsonPath.toAbsolutePath()));
        }

        // Check if directory has any JSON files
        long jsonFileCount = Files.walk(jsonPath)
                .filter(path -> path.toString().endsWith(".json"))
                .count();

        if (jsonFileCount == 0) {
            System.out.println(String.format(
                "⚠️  No JSON files found in: %s%n" +
                "💡 Create JSON files with these patterns:%n" +
                "   - *-event.json (for DTOs)%n" +
                "   - *-producer.json (for Producers)%n" +
                "   - *-consumer.json (for Consumers)", jsonPath.toAbsolutePath()));
            return;
        }

        // Create output directory if it doesn't exist
        Files.createDirectories(outputPath);

        System.out.println(String.format("🔄 Processing %d JSON files from: %s", jsonFileCount, jsonPath.toAbsolutePath()));

        // Process all JSON files in the directory
        Files.walk(jsonPath)
                .filter(path -> path.toString().endsWith(".json"))
                .forEach(jsonFile -> {
                    try {
                        processJsonFile(jsonFile.toFile(), outputPath);
                    } catch (Exception e) {
                        String fileName = jsonFile.getFileName().toString();
                        System.err.println(String.format(
                            "❌ Failed to process %s:%n" +
                            "   Error: %s%n" +
                            "💡 Common fixes:%n" +
                            "   - Validate JSON syntax with jsonlint%n" +
                            "   - Check field types against schema%n" +
                            "   - Ensure 'type' field is 'dto', 'producer', or 'consumer'",
                            fileName, e.getMessage()));
                        throw new RuntimeException("Failed to process JSON file: " + fileName, e);
                    }
                });

        System.out.println("✅ Code generation completed successfully!");
    }

    private void processJsonFile(File jsonFile, Path outputPath) throws IOException, TemplateException {
        Map<String, Object> jsonContent = objectMapper.readValue(jsonFile, Map.class);
        String type = (String) jsonContent.get("type");

        switch (type.toLowerCase()) {
            case "dto":
                generateDto(jsonFile, outputPath);
                break;
            case "producer":
                generateProducer(jsonFile, outputPath);
                break;
            case "consumer":
                generateConsumer(jsonFile, outputPath);
                break;
            default:
                System.out.println("Unknown type: " + type + " in file: " + jsonFile.getName());
        }
    }

    private void generateDto(File jsonFile, Path outputPath) throws IOException, TemplateException {
        DtoDefinition dto = objectMapper.readValue(jsonFile, DtoDefinition.class);

        Template template = freemarkerConfig.getTemplate("dto.ftl");
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("dto", dto);

        String fileName = dto.getName() + ".java";
        File outputFile = outputPath.resolve(fileName).toFile();

        try (FileWriter writer = new FileWriter(outputFile)) {
            template.process(dataModel, writer);
        }

        System.out.println("Generated DTO: " + fileName);
    }

    private void generateProducer(File jsonFile, Path outputPath) throws IOException, TemplateException {
        ProducerDefinition producer = objectMapper.readValue(jsonFile, ProducerDefinition.class);

        Template template = freemarkerConfig.getTemplate("producer.ftl");
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("producer", producer);

        String fileName = producer.getName() + ".java";
        File outputFile = outputPath.resolve(fileName).toFile();

        try (FileWriter writer = new FileWriter(outputFile)) {
            template.process(dataModel, writer);
        }

        System.out.println("Generated Producer: " + fileName);
    }

    private void generateConsumer(File jsonFile, Path outputPath) throws IOException, TemplateException {
        ConsumerDefinition consumer = objectMapper.readValue(jsonFile, ConsumerDefinition.class);

        Template template = freemarkerConfig.getTemplate("consumer.ftl");
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("consumer", consumer);

        String fileName = consumer.getName() + ".java";
        File outputFile = outputPath.resolve(fileName).toFile();

        try (FileWriter writer = new FileWriter(outputFile)) {
            template.process(dataModel, writer);
        }

        System.out.println("Generated Consumer: " + fileName);
    }
}
