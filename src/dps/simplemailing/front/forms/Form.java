package dps.simplemailing.front.forms;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ferenci84
 */
public class Form {
    protected String action;
    protected String submitLabel = "Submit";

    public Form(String action) {
        this.action = action;
    }
    
    private List<Input> inputs = new LinkedList<>();

    public List<Input> getInputs() {
        return inputs;
    }

    public void addInput(Input input)
    {
        inputs.add(input);
    }

    public String getSubmitLabel() {
        return submitLabel;
    }

    public void setSubmitLabel(String submitLabel) {
        this.submitLabel = submitLabel;
    }

    public String getAction() {
        return action;
    }
    
}
