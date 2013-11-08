package com.parlakov.medic.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.parlakov.medic.R;
import com.parlakov.medic.localdata.LocalData;

/**
 * Created by georgi on 13-11-8.
 */
public class PatientDetailsFragment extends Fragment {

    private final long mId;

    public PatientDetailsFragment(long id){
        mId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View patientDetailsView = inflater.inflate(R.layout.fragment_patient_details, container, false);

        initializeUi(patientDetailsView);

        setHasOptionsMenu(true);

        return patientDetailsView;
    }

    private void initializeUi(View view) {

        LocalData data = new LocalData(view.getContext());
        data.getPatients().getById(mId);

        // TODO remove
                ((TextView) view.findViewById(R.id.textView_patientDetails))
                .setText("Patient:" + mId);
    }
}
