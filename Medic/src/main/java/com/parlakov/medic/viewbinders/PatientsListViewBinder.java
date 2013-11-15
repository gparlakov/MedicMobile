package com.parlakov.medic.viewbinders;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ImageView;

import com.parlakov.medic.R;
import com.parlakov.medic.localdata.MedicDbContract;
import com.parlakov.medic.util.ImageHelper;

/**
 * Created by georgi on 13-11-15.
 */
public class PatientsListViewBinder implements SimpleCursorAdapter.ViewBinder {

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        if (columnIndex ==
                cursor.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH)) {
            String picturePath = cursor.getString(columnIndex);
            ImageView patientPicture = (ImageView) view;

            if (picturePath != null && !picturePath.isEmpty()) {
                // reads the bitmap and image view size and calculates size of bitmap
                // to return
                ImageHelper.loadImageFromFileAsync(picturePath, patientPicture);
            }
            else {
                patientPicture.setImageResource(R.drawable.ic_default_picture);
            }
            return true;
        }

        return false;
    }
}
