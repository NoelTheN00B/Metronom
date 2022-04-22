package de.leon.metronom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimerTask;

import de.leon.metronom.CustomClasses.DataHolder.CountDownInputHolder;
import de.leon.metronom.CustomClasses.MetronomLogic.BpmList.BpmList;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.BpmNegativeException;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.BpmNotAnIntegerException;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.BpmNullException;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.ListEndReachedException;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.NotAnIntegerException;
import de.leon.metronom.CustomClasses.MetronomLogic.CustomExceptions.TactNotAnIntegerException;
import de.leon.metronom.CustomClasses.MetronomLogic.MainLogic;
import de.leon.metronom.CustomClasses.Timer.CountDownDurationPicker;
import de.leon.metronom.CustomClasses.Timer.IncreaseAfterDurationPicker;
import de.leon.metronom.CustomClasses.Timer.MetronomeTimer;

@SuppressLint("StaticFieldLeak")
public class MainActivity extends AppCompatActivity {

    private TextView enterBpmField;
    private TextView enterTactField;
    public static TextView increaseBpmAfter;
    private TextView increaseBpmBy;
    public static TextView timerInput;
    public static TextView remainingTime;
    private TextView artistFld;
    private TextView songFld;
    private SwitchMaterial switchIncreaseBPM;
    private Button start;
    private Button stop;
    private Button openListManager;
    private Button startList;
    private Button pause;
    private Button resetTimer;
    private ProgressBar progressBar;
    private Spinner listSelection;
    private FloatingActionButton floatingActionButtonPlayPause;
    private FloatingActionButton floatingActionButtonNext;

    private MainLogic logic;
    static MediaPlayer firstTickMP;
    static MediaPlayer otherTickMP;
    static SoundPool tickSP = null;
    private HashMap<Tick, Integer> tickMap = null;

    MetronomeTimer bpmTimer = new MetronomeTimer();
    MetronomeTimer increaseBpmTimer = new MetronomeTimer();
    MetronomeTimer countDownTimer = new MetronomeTimer();

    private boolean isPlaying = false;
    private static int tact = 4;
    private int cntTact = 1;

    public TextView getRemainingTime() {
        return remainingTime;
    }

    private void connectAllUiElements() {

        enterBpmField = (TextView) findViewById(R.id.txtFldBPM);
        enterTactField = (TextView) findViewById(R.id.txtFldTact);
        increaseBpmAfter = (TextView) findViewById(R.id.txtFldIncreaseBpmAfter);
        increaseBpmBy = (TextView) findViewById(R.id.txtFldIncreaseBpmBy);
        timerInput = (TextView) findViewById(R.id.txtFldTimerInput);
        remainingTime = (TextView) findViewById(R.id.txtFldRemainingTime);
        artistFld = (TextView) findViewById(R.id.txtFldArtist);
        songFld = (TextView) findViewById(R.id.txtFldSong);
        switchIncreaseBPM = (SwitchMaterial) findViewById(R.id.switchIncreaseBPM);
        start = (Button) findViewById(R.id.btnStart);
        stop = (Button) findViewById(R.id.btnStop);
        openListManager = (Button) findViewById(R.id.btnOpenListManager);
        startList = (Button) findViewById(R.id.btnStartList);
        pause = (Button) findViewById(R.id.bntPause);
        resetTimer = (Button) findViewById(R.id.btnResetTimer);
        progressBar = (ProgressBar) findViewById(R.id.prgrsbrTact);
        listSelection = (Spinner) findViewById(R.id.listSelection);
        floatingActionButtonPlayPause = (FloatingActionButton) findViewById(R.id.floatingActionButtonPlayPause);
        floatingActionButtonNext = (FloatingActionButton) findViewById(R.id.floatingActionButtonNext);


        floatingActionButtonNext.hide();
        firstTickMP = new MediaPlayer();
        otherTickMP = new MediaPlayer();

        setClickListeners();
    }

    private void setClickListeners() {

        //open list manager
        openListManager.setOnClickListener(v -> {
            Intent activityChangeIntent = new Intent(MainActivity.this, ListManagerActivity.class);
            MainActivity.this.startActivity(activityChangeIntent);
        });

        floatingActionButtonPlayPause.setOnClickListener(v -> startPause());

        floatingActionButtonNext.setOnClickListener(v -> next());

        //start metronome
        start.setOnClickListener(v -> start());

        //pause/resume
        pause.setOnClickListener(v -> pause());

        //stop
        stop.setOnClickListener(v -> stopAll());

        //start list
        startList.setOnClickListener(v -> {
            if (!logic.isRunning()) {
                startList();
            } else {
                startList.setText(getString(R.string.next));
                next();
            }
        });

        //reset
        resetTimer.setOnClickListener(v -> {
            timerInput.setText(null);
            remainingTime.setText(null);
            logic.setCountDownInMs(0);
            CountDownInputHolder.getInstance().setDuration(0);
        });

        //timer input increase after
        increaseBpmAfter.setOnClickListener(v -> {
            Intent openDurationPickerIntent = new Intent(MainActivity.this, IncreaseAfterDurationPicker.class);
            MainActivity.this.startActivity(openDurationPickerIntent);
        });

        //timer input
        timerInput.setOnClickListener(v -> {
            Intent openDurationPickerIntent = new Intent(MainActivity.this, CountDownDurationPicker.class);
            MainActivity.this.startActivity(openDurationPickerIntent);
        });

    }

    private void initSoundPool() {

        final int maxStreams = 5;
        Context mContext = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            tickSP = new SoundPool.Builder()
                    .setMaxStreams(maxStreams)
                    .setAudioAttributes(attributes)
                    .build();
        } else {
            tickSP = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0);
        }

        tickMap = new HashMap<>();
        tickMap.put(Tick.FIRST_TICK, tickSP.load(mContext, R.raw.first_tick_converted, 1));
        tickMap.put(Tick.OTHER_TICK, tickSP.load(mContext, R.raw.other_ticks_converted, 1));

        try {
            AssetFileDescriptor afdFirstTick = getAssets().openFd(logic.getFirstTickFileName());
            AssetFileDescriptor afdOtherTick = getAssets().openFd(logic.getFirstTickFileName());

            // fill your sounds
            //tickSP.load(afdFirstTick, 1);
            //tickSP.load(afdOtherTick, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void cleanUpTickSP() {
        try {
            tickSP.release();
        } catch (NullPointerException npe) {
            //no SoundPool running
        }
        tickSP = null;
    }

    private void playTick(Tick tick) {
        if (tickMap != null) {
            tickSP.play(tickMap.get(tick), 1, 1, 1, 0, 1f);
        }
        //tickSP.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        connectAllUiElements();
    }

    @Override
    protected void onStart() {
        super.onStart();

        logic = new MainLogic();

        connectAllUiElements();
    }

    private void alertDialog(String title, String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setNeutralButton(getString(R.string.ok), (nBtnDialog, which) -> {
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

    private void startPause() {
        if (isPlaying) {
            pause();
        } else {
            start();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void start() {

        boolean increase = switchIncreaseBPM.isActivated()
                && increaseBpmAfter.getText() != null
                && increaseBpmBy.getText() != null;

        try {
            logic.start(enterBpmField.getText().toString(),
                    enterTactField.getText().toString(),
                    increase);
            tact = Integer.parseInt(enterTactField.getText().toString());

            if (tickSP == null) {
                initSoundPool();
                //firstTickMP = getMediaPlayer(logic.getFirstTickFileName());
                //otherTickMP = getMediaPlayer(logic.getOtherTicksFileName());
            }

            bpmTimer.scheduleAtFixedRate(tickTask(), 0, 60000 / logic.getBpm());

            if (increase) {
                increaseBPM();
            }

            countDown();

            //todo led
            progressBar.setMax(10 * logic.getTact());
            setProgressBarColor(Color.RED);

            floatingActionButtonPlayPause.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
            isPlaying = true;
        } catch (BpmNullException e) {
            alertDialog(getString(R.string.error), getString(R.string.err_bpm_empty));
        } catch (BpmNotAnIntegerException e) {
            alertDialog(getString(R.string.error), enterBpmField.getText().toString() + getString(R.string.err_not_number));
        } catch (BpmNegativeException e) {
            alertDialog(getString(R.string.error), getString(R.string.err_bpm_under_one));
        } catch (TactNotAnIntegerException e) {
            alertDialog(getString(R.string.error), enterTactField.getText().toString() + getString(R.string.err_not_number));
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void pause() {

        MainLogic.PauseMethodNextCall status = logic.pause();

        if (status.equals(MainLogic.PauseMethodNextCall.PAUSE)) {
            bpmTimer.startNormalTimer();
            start();
            pause.setText(getString(R.string.pause));
        } else {
            bpmTimer.cancel();
            increaseBpmTimer.cancel();
            pause.setText(getString(R.string.resume));
            floatingActionButtonPlayPause.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
            isPlaying = false;
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void stopAll() {

        bpmTimer.cancel();
        increaseBpmTimer.cancel();
        countDownTimer.cancel();
        setProgressBarColor(Color.RED);
        progressBar.setProgress(0);
        artistFld.setText("");
        songFld.setText("");
        pause.setText(getString(R.string.pause));
        startList.setText(getString(R.string.start_list));
        floatingActionButtonPlayPause.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
        isPlaying = false;
        firstTickMP.release();
        otherTickMP.release();
        cleanUpTickSP();
        tact = 4;
    }

    private void startList() {
        HashMap<MainLogic.TextFields, String> hashMap = logic.startList((BpmList) listSelection.getSelectedItem());
        enterBpmField.setText(hashMap.get(MainLogic.TextFields.BPM));
        enterTactField.setText(hashMap.get(MainLogic.TextFields.TACT));
        artistFld.setText(hashMap.get(MainLogic.TextFields.ARTIST));
        songFld.setText(hashMap.get(MainLogic.TextFields.SONG));
        tact = Integer.parseInt(Objects.requireNonNull(hashMap.get(MainLogic.TextFields.TACT)));

        start();
        startList.setText(getString(R.string.next));
    }

    private void next() {

        try {
            HashMap<MainLogic.TextFields, String> hashMap = logic.next();

            bpmTimer.cancel();
            countDownTimer.cancel();

            progressBar.setProgress(0);
            enterBpmField.setText(hashMap.get(MainLogic.TextFields.BPM));
            enterTactField.setText(hashMap.get(MainLogic.TextFields.TACT));
            artistFld.setText(hashMap.get(MainLogic.TextFields.ARTIST));
            songFld.setText(hashMap.get(MainLogic.TextFields.SONG));
            tact = Integer.parseInt(Objects.requireNonNull(hashMap.get(MainLogic.TextFields.TACT)));
            pause.setText(R.string.pause);
            start();

        } catch (ListEndReachedException e) {
            Toast.makeText(MainActivity.this, R.string.list_end_reached, Toast.LENGTH_LONG).show();
        }
    }

    private void countDown() {

        if (CountDownInputHolder.getInstance().getDuration() > 0) {
            logic.setCountDownInMs(CountDownInputHolder.getInstance().getDuration());

            countDownTimer.scheduleAtFixedRate(pauseTimerTask(), 0, logic.getCountDownInMs(), this);
        }
    }

    private void increaseBPM() {

        increaseBpmTimer.scheduleAtFixedRate(increaseBpmTimerTask(), 0, logic.getIncreaseBpmAfterInMs(), this);
    }

    private void setListsInListManager() {

        ContextWrapper contextWrapper = new ContextWrapper(this);
        File[] files = contextWrapper.getFilesDir().listFiles();

    }

    /*
    public void playFirstTick() {
        try {
            if (firstTickMP.isPlaying()) {
                firstTickMP.stop();
                firstTickMP.release();
                firstTickMP = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = getAssets().openFd(logic.getFirstTickFileName());
            firstTickMP.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            firstTickMP.prepare();
            firstTickMP.setVolume(1f, 1f);
            firstTickMP.start();
            firstTickMP.setOnCompletionListener(mp -> {
                mp.release();
                firstTickMP = new MediaPlayer();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playOtherTick() {
        try {
            if (otherTickMP.isPlaying()) {
                otherTickMP.stop();
                otherTickMP.release();
                otherTickMP = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = getAssets().openFd(logic.getOtherTicksFileName());
            otherTickMP.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            otherTickMP.prepare();
            otherTickMP.setVolume(1f, 1f);
            otherTickMP.start();
            otherTickMP.setOnCompletionListener(mp -> {
                mp.release();
                otherTickMP = new MediaPlayer();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */

    private TimerTask tickTask() {
        return new TimerTask() {
            @Override
            public void run() {

                if (cntTact == tact) {
                    cntTact = 1;
                    playTick(Tick.FIRST_TICK);
                } else {
                    playTick(Tick.OTHER_TICK);
                    cntTact++;
                }
                //setProgressBarColor(Color.RED);
                //progressBar.setProgress(10);
            }
        };
    }

    private TimerTask pauseTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                logic.pauseTimerTaskLogic();
            }
        };
    }

    private TimerTask increaseBpmTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    int newBPM = logic.increaseBpmTimerTaskLogic(increaseBpmBy.getText().toString());
                    bpmTimer.setPeriod(60000 / newBPM);
                    enterBpmField.setText(newBPM);
                } catch (NotAnIntegerException e) {
                    alertDialog(getString(R.string.error), increaseBpmBy.getText() + getString(R.string.err_not_number));
                }
            }
        };
    }

    private void setProgressBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //progressBar.setProgressTintList(ColorStateList.valueOf(color));
        } else {
            //progressBar.getProgressDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    private enum Tick {
        FIRST_TICK,
        OTHER_TICK;
    }


}