package com.parlakov.medic.async;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.parlakov.medic.R;
import com.parlakov.medic.activities.AddEditExaminationActivity;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.MedicDbContract;
import com.parlakov.uow.IUowMedic;

/**
 * Created by georgi on 13-11-11.
 */
public class PatientsAdapterWorker
        extends AsyncTask<AddEditExaminationActivity, Void, SimpleCursorAdapter> {
    private IUowMedic mData;
    private AddEditExaminationActivity activity;
    private int mPatientPosition;

    @Override
    protected SimpleCursorAdapter doInBackground(AddEditExaminationActivity... params) {
        activity = params[0];
        mData = activity.getData();

        return getPatientsCursorAdapter();
    }

    @Override
    protected void onPostExecute(SimpleCursorAdapter adapter) {
        if(activity != null){
            Spinner spinner = activity.getPatientsSpinner();
            spinner.setAdapter(adapter);
            spinner.setSelection(mPatientPosition, true);
            activity.setExaminationData();
        }
    }

    private SimpleCursorAdapter getPatientsCursorAdapter() {
        Context context = activity.getApplicationContext();

        Cursor patientsCursor = (Cursor) mData.getPatients().getAll();

        if(activity.mPatientId != 0){
            findPatientPosition(patientsCursor);
        }

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
                R.layout.item_patient_spinner_in_new_examination,
                patientsCursor,
                fromColumns,
                toViewIds,
                0);
    }

    private void findPatientPosition(Cursor patients) {
        if(patients != null){
            do{
                patients.moveToNext();
                int idIndex = patients.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_ID);
                if(activity.mPatientId == patients.getLong(idIndex)){
                    mPatientPosition = patients.getPosition();
                    break;
                }
            }while(!patients.isLast());
        }
    }
}
