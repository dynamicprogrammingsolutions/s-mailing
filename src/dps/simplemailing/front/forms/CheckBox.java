package dps.simplemailing.front.forms;

/**
 *
 * @author ferenci84
 */
public class CheckBox extends Input {

    Boolean checked;
    
    public CheckBox(String label, String name, Boolean checked) {
        super(label, name, "");
        this.checked = checked;
        this.type = "checkbox";
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
    
}
