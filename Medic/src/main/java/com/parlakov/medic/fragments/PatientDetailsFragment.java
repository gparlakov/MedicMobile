package com.parlakov.medic.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parlakov.medic.R;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.models.Patient;
import com.parlakov.medic.util.ViewHelper;

/**
 * Created by georgi on 13-11-8.
 */
public class PatientDetailsFragment extends Fragment {

    public static final String PATIENT_SAVE = "PATIENT_SAVE";

    private long mId;
    private String mPhotoPath;
    private Patient mPatient;

    public PatientDetailsFragment(){
        this(0);
    }

    public PatientDetailsFragment(long id){
        mId = id;
        mPatient = new Patient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View patientDetailsView = inflater.inflate(R.layout.fragment_patient_details, container, false);

        // the fragment has been restored from stop() and does not know which mPatient
        if(mId != 0){
            getPatientData(patientDetailsView);
            showPatientInfo(patientDetailsView);
        }

        setHasOptionsMenu(true);

        return patientDetailsView;
    }

    private void getPatientData(View view) {
        LocalData data = new LocalData(view.getContext());
        mPatient = data.getPatients().getById(mId);
    }

    private void showPatientInfo(View view) {
        String name = mPatient.getLastName();
        String firstName = mPatient.getFirstName();
        if(firstName != null && !firstName.isEmpty()){
            name = firstName + " " + name;
        }
        ViewHelper.setTextToTextView(R.id.textViewPatientName, view, name);

        ViewHelper.setTextToTextView(R.id.textViewPatientPhone, view, mPatient.getPhone());
        ViewHelper.setTextToTextView(R.id.textViewPatientAge, view, String.valueOf(mPatient.getAge()));

        mPhotoPath = mPatient.getImagePath();
        if(mPhotoPath!= null && !mPhotoPath.isEmpty()){
            ImageView patientPhotoView = (ImageView) view.findViewById(R.id.imageView_patientDetails_Photo);
            patientPhotoView.setImageDrawable(Drawable.createFromPath(mPhotoPath));
        }
    }

    @Override
         public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.patient_details, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(PATIENT_SAVE, mPatient);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null){
            mPatient = (Patient) savedInstanceState.getSerializable(PATIENT_SAVE);
            showPatientInfo(getView());
        }
    }
}
