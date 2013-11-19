package com.parlakov.medic.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorTreeAdapter;

import com.parlakov.medic.R;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.MedicDbContract;
import com.parlakov.medic.viewbinders.ExaminationsListViewBinder;
import com.parlakov.medic.viewbinders.PatientsListViewBinder;

/**
 * Created by georgi on 13-11-18.
 */
public class AdapterFactory {

    public static SimpleCursorAdapter getPatientHistoryAdapter(Cursor cursor, Context context){
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

        return adapter;
    }

    public static SimpleCursorAdapter getExaminationsSimpleCursorAdapter(
            Cursor cursor, Context context){

        String[] fromColumns =
                {
                        MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS,
                        MedicDbContract.PATIENT_FULL_NAME,
                        MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS
                };

        int[] toViewIds =
                {
                        R.id.cursor_item_examination_date,
                        R.id.cursor_item_examination_patientFullName,
                        R.id.cursor_item_examination_complaints
                };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                context,
                R.layout.item_examination,
                cursor,
                fromColumns,
                toViewIds,
                0);

        adapter.setViewBinder(new ExaminationsListViewBinder());

        return adapter;
    }

    public static SimpleCursorAdapter getPatientsSimpleCursorAdapter(
            Context context, Cursor patients) {

        String[] from = new String[]
                {
                        MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH,
                        MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME,
                        MedicDbContract.Patient.COLUMN_NAME_LAST_NAME,
                };

        int[] to = new int[]
                {
                        R.id.listItemPatientImage,
                        R.id.listItemPatientFirstName,
                        R.id.listItemPatientLastName
                };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
                R.layout.item_patient,
                patients,
                from,
                to,
                0);

        adapter.setViewBinder(new PatientsListViewBinder());

        return adapter;
    }

    public static SimpleCursorTreeAdapter getTreeAdapter(
            final Context context, Cursor cursor){

        String[] fromColumns =
                {
                        MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS,
                        MedicDbContract.PATIENT_FULL_NAME,
                        MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS
                };

        int[] toViewIds =
                {
                        R.id.cursor_item_examination_date,
                        R.id.cursor_item_examination_patientFullName,
                        R.id.cursor_item_examination_complaints
                };

        return new SimpleCursorTreeAdapter(context, cursor, android.R.layout.simple_expandable_list_item_1, null, null, R.layout.item_examination, fromColumns, toViewIds) {
            @Override
            protected Cursor getChildrenCursor(Cursor groupCursor) {
              return groupCursor;
            }
        };
    }
}
