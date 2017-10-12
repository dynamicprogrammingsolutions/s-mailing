package dps.simplemailing.admin.views;

import javax.enterprise.context.RequestScoped;

/**
 *
 * @author ferenci84
 */
//@Stateful
@RequestScoped
public class RequestBean {
    
    private String title = "";
    private String root = "";
    private String template = null;
    private String viewRoot = "";
    private Class<?> entityClass = null;
    private String entityName = "";
    private Object entityObject = null;
    private String resourceRoot = "";
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }   

    public String getViewRoot() {
        return viewRoot;
    }

    public void setViewRoot(String viewRoot) {
        this.viewRoot = viewRoot;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Object getEntityObject() {
        return entityObject;
    }

    public void setEntityObject(Object entityObject) {
        this.entityObject = entityObject;
    }


    public void serResourceRoot(String root) {
        resourceRoot = root;
    }

    public String getResourceRoot() {
        return resourceRoot;
    }
}
