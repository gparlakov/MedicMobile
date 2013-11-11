package com.parlakov.medic.util;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Adapter;
import android.widget.Spinner;

import com.parlakov.medic.R;
import com.parlakov.medic.activities.AddEditExaminationActivity;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.MedicDbContract;
import com.parlakov.uow.IUowMedic;

/**
 * Created by georgi on 13-11-11.
 */
public class PatientsSimpleCursorAdapterSetterAsyncSetter extends AsyncTask<AddEditExaminationActivity, Void, SimpleCursorAdapter> {
    private IUowMedic mData;
    private AddEditExaminationActivity activity;

    @Override
    protected SimpleCursorAdapter doInBackground(AddEditExaminationActivity... params) {
        activity = params[0];
        mData = activity.getData();

        return getPatientsCursorAdapter();
    }

    @Override
    protected void onPostExecute(SimpleCursorAdapter adapter) {
        if(activity != null){
            Spinner spinner = (Spinner) activity.findViewById(R.id.spinner_patients);
            spinner.setAdapter(adapter);
        }
    }

    private SimpleCursorAdapter getPatientsCursorAdapter() {
        Context context = activity.getApplicationContext();
        mData = new LocalData(context);

        Cursor patients = (Cursor) mData.getPatients().getAll();

        String[] fromColumns = new String[]
                {
                        MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME,
                        MedicDbContract.Patient.COLUMN_NAME_LAST_NAME
                };

        int[] toViewIds = new int[]
                {
                        R.id.cursor_item_patient_firstName,
                        R.id.cursor_item_patient_lastName
                };

        return new SimpleCursorAdapter(
                context,
                R.layout.cursor_item_patient,
                patients,
                fromColumns,
                toViewIds,
                Adapter.NO_SELECTION);
    }
}
