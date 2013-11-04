package com.parlakov.medic.localdata;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.parlakov.medic.models.Patient;
import com.parlakov.uow.IRepository;

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
    public Collection<com.parlakov.medic.models.Patient> getAll() {
        return null;
    }

    @Override
    public void add(com.parlakov.medic.models.Patient entity) {
        try{
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.onUpgrade(db,1,2);
        ContentValues values = new ContentValues();

        values.put(MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME, entity.getFirstName());
        values.put(MedicDbContract.Patient.COLUMN_NAME_LAST_NAME, entity.getLastName());
        values.put(MedicDbContract.Patient.COLUMN_NAME_AGE, entity.getAge());

        db.insert(MedicDbContract.Patient.TABLE_NAME, null, values);
        }
        catch(Exception e){
            Log.e("add:", e.getMessage());
        }
    }

    @Override
    public Boolean delete(com.parlakov.medic.models.Patient entity) {
        return null;
    }

    @Override
    public Boolean delete(int id) {
        return null;
    }

    @Override
    public com.parlakov.medic.models.Patient update(com.parlakov.medic.models.Patient entity) {
        return null;
    }
}
