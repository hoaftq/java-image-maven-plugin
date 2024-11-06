package hoaftq.spring_image;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DockerfileGenerator {

    private final String baseImage;
    private final String group;
    private final String user;
    private final String mainClass;
    private final File outputFilePath;

    public DockerfileGenerator(String baseImage, String group, String user, String mainClass, File outputFilePath) {
        this.baseImage = baseImage;
        this.group = group;
        this.user = user;
        this.mainClass = mainClass;
        this.outputFilePath = outputFilePath;
    }

    public void generate() throws IOException, TemplateException {
        Configuration config = createFreeMakerConfiguration();
        Template template = config.getTemplate("Dockerfile.ftlh");
        Map<String, Object> model = createModel();

        File outputDockerFile = Paths.get(outputFilePath.getPath(), "Dockerfile").toFile();
        try (FileWriter writer = new FileWriter(outputDockerFile)) {
            template.process(model, writer);
        }
    }

    private Configuration createFreeMakerConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_33);
        configuration.setClassForTemplateLoading(getClass(), "/");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);
        return configuration;
    }

    private Map<String, Object> createModel() {
        Map<String, Object> root = new HashMap<>();
        root.put("baseImage", baseImage);
        root.put("group", group);
        root.put("user", user);
        root.put("mainClass", mainClass);
        return root;
    }
}
