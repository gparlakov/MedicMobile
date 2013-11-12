package com.parlakov.medic.localdata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parlakov.medic.models.Patient;
import com.parlakov.uow.IRepository;

/**
 * Created by georgi on 13-11-2.
 */
public class LocalPatients implements IRepository<Patient> {
    private final MedicDbHelper mDbHelper;

    private SQLiteDatabase db;

    private SQLiteDatabase getDb() {
        if (db == null) {
            db = mDbHelper.getWritableDatabase();
        }
        return db;
    }

    public LocalPatients(MedicDbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    @Override
    public Patient getById(Object id) {
        if (id == null) {
            throw new NullPointerException("Empty patient id passed");
        }
        long idPat = Long.parseLong(String.valueOf(id));
        SQLiteDatabase db = getDb();
        Cursor cursor = null;
        Patient patient = null;

        if (db != null) {
            cursor = db.query(MedicDbContract.Patient.TABLE_NAME,
                    null,
                    MedicDbContract.Patient.COLUMN_NAME_ID + " = " + idPat,
                    null,
                    null,
                    null,
                    null);
        }

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

    public Cursor getAll() {
        SQLiteDatabase db = getDb();

        String[] columns = {
                MedicDbContract.Patient.COLUMN_NAME_ID,
                MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME,
                MedicDbContract.Patient.COLUMN_NAME_LAST_NAME,
                MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH
        };

        String groupBy = MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME;

        String orderBy = MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME;

        Cursor allPatients = db.query(MedicDbContract.Patient.TABLE_NAME,
                columns, null, null, groupBy, null, orderBy);

        return allPatients;
    }

    public void add(Patient entity) {
        if (entity == null)
            throw new NullPointerException("Passed entity is null");

        SQLiteDatabase db = getDb();

        ContentValues values = getContentValuesFromEntity(entity);

        db.insert(MedicDbContract.Patient.TABLE_NAME, null, values);
        db.close();
    }

    public void delete(Patient entity) {
        if (entity == null) {
            throw new NullPointerException("Null patient pointer passed!");
        }

        long id = entity.getId();
        SQLiteDatabase db = getDb();

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

        db.close();
    }


    public void close() {
        if (db != null)
            db.close();
    }

    public Cursor searchByName(String query) {
        String queryLowercased = query.toLowerCase();

        SQLiteDatabase db = getDb();

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


    private ContentValues getContentValuesFromEntity(Patient entity) {
        ContentValues values = new ContentValues();

        values.put(MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME, entity.getFirstName());
        values.put(MedicDbContract.Patient.COLUMN_NAME_LAST_NAME, entity.getLastName());
        values.put(MedicDbContract.Patient.COLUMN_NAME_AGE, entity.getAge());
        values.put(MedicDbContract.Patient.COLUMN_NAME_PHONE, entity.getPhone());
        values.put(MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH, entity.getPhotoPath());

        return values;
    }
}
