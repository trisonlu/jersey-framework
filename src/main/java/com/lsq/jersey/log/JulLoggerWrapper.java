package com.lsq.jersey.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by trison on 2018/5/18.
 */
public class JulLoggerWrapper extends Handler {

    // SEVERE > WARNING > INFO > CONFIG > FINE > FINER > FINEST
    // ERROR > WARN > INFO > DEBUG
    private static final int TRACE_LEVEL_THRESHOLD = Level.FINEST.intValue() - 1;
    private static final int DEBUG_LEVEL_THRESHOLD = Level.CONFIG.intValue();
    private static final int INFO_LEVEL_THRESHOLD = Level.INFO.intValue();
    private static final int WARN_LEVEL_THRESHOLD = Level.WARNING.intValue();

    private List<Handler> julHandlers;
    private boolean julUseParentHandlers = false;
    private Level julLevel;
    private Level targetLevel;
    private String name;

    public JulLoggerWrapper(String name) {
        this.name = name;
    }

    /**
     * install the wrapper
     */
    public void install() {
        java.util.logging.Logger julLogger = this.getJulLogger();

        // remove old handlers
        julHandlers = new ArrayList<Handler>();
        for (Handler handler : julLogger.getHandlers()) {
            julHandlers.add(handler);
            julLogger.removeHandler(handler);
        }

        // disable parent handler
        this.julUseParentHandlers = julLogger.getUseParentHandlers();
        julLogger.setUseParentHandlers(false);

        // record previous level
        this.julLevel = julLogger.getLevel();
        if (this.targetLevel != null) {
            julLogger.setLevel(this.targetLevel);
        }

        // install wrapper
        julLogger.addHandler(this);
    }

    /**
     * uninstall the wrapper
     */
    public void uninstall() {
        java.util.logging.Logger julLogger = this.getJulLogger();

        // uninstall wrapper
        for (Handler handler : julLogger.getHandlers()) {
            if (handler == this) {
                julLogger.removeHandler(handler);
            }
        }

        // recover work..
        julLogger.setUseParentHandlers(this.julUseParentHandlers);

        if (this.julLevel != null) {
            julLogger.setLevel(julLevel);
        }

        if (this.julHandlers != null) {
            for (Handler handler : this.julHandlers) {
                julLogger.addHandler(handler);
            }
            this.julHandlers = null;
        }
    }

    private java.util.logging.Logger getJulLogger() {
        return java.util.logging.Logger.getLogger(name);
    }

    private Logger getWrappedLogger() {
        return LoggerFactory.getLogger(name);
    }

    /**
     * 更新级别
     *
     * @param targetLevel
     */
    public void updateLevel(LoggerLevel targetLevel) {
        if (targetLevel == null) {
            return;
        }

        updateLevel(targetLevel.getJulLevel());
    }

    /**
     * 更新级别
     *
     * @param targetLevel
     */
    public void updateLevel(Level targetLevel) {
        if (targetLevel == null) {
            return;
        }

        java.util.logging.Logger julLogger = this.getJulLogger();
        if (this.julLevel == null) {
            this.julLevel = julLogger.getLevel();
        }

        this.targetLevel = targetLevel;
        julLogger.setLevel(this.targetLevel);
    }

    @Override
    public void publish(LogRecord record) {
        // Silently ignore null records.
        if (record == null) {
            return;
        }

        Logger wrappedLogger = getWrappedLogger();
        String message = record.getMessage();

        if (message == null) {
            message = "";
        }

        if (wrappedLogger instanceof LocationAwareLogger) {
            callWithLocationAwareMode((LocationAwareLogger) wrappedLogger, record);
        } else {
            callWithPlainMode(wrappedLogger, record);
        }
    }

    /**
     * get the record's i18n message
     *
     * @param record
     * @return
     */
    private String getMessageI18N(LogRecord record) {
        String message = record.getMessage();

        if (message == null) {
            return null;
        }

        ResourceBundle bundle = record.getResourceBundle();
        if (bundle != null) {
            try {
                message = bundle.getString(message);
            } catch (MissingResourceException e) {
            }
        }
        Object[] params = record.getParameters();
        // avoid formatting when 0 parameters.
        if (params != null && params.length > 0) {
            try {
                message = MessageFormat.format(message, params);
            } catch (RuntimeException e) {
            }
        }
        return message;
    }

    private void callWithPlainMode(Logger slf4jLogger, LogRecord record) {

        String i18nMessage = getMessageI18N(record);
        int julLevelValue = record.getLevel().intValue();

        if (julLevelValue <= TRACE_LEVEL_THRESHOLD) {
            slf4jLogger.trace(i18nMessage, record.getThrown());
        } else if (julLevelValue <= DEBUG_LEVEL_THRESHOLD) {
            slf4jLogger.debug(i18nMessage, record.getThrown());
        } else if (julLevelValue <= INFO_LEVEL_THRESHOLD) {
            slf4jLogger.info(i18nMessage, record.getThrown());
        } else if (julLevelValue <= WARN_LEVEL_THRESHOLD) {
            slf4jLogger.warn(i18nMessage, record.getThrown());
        } else {
            slf4jLogger.error(i18nMessage, record.getThrown());
        }
    }

    private void callWithLocationAwareMode(LocationAwareLogger lal, LogRecord record) {
        int julLevelValue = record.getLevel().intValue();
        int slf4jLevel;

        if (julLevelValue <= TRACE_LEVEL_THRESHOLD) {
            slf4jLevel = LocationAwareLogger.TRACE_INT;
        } else if (julLevelValue <= DEBUG_LEVEL_THRESHOLD) {
            slf4jLevel = LocationAwareLogger.DEBUG_INT;
        } else if (julLevelValue <= INFO_LEVEL_THRESHOLD) {
            slf4jLevel = LocationAwareLogger.INFO_INT;
        } else if (julLevelValue <= WARN_LEVEL_THRESHOLD) {
            slf4jLevel = LocationAwareLogger.WARN_INT;
        } else {
            slf4jLevel = LocationAwareLogger.ERROR_INT;
        }
        String i18nMessage = getMessageI18N(record);
        lal.log(null, java.util.logging.Logger.class.getName(), slf4jLevel, i18nMessage, null,
                record.getThrown());
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() throws SecurityException {
        // TODO Auto-generated method stub

    }

}
