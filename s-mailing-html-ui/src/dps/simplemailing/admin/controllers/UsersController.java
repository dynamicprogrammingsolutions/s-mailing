package dps.simplemailing.admin.controllers;

import dps.simplemailing.admin.interceptors.AuthenticationInterceptor;
import dps.simplemailing.admin.interceptors.RunInitMethod;
import dps.simplemailing.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.Interceptors;
import javax.persistence.metamodel.Attribute;
import javax.ws.rs.*;

@Path("users")
@ApplicationScoped
@Interceptors({RunInitMethod.class, AuthenticationInterceptor.class})
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
