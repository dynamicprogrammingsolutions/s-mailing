package dps.simplemailing.front;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author ferenci84
 */
//@Stateful
@SessionScoped
public class SessionBean implements Serializable {

    private static final long serialVersionUID = 7785231949369369798L;
    
    private List<String> errors = new LinkedList<>();
    private List<String> messages = new LinkedList<>();
    private Integer resultsPerPage = 10;

    public List<String> getErrors() {
        List<String> errors = new LinkedList<>();
        errors.addAll(this.errors);
        this.errors.clear();
        return errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public List<String> getMessages() {
        List<String> messages = new LinkedList<>();
        messages.addAll(this.messages);
        this.messages.clear();
        return messages;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }    

   public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }
    
     
}
