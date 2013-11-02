package com.parlakov.medic.localdata;

import android.provider.BaseColumns;

/**
 * Created by georgi on 13-11-2.
 */
public final class MedicDbContract {
    public MedicDbContract(){
    }

    public static abstract class User implements BaseColumns{
        public static final String TABLE_NAME = "users";

        public static final String COLUMN_NAME_ID = "id";
    }

    public static abstract class Patient implements BaseColumns{
        public static final String TABLE_NAME = "patients";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_FIRST_NAME = "firstName";
        public static final String COLUMN_NAME_LAST_NAME = "lastName";
        public static final String COLUMN_NAME_AGE = "age";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_PHONE = "phone";
    }

    public static abstract class Examination implements BaseColumns{
        public static final String TABLE_NAME = "examinations";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_PATIENT_ID = "patientId";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_COMPLAINTS = "complaints";
        public static final String COLUMN_NAME_CONCLUSION = "conclusion";
        public static final String COLUMN_NAME_TREATMENT = "treatment";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_CANCELED = "canceled";

    }

}
