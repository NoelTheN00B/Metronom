package de.leon.metronom.CustomClasses.Timer;

import android.widget.Toast;

import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.TimeDurationPickerDialogFragment;
import mobi.upod.timedurationpicker.TimeDurationUtil;

public class TimerInputDialogFragment extends TimeDurationPickerDialogFragment {

    //private long duration;
    //protected MainActivity mainActivity;

    @Override
    protected long getInitialDuration() {
        return 5 * 60 * 1000;
    }

    @Override
    protected int setTimeUnits() {
        return TimeDurationPicker.MM_SS;
    }

    @Override
    public void onDurationSet(TimeDurationPicker view, long duration) {
        final String formattedDuration = TimeDurationUtil.formatMinutesSeconds(duration);
        Toast.makeText(getActivity(), formattedDuration, Toast.LENGTH_LONG).show();
        //setInMain(duration);
    }

    /*
    public void show(FragmentManager manager, String tag, MainActivity mainActivity) {
        super.show(manager, tag);
        this.mainActivity = mainActivity;
    }

    protected void setInMain(long duration) {
        this.mainActivity.setTimerInputInMs(duration);
        this.mainActivity.getTimerInput().setText(TimeDurationUtil.formatMinutesSeconds(duration));
        this.mainActivity.getRemainingTime().setText(TimeDurationUtil.formatMinutesSeconds(duration));
    }

    public long getDuration() {
        return duration;
    }
     */
}
