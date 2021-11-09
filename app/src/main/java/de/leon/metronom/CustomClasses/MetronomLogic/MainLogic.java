package de.leon.metronom.CustomClasses.MetronomLogic;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

import de.leon.metronom.CustomClasses.MetronomLogic.BpmList.BpmList;
import de.leon.metronom.CustomClasses.MetronomLogic.BpmList.ListEntry;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.BpmNegativeException;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.BpmNotAnIntegerException;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.BpmNullException;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.ListEndReachedException;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.NotAnIntegerException;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.TactNegativeException;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.TactNotAnIntegerException;

public class MainLogic {

    private final String firstTickFileName = "first_tick.wav";
    private final String otherTicksFileName = "other_ticks.wav";

    private int bpm = 0;
    private int tact = 4;
    private int cntTick = 1;
    private int cntListEntry = 1;
    private long increaseBpmAfterInMs = 0;
    private long countDownInMs = 0;
    private int increaseBpmByInt = 0;

    private boolean isPaused = false;
    private boolean isRunning = false;

    private BpmList currentBpmList;

    /**
     * Logic for Main class
     */
    public MainLogic() {
    }

    /**
     * start the metronom
     *
     * @param bpmInput  BPM user input
     * @param tactInput Tact user input
     * @throws BpmNotAnIntegerException  When BPM is not a number
     * @throws BpmNullException          When BPM is Null (this should not happen)
     * @throws BpmNegativeException      When BPM is 0 or smaller
     * @throws TactNotAnIntegerException When the tact is not a number
     * @throws TactNegativeException     When the tact us 0 or smaller
     */
    public void start(@NotNull String bpmInput, String tactInput) throws TactNegativeException, TactNotAnIntegerException, BpmNotAnIntegerException, BpmNegativeException, BpmNullException {
        start(bpmInput, tactInput, false);
    }

    /**
     * start the metronom
     *
     * @param bpmInput BPM user input
     * @param increase If bpm should increase
     * @throws BpmNotAnIntegerException  When BPM is not a number
     * @throws BpmNullException          When BPM is Null (this should not happen)
     * @throws BpmNegativeException      When BPM is 0 or smaller
     * @throws TactNotAnIntegerException When the tact is not a number
     * @throws TactNegativeException     When the tact us 0 or smaller
     */
    public void start(@NotNull String bpmInput, boolean increase) throws TactNegativeException, TactNotAnIntegerException, BpmNotAnIntegerException, BpmNegativeException, BpmNullException {
        start(bpmInput, 4 + "", increase);
    }

    /**
     * start the metronom
     *
     * @param bpmInput BPM user input
     * @throws BpmNotAnIntegerException  When BPM is not a number
     * @throws BpmNullException          When BPM is Null (this should not happen)
     * @throws BpmNegativeException      When BPM is 0 or smaller
     * @throws TactNotAnIntegerException When the tact is not a number
     * @throws TactNegativeException     When the tact us 0 or smaller
     */
    public void start(@NotNull String bpmInput) throws TactNegativeException, TactNotAnIntegerException, BpmNotAnIntegerException, BpmNegativeException, BpmNullException {
        start(bpmInput, 4 + "", false);
    }

    /**
     * start the metronom
     *
     * @param bpmInput  BPM user input
     * @param tactInput Tact user input
     * @param increase  If bpm should increase
     * @throws BpmNotAnIntegerException  When BPM is not a number
     * @throws BpmNullException          When BPM is Null (this should not happen)
     * @throws BpmNegativeException      When BPM is 0 or smaller
     * @throws TactNotAnIntegerException When the tact is not a number
     */
    public void start(@NotNull String bpmInput, String tactInput, boolean increase) throws BpmNullException, BpmNegativeException, TactNotAnIntegerException, BpmNotAnIntegerException {

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

            isRunning = true;

        } else {
            throw new BpmNullException();
        }

    }

    /**
     * Pauses the Metronom
     *
     * @return {@link PauseMethodNextCall} representing what will happen next time when this method will be called
     */
    public PauseMethodNextCall pause() {

        if (isPaused) {
            isPaused = false;
            return PauseMethodNextCall.PAUSE;
        } else {
            cntTick = 1;
            isPaused = true;
            return PauseMethodNextCall.RESUME;
        }
    }

    /**
     * Stops the Metronom completely
     */
    public void stopAll() {

        cntTick = 1;
        tact = 4;
        cntListEntry = 0;
        isPaused = false;
        isRunning = false;
    }

    public HashMap<TextFields, String> startList(BpmList selectedList) {
        currentBpmList = selectedList;

        HashMap<TextFields, String> hashMap = new HashMap<>();

        bpm = currentBpmList.getListEntry(cntListEntry).getBPM();
        hashMap.put(TextFields.BPM, bpm + "");
        tact = currentBpmList.getListEntry(cntListEntry).getTact();
        hashMap.put(TextFields.TACT, tact + "");
        hashMap.put(TextFields.ARTIST, currentBpmList.getListEntry(cntListEntry).getArtist());
        hashMap.put(TextFields.SONG, currentBpmList.getListEntry(cntListEntry).getSong());

        isRunning = true;

        return hashMap;
    }

    /**
     * Goes to the next Entry in the current List and returns the Values of the Entry
     *
     * @return A HashMap with {@link TextFields} Enum as Keys and the Values
     * @throws ListEndReachedException When the end of the list is reached
     */
    public HashMap<TextFields, String> next() throws ListEndReachedException {

        cntListEntry++;

        if (cntListEntry < currentBpmList.getEntryCount()) {

            HashMap<TextFields, String> returnHashMap = new HashMap<>();

            cntTick = 1;

            ListEntry currentListEntry = currentBpmList.getListEntry(cntListEntry);
            bpm = currentListEntry.getBPM();
            tact = currentListEntry.getTact();

            isPaused = false;

            returnHashMap.put(TextFields.BPM, bpm + "");
            returnHashMap.put(TextFields.TACT, tact + "");
            returnHashMap.put(TextFields.ARTIST, currentListEntry.getArtist());
            returnHashMap.put(TextFields.SONG, currentListEntry.getSong());

            return returnHashMap;

        } else {
            stopAll();
            throw new ListEndReachedException();
        }
    }

    //Timer tasks logics
    public void pauseTimerTaskLogic() {
        pause();
    }

    /**
     * Logic for the increaseBpmTimerTask
     *
     * @param increaseBpmBy the amount
     * @return the new BPM
     * @throws NotAnIntegerException When the amount is not a number
     */
    public int increaseBpmTimerTaskLogic(String increaseBpmBy) throws NotAnIntegerException {
        try {
            increaseBpmByInt = Integer.parseInt(increaseBpmBy);
        } catch (Exception e) {
            throw new NotAnIntegerException();
        }

        bpm += increaseBpmByInt;
        return bpm;
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

    public int getCntTick() {
        return cntTick;
    }

    public void setCntTick(int cntTick) {
        this.cntTick = cntTick;
    }

    public int getCntListEntry() {
        return cntListEntry;
    }

    public void setCntListEntry(int cntListEntry) {
        this.cntListEntry = cntListEntry;
    }

    public long getIncreaseBpmAfterInMs() {
        return increaseBpmAfterInMs;
    }

    public void setIncreaseBpmAfterInMs(long increaseBpmAfterInMs) {
        this.increaseBpmAfterInMs = increaseBpmAfterInMs;
    }

    public int getIncreaseBpmByInt() {
        return increaseBpmByInt;
    }

    public void setIncreaseBpmByInt(int increaseBpmByInt) {
        this.increaseBpmByInt = increaseBpmByInt;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public BpmList getCurrentBpmList() {
        return currentBpmList;
    }

    public void setCurrentBpmList(BpmList currentBpmList) {
        this.currentBpmList = currentBpmList;
    }

    public String getFirstTickFileName() {
        return firstTickFileName;
    }

    public String getOtherTicksFileName() {
        return otherTicksFileName;
    }

    //Enums
    public enum PauseMethodNextCall {
        PAUSE,
        RESUME;
    }

    public enum TextFields {
        BPM,
        TACT,
        ARTIST,
        SONG;
    }
}
