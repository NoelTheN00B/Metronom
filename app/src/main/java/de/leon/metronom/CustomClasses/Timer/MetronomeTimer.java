package de.leon.metronom.CustomClasses.Timer;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.leon.metronom.MainActivity;
import de.leon.metronom.R;

public class MetronomeTimer extends Timer {

    private TimerTask task;
    private CountDownTimer countDownTimer;
    private long delay;
    private long period;
    private MainActivity mainActivity;

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

    public void scheduleAtFixedRate(TimerTask task, long delay, long period, MainActivity mainActivity) {
        scheduleAtFixedRate(task, delay, period);
        this.mainActivity = mainActivity;

        countDownTimer = new CountDownTimer(period, 77) {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onTick(long millisUntilFinished) {
                mainActivity.getRemainingTime().setText(new SimpleDateFormat("mm:ss:SS").format(new Date(millisUntilFinished)));
            }

            @Override
            public void onFinish() {
                mainActivity.getRemainingTime().setText(mainActivity.getString(R.string.done));
            }
        }.start();
    }

    public void setDelay(long delay) {
        scheduleAtFixedRate(this.task, delay, this.period);
        this.delay = delay;
    }

    public void setPeriod(long period) {
        scheduleAtFixedRate(this.task, this.delay, period);
        this.period = period;
    }

    public void setTask(TimerTask task) {
        scheduleAtFixedRate(task, this.delay, this.period);
        this.task = task;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void startNormalTimer() {
        scheduleAtFixedRate(task, delay, period);
    }

    public void startCountDownTimer() {
        scheduleAtFixedRate(task, delay, period, mainActivity);
    }
}
