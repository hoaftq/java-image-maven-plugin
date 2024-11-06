package hoaftq.spring_image;

import freemarker.template.TemplateException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;

@Mojo(name = "generateDockerfile", defaultPhase = LifecyclePhase.PACKAGE)
public class GenerateDockerfileMojo extends AbstractMojo {

    @Parameter(property = "baseImage", required = true)
    private String baseImage;

    @Parameter(property = "group", defaultValue = "spring")
    private String group;

    @Parameter(property = "user", defaultValue = "spring")
    private String user;

    @Parameter(property = "mainClass", required = true)
    private String mainClass;

    @Parameter(defaultValue = "${project.basedir}", readonly = true)
    private File outputFilePath;

    @Override
    public void execute() throws MojoExecutionException {
        var generator = new DockerfileGenerator(baseImage, group, user, mainClass, outputFilePath);

        try {
            generator.generate();
        } catch (IOException | TemplateException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            throw new MojoExecutionException(writer.toString());
        }
    }
}
