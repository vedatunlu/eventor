package io.eventor.maven;

import io.eventor.generator.EventryGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class EventryMavenPlugin extends AbstractMojo {

    @Parameter(property = "eventry.jsonDir", defaultValue = "${project.basedir}/src/main/resources/eventry")
    private File jsonDir;

    @Parameter(property = "eventry.outputDir", defaultValue = "${project.build.directory}/generated-sources/eventry")
    private File outputDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            getLog().info("Eventry Spring Event Generator Maven Plugin");
            getLog().info("JSON Directory: " + jsonDir.getAbsolutePath());
            getLog().info("Output Directory: " + outputDir.getAbsolutePath());

            if (!jsonDir.exists()) {
                getLog().warn("JSON directory does not exist: " + jsonDir.getAbsolutePath());
                getLog().warn("Skipping code generation");
                return;
            }

            // Create output directory if it doesn't exist
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            EventryGenerator generator = new EventryGenerator();
            generator.generateFromJsonDirectory(jsonDir.getAbsolutePath(), outputDir.getAbsolutePath());

            getLog().info("Code generation completed successfully!");

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate classes", e);
        }
    }
}
