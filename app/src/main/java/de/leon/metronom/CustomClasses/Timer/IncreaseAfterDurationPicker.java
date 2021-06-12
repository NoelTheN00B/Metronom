package de.leon.metronom.CustomClasses.Timer;

import de.leon.metronom.CustomClasses.DataHolder.IncreaseAfterInputHolder;
import de.leon.metronom.MainActivity;
import mobi.upod.timedurationpicker.TimeDurationUtil;

public class IncreaseAfterDurationPicker extends CountDownDurationPicker {

    @Override
    protected void btnOkClickListener() {
        IncreaseAfterInputHolder.getInstance().setDuration(inputInMs);
        MainActivity.increaseBpmAfter.setText(TimeDurationUtil.formatMinutesSeconds(inputInMs));
        finish();
    }
}
