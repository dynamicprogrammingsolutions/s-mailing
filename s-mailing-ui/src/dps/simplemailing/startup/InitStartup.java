package dps.simplemailing.startup;

import dps.commons.startup.StartupInit;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

@Singleton
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
public class InitStartup {

    @Inject
    StartupInit startupInit;

    @PostConstruct
    public void init()
    {
        startupInit.init();
    }

}
