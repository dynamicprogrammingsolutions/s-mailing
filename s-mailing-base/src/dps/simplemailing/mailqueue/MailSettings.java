package dps.simplemailing.mailqueue;

import dps.logging.HasLogger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.json.*;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class MailSettings implements HasLogger {

    private String host = "";
    private String port = "";
    private String username = "";
    private String password = "";

    public void startUp(@Observes @Initialized(ApplicationScoped.class) Object init)
    {

    }

    final private String FILENAME = "/etc/s-mailing/smtp.conf";

    @PostConstruct
    void init() throws IOException
    {
        setLogLevel(Level.INFO);

        Path path = Paths.get("/etc/s-mailing/smtp-conf.json");
        try (InputStream smtpConf = new BufferedInputStream(Files.newInputStream(path),4096)) {
            JsonReader jsonReader = Json.createReader(smtpConf);
            JsonObject jsonObject = jsonReader.readObject();
            host = jsonObject.getString("host");
            port = jsonObject.getString("port");
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");
            log(Level.INFO, "settings file loaded: host: " + host + " port: " + port);
        }

    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
