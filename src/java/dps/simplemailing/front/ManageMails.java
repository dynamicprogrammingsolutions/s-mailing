/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import dps.servletcontroller.Path;
import dps.simplemailing.back.Crud;
import java.io.IOException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
@Stateless
@Path("/mails/(.*)")
public class ManageMails {

    @Inject Crud crud;
     
    @Path("new")
    public void showUsers() throws IOException
    {

    }
}
