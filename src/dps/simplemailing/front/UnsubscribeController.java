package dps.simplemailing.front;

import dps.simplemailing.back.Crud;
import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.User;
import dps.simplemailing.manage.CampaignManager;
import dps.simplemailing.manage.UserManager;

import java.io.IOException;
import java.io.PrintWriter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

/**
 *
 * @author ferenci84
 */
@ApplicationScoped
public class UnsubscribeController {
    
    @Inject
    UserManager userManager;

    @Inject
    CampaignManager campaigns;
    
    @Inject Crud crud;

    @Transactional(Transactional.TxType.REQUIRED)
    public void unsubscribe(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String email = request.getParameter("email");
            User user = crud.find(id,User.class);
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
