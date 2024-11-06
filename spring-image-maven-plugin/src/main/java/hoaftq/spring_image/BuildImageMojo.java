package hoaftq.spring_image;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

@Mojo(name = "buildImage", defaultPhase = LifecyclePhase.INSTALL)
public class BuildImageMojo extends AbstractMojo {

    @Parameter(property = "imageTag", defaultValue = "${project.groupId}/${project.artifactId}")
    private String imageTag;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        var targetDirectory = mavenProject.getBuild().getDirectory();
        String executableFilePath = Paths.get(targetDirectory, mavenProject.getBuild().getFinalName() + ".jar").toString();
        String extractedPath = Paths.get(targetDirectory, "dependency").toString();
        try {
            ZipUtil.unzip(executableFilePath, extractedPath);
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to unzip the jar file " + executableFilePath, e);
        }

        try {
            runDockerBuild();
        } catch (Exception e) {
            throw new MojoExecutionException("Error occurred while executing docker build", e);
        }
    }

    private void runDockerBuild() throws Exception {
        var process = Runtime.getRuntime().exec(new String[]{"docker", "build", ".", "-t", imageTag});
        var exitCode = process.waitFor();
        try (var inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             var errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            inputReader.lines().forEach(l -> getLog().info(l));
            errorReader.lines().forEach(l -> getLog().info(l));
        }

        if (exitCode != 0) {
            throw new Exception("Running docker build command ended abnormally");
        }
    }
}
