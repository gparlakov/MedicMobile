package com.parlakov.medic.localdata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parlakov.medic.models.Patient;
import com.parlakov.uow.IRepository;

import static com.parlakov.medic.util.PatientDataHelper.getContentValuesFromEntity;
import static com.parlakov.medic.util.PatientDataHelper.getPatient;

/**
 * Created by georgi on 13-11-2.
 */
public class LocalPatients implements IRepository<Patient> {
    private final MedicDbHelper mDbHelper;

    private SQLiteDatabase db;

    private SQLiteDatabase open() {
        if (db == null) {
            db = mDbHelper.getWritableDatabase();
        }
        return db;
    }

    public void close() {
        if (db != null){
            db.close();
            db = null;
        }
    }

    //constructor
    public LocalPatients(MedicDbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    @Override
    public Patient getById(Object id) {
        if (id == null) {
            throw new NullPointerException("Empty patient id passed");
        }
        long idPat = Long.parseLong(String.valueOf(id));
        SQLiteDatabase db = open();
        Cursor cursor = null;

        if (db != null) {
            cursor = db.query(MedicDbContract.Patient.TABLE_NAME,
                    null, MedicDbContract.Patient.COLUMN_NAME_ID + " = " + idPat,
                    null, null, null, null);
        }

        Patient patient = getPatient(idPat, cursor);

        return patient;
    }

    public Cursor getAll() {
        SQLiteDatabase db = open();

        String[] columns = {
                MedicDbContract.Patient.COLUMN_NAME_ID,
                MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME,
                MedicDbContract.Patient.COLUMN_NAME_LAST_NAME,
                MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH
        };

        String orderBy = MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME;

        Cursor allPatients = db.query(MedicDbContract.Patient.TABLE_NAME,
                columns, null, null, null, null, orderBy);

        return allPatients;
    }

    public void add(Patient entity) {
        if (entity == null)
            throw new NullPointerException("Passed entity is null");

        SQLiteDatabase db = open();

        ContentValues values = getContentValuesFromEntity(entity);

        db.insert(MedicDbContract.Patient.TABLE_NAME, null, values);
        close();
    }

    @Override
    public void deleteOnId(Object id) {

    }

    public void delete(Patient entity) {
        if (entity == null) {
            throw new NullPointerException("Null patient pointer passed!");
        }

        long id = entity.getId();
        SQLiteDatabase db = open();

        db.delete(MedicDbContract.Patient.TABLE_NAME,
                MedicDbContract.Patient.COLUMN_NAME_ID + " = " + id,
                null);

    }

    public void update(Patient entity) {
        if (entity == null)
            throw new NullPointerException("No patient to update");

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = getContentValuesFromEntity(entity);

        db.update(MedicDbContract.Patient.TABLE_NAME,
                values,
                MedicDbContract.Patient.COLUMN_NAME_ID + " = " + entity.getId(),
                null);

        close();
    }

    public Cursor searchByName(String query) {
        SQLiteDatabase db = open();

        String[] columns = {
                MedicDbContract.Patient.COLUMN_NAME_ID,
                MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME,
                MedicDbContract.Patient.COLUMN_NAME_LAST_NAME,
                MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH
        };

        String selection = MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME +
                " LIKE ? OR " + MedicDbContract.Patient.COLUMN_NAME_LAST_NAME +
                " LIKE ?";

        String[] args = { query + "%", query + "%" };

        String orderBy = MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME;

        Cursor cursor = db.query(MedicDbContract.Patient.TABLE_NAME,
                columns, selection, args, null, null, orderBy);

        return cursor;
    }
}
