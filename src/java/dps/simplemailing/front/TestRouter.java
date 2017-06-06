/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import dps.servletcontroller.Controller;
import dps.servletcontroller.ControllerParam;
import dps.servletcontroller.Filter;
import dps.servletcontroller.Param;
import dps.servletcontroller.Path;
import dps.simplemailing.back.Campaigns;
import dps.simplemailing.back.Crud;
import dps.simplemailing.back.MailGenerator;
import dps.simplemailing.back.MailQueue;
import dps.simplemailing.back.MailQueueStatus;
import dps.simplemailing.back.MailSending;
import dps.simplemailing.back.MailSeries;
import dps.simplemailing.back.Mails;
import dps.simplemailing.back.Users;
import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.GeneratedMail;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.QueuedMail;
import dps.simplemailing.entities.Series;
import dps.simplemailing.entities.SeriesItem;
import dps.simplemailing.entities.SeriesMail;
import dps.simplemailing.entities.SeriesSubscription;
import dps.simplemailing.entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
@Stateless
@Path(value="/testrouter/(?<cparam>[0-9]*)/(.*)",pathGroup=2)
public class TestRouter extends Controller {
    
    @Inject Users userManager;
    @Inject Mails mailManager;
    @Inject MailQueue mailQueue;
    @Inject MailGenerator generatedMails;
    @Inject MailSending mailSending;
    @Inject MailSeries mailSeries;
    @Inject MailQueueStatus mailQueueStatus;
    @Inject Campaigns campaigns;
    
    @Inject Crud crud;
    
    @Filter
    public void filter(HttpServletRequest request, HttpServletResponse response, Method method, Object[] args) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("testfilter");
        
        String result = (String)method.invoke(this, args);
        
        writer.println(result);
        
    }
    
    @Path("test/(?<param>[0-9]*)")
    public String test(HttpServletRequest request, HttpServletResponse response, @Param(group=1) String param, @ControllerParam("cparam") String cparam) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("test");
        writer.println(param);
        writer.println(cparam);
        return "result";
    }
    
}
