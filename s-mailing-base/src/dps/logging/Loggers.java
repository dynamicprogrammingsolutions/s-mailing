package dps.logging;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Loggers {

    Map<String,Logger> loggers = new HashMap<>();

    private Loggers() {}
    private static Loggers INSTANCE = new Loggers();
    static Loggers getInstance() { return INSTANCE; }

    public Logger getLogger(String subsystem) {
        Logger logger = loggers.get(subsystem);
        if (logger == null) {
            logger = Logger.getLogger(subsystem);
            loggers.put(subsystem,logger);
        }
        return logger;
    }

}
