package dps.simplemailing.admin.authentication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ElementType.METHOD})
public @interface RestrictedAccess {
    String[] value() default {};
}
