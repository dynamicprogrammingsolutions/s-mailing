/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front.forms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author ferenci84
 */
@Stateless
public class ProcessForm {

    public void process(Object entityObject, Map<String, String[]> parameterMap)
    {
        Class<?> entityClass = entityObject.getClass();
        Method[] methods = entityClass.getMethods();
        for (Map.Entry<String, String[]> parameterEntry: parameterMap.entrySet()) {
            String key = parameterEntry.getKey();
            String value = parameterEntry.getValue()[0];
            if (key.length() < 2) continue;
            String methodName = "set".concat(key.substring(0, 1).toUpperCase()).concat(key.substring(1));
            System.out.println("method name: "+methodName);
            for (Method method: methods) {
                if (method.getName().equals(methodName)) {
                    Parameter[] parameters = method.getParameters();
                    if (parameters.length != 1) break;
                    try {
                        method.invoke(entityObject, convertParam(parameters[0],value));
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(ProcessForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(ProcessForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(ProcessForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    Object convertParam(Parameter parameter, String value)
    {
        Class<?> type = parameter.getType();
        if (type.equals(String.class)) return value;
        if (type.equals(Integer.class)) return Integer.parseInt(value);
        if (type.equals(Long.class)) return Long.parseLong(value);
        if (type.equals(Boolean.class)) {
            if (value.equals("on")) return true;
            return Boolean.parseBoolean(value);
        }
        return null;
    }
}
