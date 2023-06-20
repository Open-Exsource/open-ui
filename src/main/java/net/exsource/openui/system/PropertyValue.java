package net.exsource.openui.system;

public record PropertyValue(Object value) {

    public String asString() {
        return (String) value;
    }

    public Boolean asBoolean() {
        return (Boolean) value;
    }

    public Integer asInt() {
        return (Integer) value;
    }

    public Double asDouble() {
        return (Double) value;
    }

    public Long asLong() {
        return (Long) value;
    }

    public Float asFloat() {
        return (Float) value;
    }

    public Byte asByte() {
        return (Byte) value;
    }

    public Short asShort() {
        return (Short) value;
    }

}
