package com.parlakov.medic.util;

import android.content.ContentValues;
import android.database.Cursor;

import com.parlakov.medic.localdata.MedicDbContract;
import com.parlakov.medic.models.Examination;
import com.parlakov.medic.viewModels.ExaminationDetails;

/**
 * Created by georgi on 13-11-18.
 */
public class ExaminationDataHelper {

    public static ContentValues getContentValues(Examination entity) {
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

    public static Examination getExamination(Cursor cursor) {
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

    public static ExaminationDetails getExaminationDetails(Cursor cursor) {
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
}
