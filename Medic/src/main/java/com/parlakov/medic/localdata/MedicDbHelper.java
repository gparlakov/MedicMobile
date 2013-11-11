package com.parlakov.medic.localdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * Created by georgi on 13-11-2.
 */
public class MedicDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_FOLDER = "medic";
    public static final String DATABASE_NAME = "Medic.db";

    public static int DATABASE_VERSION = 4;

    //<editor-fold desc="SQL_QUERIES">
    public static final String SQL_CREATE_TABLE_PATIENTS =
            "CREATE TABLE " + MedicDbContract.Patient.TABLE_NAME + "(" +
                    MedicDbContract.Patient.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME + " TEXT, " +
                    MedicDbContract.Patient.COLUMN_NAME_LAST_NAME + " TEXT NOT NULL," +
                    MedicDbContract.Patient.COLUMN_NAME_AGE + " INTEGER," +
                    MedicDbContract.Patient.COLUMN_NAME_PHONE + " TEXT," +
                    MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH + " TEXT)";

    public static final String SQL_CREATE_TABLE_EXAMINATIONS =
            "CREATE TABLE " + MedicDbContract.Examination.TABLE_NAME + "(" +
                    MedicDbContract.Examination.COLUMN_NAME_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MedicDbContract.Examination.COLUMN_NAME_PATIENT_ID +
                    " INTEGER NOT NULL, " +
                    MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS +
                    " TEXT, " +
                    MedicDbContract.Examination.COLUMN_NAME_CONCLUSION +
                    " TEXT, " +
                    MedicDbContract.Examination.COLUMN_NAME_TREATMENT +
                    " TEXT, " +
                    MedicDbContract.Examination.COLUMN_NAME_NOTES +
                    " TEXT, " +
                    MedicDbContract.Examination.COLUMN_NAME_CANCELED +
                    " BOOLEAN, " +
                    MedicDbContract.Examination.COLUMN_NAME_DATE +
                    " TEXT NOT NULL," +
                    "CONSTRAINT FK_PATIENT_ID FOREIGN KEY (" +
                    MedicDbContract.Examination.COLUMN_NAME_PATIENT_ID + ")" +
                    "REFERENCES " + MedicDbContract.Patient.TABLE_NAME + "(" +
                    MedicDbContract.Patient.COLUMN_NAME_ID + ")" +
                    ")";

    public static final String SQL_DROP_TABLE_PATIENTS =
            "DROP TABLE IF EXISTS " + MedicDbContract.Patient.TABLE_NAME;

    public static final String SQL_DROP_TABLE_EXAMINATIONS=
            "DROP TABLE IF EXISTS " + MedicDbContract.Examination.TABLE_NAME;
    //</editor-fold>

    public MedicDbHelper(Context context, String databasePath) {
        super(context, databasePath, null, DATABASE_VERSION);
    }

    public static String getSDDatabasePath() {
        return Environment.getExternalStorageDirectory() + File.separator +
                DATABASE_FOLDER + File.separator + DATABASE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PATIENTS);
        db.execSQL(SQL_CREATE_TABLE_EXAMINATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE_EXAMINATIONS);
        db.execSQL(SQL_DROP_TABLE_PATIENTS);

        onCreate(db);
    }
}
