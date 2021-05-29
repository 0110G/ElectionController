package com.electionController.logger;

import com.electionController.constants.ControllerOperations;

public class FileLogger {
    private String filePath = "defaultFilePath";

    FileLogger(final String filePath) {
        this.filePath = filePath;
    }

    static void Log(final String message) {
        // Write message to specified file
    }

    public static void Log(final ControllerOperations controllerOperation, final String message, final Object... params) {
        StringBuilder logMessage = new StringBuilder("[LOGGER] " +
                "[" + controllerOperation.getBaseController() + "::" + controllerOperation.getController() + "] " +
                message + " [Params]: ");
        for (Object param : params) {
            logMessage.append(param.toString());
        }
        // Write logMessage to specified file
    }
}
