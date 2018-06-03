package dps.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface HasLogger {

    default Logger getLogger() {
        return Loggers.getInstance().getLogger(loggerName());
    }

    default String loggerName() {
        return this.getClass().getPackage().getName();
    }

    default void setLogLevel(Level level)
    {
        getLogger().setLevel(level);
    }

    default void log(Level level, String message)
    {
        getLogger().log(level,message);
    }

    default void log(Level level, String message, Throwable e)
    {
        getLogger().log(level,message,e);
    }

    default void logInfo(String message)
    {
        this.log(Level.INFO,message);
    }

    default void logWarning(String message)
    {
        this.log(Level.WARNING,message);
    }

    default void logWarning(String message, Throwable e)
    {
        this.log(Level.WARNING,message,e);
    }

    default void logSevere(String message)
    {
        this.log(Level.SEVERE,message);
    }

    default void logSevere(String message, Throwable e)
    {
        this.log(Level.SEVERE,message,e);
    }

    default void logFine(String message)
    {
        this.log(Level.FINE,message);
    }

    default void logFiner(String message)
    {
        this.log(Level.FINER,message);
    }

    default void logFinest(String message)
    {
        this.log(Level.FINEST,message);
    }

}
