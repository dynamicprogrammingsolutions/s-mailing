package dps.simplemailing.admin.config;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileNotFoundException;
import java.io.InputStream;

@ApplicationScoped
public class AdminConfig {

    private String resourcePath;
    private String basePath;

    public void startUp(@Observes @Initialized(ApplicationScoped.class) Object init)
    {

    }

    @PostConstruct
    void init() throws FileNotFoundException
    {
        InputStream conf = Thread.currentThread().getContextClassLoader().getResourceAsStream("html-ui-conf.json");
        if (conf != null) {
            JsonReader jsonReader = Json.createReader(conf);
            JsonObject jsonObject = jsonReader.readObject();
            resourcePath = jsonObject.getString("resourcePath");
            basePath = jsonObject.getString("basePath");
        } else {
            throw new FileNotFoundException("Cannot find configuration file: "+"html-ui-conf.json");
        }
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public String getBasePath() {
        return basePath;
    }

}
