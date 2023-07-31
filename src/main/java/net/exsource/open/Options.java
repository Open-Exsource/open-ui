package net.exsource.open;

public final class Options {

    private String openglVersion;
    private int nanoVGVersion;
    private int maxThreads;

    public Options() {
        this.openglVersion = "4.6";
        this.nanoVGVersion = 3;
        this.maxThreads = 5;
    }

    public void setOpenglVersion(String openglVersion) {
        this.openglVersion = openglVersion;
    }

    public void setNanoVGVersion(int nanoVGVersion) {
        this.nanoVGVersion = nanoVGVersion;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public String getOpenglVersion() {
        return openglVersion;
    }

    public int getNanoVGVersion() {
        return nanoVGVersion;
    }

    public int getMaxThreads() {
        return maxThreads;
    }
}
