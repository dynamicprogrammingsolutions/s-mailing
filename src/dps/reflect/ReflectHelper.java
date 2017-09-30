package dps.reflect;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Metamodel;
import java.lang.reflect.*;

/**
 *
 * @author ferenci84
 */
public class ReflectHelper {
    public static Constructor findConstructor(Class<?> clazz, Object... args) throws NoSuchMethodException
    {
        for(Constructor constructor: clazz.getConstructors()) {
            Boolean compatible = true;
            Parameter[] parameters = constructor.getParameters();
            for (int i = 0; i != parameters.length; i++) {
                if (!parameters[i].getType().isInstance(args[i])) compatible = false;
            }
            if (compatible) return constructor;
        }
        throw new NoSuchMethodException();
    }
    public static Object newInstance(Class<?> clazz, Object... args)
    {
        Constructor constructor;
        try {
            constructor = findConstructor(clazz, args);
        } catch (NoSuchMethodException ex) {
            throw new NoSuchConstcutorError();
        }
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new NoSuchConstcutorError();
        }
    }
    public static Class<?> getTypeParameter(Class<?> clazz)
    {
        return getTypeParameter(clazz,0);
    }
    public static <T> Class<T> getTypeParameter(Class<?> clazz, int idx)
    {
        Type superClass = clazz.getGenericSuperclass();
        if (!(superClass instanceof ParameterizedType)) {
            clazz = clazz.getSuperclass();
            if (clazz == null) throw new IllegalArgumentException("Cannot get ParametrizedType");
            superClass = clazz.getGenericSuperclass();
        }
        if (superClass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superClass;
            Type typeArgument = parameterizedType.getActualTypeArguments()[idx];
            return (Class<T>) typeArgument;
        } else  {
            throw new IllegalArgumentException("Cannot cast to ParametrizedType");
        }
    }
    public static Class<?> getEntityIdType(EntityManager em, Class<?> entityClass)
    {
        Metamodel metamodel = em.getMetamodel();
        javax.persistence.metamodel.EntityType entityType = metamodel.entity(entityClass);
        javax.persistence.metamodel.Type<?> idType = entityType.getIdType();
        return idType.getJavaType();
    }
    public static String getGetterName(String field)
    {
        return "get".concat(field.substring(0, 1).toUpperCase()).concat(field.substring(1));
    }
    public static String getSetterName(String field)
    {
        return "set".concat(field.substring(0, 1).toUpperCase()).concat(field.substring(1));
    }
    public static Method findMethod(Class<?> clazz, String name, Object... args) throws NoSuchMethodException
    {
        Method[] methods = clazz.getMethods();
        for (Method method: methods) {
            if (method.getName().equals(name)) {
                Boolean compatible = true;
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i != parameters.length; i++) {
                    if (!parameters[i].getType().isInstance(args[i])) compatible = false;
                }
                if (compatible) return method;
            }
        }
        throw new NoSuchMethodException();
    }
    public static Object invokeMethod(Class<?> clazz, String name, Object obj, Object... args)
    {
        Method method;
        try {
            method = findMethod(clazz, name, args);
        } catch (NoSuchMethodException ex) {
            throw new NoSuchMethodError();
        }
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new NoSuchMethodError();
        }
    }
    public static Object invokeMethod(Method method, Object obj, Object... args)
    {
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new NoSuchMethodError();
        }
    }
}
