package dps.simplemailing.front.admin_new.interceptors;

import dps.simplemailing.front.admin_new.controllers.ControllerInit;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class RunInitMethod {
    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        Object target = ctx.getTarget();
        if (target instanceof ControllerInit) {
            System.out.println("running init");
            ((ControllerInit) target).init();
        }
        return ctx.proceed();
    }

}
