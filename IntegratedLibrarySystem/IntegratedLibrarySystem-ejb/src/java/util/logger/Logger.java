/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.logger;

import java.util.logging.Level;

/**
 *
 * @author Marcus
 */
public class Logger {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Logger.class.getName());
    
    public static final Level SEVERE = Level.SEVERE;
    public static final Level WARN = Level.WARNING;
    public static final Level INFO = Level.INFO;
    
    public static void log(Level level, String className, String methodName, String message) {
        String output = "[" + className + "] " + methodName + " -> " + message;
        LOGGER.log(level, output);
    }
    
    public static void log(Level level, String className, String methodName) {
        String output = "[" + className + "] " + methodName;
        LOGGER.log(level, output);
    }
}
