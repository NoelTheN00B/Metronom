package de.leon.metronom;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.TimerTask;

import de.leon.metronom.CustomClasses.BpmList.BpmList;
import de.leon.metronom.CustomClasses.BpmList.ListEntry;
import de.leon.metronom.CustomClasses.DataHolder.CountDownInputHolder;
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

    private int bpm = 0;
    private int tact = 4;
    private int cntTick = 1;
    private int cntListEntry = 1;
    private long increaseBpmAfterInMs = 0;
    private int increaseBpmByInt = 0;
    private long countDownInMs = 0;

    private boolean isPaused = false;
    private boolean isRunning = false;

    private BpmList currentBpmList;

    MediaPlayer mediaPlayer;
    MetronomeTimer bpmTimer = new MetronomeTimer();
    MetronomeTimer countDownTimer = new MetronomeTimer();
    MetronomeTimer increaseBpmTimer = new MetronomeTimer();

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

        setClickListeners();
    }

    private void setClickListeners() {

        //open list manager
        openListManager.setOnClickListener(v -> {
            Intent activityChangeIntent = new Intent(MainActivity.this, ListManagerActivity.class);
            MainActivity.this.startActivity(activityChangeIntent);
        });

        //start metronome
        start.setOnClickListener(v -> start());

        //pause/resume
        pause.setOnClickListener(v -> pause());

        //stop
        stop.setOnClickListener(v -> stopAll());

        //start list
        startList.setOnClickListener(v -> {
            if (!isRunning) {
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
            countDownInMs = 0;
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

        connectAllUiElements();
    }

    private void alertDialog(String title, String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setNeutralButton(getString(R.string.ok), (nBtnDialog, which) -> { });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

    private void start() {

        if (!enterBpmField.getText().toString().isEmpty()) {

            try {
                bpm = Integer.parseInt(enterBpmField.getText().toString());
            } catch (Exception e) {
                alertDialog(getString(R.string.error), enterBpmField.getText() + getString(R.string.err_not_number));
                System.out.println(Arrays.toString(e.getStackTrace()));
            }

            if (bpm <= 0) {
                alertDialog(getString(R.string.error), getString(R.string.err_bpm_under_one));
                return;
            }

            if (enterTactField.getText().toString().isEmpty()) {
                tact = 4;
            } else {
                try {
                    tact = Integer.parseInt(enterTactField.getText().toString());
                } catch (Exception e) {
                    alertDialog(getString(R.string.error), enterTactField.getText() + getString(R.string.err_not_number));
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
                return;
            }

            if (switchIncreaseBPM.isActivated() || switchIncreaseBPM.isChecked()) {
                increaseBPM();
            }

            countDown();

            //todo led
            progressBar.setMax(10 * tact);
            setProgressBarColor(Color.RED);

            bpmTimer.scheduleAtFixedRate(firstTick(), 0, 60000 / bpm);
            isRunning = true;

        } else {
            alertDialog(getString(R.string.error), getString(R.string.err_bpm_empty));
        }

    }

    private void pause() {
        if (isPaused) {
            start();
            bpmTimer.startNormalTimer();
            isPaused = false;
            pause.setText(getString(R.string.pause));
        } else {
            bpmTimer.cancel();
            increaseBpmTimer.cancel();
            cntTick = 1;
            isPaused = true;
            pause.setText(getString(R.string.resume));
        }
    }

    private void stopAll() {
        bpmTimer.cancel();
        increaseBpmTimer.cancel();
        countDownTimer.cancel();
        setProgressBarColor(Color.RED);
        progressBar.setProgress(0);

        cntTick = 1;
        tact = 4;
        cntListEntry = 0;
        artistFld.setText("");
        songFld.setText("");
        pause.setText(getString(R.string.pause));
        startList.setText(getString(R.string.start_list));
        isPaused = false;
        isRunning = false;
    }

    private void startList() {
        //todo implement
    }

    private void next() {

        cntListEntry++;

        if (cntListEntry < currentBpmList.getEntries()) {

            bpmTimer.cancel();
            countDownTimer.cancel();

            progressBar.setProgress(0);

            cntTick = 1;

            ListEntry currentListEntry = currentBpmList.getListEntry(cntListEntry);
            bpm = currentListEntry.getBPM();
            enterBpmField.setText(bpm);
            tact = currentListEntry.getTact();
            enterTactField.setText(tact);
            artistFld.setText(currentListEntry.getArtist());
            songFld.setText(currentListEntry.getSong());

            isPaused = false;
            pause.setText(R.string.pause);
            start();

        } else {

            Toast.makeText(MainActivity.this, R.string.list_end_reached, Toast.LENGTH_LONG).show();
            stopAll();
        }
    }

    private void setListsInListManager() {

        ContextWrapper contextWrapper = new ContextWrapper(this);
        File[] files = contextWrapper.getFilesDir().listFiles();

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

    private TimerTask pauseTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                pause();
            }
        };
    }

    private TimerTask firstTick() {
        return new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {

                try {
                    AssetFileDescriptor assetFileDescriptor = getAssets().openFd("first_tick.wav");
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bpmTimer.setTask(otherTicks());
                progressBar.setProgress(10, false);
            }
        };
    }

    private TimerTask otherTicks() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    AssetFileDescriptor assetFileDescriptor = getAssets().openFd("other_ticks.wav");
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                setProgressBarColor(Color.GREEN);
                progressBar.setProgress(progressBar.getProgress() + 10);
            }
        };
    }

    private TimerTask increaseBpmTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    increaseBpmByInt = Integer.parseInt(increaseBpmBy.getText().toString());
                } catch (Exception e) {
                    alertDialog(getString(R.string.error), increaseBpmBy.getText() + getString(R.string.err_not_number));
                    return;
                }

                bpm += increaseBpmByInt;
                bpmTimer.setPeriod(60000 / bpm);
                enterBpmField.setText(bpm);
            }
        };
    }

    private void setProgressBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintList(ColorStateList.valueOf(color));
        } else {
            progressBar.getProgressDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }



}