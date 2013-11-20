package com.parlakov.medic.viewbinders;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.TextView;

import com.parlakov.medic.localdata.MedicDbContract;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by georgi on 13-11-15.
 */
public class ExaminationsListViewBinder
        implements SimpleCursorAdapter.ViewBinder {

    @Override
    public boolean setViewValue(View view, Cursor cursor, int colIndex) {
        if (colIndex == cursor.getColumnIndex(
                MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS)){

            long milliseconds = cursor.getLong(colIndex);
            Date date = new Date(milliseconds);

            String formated = new SimpleDateFormat("HH:mm EEE\ndd/MM/yyyy")
                    .format(date);
            ((TextView)view).setText(formated);
            return true;
        }

        if(colIndex == cursor.getColumnIndex(MedicDbContract.PATIENT_FULL_NAME)){
            // if first name is empty trims the rest
            String name = cursor.getString(colIndex);
            ((TextView)view).setText(name.trim());
            return true;
        }

        return false;
    }

}
