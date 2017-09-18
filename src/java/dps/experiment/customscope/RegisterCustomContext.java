/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.experiment.customscope;

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

/**
 *
 * @author ferenci84
 */
public class RegisterCustomContext implements Extension, Serializable {
    public void afterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager manager) {
        System.out.println("Adding context");
        event.addContext(CustomContext.getInstance());
    }
}
