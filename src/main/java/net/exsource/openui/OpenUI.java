package net.exsource.openui;

import net.exsource.openlogger.Logger;
import net.exsource.openlogger.util.ConsoleColor;
import net.exsource.openui.annotations.init.Init;
import net.exsource.openui.annotations.init.PostInit;
import net.exsource.openui.annotations.AnnotationProcessor;
import net.exsource.openui.annotations.init.PreInit;
import net.exsource.openui.events.UIPreLoadEvent;
import net.exsource.openutils.event.EventManager;
import net.exsource.openutils.io.IOController;
import net.exsource.openutils.io.controller.PropertiesController;

import java.io.IOException;

public final class OpenUI {

    private static final Logger logger = Logger.getLogger();

    private static boolean called;
    private static int errorCode;
    private static PropertiesController properties;

    public static void launch(String[] args) {
        if(getMainClass() == null) {
            throw new RuntimeException("Main class not found!");
        }

        String className = getMainClass().getSimpleName();
        if(called) {
            logger.warn("OpenUI is already initialized in class: " + className);
            return;
        }

        errorCode = 0;
        logger.info("Try to launch OpenUI in class: " + ConsoleColor.GREEN + className + ConsoleColor.RESET);
        AnnotationProcessor.invokeAnnotation(getMainClass(), PreInit.class);
        checkProgramArgs(args);
        EventManager.callEvent(new UIPreLoadEvent(getMainClass(), properties));
        AnnotationProcessor.invokeAnnotation(getMainClass(), Init.class);
        checkOpenGL();
        checkErrors();
        AnnotationProcessor.invokeAnnotation(getMainClass(), PostInit.class);
        called = true;
        logger.info("Successfully launch OpenUI in class: " + ConsoleColor.GREEN + className + ConsoleColor.RESET);
    }

    private static Class<?> getMainClass() {
        String classPath = null;
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if(trace.length > 0) {
            classPath = trace[trace.length -1].getClassName();
        }
        try {
            return Class.forName(classPath);
        }  catch (ClassNotFoundException exception) {
            logger.fatal(exception);
        }
        return null;
    }

    private static void checkProgramArgs(String[] args) {
        try {
            properties = IOController.fromArgs(args, PropertiesController.class);
        } catch (IOException exception) {
            errorCode = 101;
            logger.error(exception);
        }
    }

    private static void checkOpenGL() {

    }

    private static void checkErrors() {
        switch (errorCode) {
            case 0 -> {
                logger.info("Program have no errors! Status [ " + ConsoleColor.GREEN + errorCode + ConsoleColor.RESET + " ]");
            }
            case 101 -> {
                logger.warn("Program have wrong program arguments! Status [ " + ConsoleColor.YELLOW + errorCode + ConsoleColor.RESET + " ]");
            }
            case 304 -> {
                logger.fatal("Program can't find OpenGL [ " + ConsoleColor.RED_BOLD + errorCode + ConsoleColor.RESET + " ]");
                System.exit(errorCode);
            }
        }
    }

}
