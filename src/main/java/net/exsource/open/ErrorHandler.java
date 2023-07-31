package net.exsource.open;

import net.exsource.open.enums.Errors;
import net.exsource.openlogger.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Class to handle global errors, this handler will be shutdown the {@link OpenUI}
 * project if there is a fatal error. The handler need an {@link Errors} code and description
 * to work. If the {@link Errors#isNeedStop()} returning true the program will call
 * {@link System#exit(int)} with the given {@link Errors#getId()} code.
 * <p>
 * @apiNote The code format is not useless because it has a pattern. The pattern is as example:
 * {@link Errors#GLFW_INIT} 7 = G, 3 = C, 9 = I. This means GLFW_CANT_INITIALIZE. Please make sure
 * that you use {@link #handle(int)} for own error codes and use the correct pattern.
 * @since 1.0.0
 * @see Errors
 * @see System
 * @author Daniel Ramke
 */
public final class ErrorHandler {

    private static final Logger logger = Logger.getLogger();

    /**
     * Function called the {@link #handle(Errors)} function and give the id as converted {@link Enum} code.
     * @param code the id of the error this means an {@link Integer} value.
     * @see Errors
     */
    public static void handle(int code) {
        handle(Errors.get(code));
    }

    /**
     * Function to handle errors, if the error need
     * to stop the execution of the program then call this function.
     * This will call {@link System#exit(int)} and give the current program status.
     * The status is {@link Errors#getId()} which is display by the exit command.
     * @param code the code as {@link Enum} from {@link Errors} enum.
     */
    public static void handle(@NotNull Errors code) {
        if(code.isNeedStop()) {
            System.exit(code.getId());
            return;
        }

        logger.error("Correct handle of error " + code.getId());
        logger.error("Description of " + code.getId() + " - " + code.getDescription());
    }

}
