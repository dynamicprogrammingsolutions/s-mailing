/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import dps.simplemailing.back.Users;
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

    public void unsubscribe(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String email = request.getParameter("email");
            System.out.println("unsubscribing id "+id+" email "+email);
            User user = userManager.find(id);
            if (user.getEmail().equals(email)) {
                userManager.unsubscribe(user);
                writer.println("Successfully unsubscribed");
            } else {
                throw new Exception();
            }
        } catch(Exception e) {
            writer.println("Unsubscribe unsuccessful");
        }
    }
}
