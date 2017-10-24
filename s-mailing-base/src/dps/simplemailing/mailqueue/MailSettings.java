package dps.simplemailing.mailqueue;

import dps.logging.HasLogger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.json.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class MailSettings implements HasLogger {

    /*private String host = "email-smtp.us-west-2.amazonaws.com";
    private String port = "587";
    private String username = "AKIAJ5FEM3AFFMKMCC2A";
    private String password = "AjJGp8+C/yC8NuFYwhR053/JV8abWaRWI7xJ7LjFcvz4";*/

    private String host = "";
    private String port = "";
    private String username = "";
    private String password = "";

    public void startUp(@Observes @Initialized(ApplicationScoped.class) Object init)
    {

    }

    final private String FILENAME = "/etc/s-mailing/smtp.conf";

    @PostConstruct
    void init() throws FileNotFoundException
    {
        setLogLevel(Level.INFO);
        InputStream smtpConf = Thread.currentThread().getContextClassLoader().getResourceAsStream("smtp-conf.json");
        if (smtpConf != null) {
            JsonReader jsonReader = Json.createReader(smtpConf);
            JsonObject jsonObject = jsonReader.readObject();
            host = jsonObject.getString("host");
            port = jsonObject.getString("port");
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");
            log(Level.INFO,"settings file loaded");
        } else {
            throw new FileNotFoundException("Cannot find configuration file: "+"smtp-conf.json");
        }

        /*try (java.io.FileReader reader = new java.io.FileReader(FILENAME)) {
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject jsonObject = jsonReader.readObject();
            host = jsonObject.getString("host");
            port = jsonObject.getString("port");
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");
        } catch (FileNotFoundException e) {
            save();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    void save()
    {
        /*try(java.io.FileWriter writer = new java.io.FileWriter(FILENAME)) {
            JsonWriter jsonWriter = Json.createWriter(writer);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("host",host);
            objectBuilder.add("port",port);
            objectBuilder.add("username",username);
            objectBuilder.add("password",password);
            jsonWriter.writeObject(objectBuilder.build());
            jsonWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        save();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
        save();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        save();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        save();
    }

}
