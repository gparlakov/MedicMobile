package com.parlakov.medic.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.parlakov.medic.MainActivity;

import java.util.Calendar;

/**
 * Created by georgi on 13-11-12.
 */
public class TimePickerFragment extends DialogFragment {

    private final Calendar mCalendar;
    private TimePickerDialog.OnTimeSetListener mListener;

    public TimePickerFragment(TimePickerDialog.OnTimeSetListener listener,
                              Calendar calendar) {
        mListener = listener;
        mCalendar = calendar;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int minutes = mCalendar.get(Calendar.MINUTE);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);

        Calendar currentCal = Calendar.getInstance();
        int currMinute = currentCal.get(Calendar.MINUTE);
        int currHour = currentCal.get(Calendar.HOUR_OF_DAY);

        // checks if the time has not been set by user
        // and is the default = current time
        // if so sets the hour to be nine o'clock
        if(minutes == currMinute && hour == currHour){
            //TODO - make the default time to be an preference option
            minutes = 0;
            hour = 9;
        }

        return new TimePickerDialog(getActivity(), mListener, hour, minutes, false);
    }
}
