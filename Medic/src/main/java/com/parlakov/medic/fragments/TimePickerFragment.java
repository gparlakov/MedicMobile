package com.parlakov.medic.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by georgi on 13-11-12.
 */
public class TimePickerFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener mListener;

    public TimePickerFragment(TimePickerDialog.OnTimeSetListener listener) {
        mListener = listener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new TimePickerDialog(getActivity(), mListener, 9, 0, false);
    }
}
