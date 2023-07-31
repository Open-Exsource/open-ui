package net.exsource.open.ui.component.control;

import net.exsource.open.enums.geometry.Alignment;
import net.exsource.open.ui.component.Component;
import net.exsource.open.ui.font.Font;
import net.exsource.open.ui.style.generic.FontLook;

public interface Labeled {

    void setText(String text);

    String getText();

    void setWarping(boolean warping);

    boolean isWarping();

    void update();

    boolean isUpdated();

    int getMaxRows();

    void setFont(String name);

    void setFont(Font font);

    Font getFont();

    void setLook(FontLook look);

    FontLook getLook();

    double getTextPositionX();

    double getTextPositionY();

    void setTextAbsolute(double x, double y);

    double getTextAbsoluteX();

    double getTextAbsoluteY();

    double getLableWidth();

    double getLableHeight();

    void setTextAlignment(Alignment alignment);

    Alignment getTextAlignment();

    Component getHolder();

}
