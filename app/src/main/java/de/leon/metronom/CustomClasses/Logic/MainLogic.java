package de.leon.metronom.CustomClasses.Logic;

import android.graphics.Color;
import android.media.MediaPlayer;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import de.leon.metronom.CustomClasses.BpmList.BpmList;
import de.leon.metronom.CustomClasses.CustomExceptions.BpmNegativeException;
import de.leon.metronom.CustomClasses.CustomExceptions.BpmNotAnIntegerException;
import de.leon.metronom.CustomClasses.CustomExceptions.BpmNullException;
import de.leon.metronom.CustomClasses.CustomExceptions.TactNegativeException;
import de.leon.metronom.CustomClasses.CustomExceptions.TactNotAnIntegerException;
import de.leon.metronom.CustomClasses.DataHolder.CountDownInputHolder;
import de.leon.metronom.CustomClasses.Timer.MetronomeTimer;
import de.leon.metronom.MainActivity;

public class MainLogic {

    private int bpm = 0;
    private int tact = 4;
    private int cntTick = 1;
    private int cntListEntry = 1;
    private long increaseBpmAfterInMs = 0;
    private int increaseBpmByInt = 0;

    private boolean isPaused = false;
    private boolean isRunning = false;

    private BpmList currentBpmList;

    MediaPlayer mediaPlayer;
    MetronomeTimer bpmTimer = new MetronomeTimer();
    MetronomeTimer increaseBpmTimer = new MetronomeTimer();

    private final MainActivity activity;

    public MainLogic() {
    }

    /**
     * start the metronom
     *
     * @param bpmInput BPM user input
     * @param tactInput Tact user input
     * @throws BpmNotAnIntegerException When BPM is not a number
     * @throws BpmNullException When BPM is Null (this should not happen)
     * @throws BpmNegativeException When BPM is 0 or smaller
     * @throws TactNotAnIntegerException When the tact is not a number
     * @throws TactNegativeException When the tact us 0 or smaller
     */
    public void start(@NotNull String bpmInput, String tactInput) throws TactNegativeException, TactNotAnIntegerException, BpmNotAnIntegerException, BpmNegativeException, BpmNullException {
        start(bpmInput, tactInput, false);
    }

    /**
     * start the metronom
     *
     * @param bpmInput BPM user input
     * @param increase If bpm should increase
     * @throws BpmNotAnIntegerException When BPM is not a number
     * @throws BpmNullException When BPM is Null (this should not happen)
     * @throws BpmNegativeException When BPM is 0 or smaller
     * @throws TactNotAnIntegerException When the tact is not a number
     * @throws TactNegativeException When the tact us 0 or smaller
     */
    public void start(@NotNull String bpmInput, boolean increase) throws TactNegativeException, TactNotAnIntegerException, BpmNotAnIntegerException, BpmNegativeException, BpmNullException {
        start(bpmInput, 4 + "", increase);
    }

    /**
     * start the metronom
     *
     * @param bpmInput BPM user input
     * @throws BpmNotAnIntegerException When BPM is not a number
     * @throws BpmNullException When BPM is Null (this should not happen)
     * @throws BpmNegativeException When BPM is 0 or smaller
     * @throws TactNotAnIntegerException When the tact is not a number
     * @throws TactNegativeException When the tact us 0 or smaller
     */
    public void start(@NotNull String bpmInput) throws TactNegativeException, TactNotAnIntegerException, BpmNotAnIntegerException, BpmNegativeException, BpmNullException {
        start(bpmInput, 4 + "", false);
    }

    /**
     * start the metronom
     *
     * @param bpmInput BPM user input
     * @param tactInput Tact user input
     * @param increase If bpm should increase
     * @throws BpmNotAnIntegerException When BPM is not a number
     * @throws BpmNullException When BPM is Null (this should not happen)
     * @throws BpmNegativeException When BPM is 0 or smaller
     * @throws TactNotAnIntegerException When the tact is not a number
     */
    public void start(@NotNull String bpmInput, String tactInput, boolean increase) throws BpmNotAnIntegerException, BpmNullException, BpmNegativeException, TactNotAnIntegerException {

        if (!bpmInput.isEmpty()) {

            try {
                bpm = Integer.parseInt(bpmInput);
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
                throw new BpmNotAnIntegerException();
            }

            if (bpm <= 0) {
                throw new BpmNegativeException();
            }

            if (tactInput.isEmpty()) {
                tact = 4;
            } else {
                try {
                    tact = Integer.parseInt(tactInput);
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                    throw new TactNotAnIntegerException();
                }
                return;
            }

            if (increase) {
                increaseBPM();
            }

            countDown();

            //todo led
            progressBar.setMax(10 * tact);
            setProgressBarColor(Color.RED);

            bpmTimer.scheduleAtFixedRate(firstTick(), 0, 60000 / bpm);
            isRunning = true;

        } else {
            throw new BpmNullException();
        }

    }

    public PauseButtonStatus pause() {

        if (isPaused) {
            start();
            bpmTimer.startNormalTimer();
            isPaused = false;
            return PauseButtonStatus.pauseButton;
        } else {
            bpmTimer.cancel();
            increaseBpmTimer.cancel();
            cntTick = 1;
            isPaused = true;
            return PauseButtonStatus.resumeButton;
        }
    }


    private void countDown() {

        if (CountDownInputHolder.getInstance().getDuration() > 0) {
            countDownInMs = CountDownInputHolder.getInstance().getDuration();

            countDownTimer.scheduleAtFixedRate(pauseTimerTask(), 0, countDownInMs, this);
        }
    }

    private void increaseBPM() {

        increaseBpmTimer.scheduleAtFixedRate(increaseBpmTimerTask(), 0, increaseBpmAfterInMs);
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public int getTact() {
        return tact;
    }

    public void setTact(int tact) {
        this.tact = tact;
    }

    public long getCountDownInMs() {
        return countDownInMs;
    }

    public void setCountDownInMs(long countDownInMs) {
        this.countDownInMs = countDownInMs;
    }

    public MetronomeTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(MetronomeTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

    //Enums
    public enum PauseButtonStatus {
        pauseButton,
        resumeButton;
    }
}
