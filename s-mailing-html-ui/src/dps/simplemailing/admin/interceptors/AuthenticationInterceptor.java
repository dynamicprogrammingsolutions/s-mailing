package dps.simplemailing.admin.interceptors;

import dps.authentication.AuthenticationManager;
import dps.simplemailing.admin.authentication.RestrictedAccess;
import dps.simplemailing.admin.authentication.UsingAuthenticationManager;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ws.rs.NotAuthorizedException;
import java.lang.reflect.Method;

public class AuthenticationInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        Object target = ctx.getTarget();
        if (target instanceof UsingAuthenticationManager) {
            Method method = ctx.getMethod();
            RestrictedAccess restrictedAccess = method.getAnnotation(RestrictedAccess.class);
            if (restrictedAccess != null) {
                AuthenticationManager am = ((UsingAuthenticationManager) target).getAuthenticationManager();
                if (!am.isAuthenticated()) throw new NotAuthorizedException("not authenticated");
                for (String role: restrictedAccess.value()) {
                    if (!am.isAuthorized(role)) throw new NotAuthorizedException("not in role "+role);
                }
            }
        }
        return ctx.proceed();
    }
}
