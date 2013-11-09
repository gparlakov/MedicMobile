package com.parlakov.medic.localdata;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parlakov.medic.models.Patient;
import com.parlakov.uow.IRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Queue;

/**
 * Created by georgi on 13-11-2.
 */
public class LocalPatients {

    private final MedicDbHelper mDbHelper;

    public LocalPatients(MedicDbHelper dbHelper){
        mDbHelper = dbHelper;
    }

    public Patient getById(long id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = null;
        Patient patient = null;

        if(db != null){
            cursor = db.query(MedicDbContract.Patient.TABLE_NAME,
                    null,
                    MedicDbContract.Patient.COLUMN_NAME_ID + " = " + id,
                    null,
                    null,
                    null,
                    null);
        }
        if(cursor != null){
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
                    cursor.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_IMAGE_PATH)));

            patient.setId(id);
        }


        return patient;
    }

    public Cursor getAll() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor allPatients = db.rawQuery("Select * from " +
                MedicDbContract.Patient.TABLE_NAME, null);

//        db.close();
        return allPatients;
    }

    public void add(Patient entity) {
        if(entity == null)
            throw new NullPointerException("Passed entity is null");

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME, entity.getFirstName());
        values.put(MedicDbContract.Patient.COLUMN_NAME_LAST_NAME, entity.getLastName());
        values.put(MedicDbContract.Patient.COLUMN_NAME_AGE, entity.getAge());
        values.put(MedicDbContract.Patient.COLUMN_NAME_PHONE, entity.getPhone());
        values.put(MedicDbContract.Patient.COLUMN_NAME_IMAGE_PATH, entity.getImagePath());


        db.insert(MedicDbContract.Patient.TABLE_NAME, null, values);
        db.close();
    }

    public Boolean delete(int id) {
        return null;
    }

    public Patient update(Patient entity) {
        return null;
    }
}
