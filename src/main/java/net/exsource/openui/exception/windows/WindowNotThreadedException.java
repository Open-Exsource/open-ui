package net.exsource.openui.exception.windows;

public class WindowNotThreadedException extends IllegalArgumentException {

    public WindowNotThreadedException(String message) {
        super(message);
    }

}
