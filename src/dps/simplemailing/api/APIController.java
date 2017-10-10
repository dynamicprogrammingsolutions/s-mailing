package dps.simplemailing.api;

import dps.router.Controller;
import dps.router.Path;
import dps.simplemailing.crud.Crud;
import dps.simplemailing.entities.Series;
import dps.simplemailing.entities.SeriesSubscription;
import dps.simplemailing.entities.User;
import dps.simplemailing.manage.SeriesManager;
import dps.simplemailing.manage.UserManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

/**
 *
 * @author ferenci84
 */

// TODO: replace crud: check if important operations exist in manager

@ApplicationScoped
@Path("/(.*)")
public class APIController extends Controller {
    
    @Inject
    UserManager userManager;
    @Inject
    SeriesManager seriesManager;

    @Inject Crud crud;
    
    @Path("subscribe")
    @Transactional(Transactional.TxType.REQUIRED)
    public void subscribe(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        try {
            String email = request.getParameter("email");
            User user = userManager.getByEmail(email);
            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setFirstName(request.getParameter("firstname"));
                user.setLastName(request.getParameter("lastname"));
                user.setStatus(User.Status.subscribed);
                userManager.create(user);
            } else {
                user.setFirstName(request.getParameter("firstname"));
                user.setLastName(request.getParameter("lastname"));
                user.setStatus(User.Status.subscribed);
                userManager.modify(user);
            }
            writer.println("OK");
            
        } catch (Exception e) {
            writer.println("Failed");
        }
        
    }
    
    @Path("subscribe_series")
    @Transactional(Transactional.TxType.REQUIRED)
    public void subscribeSeries(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        try {
            User user = userManager.getByEmail(request.getParameter("email"));
            Series series = seriesManager.getByName(request.getParameter("series_name"));
            
            java.util.Date time;
            String timeString = request.getParameter("subscribe_time");
            if (timeString != null) {
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                time = fm.parse(timeString);
            } else {
                time = new java.util.Date();
            }
            
            SeriesSubscription seriesSubscription = seriesManager.getSubscription(user,series);
            if (seriesSubscription == null) {
                seriesSubscription = new SeriesSubscription();
                seriesSubscription.setSeries(series);
                seriesSubscription.setUser(user);
                seriesSubscription.setSubscribeTime(time);
                crud.create(seriesSubscription);
            } else {
                if (series.getUpdateSubscribeTime()) {
                    seriesSubscription.setSubscribeTime(time);
                    crud.edit(seriesSubscription);    
                }
            }
            writer.println("OK");
        } catch (Exception e) {
            writer.println("Failed");
        }
    }
    
    @Path("unsubscribe_series")
    @Transactional(Transactional.TxType.REQUIRED)
    public void unsubscribeSeries(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        try {
            User user = userManager.getByEmail(request.getParameter("email"));
            Series series = seriesManager.getByName(request.getParameter("series_name"));
            
            SeriesSubscription seriesSubscription = seriesManager.getSubscription(user,series);
            if (seriesSubscription != null) {
                crud.remove(seriesSubscription);
            }
            
            writer.println("OK");
        } catch (Exception e) {
            writer.println("Failed");
        }
    }
    
    @Path("set_extradata")
    @Transactional(Transactional.TxType.REQUIRED)
    public void setExtradata(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        try {
            User user = userManager.getByEmail(request.getParameter("email"));
            Series series = seriesManager.getByName(request.getParameter("series_name"));
            
            SeriesSubscription seriesSubscription = seriesManager.getSubscription(user,series);
            if (seriesSubscription != null) {
                
                Map<String, String[]> parameterMap = request.getParameterMap();
                HashMap<String, String> extraData = new HashMap<>();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    String key = entry.getKey();
                    String[] values = entry.getValue();
                    if (key.equals("email")) continue;
                    if (key.equals("series_name")) continue;
                    extraData.put(key, values[0]);
                }
                seriesSubscription.setExtraData(extraData.toString());
                crud.edit(seriesSubscription);
                writer.println("OK");
            } else {
                writer.println("No Subscription");
            }
            
        } catch (Exception e) {
            writer.println("Failed");
        }
    }
    
    @Path("bounce_notification")
    @Transactional(Transactional.TxType.REQUIRED)
    public void bounceNotification(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        JsonObject jsonObject = Json.createReader(request.getReader()).readObject();
        if (jsonObject == null) return;
        
        System.out.println("bounce notification");
        System.out.println("contents: "+jsonObject);
        
        JsonString type = jsonObject.getJsonString("Type");
        if (type != null && type.getString().equals("Notification")) {

            String message = jsonObject.getJsonString("Message").getString();
            String[] emails = getEmailsFromBounceMessage(message);

            for(String email: emails) {
                User user = userManager.getByEmail(email);
                if (user != null && user.getStatus() != User.Status.test) {
                    System.out.println("bounced user: "+user.getId()+" "+user.getEmail());
                    user.setStatus(User.Status.bounced);
                    crud.edit(user);
                }
            }
        }

    }

    @Path("complaint_notification")
    @Transactional(Transactional.TxType.REQUIRED)
    public void complaintNotification(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        JsonObject jsonObject = Json.createReader(request.getReader()).readObject();
        if (jsonObject == null) return;
        
        System.out.println("complaint notification");
        System.out.println("contents: "+jsonObject);
        
        JsonString type = jsonObject.getJsonString("Type");
        if (type != null && type.getString().equals("Notification")) {

            String message = jsonObject.getJsonString("Message").getString();
            String[] emails = getEmailsFromComplaintMessage(message);

            for(String email: emails) {
                User user = userManager.getByEmail(email);
                if (user != null && user.getStatus() != User.Status.test) {
                    System.out.println("complained user: "+user.getId()+" "+user.getEmail());
                    user.setStatus(User.Status.unsubscribed);
                    crud.edit(user);
                }
            }
        }
        
    }
    
    private String[] getEmailsFromBounceMessage(String message)
    {
        JsonObject messageObject = Json.createReader(new StringReader(message)).readObject();
        
        JsonString type = messageObject.getJsonString("notificationType");
        if (type != null && type.getString().equals("Bounce")) {

            JsonObject bounce = (JsonObject)messageObject.get("bounce");
            JsonArray bouncedRecipients = (JsonArray)bounce.get("bouncedRecipients");

            List<String> emails = new LinkedList<>();

            for (JsonValue bouncedRecipientJsonValue: bouncedRecipients) {
                JsonObject bouncedRecipient = (JsonObject)bouncedRecipientJsonValue;
                JsonString emailAddress = (JsonString)bouncedRecipient.get("emailAddress");
                emails.add(emailAddress.getString());
            }

            return emails.toArray(new String[emails.size()]);
            
        } else {
            return new String[0];
        }
    }
    
    private String[] getEmailsFromComplaintMessage(String message)
    {
        JsonObject messageObject = Json.createReader(new StringReader(message)).readObject();
        
        JsonString type = messageObject.getJsonString("notificationType");
        if (type != null && type.getString().equals("Complaint")) {

            JsonObject bounce = (JsonObject)messageObject.get("complaint");
            JsonArray bouncedRecipients = (JsonArray)bounce.get("complainedRecipients");

            List<String> emails = new LinkedList<>();

            for (JsonValue bouncedRecipientJsonValue: bouncedRecipients) {
                JsonObject bouncedRecipient = (JsonObject)bouncedRecipientJsonValue;
                JsonString emailAddress = (JsonString)bouncedRecipient.get("emailAddress");
                emails.add(emailAddress.getString());
            }

            return emails.toArray(new String[emails.size()]);
        } else {
            return new String[0];
        }
    }
    
}
