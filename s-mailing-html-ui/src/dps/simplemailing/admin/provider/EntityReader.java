package dps.simplemailing.admin.provider;

import dps.reflect.NoSuchConstructorError;
import dps.reflect.ReflectHelper;
import dps.simplemailing.manage.UseEntityManager;
import org.jboss.resteasy.plugins.providers.FormUrlEncodedProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Entity;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

@Produces(MediaType.APPLICATION_FORM_URLENCODED)
@ApplicationScoped
public class EntityReader extends UseEntityManager implements MessageBodyReader<Object> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        System.out.println("is readable");
        if (ReflectHelper.hasAnnotation(type,Entity.class)) {
            Metamodel metamodel = em.getMetamodel();
            EntityType<?> entity = metamodel.entity(type);
            if (entity != null) return true;
            else return false;
        }
        return false;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        System.out.println("reading");
        Metamodel metamodel = em.getMetamodel();

        FormUrlEncodedProvider formUrlEncodedProvider = new FormUrlEncodedProvider();
        MultivaluedMap<String,String> formData = formUrlEncodedProvider.readFrom(MultivaluedMap.class,MultivaluedMap.class,annotations,mediaType,httpHeaders,entityStream);

        EntityType<Object> entity = metamodel.entity(type);

        Object newentity = ReflectHelper.newInstance(type);

        Set<Attribute<Object, ?>> declaredAttributes = entity.getDeclaredAttributes();
        for (Attribute<Object, ?> attr: declaredAttributes) {
            String name = attr.getName();

            List<String> values = formData.get(name);
            if (values != null) {
                String value = values.get(0);

                Member attrMember = attr.getJavaMember();
                String setterName = null;
                if (attrMember instanceof Field) {
                    setterName = ReflectHelper.getSetterName(attrMember.getName());
                    try {
                        Method setterMethod = ReflectHelper.findMethodWithName(type, setterName, 1);
                        Class<?> paramType = setterMethod.getParameterTypes()[0];
                        if (paramType.isInstance(value)) {
                            ReflectHelper.invokeMethod(setterMethod, newentity, value);
                        } else if (paramType.equals(Boolean.class)) {
                            ReflectHelper.invokeMethod(setterMethod, newentity, "on".equals(value));
                        } else {
                            Method valueOf = paramType.getMethod("valueOf", String.class);
                            ReflectHelper.invokeMethod(valueOf, null, value);
                        }
                    } catch (NoSuchMethodException|NoSuchConstructorError|NoSuchMethodError e) {
                        //e.printStackTrace();
                    }
                }
            }
        }

        return newentity;
    }
}
