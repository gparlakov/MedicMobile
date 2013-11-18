package com.parlakov.medic.util;

import android.content.ContentValues;
import android.database.Cursor;

import com.parlakov.medic.localdata.MedicDbContract;
import com.parlakov.medic.models.Patient;

/**
 * Created by georgi on 13-11-18.
 */
public class PatientDataHelper {
    public static Patient getPatient(long idPat, Cursor cursor) {
        Patient patient = null;

        if (cursor != null) {
            cursor.moveToFirst();
            patient = new Patient();
            patient.setFirstName(cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME)));

            patient.setLastName(cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_LAST_NAME)));

            patient.setAge(cursor.getInt(
                    cursor.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_AGE)));

            patient.setPhone(cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_PHONE)));

            patient.setImagePath(cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH)));

            patient.setId(idPat);
        }
        return patient;
    }

    public static ContentValues getContentValuesFromEntity(Patient entity) {
        ContentValues values = new ContentValues();

        values.put(MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME, entity.getFirstName());
        values.put(MedicDbContract.Patient.COLUMN_NAME_LAST_NAME, entity.getLastName());
        values.put(MedicDbContract.Patient.COLUMN_NAME_AGE, entity.getAge());
        values.put(MedicDbContract.Patient.COLUMN_NAME_PHONE, entity.getPhone());
        values.put(MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH, entity.getPhotoPath());

        return values;
    }
}
