package io.github.vedatunlu.eventor.cli;

import io.github.vedatunlu.eventor.core.generator.EventorGenerator;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(
    name = "Eventor",
    mixinStandardHelpOptions = true,
    version = "Eventor 0.1.0-SNAPSHOT",
    description = "Spring Event Generator - Generate DTOs, Producers, and Consumers from JSON definitions"
)
public class EventorCliMain implements Callable<Integer> {

    @Option(
        names = {"--jsonDir", "-j"},
        description = "Directory containing JSON definition files",
        required = true
    )
    private String jsonDir;

    @Option(
        names = {"--outputDir", "-o"},
        description = "Output directory for generated Java classes",
        required = true
    )
    private String outputDir;

    @Override
    public Integer call() throws Exception {
        try {
            System.out.println("Eventor Spring Event Generator");
            System.out.println("JSON Directory: " + jsonDir);
            System.out.println("Output Directory: " + outputDir);
            System.out.println("Generating classes...");

            EventorGenerator generator = new EventorGenerator();
            generator.generateFromJsonDirectory(jsonDir, outputDir);

            System.out.println("Generation completed successfully!");
            return 0;
        } catch (Exception e) {
            System.err.println("Error during generation: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new EventorCliMain()).execute(args);
        System.exit(exitCode);
    }
}
