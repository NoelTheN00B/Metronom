package de.leon.metronom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;
import java.util.Arrays;
import java.util.TimerTask;

import de.leon.metronom.CustomClasses.Timer.IncreaseBpmAfterDialogFragment;
import de.leon.metronom.CustomClasses.Timer.MetronomeTimer;
import de.leon.metronom.CustomClasses.Timer.TimerInputDialogFragment;
import mobi.upod.timedurationpicker.TimeDurationUtil;

public class MainActivity extends AppCompatActivity {

    private TextView enterBpmField;
    private TextView enterTactField;
    private TextView increaseBpmAfter;
    private TextView increaseBpmBy;
    private TextView timerInput;
    private TextView remainingTime;
    private TextView artistFld;
    private TextView songFld;
    private SwitchMaterial switchIncreaseBPM;
    private Button start;
    private Button stop;
    private Button openListManager;
    private Button startList;
    private Button pause;

    private int bpm = 0;
    private int tact = 4;
    private long increaseBpmAfterInMs = 0;
    private int increaseBpmByInt = 0;
    private long timerInputInMs = 0;

    MediaPlayer mediaPlayer;
    MetronomeTimer bpmTimer = new MetronomeTimer();
    MetronomeTimer increaseBpmTimer = new MetronomeTimer();
    TimerInputDialogFragment timerInputDialogFragment = new TimerInputDialogFragment();
    IncreaseBpmAfterDialogFragment increaseBpmAfterDialogFragment = new IncreaseBpmAfterDialogFragment();

    public void setIncreaseBpmAfterInMs(long increaseBpmAfterInMs) {
        this.increaseBpmAfterInMs = increaseBpmAfterInMs;
    }

    public void setTimerInputInMs(long timerInputInMs) {
        this.timerInputInMs = timerInputInMs;
    }

    public TextView getIncreaseBpmAfter() {
        return increaseBpmAfter;
    }

    public TextView getTimerInput() {
        return timerInput;
    }

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

        //timer input increase after
        increaseBpmAfter.setOnClickListener(v -> {
            timerInputDialogFragment.show(getFragmentManager(), "dialog-increase");
            //increaseBpmAfter.setText(TimeDurationUtil.formatMinutesSeconds(increaseBpmAfterInMs));
        });

        //timer input
        timerInput.setOnClickListener(v -> {
            timerInputDialogFragment.show(getFragmentManager(), "dialog-timer");
            //timerInput.setText(TimeDurationUtil.formatMinutesSeconds(timerInputInMs));
            //remainingTime.setText(TimeDurationUtil.formatMinutesSeconds(timerInputInMs));
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

        } else {
            alertDialog(getString(R.string.error), getString(R.string.err_bpm_empty));
        }

    }

    private TimerTask playFirstSound() {
        return new TimerTask() {
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
            }
        };
    }

    private TimerTask playOtherSounds() {
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
            }
        };
    }

    private void countDown() {

        if (timerInputInMs > 0) {

        }

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

    private void increaseBPM() {

        increaseBpmTimer.scheduleAtFixedRate(increaseBpmTimerTask(), 0, increaseBpmAfterInMs);
    }

}