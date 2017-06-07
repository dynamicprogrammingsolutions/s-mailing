/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import dps.servletcontroller.Controller;
import dps.servletcontroller.Path;
import dps.simplemailing.back.Crud;
import dps.simplemailing.back.MailSeries;
import dps.simplemailing.back.Users;
import dps.simplemailing.entities.Series;
import dps.simplemailing.entities.SeriesSubscription;
import dps.simplemailing.entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
@Stateless
@Path("/(.*)")
public class APIController extends Controller {
    
    @Inject Users users;
    @Inject MailSeries mailSeries;
    @Inject Crud crud;
    
    
    
    @Path("subscribe")
    public void subscribe(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        try {
            String email = request.getParameter("email");
            User user = users.getByEmail(email);
            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setFirstName(request.getParameter("firstname"));
                user.setLastName(request.getParameter("lastname"));
                user.setStatus(User.Status.subscribed);
                crud.create(user);
            } else {
                user.setFirstName(request.getParameter("firstname"));
                user.setLastName(request.getParameter("lastname"));
                user.setStatus(User.Status.subscribed);
                crud.edit(user);
            }
            writer.println("OK");
            
        } catch (Exception e) {
            writer.println("Failed");
        }
        
    }
    
    @Path("subscribe_series")
    public void subscribeSeries(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        try {
            User user = users.getByEmail(request.getParameter("email"));
            Series series = mailSeries.getByName(request.getParameter("series_name"));
            
            java.util.Date time = null;
            String timeString = request.getParameter("subscribe_time");
            if (timeString != null) {
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                time = fm.parse(timeString);
            } else {
                time = new java.util.Date();
            }
            
            SeriesSubscription seriesSubscription = mailSeries.getSubscription(user,series);
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
    public void unsubscribeSeries(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        try {
            User user = users.getByEmail(request.getParameter("email"));
            Series series = mailSeries.getByName(request.getParameter("series_name"));
            
            SeriesSubscription seriesSubscription = mailSeries.getSubscription(user,series);
            if (seriesSubscription != null) {
                crud.remove(seriesSubscription);
            }
            
            writer.println("OK");
        } catch (Exception e) {
            writer.println("Failed");
        }
    }
    
    @Path("set_extradata")
    public void setExtradata(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        try {
            User user = users.getByEmail(request.getParameter("email"));
            Series series = mailSeries.getByName(request.getParameter("series_name"));
            
            SeriesSubscription seriesSubscription = mailSeries.getSubscription(user,series);
            if (seriesSubscription != null) {
                
                Map<String, String[]> parameterMap = request.getParameterMap();
                HashMap<String, String> extraData = new HashMap<String,String>();
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
                User user = users.getByEmail(email);
                System.out.println("bounced user: "+user.getId()+" "+user.getEmail());
                if (user != null) {
                    user.setStatus(User.Status.bounced);
                    crud.edit(user);
                }
            }
        }

    }

    @Path("complaint_notification")
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
                User user = users.getByEmail(email);
                System.out.println("complained user: "+user.getId()+" "+user.getEmail());
                if (user != null) {
                    user.setStatus(User.Status.unsubscribed);
                    crud.edit(user);
                }
            }
        }
        
    }
    
    public String[] getEmailsFromBounceMessage(String message)
    {
        JsonObject messageObject = Json.createReader(new StringReader(message)).readObject();
        
        JsonString type = messageObject.getJsonString("notificationType");
        if (type != null && type.getString().equals("Bounce")) {

            JsonObject bounce = (JsonObject)messageObject.get("bounce");
            JsonArray bouncedRecipients = (JsonArray)bounce.get("bouncedRecipients");

            List<String> emails = new LinkedList();

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
    
    public String[] getEmailsFromComplaintMessage(String message)
    {
        JsonObject messageObject = Json.createReader(new StringReader(message)).readObject();
        
        JsonString type = messageObject.getJsonString("notificationType");
        if (type != null && type.getString().equals("Complaint")) {

            JsonObject bounce = (JsonObject)messageObject.get("complaint");
            JsonArray bouncedRecipients = (JsonArray)bounce.get("complainedRecipients");

            List<String> emails = new LinkedList();

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