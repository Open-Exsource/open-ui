package net.exsource.openui.enums;

public enum Errors {

    UNKNOWN(-1, "The program throw an unknown error..."),
    OK(0, "The program have no errors or warnings."),
    GLFW_GL_THREAD(304, "OpenGL can't create in current thread."),
    PROGRAM_ARGS(101, "Program arguments wasn't not correct."),
    WINDOW_NOT_CONTAINS_NVG(94, "NanoVG can't find an mem address ID!"),
    WINDOW_BUILD(204, "Can't build window");

    private final int code;
    private final String description;

    Errors(final int code, final String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Errors get(int code) {
        Errors errors = UNKNOWN;
        for(Errors error : values()) {
            if(error.getCode() == code) {
                errors = error;
                break;
            }
        }
        return errors;
    }
}
