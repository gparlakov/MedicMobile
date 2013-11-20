package com.parlakov.medic.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by georgi on 13-11-12.
 */
public class DatePickerFragment extends DialogFragment{

    private final DatePickerDialog.OnDateSetListener mListener;
    private final Calendar mCalendar;

    public DatePickerFragment(DatePickerDialog.OnDateSetListener listener,
                              Calendar calendar) {
        mListener = listener;
        mCalendar = calendar;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), mListener, year, month, day);
    }
}
