package de.leon.metronom.CustomClasses.Timer;

import java.util.Timer;
import java.util.TimerTask;

public class MetronomeTimer extends Timer {

    private TimerTask task;
    private long delay;
    private long period;

    public MetronomeTimer() {
        super();
    }

    @Override
    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        super.scheduleAtFixedRate(task, delay, period);
        this.task = task;
        this.delay = delay;
        this.period = period;
    }

    public void setDelay(long delay) {
        super.scheduleAtFixedRate(this.task, delay, this.period);
        this.delay = delay;
    }

    public void setPeriod(long period) {
        super.scheduleAtFixedRate(this.task, this.delay, period);
        this.period = period;
    }

    public void setTask(TimerTask task) {
        super.scheduleAtFixedRate(task, this.delay, this.period);
        this.task = task;
    }
}
