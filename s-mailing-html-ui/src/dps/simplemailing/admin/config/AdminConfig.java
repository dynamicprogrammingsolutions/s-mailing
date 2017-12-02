package dps.simplemailing.admin.config;

import dps.logging.HasLogger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

@ApplicationScoped
public class AdminConfig implements HasLogger {

    private String resourcePath;
    private String basePath;

    public void startUp(@Observes @Initialized(ApplicationScoped.class) Object init)
    {

    }

    @PostConstruct
    void init() throws IOException
    {
        setLogLevel(Level.INFO);
        Path path = Paths.get("/etc/s-mailing/html-ui-conf.json");
        try (InputStream conf = new BufferedInputStream(Files.newInputStream(path),4096)) {
            JsonReader jsonReader = Json.createReader(conf);
            JsonObject jsonObject = jsonReader.readObject();
            resourcePath = jsonObject.getString("resourcePath");
            basePath = jsonObject.getString("basePath");
            log(Level.INFO, "html-ui config file loaded: resourcePath: " + resourcePath + " basePath: " + basePath);
        }
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public String getBasePath() {
        return basePath;
    }

}
