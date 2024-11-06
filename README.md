### Spring image Maven plugin
A Maven plugin to generate Dockerfile and build a docker image based on that file for Spring boot projects.
There are 2 goals in this plugin.
- generateDockerfile:  
    - Generate a Dockerfile in the root of the project.  
    - Config with `baseImage` and `mainClass` parameters.  
    - This can be bound to any phases, default to `generate-resources`. 
- buildImage:  
    - Execute `docker build` with the generated Dockerfile to build a Docker image.  
    - Configure image tag with `imageTag` parameter.  
    - It uses the jar file to build the image, so it should be bound to a phase after `package` such as `install`. 

### How to use
- cd to `spring-image-maven-plugin` folder and run `mvn install` to install the plugin package to local repository.
- Config the project that we want to use the plugin as the snippet below.  
  With this, we can run `mvn generate-resources` to generate a Dockerfile or `mvn install` to generate the Dockerfile and build a Docker image.
    ```xml
    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>hoaftq.spring_image</groupId>
                    <artifactId>spring-image-maven-plugin</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <configuration>
                        <baseImage>eclipse-temurin:23.0.1_11-jre-ubi9-minimal</baseImage>
                        <mainClass>
                            hoaftq.example.spring_image_maven_plugin_example.SpringImageMavenPluginExampleApplication
                        </mainClass>
                        <imageTag>hoaftq/spring-image-maven-plugin-example</imageTag>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
        <plugins>
            <plugin>
                <groupId>hoaftq.spring_image</groupId>
                <artifactId>spring-image-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>docker-file</id>
                        <phase>generate-resources</phase>
                        <goals>
                            <goal>generateDockerfile</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>docker-image</id>
                        <phase>install</phase>
                        <goals>
                            <goal>buildImage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ```

They can also be run directly with
- `mvn spring-image:generateDockerfile`
- `mvn spring-image:buildImage`: this goal use the jar file, so it should be run after `mvn package`


