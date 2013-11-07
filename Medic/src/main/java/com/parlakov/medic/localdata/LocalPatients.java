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

/**
 * Created by georgi on 13-11-2.
 */
public class LocalPatients {

    private final MedicDbHelper mDbHelper;

    public LocalPatients(MedicDbHelper dbHelper){
        mDbHelper = dbHelper;
    }

    public com.parlakov.medic.models.Patient getById(int id) {
        return null;
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
