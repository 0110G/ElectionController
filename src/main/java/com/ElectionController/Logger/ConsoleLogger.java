package com.ElectionController.Logger;

import com.ElectionController.Constants.ControllerOperations;

/*
* Logs a message on the console
*/
public class ConsoleLogger {

    // Logs a general message
    public static void Log(String logMessage) {
        System.out.println(logMessage);
    }

    public static void Log(final ControllerOperations controllerOperation, final String message, final Object... params) {
        StringBuilder logMessage = new StringBuilder("[LOGGER] " +
                "[" + controllerOperation.getBaseController() + "::" + controllerOperation.getController() + "] " +
                message + " [Params]: ");
        for (Object param : params) {
            if (param == null) {
                logMessage.append("<null>");
            } else {
                logMessage.append(param.toString());
            }
        }
        System.out.println(logMessage.toString());
    }

}
