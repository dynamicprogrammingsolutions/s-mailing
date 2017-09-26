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
public class TextArea extends Input {

    public TextArea(String label, String name, String value) {
        super(label, name, value);
        this.type = "textarea";
    }
    
}
