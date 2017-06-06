/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.servletcontroller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 * @param <T>
 */
@Stateless
public class Router<T extends ControllerBase> {
    @Inject Instance<ControllerBase> controllers;

    public Boolean process(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        for(ControllerBase controller: controllers) {
            if (processController(controller,path,request,response)) return true;
        }
        return false;
    }
    
    public Boolean processController(ControllerBase controller, String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Class<?> contollerClass = controller.getOriginalClass();
        Path pathAnnotation = contollerClass.getAnnotation(Path.class);
        if (pathAnnotation == null) return false;
        
        Matcher controllerMatches = checkPath(path,pathAnnotation);
        if (controllerMatches == null) return false;
        
        int pathGroup = pathAnnotation.pathGroup();
        String nextPath;
        nextPath = controllerMatches.group(pathGroup);

        Method[] methods = contollerClass.getMethods();
        
        Method filterMethod = null;
        
        for (Method method: methods) {
            if (method.isAnnotationPresent(Filter.class)) {
                filterMethod = method;
                break;
            }
        }
        
        for (Method method: methods) {
            if (method.isAnnotationPresent(Path.class)) {
                if (processAction(controller, method, filterMethod, nextPath, controllerMatches, request, response)) return true;
            }
        }
        return false;
    }
    
    public Boolean processAction(ControllerBase controller, Method method, Method filterMethod, String path, Matcher controllerMatches, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Path pathAnnotation = method.getAnnotation(Path.class);
        if (pathAnnotation == null) return false;
        
        Matcher actionMatches = checkPath(path,pathAnnotation);
        if (actionMatches == null) return false;
        
        Object[] args = resolveParameters(method, actionMatches, controllerMatches, request, response);
        
        Object[] filterArgs = null;
        
        if (filterMethod != null) {
            filterArgs = resolveFilterParameters(filterMethod, actionMatches, controllerMatches, request, response, method, args);
        }
        
        try {
            if (filterMethod != null) {
                filterMethod.invoke(controller, filterArgs);
            } else {
                method.invoke(controller,args);
            }
            return true;
        } catch (IllegalAccessException ex) {
            System.out.println("IllegalAccessException");
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            System.out.println("IllegalArgumentException");
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            System.out.println("InvocationTargetException");
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    Object[] resolveParameters(Method method, Matcher actionMatches, Matcher controllerMatches, HttpServletRequest request, HttpServletResponse response)
    {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i != parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.getParameterizedType().equals(HttpServletRequest.class)) {
                args[i] = request;
            } else if (parameter.getParameterizedType().equals(HttpServletResponse.class)) {
                args[i] = response;
            } else if (parameter.isAnnotationPresent(Param.class)) {
                Param paramAnnotation = parameter.getAnnotation(Param.class);
                String paramName = paramAnnotation.value();
                int group = paramAnnotation.group();
                if (!paramName.isEmpty()) {
                    args[i] = actionMatches.group(paramName);
                } else {
                    args[i] = actionMatches.group(group);
                }
            } else if (parameter.isAnnotationPresent(ControllerParam.class)) {
                ControllerParam paramAnnotation = parameter.getAnnotation(ControllerParam.class);
                String paramName = paramAnnotation.value();
                int group = paramAnnotation.group();
                if (!paramName.isEmpty()) {
                    args[i] = controllerMatches.group(paramName);
                } else {
                    args[i] = controllerMatches.group(group);
                }
            }
        }
        return args;
    }
    
    Object[] resolveFilterParameters(Method method, Matcher actionMatches, Matcher controllerMatches,
            HttpServletRequest request, HttpServletResponse response,
            Method dispatchMethod, Object[] dispatchArgs)
    {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i != parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.getParameterizedType().equals(HttpServletRequest.class)) {
                args[i] = request;
            } else if (parameter.getParameterizedType().equals(HttpServletResponse.class)) {
                args[i] = response;
            } else if (parameter.getParameterizedType().equals(Object[].class)) {
                args[i] = dispatchArgs;
            } else if (parameter.getParameterizedType().equals(Method.class)) {
                args[i] = dispatchMethod;
            } else if (parameter.isAnnotationPresent(Param.class)) {
                Param paramAnnotation = parameter.getAnnotation(Param.class);
                String paramName = paramAnnotation.value();
                int group = paramAnnotation.group();
                if (!paramName.isEmpty()) {
                    args[i] = actionMatches.group(paramName);
                } else {
                    args[i] = actionMatches.group(group);
                }
            } else if (parameter.isAnnotationPresent(ControllerParam.class)) {
                ControllerParam paramAnnotation = parameter.getAnnotation(ControllerParam.class);
                String paramName = paramAnnotation.value();
                int group = paramAnnotation.group();
                if (!paramName.isEmpty()) {
                    args[i] = controllerMatches.group(paramName);
                } else {
                    args[i] = controllerMatches.group(group);
                }
            }
        }
        return args;
    }
    
    Matcher checkPath(String path, Path pathAnnotation)
    {
        String urlPattern = pathAnnotation.value();
        Pattern compiledPattern = getCompiledPattern(urlPattern);
        Matcher match = compiledPattern.matcher(path);
        if (match.matches()) {
            return match;
        }
        return null;
    }
    
    Pattern getCompiledPattern(String urlPattern)
    {
        return Pattern.compile(getPattern(urlPattern));
    }
    
    String getPattern(String urlPattern)
    {
        return urlPattern;
    }
    
    static Pattern namedGroupPattern = Pattern.compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>");    
    
    static Boolean hasNamedGroup(String regex)
    {
        return namedGroupPattern.matcher(regex).find();
    }
    
    static Set<String> getGroupNames(String regex) {
        Set<String> namedGroups = new TreeSet<String>();

        Matcher m = namedGroupPattern.matcher(regex);

        while (m.find()) {
            namedGroups.add(m.group(1));
        }

        return namedGroups;
    }
    
}
