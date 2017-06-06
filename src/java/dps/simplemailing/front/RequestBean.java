/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateful;
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
    
    
    
}
