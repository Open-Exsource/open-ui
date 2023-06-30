package net.exsource.openui.exception;

public class WindowNotThreadedException extends IllegalArgumentException {

    public WindowNotThreadedException(String message) {
        super(message);
    }

}
