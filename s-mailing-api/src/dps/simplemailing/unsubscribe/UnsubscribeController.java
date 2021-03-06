package dps.simplemailing.unsubscribe;

import dps.logging.HasLogger;
import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.User;
import dps.simplemailing.manage.CampaignManager;
import dps.simplemailing.manage.UserManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

/**
 *
 * @author ferenci84
 */
@ApplicationScoped

public class UnsubscribeController implements HasLogger {

    @Inject
    UserManager userManager;

    @Inject
    CampaignManager campaignManager;
    
    public void unsubscribe(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String email = request.getParameter("email");
            User user = userManager.getById(id);
            if (user.getEmail().equals(email)) {
                String campaignName = request.getParameter("campaign");
                if (campaignName == null) {
                    logInfo("unsubscribing id "+id+" email "+email);
                    userManager.unsubscribe(user);
                    writer.println("Successfully unsubscribed");
                } else {
                    Campaign campaign = campaignManager.getByName(request.getParameter("campaign"));
                    logInfo("unsubscribing id "+id+" email "+email+" campaign "+campaign.getName());
                    campaignManager.unsubscribeUser(campaign,user);
                    writer.println("Successfully unsubscribed from campaign "+campaign.getLongName()); 
                }
            } else {
                throw new Exception();
            }
        } catch(Exception e) {
            e.printStackTrace();
            writer.println("Unsubscribe unsuccessful");
        }
    }
}
