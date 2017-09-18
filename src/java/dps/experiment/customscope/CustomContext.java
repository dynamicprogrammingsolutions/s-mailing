/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.experiment.customscope;

import java.lang.annotation.Annotation;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;


/**
 *
 * @author ferenci84
 */
public class CustomContext implements Context {
    private final static CustomContext INSTANCE = new CustomContext();
    private CustomContext() {
        
    }
    public static CustomContext getInstance() {
        return INSTANCE;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return Custom.class;
    }

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isActive() {
        return true;
    }

    
    
}
