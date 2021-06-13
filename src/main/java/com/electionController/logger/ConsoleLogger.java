package com.electionController.logger;

import com.electionController.constants.ControllerOperation;

/*
* Logs a message on the console
*/
public class ConsoleLogger {

    // Logs a general message
    public static void Log(String logMessage) {
        System.out.println(logMessage);
    }

    public static void Log(final ControllerOperation controllerOperation, final String message, final Object... params) {
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
