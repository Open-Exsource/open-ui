package net.exsource.open.enums;

public enum Errors {

    UNKNOWN(-1, false, "There was an unexpected error!"),
    NO_MAIN(14136, true, "There was no main located by execution try of OpenUI!"),
    WINDOW_GENERATION_FAILED(2376, true, "There was a generation error by try to generate a new window!"),
    WINDOW_NOT_CONTAINS_NVG(2314314, true, "The given window doesn't contains a NanoVG context!"),
    UI_NO_COMPONENT(21143, true, "The given ui element dosen't extended Component.class!"),
    GLFW_INIT(739, true, "There was no glfw context in the current thread or it can't be initialize glfw!");

    private final int id;
    private final boolean needStop;
    private final String description;

    Errors(int id, boolean needStop, String description) {
        this.id = id;
        this.needStop = needStop;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public boolean isNeedStop() {
        return needStop;
    }

    public String getDescription() {
        return description;
    }

    public static Errors get(int id) {
        Errors code = UNKNOWN;
        for(Errors codes : values()) {
            if(codes.getId() == id) {
                code = codes;
                break;
            }
        }
        return code;
    }

}
