/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.experiment.customscope;

import javax.annotation.PostConstruct;

/**
 *
 * @author ferenci84
 */
@Custom
public class CustomClass {
    
    @PostConstruct
    public void init() {
        System.out.println("CustomClass initialized");
    }
    
    public void call() {
        System.out.println("Called CustomClass");
    }
}
