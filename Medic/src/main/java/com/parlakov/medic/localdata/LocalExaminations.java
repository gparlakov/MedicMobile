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

import static com.parlakov.medic.util.ExaminationDataHelper.getContentValues;
import static com.parlakov.medic.util.ExaminationDataHelper.getExamination;
import static com.parlakov.medic.util.ExaminationDataHelper.getExaminationDetails;

/**
 * Created by georgi on 13-11-11.
 */
public class LocalExaminations implements IRepository<Examination> {

    private final SQLiteOpenHelper mDbHelper;
    private SQLiteDatabase mDb;

    private SQLiteDatabase open() {
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

    public static final String QUERY_COUNT_OF_EXAMINATIONS_IN_GIVEN_TIME_PERIOD_EXCLUDING_CURRENT =
            "SELECT COUNT(" + MedicDbContract.Examination._ID + ")" +
                    "FROM " + MedicDbContract.Examination.TABLE_NAME +" " +
                    "WHERE " + MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS +
                        " > ? AND " + MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS +
                        " < ? AND " + MedicDbContract.Examination._ID + " != ?";
    //</editor-fold>

    // constructor
    public LocalExaminations(SQLiteOpenHelper openDbHelper) {
        mDbHelper = openDbHelper;
    }

    public ExaminationDetails getDetailsById(long mExaminationId) {
        SQLiteDatabase db = open();
        Cursor examinationDetailsCursor = db
                .rawQuery(QUERY_EXAMINATIONS_BY_ID_WITH_PATIENT_NAMES_AND_PHOTO,
                        new String[]{String.valueOf(mExaminationId)});

        return getExaminationDetails(examinationDetailsCursor);
    }

    public Cursor getByPatientId(long patientId){
        SQLiteDatabase db = open();

        String[] columns =
                {
                        MedicDbContract.Examination._ID,
                        MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS,
                        MedicDbContract.Examination.COLUMN_NAME_TREATMENT,
                        MedicDbContract.Examination.COLUMN_NAME_CONCLUSION,
                        MedicDbContract.Examination.COLUMN_NAME_NOTES,
                        MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS,
                };

        String selection = MedicDbContract.Examination.COLUMN_NAME_PATIENT_ID + " = ? AND " +
                MedicDbContract.Examination.COLUMN_NAME_CANCELED + " IS NULL";
        String[] args = { String.valueOf(patientId) };

        String orderby =  MedicDbContract.Examination.COLUMN_NAME_DATE_IN_MILLIS + " DESC";

        return db.query(MedicDbContract.Examination.TABLE_NAME, columns,
                selection, args, null, null, orderby);
    }

    @Override
    public Examination getById(Object id) {
        SQLiteDatabase db = open();

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
        SQLiteDatabase db = open();
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

        SQLiteDatabase db = open();

        ContentValues values = getContentValues(entity);

        db.insert(MedicDbContract.Examination.TABLE_NAME, null, values);
    }

    @Override
    public void deleteOnId(Object id) {
        SQLiteDatabase db = open();
        String[] args = {String.valueOf(id)};
        db.delete(MedicDbContract.Examination.TABLE_NAME,
                MedicDbContract.Examination.COLUMN_NAME_ID + " = ?", args);
    }

    @Override
    public void delete(Examination entity) {
        SQLiteDatabase db = open();
        String[] args = {String.valueOf(entity.getPatientId())};
        db.delete(MedicDbContract.Examination.TABLE_NAME,
                MedicDbContract.Examination.COLUMN_NAME_ID + " = ?", args);
    }

    @Override
    public void update(Examination entity) throws MedicException {
        // if examination is not cancelled check for overlapping
        if(entity.getCancelled() == null || !entity.getCancelled()){
            // will throw MedicException to be intercepted by calling method
            checkForOverlappingExaminations(entity);
        }

        long id = entity.getId();
        ContentValues values = getContentValues(entity);
        SQLiteDatabase db = open();

        String where = MedicDbContract.Examination._ID + " = ?";
        String[] args = {String.valueOf(id)};

        db.update(MedicDbContract.Examination.TABLE_NAME,
                values, where, args);
    }

    private void checkForOverlappingExaminations(Examination entity)
            throws MedicException {
        long examLengthInMillis = Global.EXAMINATION_LENGTH_IN_MINUTES * 60 * 1000;
        long minusThirtyMinutes = entity.getDateInMillis() - examLengthInMillis;
        long plusThirtyMinutes = entity.getDateInMillis() + examLengthInMillis;

        int countOfExaminationsOverlapping = getExaminationsCountInTimePeriod(minusThirtyMinutes,
                plusThirtyMinutes, entity.getId());

        if(countOfExaminationsOverlapping > 0){
            throw new MedicException("Overlapping examinations found!");
        }
    }

    private int getExaminationsCountInTimePeriod(long start, long end, long currentExaminationId){
        SQLiteDatabase db = open();

        String[] args = { String.valueOf(start), String.valueOf(end),
                String.valueOf(currentExaminationId)};
        Cursor countCursor =
                db.rawQuery(QUERY_COUNT_OF_EXAMINATIONS_IN_GIVEN_TIME_PERIOD_EXCLUDING_CURRENT, args);

        int count = 0;
        if(countCursor != null){
            countCursor.moveToFirst();
            count = countCursor.getInt(0);
        }

        return count;
    }
}
