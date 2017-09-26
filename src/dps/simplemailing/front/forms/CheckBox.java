/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
