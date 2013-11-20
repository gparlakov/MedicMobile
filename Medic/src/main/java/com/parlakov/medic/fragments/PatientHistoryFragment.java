package com.parlakov.medic.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.parlakov.medic.R;
import com.parlakov.medic.adapters.AdapterFactory;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.LocalExaminations;

/**
 * Created by georgi on 13-11-18.
 */
public class PatientHistoryFragment extends ListFragment {

    private static final String PATIENT_ID = "patient id";
    private static final String PATIENT_NAME = "patient name";
    private String mPatientName;

    public PatientHistoryFragment(){
        this(0, null);
    }

    public PatientHistoryFragment(long patientId, String patientName) {
        mPatientId = patientId;
        mPatientName = patientName;
    }

    private long mPatientId;
    private LocalData mData;

    private LocalData getData(){
        if(mData == null){
            mData = new LocalData(getActivity());
        }
        return mData;
    }

    @Override
    public void onStart() {
        super.onStart();

        initialize();

        setEmptyText(getString(R.string.empty_text_empty_history));

        addPatientNameToTitle();
    }

    private void addPatientNameToTitle() {

        String title = getString(R.string.title_patientHistory);
        if(mPatientName != null){
            title += ": " + mPatientName;
        }
        ((ActionBarActivity)getActivity())
                .getSupportActionBar().setTitle(title);

    }

    @Override
    public void onStop() {
        super.onStop();

        closeDb();
    }

    private void closeDb() {
        if(mData != null){
            mData.closeDb();
            mData = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(PATIENT_ID, mPatientId);
        outState.putString(PATIENT_NAME, mPatientName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            mPatientId = savedInstanceState.getLong(PATIENT_ID);
            mPatientName = savedInstanceState.getString(PATIENT_NAME);
        }
    }

    private void initialize() {
        new AsyncTask<Void, Void, SimpleCursorAdapter>() {
            @Override
            protected SimpleCursorAdapter doInBackground(Void... params) {
                SimpleCursorAdapter patientHistoryAdapter = null;

                try {
                    LocalExaminations examinations = (LocalExaminations) getData()
                            .getExaminations();

                    Cursor examHistoryCursor = examinations.getByPatientId(mPatientId);

                    return AdapterFactory.getPatientHistoryAdapter(examHistoryCursor,
                            getActivity());

                }
                catch (SQLiteException e) {
                    e.printStackTrace();
                    // a null value will be returned
                }

                return patientHistoryAdapter;
            }

            @Override
            protected void onPostExecute(SimpleCursorAdapter simpleCursorAdapter) {
                if (simpleCursorAdapter == null){
                    Toast.makeText(getActivity(),
                            getString(R.string.toast_exception_could_not_extract_patient_history),
                            Toast.LENGTH_LONG)
                            .show();
                }

                setListAdapter(simpleCursorAdapter);
            }
        }.execute();
    }
}
