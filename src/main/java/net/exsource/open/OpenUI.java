package net.exsource.open;

import net.exsource.open.annotation.AnnotationProcessor;
import net.exsource.open.annotation.start.Initialization;
import net.exsource.open.annotation.start.PostInitialization;
import net.exsource.open.annotation.start.PreInitialisation;
import net.exsource.open.enums.Errors;
import net.exsource.openlogger.Logger;
import net.exsource.openlogger.level.LogLevel;
import net.exsource.openlogger.util.ConsoleColor;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.0.0
 * @author Daniel Ramke
 */
@SuppressWarnings("unused")
public final class OpenUI {

    private static final Logger logger = Logger.getLogger();

    private static boolean used;
    private static Class<?> caller;

    private static final Options options = new Options();
    private static final List<String> programArguments = new ArrayList<>();

    /**
     * Function setups the called program for {@link GLFW} usage.
     * This is absolute needed to work with our {@link OpenUI} library.
     * If you don't call this function, it will come to errors if you try to generate a window
     * or something from the library which used opengl stuff. The best location to
     * call this function is in your main function. Don't worry it will not stop your working program
     * because {@link OpenUI} worked with own {@link Thread}'s.
     * @param args the program arguments can be give by your main class.
     * @see GLFW
     * @see Thread
     */
    public static void launch(String[] args) {
        if(used) {
            logger.warn("OpenUI is already used by class " + (getCaller() != null ? getCaller().getSimpleName() : "?"));
            return;
        }
        Class<?> mainClass = getMainClass();
        if(mainClass == null) {
            logger.fatal("Stopping executing OpenUI in unknown class... see the logs for more information");
            ErrorHandler.handle(Errors.NO_MAIN);
            return;
        }
        logger.info("Launch OpenUI for " + mainClass.getSimpleName() + "...");

        handleProgramArguments(args);
        AnnotationProcessor.invoke(mainClass, PreInitialisation.class);
        if(!GLFW.glfwInit()) {
            logger.fatal("Can't initialize GLFW in thread [ " + Thread.currentThread().getName() + " ]");
            ErrorHandler.handle(Errors.GLFW_INIT);
            return;
        }
        logger.info("Initialize GLFW successfully in thread [ " + Thread.currentThread().getName() + " ]");
        AnnotationProcessor.invoke(mainClass, Initialization.class);
        handleInformation();
        caller = mainClass;
        used = true;
        AnnotationProcessor.invoke(mainClass, PostInitialization.class);
    }

    /**
     * Function to filter the current caller class (Main.class) {@link Class}.
     * This function can return null if no main class was found. This is a
     * fatal error and need to be handel. The program will stop executing {@link OpenUI}!
     * @return {@link Class} - the founded main class, can be null.
     */
    public static Class<?> getMainClass() {
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

    /**
     * @return {@link Boolean} - true if the library currently in use.
     */
    public static boolean isUsed() {
        return used;
    }

    /**
     * @return {@link Boolean} - true if the program used arguments.
     */
    public static boolean hasActiveArguments() {
        return !programArguments.isEmpty();
    }

    /**
     * Function returned the current caller which used the {@link #launch(String[])} function.
     * The caller can be null if the {@link #launch(String[])} not be called.
     * @return {@link Class} - the current caller as {@link Class} object.
     */
    public static Class<?> getCaller() {
        return caller;
    }

    /**
     * Function to return the current {@link Options} object which is by default initialized.
     * This object is absolute needed because it stores all options of {@link OpenUI}.
     * @return {@link Options} - the {@link Options} object.
     */
    public static Options getOptions() {
        return options;
    }

    /**
     * @return {@link List} - list which contains all loaded and used program arguments.
     */
    public static List<String> getProgramArguments() {
        return programArguments;
    }

    /**
     * Private function to check the reading program arguments and set it to the {@link Options} object.
     * This function is called in {@link #launch(String[])} to check the given array.
     * We count the invalid arguments and send at the end a report to the log with {@link Logger}.
     * @param args the args which contains arguments.
     */
    private static void handleProgramArguments(String[] args) {
        if(args == null || args.length == 0) {
            logger.info("Skipping program arguments check...");
            return;
        }
        logger.info("Starting argument check...");

        int invalid = 0;
        for(String argument : args) {
            if(!argument.contains("=")) {
                invalid++;
                continue;
            }
            String[] split = argument.split("=");
            String key = split[0];
            String value = split[1];

            switch (key) {
                case "gl-version" -> {
                    options.setOpenglVersion(value);
                }
                case "nvg-version" -> {
                    try {
                        options.setNanoVGVersion(Integer.parseInt(value));
                    } catch (NumberFormatException exception) {
                        options.setNanoVGVersion(3);
                    }
                }
                case "max-threads" -> {
                    try  {
                        options.setMaxThreads(Integer.parseInt(value));
                    } catch (NumberFormatException exception) {
                        options.setMaxThreads(5);
                    }
                }
                default -> {
                    logger.warn("Key: " + key + ", not in use by OpenUI! Look at our website for more information!");
                }
            }
            if(programArguments.contains(key))
                continue;

            programArguments.add(key);
        }

        if(invalid > 0) {
            logger.warn("There was " + invalid + " invalid program arguments found!");
        }

        logger.info("Successfully ends with program arguments, found [" + invalid + "] Invalid arguments!");
    }

    /**
     * Function to print general information about this library and his dependencies to the console.
     */
    private static void handleInformation() {
        List<String> tmp = new ArrayList<>();
        tmp.add("LWJGL Version, " + Version.getVersion().substring(0, 5));
        tmp.add("OpenUI Version, " + "1.0.0");
        tmp.add(" ");
        tmp.add("Arguments Valid, " + programArguments.size());
        logger.list(tmp, "General Information OpenUI", ConsoleColor.GREEN, LogLevel.INFO);
    }
}
