package dps.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;

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
        return ((Class) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0]);
    }
    public static Class<?> getTypeParameter(Class<?> clazz, int idx)
    {
        return ((Class) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[idx]);
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
