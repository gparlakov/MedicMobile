package com.parlakov.medic.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.parlakov.medic.Global;
import com.parlakov.medic.R;
import com.parlakov.medic.fragments.ExaminationDetailsFragment;

/**
 * Created by georgi on 13-11-18.
 */
public class ExaminationManagementActivity extends ActionBarActivity implements FragmentManager.OnBackStackChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_details);

        FragmentManager fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(this);

        String action = getIntent().getAction();
        if(action != null && action.equals(Global.VIEW_EXAMINATION_ACTION)){

            long examinationId = getIntent()
                    .getLongExtra(Global.EXAMINATION_DETAILS_ID_TO_VIEW, 0);

            if(examinationId == 0){
                Toast.makeText(this,
                        "No examination to view details of found.",
                        Toast.LENGTH_LONG)
                        .show();
                finish();
                return;
            }

            fm.beginTransaction()
                    .replace(R.id.container_examinations_details,
                            new ExaminationDetailsFragment(examinationId))
                    .addToBackStack(null)
                    .commit();

        }
        else{
            // unknown action - does not know what to do
            finish();
        }

    }


    @Override
    public void onBackStackChanged() {
        int entries = getSupportFragmentManager().getBackStackEntryCount();
        if(entries == 0){
            finish();
        }
    }
}
