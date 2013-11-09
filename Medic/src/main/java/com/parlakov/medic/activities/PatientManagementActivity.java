package com.parlakov.medic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.parlakov.medic.R;
import com.parlakov.medic.fragments.PatientDetailsFragment;

/**
 * Created by georgi on 13-11-9.
 */
public class PatientManagementActivity extends ActionBarActivity {

    public static final String PATIENT_ID_EXTRA = "patient id extra";

    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_management);

        Intent intent = getIntent();
        if(intent != null){
            mId = intent.getLongExtra(PATIENT_ID_EXTRA, 0);
        }

        visualizePatientDetailsFragment();
    }

    private void visualizePatientDetailsFragment() {
        if(mId != 0){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_patient_details, new PatientDetailsFragment(mId))
                    .commit();
        }
    }
}
