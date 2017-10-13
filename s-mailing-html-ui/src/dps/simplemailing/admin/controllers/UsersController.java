package dps.simplemailing.admin.controllers;

import dps.simplemailing.admin.interceptors.RunInitMethod;
import dps.simplemailing.admin.provider.View;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.Mail_;
import dps.simplemailing.entities.User;
import dps.simplemailing.manage.MailManager;
import dps.simplemailing.rs.Redirect;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.Interceptors;
import javax.persistence.EntityNotFoundException;
import javax.persistence.metamodel.Attribute;
import javax.ws.rs.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Path("users")
@ApplicationScoped
@Interceptors({RunInitMethod.class})
public class UsersController extends CrudController<User,Long> {

    @Override
    protected String getSubfolder() {
        return "users";
    }

    @Override
    protected String getTitle()
    {
        return "Users";
    }

    @Override
    protected Attribute<User,?>[] getExtraAttributes()
    {
        Attribute[] arr = {};
        return arr;
    }


}
