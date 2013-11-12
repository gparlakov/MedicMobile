package com.parlakov.medic.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.parlakov.medic.R;

/**
 * Created by georgi on 13-11-12.
 */
public class TodaysAppointmentsFragment extends ListFragment {

    @Override
    public void onStart() {
        super.onStart();

        setEmptyText(getString(R.string.emptyText_todaysAppointments));
    }
}
