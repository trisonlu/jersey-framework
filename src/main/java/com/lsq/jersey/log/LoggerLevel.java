package com.lsq.jersey.log;

import lombok.Getter;

import java.util.logging.Level;

/**
 * Created by trison on 2018/5/18.
 */
@Getter
public enum LoggerLevel {

    TRACE(Level.ALL),
    DEBUG(Level.CONFIG),
    INFO(Level.INFO),
    WARN(Level.WARNING),
    ERROR(Level.SEVERE);

    private Level julLevel;

    LoggerLevel(Level julLevel) {
        this.julLevel = julLevel;
    }
}
