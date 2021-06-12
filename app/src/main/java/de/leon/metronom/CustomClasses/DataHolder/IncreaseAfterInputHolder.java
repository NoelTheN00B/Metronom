package de.leon.metronom.CustomClasses.DataHolder;

public class IncreaseAfterInputHolder {

    private long duration = 0;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    private static final IncreaseAfterInputHolder holder = new IncreaseAfterInputHolder();
    public static IncreaseAfterInputHolder getInstance() {
        return holder;
    }
}
