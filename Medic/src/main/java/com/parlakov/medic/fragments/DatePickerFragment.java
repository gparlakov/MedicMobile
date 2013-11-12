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

    public DatePickerFragment(DatePickerDialog.OnDateSetListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), mListener, year, month, day);
    }
}
