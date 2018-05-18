package com.lsq.jersey.log;

import java.util.logging.Logger;

/**
 * Created by trison on 2018/5/18.
 */
public class JulRouter {
    private static String loggerName = JulRouter.class.getPackage().getName();
    private static Logger logger = Logger.getLogger(loggerName);
    private static void writeLogs() {
        logger.warning("this the warining message");
        logger.severe("this the severe message");
        logger.info("this the info message");
        logger.finest("this the finest message");
    }
    public static void main(String[] args) {
        Thread.currentThread().setName("JUL-Thread");
        JulLoggerWrapper wrapper = new JulLoggerWrapper(loggerName);
        wrapper.updateLevel(LoggerLevel.DEBUG);
        System.out.println("slf4j print===========");
        wrapper.install();
        writeLogs();
        System.out.println("jul print===========");
        wrapper.uninstall();
        writeLogs();
    }
}
