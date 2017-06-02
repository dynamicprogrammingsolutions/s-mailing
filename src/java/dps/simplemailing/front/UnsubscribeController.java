/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import com.sun.javafx.application.ParametersImpl;
import dps.simplemailing.back.Campaigns;
import dps.simplemailing.back.Crud;
import dps.simplemailing.back.Users;
import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
@Stateless
public class UnsubscribeController {
    
    @Inject Users userManager;
    @Inject Campaigns campaigns;
    
    @Inject Crud crud;

    public void unsubscribe(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String email = request.getParameter("email");
            User user = (User)crud.find(id,User.class);
            if (user.getEmail().equals(email)) {
                String campaignName = request.getParameter("campaign");
                if (campaignName == null) {
                    System.out.println("unsubscribing id "+id+" email "+email);
                    userManager.unsubscribe(user);
                    writer.println("Successfully unsubscribed");
                } else {
                    Campaign campaign = campaigns.getByName(request.getParameter("campaign"));
                    System.out.println("unsubscribing id "+id+" email "+email+" campaign "+campaign.getName());
                    campaign.getUnsubscribedUsers().add(user);
                    crud.edit(campaign);
                    writer.println("Successfully unsubscribed from campaign "+campaign.getLongName()); 
                }
            } else {
                throw new Exception();
            }
        } catch(Exception e) {
            
            writer.println("Unsubscribe unsuccessful");
        }
    }
}
