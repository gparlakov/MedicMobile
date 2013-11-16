package com.parlakov.medic.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parlakov.medic.R;
import com.parlakov.medic.async.CancelAppointmentWorker;
import com.parlakov.medic.dto.CancelAppointmentDTO;
import com.parlakov.medic.interfaces.ChildFragmentListener;
import com.parlakov.medic.localdata.LocalData;

/**
 * Created by georgi on 13-11-15.
 */
public class ExaminationDetailsFragment extends Fragment {

    private final long mExaminationId;
    private LocalData mData;

    public LocalData getData() {
        if(mData == null){
            mData = new LocalData(getActivity());
        }
        return mData;
    }

    public void setData(LocalData data) {
        this.mData = data;
    }

    public ExaminationDetailsFragment(long examinationId) {
        mExaminationId = examinationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_examination_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        initialize();
    }

    private void initialize() {
        //TODO get examination data and put on screen
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.at_examinations_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean handled = true;

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_cancelAppointment:
                cancelAppointment(mExaminationId);
                break;
            case R.id.action_setResults:
                //TODO
                break;
            case R.id.action_changeDate:
                //TODO
                break;
            case R.id.action_returnToMain:
                ChildFragmentListener listener =
                        (ChildFragmentListener) getActivity();
                listener.onChildFragmentClose();
                break;
            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }

        return handled;
    }

    private void cancelAppointment(long examinationId) {
        CancelAppointmentDTO cancelAppointmentDTO =
                new CancelAppointmentDTO(getData(), examinationId);

        new CancelAppointmentWorker().execute(cancelAppointmentDTO);

        //TODO - do stuff on successful finish or failure
    }

}
