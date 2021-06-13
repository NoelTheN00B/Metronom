package de.leon.metronom.CustomClasses.Timer;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import de.leon.metronom.CustomClasses.DataHolder.CountDownInputHolder;
import de.leon.metronom.MainActivity;
import de.leon.metronom.R;
import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.TimeDurationUtil;

public class CountDownDurationPicker extends AppCompatActivity {

    protected TimeDurationPicker timeDurationPicker;
    private Button btnOk;
    private Button btnCancel;

    protected long inputInMs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_picker);

        timeDurationPicker = findViewById(R.id.timeDurationInput);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        timeDurationPicker.setTimeUnits(TimeDurationPicker.MM_SS);
        timeDurationPicker.setOnDurationChangeListener((view, duration) -> inputInMs = duration);

        btnOk.setOnClickListener(v -> btnOkClickListener());

        btnCancel.setOnClickListener(v -> finish());
    }

    protected void btnOkClickListener() {
        CountDownInputHolder.getInstance().setDuration(inputInMs);
        MainActivity.timerInput.setText(TimeDurationUtil.formatMinutesSeconds(inputInMs));
        MainActivity.remainingTime.setText(TimeDurationUtil.formatMinutesSeconds(inputInMs));
        finish();
    }

}