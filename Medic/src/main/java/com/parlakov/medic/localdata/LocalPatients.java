package com.parlakov.medic.localdata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parlakov.medic.models.Patient;
import com.parlakov.uow.IRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by georgi on 13-11-2.
 */
public class LocalPatients implements IRepository<Patient> {

    private final MedicDbHelper mDbHelper;

    public LocalPatients(MedicDbHelper dbHelper){
        mDbHelper = dbHelper;
    }

    @Override
    public com.parlakov.medic.models.Patient getById(int id) {
        return null;
    }

    @Override
    public Collection<Patient> getAll() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor allPatients = db.rawQuery("Select * from " + MedicDbContract.Patient.TABLE_NAME, null);

        Collection<Patient> patients = getCollection(allPatients);
        db.close();
        return  patients;
    }

    private Collection<Patient> getCollection(Cursor allPatients) {
        ArrayList<Patient> patientsList = new ArrayList<Patient>();
        if (allPatients != null && allPatients.getCount() > 0) {
            allPatients.moveToFirst();
            do {
                Patient nextPatient = new Patient();
                nextPatient.setFirstName(allPatients.getString(allPatients.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME)));
                nextPatient.setLastName(allPatients.getString(allPatients.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_LAST_NAME)));
                nextPatient.setAge(allPatients.getInt(allPatients.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_AGE)));
                nextPatient.setId(allPatients.getInt(allPatients.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_ID)));
                nextPatient.setPhone(allPatients.getString(allPatients.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_PHONE)));

                patientsList.add(nextPatient);

            } while (allPatients.moveToNext());
        }

        return patientsList;
    }

    @Override
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

    @Override
    public Boolean delete(Patient entity) {
        return null;
    }

    @Override
    public Boolean delete(int id) {
        return null;
    }

    @Override
    public Patient update(Patient entity) {
        return null;
    }
}
