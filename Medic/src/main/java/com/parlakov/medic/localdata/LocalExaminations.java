package com.parlakov.medic.localdata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.parlakov.medic.models.Examination;
import com.parlakov.uow.IRepository;

/**
 * Created by georgi on 13-11-11.
 */
public class LocalExaminations implements IRepository<Examination> {
    private final SQLiteOpenHelper mDbHelper;
    private SQLiteDatabase mDb;

    //<editor-fold desc="Query examinations with patient full name">
    private String QUERY_EXAMINATIONS_WITH_PATIENT_NAMES =
            " SELECT" +
            "    e.[" + MedicDbContract.Examination.COLUMN_NAME_ID + "]," +
            "    e.[" + MedicDbContract.Examination.COLUMN_NAME_DATE + "]," +
            "    firstName || ' ' || lastName as [" + MedicDbContract.PATIENT_FULL_NAME + "]," +
            "    e.[" + MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS + "]" +
            "FROM examinations e LEFT JOIN patients p on e.[" +
                    MedicDbContract.Examination.COLUMN_NAME_PATIENT_ID +
                    "] = p.[" + MedicDbContract.Patient.COLUMN_NAME_ID + "] " +
            "ORDER BY e.[" + MedicDbContract.Examination.COLUMN_NAME_DATE + "] DESC";
    //</editor-fold>

    public LocalExaminations(SQLiteOpenHelper openDbHelper) {
        mDbHelper = openDbHelper;
    }

    @Override
    public Examination getById(Object id) {
        SQLiteDatabase db = getDb();
        return null;
    }

    @Override
    public Object getAll() {
        SQLiteDatabase db = getDb();
        return null;
    }

    public Cursor getAllWithPatientNames(){
        SQLiteDatabase db = getDb();

        return db.rawQuery(QUERY_EXAMINATIONS_WITH_PATIENT_NAMES, null);
    }

    @Override
    public void add(Examination entity) {
        SQLiteDatabase db = getDb();

        ContentValues values = getContentValues(entity);

        db.insert(MedicDbContract.Examination.TABLE_NAME, null, values);
    }

    @Override
    public void delete(Examination entity) {

    }

    @Override
    public void update(Examination entity) {

    }

    public void close(){
        if(mDb != null){
            mDb.close();
        }
    }


    private ContentValues getContentValues(Examination entity) {
        ContentValues values = new ContentValues();
        values.put(MedicDbContract.Examination.COLUMN_NAME_PATIENT_ID, entity.getPatientId());
        values.put(MedicDbContract.Examination.COLUMN_NAME_DATE, entity.getDate().toString());
        values.put(MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS, entity.getComplaints());
        values.put(MedicDbContract.Examination.COLUMN_NAME_CONCLUSION, entity.getConclusion());
        values.put(MedicDbContract.Examination.COLUMN_NAME_TREATMENT, entity.getTreatment());
        values.put(MedicDbContract.Examination.COLUMN_NAME_NOTES, entity.getNotes());
        values.put(MedicDbContract.Examination.COLUMN_NAME_ID, entity.getId());
        values.put(MedicDbContract.Examination.COLUMN_NAME_CANCELED, entity.getCancelled());
        return values;
    }

    private SQLiteDatabase getDb() {
        if(mDb == null){
            mDb = mDbHelper.getWritableDatabase();
        }
        return mDb;
    }

}
