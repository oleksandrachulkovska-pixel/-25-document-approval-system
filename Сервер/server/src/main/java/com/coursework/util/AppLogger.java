package com.coursework.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppLogger {

    private static final Logger LOGGER =
            Logger.getLogger("DocumentApprovalSystem");

    static {
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);

        LOGGER.addHandler(consoleHandler);
    }

    private AppLogger() {
        // Забороняємо створення об'єктів цього класу
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}