package net.exsource.open.ui.component.shapes;

import net.exsource.open.ui.component.Component;

public class Rectangle extends Component {

    /**
     * Constructor for creating new {@link Component}'s. This constructor
     * is absolute needed to create a new component instance. For example,
     * you create an {@link Rectangle} an inherit {@link Component} to this object
     * to declared that this object is a component.
     *
     * @param localizedName the wish identifier name.
     */
    public Rectangle(String localizedName) {
        super(localizedName);
        this.setSize(200, 100);
    }

}
