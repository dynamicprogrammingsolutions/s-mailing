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
        if (ReflectHelper.hasAnnotation(type,Entity.class)) {
            Metamodel metamodel = em.getMetamodel();
            EntityType<?> entity = metamodel.entity(type);
            if (entity != null) return true;
            else return false;
        }
        return false;
    }

    //TODO: remove debug logging
    //TODO: if id is set, load entity first
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
            System.out.println("field:"+name);
            List<String> values = formData.get(name);
            if (values != null) {
                String value = values.get(0);
                System.out.println("value:"+value);
                Member attrMember = attr.getJavaMember();
                String setterName = null;
                if (attrMember instanceof Field) {
                    setterName = ReflectHelper.getSetterName(attrMember.getName());
                    try {
                        Method[] setterMethods = ReflectHelper.findMethodsWithName(type, setterName, 1);
                        for (Method setterMethod: setterMethods) {
                            Class<?> paramType = setterMethod.getParameterTypes()[0];
                            System.out.println("paramtype:"+paramType);
                            if (paramType.isInstance(value)) {
                                System.out.println("running setterMethod");
                                ReflectHelper.invokeMethod(setterMethod, newentity, value);
                                break;
                            } else if (paramType.equals(Boolean.class)) {
                                System.out.println("running bool setter");
                                ReflectHelper.invokeMethod(setterMethod, newentity, "on".equals(value));
                                break;
                            } else {
                                try {
                                    System.out.println("running valueOf");
                                    Method valueOf = paramType.getMethod("valueOf", String.class);
                                    Object valueValueOf = ReflectHelper.invokeMethod(valueOf, null, value);
                                    ReflectHelper.invokeMethod(setterMethod, newentity, valueValueOf);
                                    break;
                                } catch (NoSuchMethodException e) {
                                    //e.printStackTrace();
                                }
                            }
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
