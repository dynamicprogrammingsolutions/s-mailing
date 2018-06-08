package dps.simplemailing.mailqueue;

import dps.commons.configuration.XmlConfiguration;
import dps.commons.startup.Startup;
import dps.logging.HasLogger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

@ApplicationScoped
@Startup
@XmlRootElement
public class MailSettings  extends XmlConfiguration {

    private String host = "";
    private String port = "";
    private String username = "";
    private String password = "";

    @Override
    protected String getSettingsFile() {
        return "META-INF/s-mailing.conf.xml";
    }


    /*public void startUp(@Observes @Initialized(ApplicationScoped.class) Object init)
    {

    }

    final private String FILENAME = "/etc/s-mailing/smtp.conf";

    @PostConstruct
    void init() throws IOException
    {
        setLogLevel(Level.INFO);

        Path path;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            path = Paths.get("C:/Program Files/s-mailing/smtp-conf.json");
        } else {
            path =  Paths.get("/etc/s-mailing/smtp-conf.json");
        }
        try (InputStream smtpConf = new BufferedInputStream(Files.newInputStream(path),4096)) {
            JsonReader jsonReader = Json.createReader(smtpConf);
            JsonObject jsonObject = jsonReader.readObject();
            host = jsonObject.getString("host");
            port = jsonObject.getString("port");
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");
            log(Level.INFO, "settings file loaded: host: " + host + " port: " + port);
        }

    }*/

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

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
