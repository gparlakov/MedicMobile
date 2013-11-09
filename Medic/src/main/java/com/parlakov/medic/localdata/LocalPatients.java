package com.parlakov.medic.localdata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parlakov.medic.models.Patient;

import java.io.File;

/**
 * Created by georgi on 13-11-2.
 */
public class LocalPatients {
    private final MedicDbHelper mDbHelper;

    private SQLiteDatabase db;
    private SQLiteDatabase getDb(){
        if (db == null){
            db = mDbHelper.getWritableDatabase();
        }
        return  db;
    }

    public LocalPatients(MedicDbHelper dbHelper){
        mDbHelper = dbHelper;
    }

    public Patient getById(long id) {
        SQLiteDatabase db = getDb();
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
                    cursor.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH)));

            patient.setId(id);
        }

        return patient;
    }

    public Cursor getAll() {
        SQLiteDatabase db = getDb();
        Cursor allPatients = db.rawQuery("Select * from " +
                MedicDbContract.Patient.TABLE_NAME, null);

        return allPatients;
    }

    public void add(Patient entity) {
        if(entity == null)
            throw new NullPointerException("Passed entity is null");

        SQLiteDatabase db = getDb();

        ContentValues values = new ContentValues();
        values.put(MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME, entity.getFirstName());
        values.put(MedicDbContract.Patient.COLUMN_NAME_LAST_NAME, entity.getLastName());
        values.put(MedicDbContract.Patient.COLUMN_NAME_AGE, entity.getAge());
        values.put(MedicDbContract.Patient.COLUMN_NAME_PHONE, entity.getPhone());
        values.put(MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH, entity.getPhotoPath());


        db.insert(MedicDbContract.Patient.TABLE_NAME, null, values);
        db.close();
    }

    public void delete(long id) {
        SQLiteDatabase db = getDb();

        db.delete(MedicDbContract.Patient.TABLE_NAME,
                MedicDbContract.Patient.COLUMN_NAME_ID + " = " + id,
                null);
    }

    public void update(Patient entity) {
        if(entity == null)
            throw new NullPointerException("No patient to update");

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME, entity.getFirstName());
        values.put(MedicDbContract.Patient.COLUMN_NAME_LAST_NAME, entity.getLastName());
        values.put(MedicDbContract.Patient.COLUMN_NAME_AGE, entity.getAge());
        values.put(MedicDbContract.Patient.COLUMN_NAME_PHONE, entity.getPhone());
        values.put(MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH, entity.getPhotoPath());

        db.update(MedicDbContract.Patient.TABLE_NAME,
                values,
                MedicDbContract.Patient.COLUMN_NAME_ID + " = " + entity.getId(),
                null);

        db.close();
    }

    public void deletePhoto(String photoPath) {
        if(photoPath != null && !photoPath.isEmpty()){
            File photo = new File(photoPath);

            if(photo != null){
                photo.delete();
            }
        }
    }

    public void close(){
        if(db != null)
            db.close();
    }
}
