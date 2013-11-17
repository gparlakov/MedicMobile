package com.parlakov.medic.localdata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.parlakov.medic.Global;
import com.parlakov.medic.exceptions.MedicException;
import com.parlakov.medic.models.Examination;
import com.parlakov.medic.viewModels.ExaminationDetails;
import com.parlakov.uow.IRepository;

/**
 * Created by georgi on 13-11-11.
 */
public class LocalExaminations implements IRepository<Examination> {

    private final SQLiteOpenHelper mDbHelper;
    private SQLiteDatabase mDb;

    private SQLiteDatabase openDb() {
        if (mDb == null) {
            mDb = mDbHelper.getWritableDatabase();
        }
        return mDb;
    }

    public void close() {
        if (mDb != null) {
            mDb.close();
            mDb = null;
        }
    }

    //<editor-fold desc="Query examinations with patient full name">
    private String QUERY_EXAMINATIONS_WITH_PATIENT_NAMES =
            " SELECT " +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_ID + "]," +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS + "]," +
                    " p.[" + MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME + "] " +
                    " || ' ' || p.[" +
                    MedicDbContract.Patient.COLUMN_NAME_LAST_NAME + "] " +
                    " AS [" + MedicDbContract.PATIENT_FULL_NAME + "]," +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS + "] " +
                "FROM " + MedicDbContract.Examination.TABLE_NAME + " e INNER JOIN " +
                    MedicDbContract.Patient.TABLE_NAME + " p ON e.[" +
                    MedicDbContract.Examination.COLUMN_NAME_PATIENT_ID +
                    "] = p.[" + MedicDbContract.Patient.COLUMN_NAME_ID + "] " +
                "WHERE " + MedicDbContract.Examination.COLUMN_NAME_CANCELED + " IS NULL " +
                "ORDER BY e.[" + MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS + "] DESC;";

    private String QUERY_EXAMINATIONS_WITH_PATIENT_NAMES_TIME_WITHIN_TIME_PERIOD =
            " SELECT " +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_ID + "]," +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS + "]," +
                    " p.[" + MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME + "] " +
                    "|| ' ' || p.[" +
                    MedicDbContract.Patient.COLUMN_NAME_LAST_NAME + "] " +
                    "AS [" + MedicDbContract.PATIENT_FULL_NAME + "]," +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS + "] " +
                "FROM " + MedicDbContract.Examination.TABLE_NAME + " e INNER JOIN " +
                    MedicDbContract.Patient.TABLE_NAME + " p ON e.[" +
                    MedicDbContract.Examination.COLUMN_NAME_PATIENT_ID +
                    "] = p.[" + MedicDbContract.Patient.COLUMN_NAME_ID + "] " +
                " WHERE e.[" + MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS + "] > ? AND " +
                    "e.[" + MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS + "] < ? AND " +
                    MedicDbContract.Examination.COLUMN_NAME_CANCELED + " IS NULL " +
                "ORDER BY e.[" + MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS + "];";

    private String QUERY_EXAMINATIONS_BY_ID_WITH_PATIENT_NAMES_AND_PHOTO =
            " SELECT " +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_ID + "]," +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS + "], " +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS + "], " +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_CONCLUSION + "], " +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_TREATMENT + "], " +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_NOTES + "]," +
                    " e.[" + MedicDbContract.Examination.COLUMN_NAME_PATIENT_ID + "]," +
                    " p.[" + MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME + "] " +
                        "|| ' ' || p.[" + MedicDbContract.Patient.COLUMN_NAME_LAST_NAME + "]" +
                        "AS [" + MedicDbContract.PATIENT_FULL_NAME + "]," +
                    " p.[" + MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH + "] " +
                "FROM " + MedicDbContract.Examination.TABLE_NAME + " e INNER JOIN " +
                    MedicDbContract.Patient.TABLE_NAME + " p ON e.[" +
                    MedicDbContract.Examination.COLUMN_NAME_PATIENT_ID +
                    "] = p.[" + MedicDbContract.Patient.COLUMN_NAME_ID + "] " +
                "WHERE e.[" + MedicDbContract.Examination._ID + "] = ? ;";

    public static final String QUERY_COUNT_OF_EXAMINATIONS_IN_GIVEN_TIME_PERIOD =
            "SELECT COUNT(" + MedicDbContract.Examination._ID + ")" +
                    "FROM " + MedicDbContract.Examination.TABLE_NAME +" " +
                    "WHERE " + MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS +
                        " > ? AND " + MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS +
                        " < ?";
    //</editor-fold>

    public LocalExaminations(SQLiteOpenHelper openDbHelper) {
        mDbHelper = openDbHelper;
    }

    public ExaminationDetails getDetailsById(long mExaminationId) {
        SQLiteDatabase db = openDb();
        Cursor examinationDetailsCursor = db
                .rawQuery(QUERY_EXAMINATIONS_BY_ID_WITH_PATIENT_NAMES_AND_PHOTO,
                        new String[]{String.valueOf(mExaminationId)});

        return getExaminationDetails(examinationDetailsCursor);
    }

    @Override
    public Examination getById(Object id) {
        SQLiteDatabase db = openDb();

        String query = "SELECT * FROM " + MedicDbContract.Examination.TABLE_NAME + " WHERE " +
                MedicDbContract.Examination.COLUMN_NAME_ID + " = ?";
        String[] args = {String.valueOf(id)};

        Cursor cursor = db.rawQuery(query, args);

        return getExamination(cursor);
    }

    @Override
    public Object getAll() {
        return null;
    }

    public Cursor getAllWithPatientNames(long startInMillis, long endInMillis) {
        SQLiteDatabase db = openDb();
        Cursor result;

        if (startInMillis != 0 && endInMillis != 0) {
            String[] selectionArgs =
                    {
                            String.valueOf(startInMillis),
                            String.valueOf(endInMillis),
                    };
            result = db.rawQuery(QUERY_EXAMINATIONS_WITH_PATIENT_NAMES_TIME_WITHIN_TIME_PERIOD,
                    selectionArgs);
        } else {
            result = db.rawQuery(QUERY_EXAMINATIONS_WITH_PATIENT_NAMES, null);
        }

        return result;
    }

    @Override
    public void add(Examination entity) throws MedicException {
        checkForOverlappingExaminations(entity);

        SQLiteDatabase db = openDb();

        ContentValues values = getContentValues(entity);

        db.insert(MedicDbContract.Examination.TABLE_NAME, null, values);
    }

    @Override
    public void delete(Examination entity) {

    }

    @Override
    public void update(Examination entity) throws MedicException {
        checkForOverlappingExaminations(entity);

        long id = entity.getId();
        ContentValues values = getContentValues(entity);
        SQLiteDatabase db = openDb();

        String where = MedicDbContract.Examination._ID + " = ?";
        String[] args = {String.valueOf(id)};

        db.update(MedicDbContract.Examination.TABLE_NAME,
                values, where, args);
    }

    private ContentValues getContentValues(Examination entity) {
        ContentValues values = new ContentValues();
        values.put(MedicDbContract.Examination.COLUMN_NAME_PATIENT_ID, entity.getPatientId());
        values.put(MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS, entity.getDateInMillis());
        values.put(MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS, entity.getComplaints());
        values.put(MedicDbContract.Examination.COLUMN_NAME_CONCLUSION, entity.getConclusion());
        values.put(MedicDbContract.Examination.COLUMN_NAME_TREATMENT, entity.getTreatment());
        values.put(MedicDbContract.Examination.COLUMN_NAME_NOTES, entity.getNotes());
        values.put(MedicDbContract.Examination.COLUMN_NAME_CANCELED, entity.getCancelled());
        return values;
    }

    private Examination getExamination(Cursor cursor) {
        Examination examination = new Examination();
        if (cursor != null) {
            cursor.moveToFirst();

            long patientId = cursor.getLong(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_PATIENT_ID));
            long examinationId = cursor.getLong(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_ID));
            long dateInMillis = cursor.getLong(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS));

            String complaints = cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS));
            String notes = cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_NOTES));
            String treatment = cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_TREATMENT));
            String conclusion = cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_CONCLUSION));

            examination.setId(examinationId);
            examination.setPatientId(patientId);
            examination.setDateInMillis(dateInMillis);
            examination.setComplaints(complaints);
            examination.setNotes(notes);
            examination.setTreatment(treatment);
            examination.setConclusion(conclusion);
        }
        return examination;
    }

    private ExaminationDetails getExaminationDetails(Cursor cursor) {
//        ExaminationDetails details = (ExaminationDetails) getExamination(cursor);
        ExaminationDetails details = new ExaminationDetails();

        if (cursor != null) {
            cursor.moveToFirst();

            long patientId = cursor.getLong(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_PATIENT_ID));
            long examinationId = cursor.getLong(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_ID));
            long dateInMillis = cursor.getLong(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS));

            String complaints = cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS));
            String notes = cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_NOTES));
            String treatment = cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_TREATMENT));
            String conclusion = cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_CONCLUSION));

            details.setId(examinationId);
            details.setPatientId(patientId);
            details.setDateInMillis(dateInMillis);
            details.setComplaints(complaints);
            details.setNotes(notes);
            details.setTreatment(treatment);
            details.setConclusion(conclusion);


            String patientName = cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.PATIENT_FULL_NAME));

            String photoPath = cursor.getString(
                    cursor.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH));

            details.setPatientName(patientName);
            details.setPatientPhotoPath(photoPath);
        }

        return details;
    }

    private void checkForOverlappingExaminations(Examination entity)
            throws MedicException {
        long examLengthInMillis = Global.EXAMINATION_LENGTH_IN_MINUTES * 60 * 1000;
        long minusThirtyMinutes = entity.getDateInMillis() - examLengthInMillis;
        long plusThirtyMinutes = entity.getDateInMillis() + examLengthInMillis;

        int countOfExaminationsOverlapping = getExaminationsCountInTimePeriod(minusThirtyMinutes,
                plusThirtyMinutes);

        if(countOfExaminationsOverlapping > 0){
            throw new MedicException("Overlapping examinations found!");
        }
    }

    private int getExaminationsCountInTimePeriod(long start, long end){
        SQLiteDatabase db = openDb();

        String[] args = { String.valueOf(start), String.valueOf(end)};
        Cursor countCursor =
                db.rawQuery(QUERY_COUNT_OF_EXAMINATIONS_IN_GIVEN_TIME_PERIOD, args);

        int count = 0;
        if(countCursor != null){
            countCursor.moveToFirst();
            count = countCursor.getInt(0);
        }

        return count;
    }
}
