package dps.router;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
@Dependent
public class Router {
    @Inject Instance<ControllerBase> controllers;

    public Boolean process(String path, HttpServletRequest request, HttpServletResponse response) {
        for(ControllerBase controller: controllers) {
            if (processController(controller,path,request,response)) return true;
        }
        return false;
    }
    
    public Boolean process(Class<? extends ControllerBase> controllerClass, String path, HttpServletRequest request, HttpServletResponse response) {
        for(ControllerBase controller: controllers) {
            if (controllerClass.isInstance(controller)) {
                if (processController(controller,path,request,response)) return true;
            }
        }
        return false;
    }
    
    public Boolean processController(ControllerBase controller, String path, HttpServletRequest request, HttpServletResponse response) {
        if (path == null) path = "";

        Class<?> controllerClass = controller.getClass().getSuperclass();
        Path pathAnnotation = controllerClass.getAnnotation(Path.class);
        if (pathAnnotation == null) return false;

        Matcher controllerMatches = checkPath(path,pathAnnotation);
        if (controllerMatches == null) return false;

        int pathGroup = pathAnnotation.pathGroup();
        String nextPath;
        nextPath = controllerMatches.group(pathGroup);

        Method[] methods = controllerClass.getMethods();
        
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
    
    public Boolean processAction(ControllerBase controller, Method method, Method filterMethod, String path, Matcher controllerMatches, HttpServletRequest request, HttpServletResponse response) {
        Path pathAnnotation = method.getAnnotation(Path.class);
        if (pathAnnotation == null) return false;
        
        Matcher actionMatches = checkPath(path,pathAnnotation);
        if (actionMatches == null) return false;
        
        Object[] args = resolveParameters(method, actionMatches, controllerMatches, request, response);
        
        Object[] filterArgs = null;
        
        if (filterMethod != null) {
            filterArgs = resolveFilterParameters(filterMethod, actionMatches, controllerMatches, request, response, controller, method, args);
        }
        
        try {
            if (filterMethod != null) {
                filterMethod.invoke(controller, filterArgs);
            } else {
                method.invoke(controller,args);
            }
            return true;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
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
                    args[i] = convertParam(parameter,actionMatches.group(paramName));
                } else {
                    args[i] = convertParam(parameter,actionMatches.group(group));
                }
            } else if (parameter.isAnnotationPresent(ControllerParam.class)) {
                ControllerParam paramAnnotation = parameter.getAnnotation(ControllerParam.class);
                String paramName = paramAnnotation.value();
                int group = paramAnnotation.group();
                if (!paramName.isEmpty()) {
                    args[i] = convertParam(parameter,controllerMatches.group(paramName));
                } else {
                    args[i] = convertParam(parameter,controllerMatches.group(group));
                }
            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam paramAnnotation = parameter.getAnnotation(RequestParam.class);
                String paramName = paramAnnotation.value();
                String value = request.getParameter(paramName);
                if (value != null) {
                    args[i] = convertParam(parameter,value);
                }
            }
        }
        return args;
    }
    
    Object[] resolveFilterParameters(Method method, Matcher actionMatches, Matcher controllerMatches,
            HttpServletRequest request, HttpServletResponse response,
            ControllerBase dispatchController, Method dispatchMethod, Object[] dispatchArgs)
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
            } else if (parameter.getParameterizedType().equals(ControllerBase.class)) {
                args[i] = dispatchController;
            } else if (parameter.isAnnotationPresent(Param.class)) {
                Param paramAnnotation = parameter.getAnnotation(Param.class);
                String paramName = paramAnnotation.value();
                int group = paramAnnotation.group();
                if (!paramName.isEmpty()) {
                    args[i] = convertParam(parameter,actionMatches.group(paramName));
                } else {
                    args[i] = convertParam(parameter,actionMatches.group(group));
                }
            } else if (parameter.isAnnotationPresent(ControllerParam.class)) {
                ControllerParam paramAnnotation = parameter.getAnnotation(ControllerParam.class);
                String paramName = paramAnnotation.value();
                int group = paramAnnotation.group();
                if (!paramName.isEmpty()) {
                    args[i] = convertParam(parameter,controllerMatches.group(paramName));
                } else {
                    args[i] = convertParam(parameter,controllerMatches.group(group));
                }
            }
        }
        return args;
    }
    
    Object convertParam(Parameter parameter, String value)
    {
        if (value == null) return null;
        Class<?> type = parameter.getType();
        if (type.equals(String.class)) return value;
        if (type.equals(Integer.class)) return Integer.parseInt(value);
        if (type.equals(Long.class)) return Long.parseLong(value);
        return null;
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
        Set<String> namedGroups = new TreeSet<>();

        Matcher m = namedGroupPattern.matcher(regex);

        while (m.find()) {
            namedGroups.add(m.group(1));
        }

        return namedGroups;
    }
    
}
