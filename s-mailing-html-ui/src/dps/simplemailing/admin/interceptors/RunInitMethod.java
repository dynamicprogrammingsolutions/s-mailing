package dps.simplemailing.admin.interceptors;

import dps.simplemailing.admin.controllers.ControllerInit;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class RunInitMethod {
    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        Object target = ctx.getTarget();
        if (target instanceof ControllerInit) {
            ((ControllerInit) target).init();
        }
        return ctx.proceed();
    }

}
