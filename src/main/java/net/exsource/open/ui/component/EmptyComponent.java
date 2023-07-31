package net.exsource.open.ui.component;

import net.exsource.open.ui.component.shapes.Rectangle;

public class EmptyComponent extends Component {

    /**
     * Constructor for creating new {@link Component}'s. This constructor
     * is absolute needed to create a new component instance. For example,
     * you create an {@link Rectangle} an inherit {@link Component} to this object
     * to declared that this object is a component.
     */
    public EmptyComponent() {
        super("empty");
    }
}
