package dps.simplemailing.front.forms;

/**
 *
 * @author ferenci84
 */
public class TextArea extends Input {

    public TextArea(String label, String name, String value) {
        super(label, name, value);
        this.type = "textarea";
    }
    
}
