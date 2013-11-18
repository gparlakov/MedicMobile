package com.parlakov.medic.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;

import com.parlakov.medic.R;
import com.parlakov.medic.localdata.MedicDbContract;
import com.parlakov.medic.viewbinders.ExaminationsListViewBinder;

/**
 * Created by georgi on 13-11-18.
 */
public class PatientHistoryCursorAdapter  {
    public static SimpleCursorAdapter getAdapter(Cursor cursor,Context context){
        String[] from =
                {
                        MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS,
                        MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS,
                        MedicDbContract.Examination.COLUMN_NAME_TREATMENT,
                        MedicDbContract.Examination.COLUMN_NAME_CONCLUSION,
                        MedicDbContract.Examination.COLUMN_NAME_NOTES,
                };

        int[] to =
                {
                        R.id.textView_date_patientHistory,
                        R.id.textView_complaints_patientHistory,
                        R.id.textView_treatment_patientHistory,
                        R.id.textView_conclusion_patientHistory,
                        R.id.textView_notes_patientHistory,
                };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
                R.layout.item_patient_history, cursor, from, to, 0 );

        adapter.setViewBinder(new ExaminationsListViewBinder());
//        SimpleCursorAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//                if (columnIndex == cursor.getColumnIndex(
//                        MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS)){
//
//                    long dateInMillis = cursor.getLong(columnIndex);
//                    String dateAsText =
//                }
//
//                return false;
//            }
//        }

        return adapter;
    }
}
