package de.leon.metronom.CustomClasses.DataHolder;

public class CountDownInputHolder {

    private long duration = 0;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    private static final CountDownInputHolder holder = new CountDownInputHolder();
    public static CountDownInputHolder getInstance() {
        return holder;
    }
}
