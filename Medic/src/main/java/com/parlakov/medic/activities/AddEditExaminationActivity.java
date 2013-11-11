package com.parlakov.medic.activities;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.parlakov.medic.R;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.MedicDbContract;
import com.parlakov.medic.models.Examination;
import com.parlakov.medic.util.PatientsSimpleCursorAdapterSetterAsyncSetter;
import com.parlakov.medic.util.ViewHelper;
import com.parlakov.uow.IUowMedic;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by georgi on 13-11-11.
 */
public class AddEditExaminationActivity extends ActionBarActivity {

    public IUowMedic mData;
    private Examination mExamination;

    public Examination getExamination() {
        if(mExamination == null){
            mExamination = new Examination();
        }
        return mExamination;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_examination);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initialize();

        initializeUi();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mData != null){
            mData.getPatients().close();
        }
    }

    private void initialize() {
        PatientsSimpleCursorAdapterSetterAsyncSetter getter =
                new PatientsSimpleCursorAdapterSetterAsyncSetter();

        getter.execute(this);
    }

    private void initializeUi() {
        Button saveExamination = (Button) findViewById(R.id.button_examination_save);
        saveExamination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveExamination();
            }
        });
    }

    private void doSaveExamination() {
        Examination examination = getExaminationFromUi();

        IUowMedic data = getData();

        data.getExaminations().add(examination);
    }

    private Examination getExaminationFromUi() {
        Examination examination = getExamination();

        Spinner patientSpinner = (Spinner) findViewById(R.id.spinner_patients);
        long selectedPatientId = patientSpinner.getSelectedItemId();

        Date examDate = getExaminationDate();

        String complaints = ViewHelper
                .getTextFromEditView(R.id.editText_examination_complaints, this);
        String conclusion = ViewHelper
                .getTextFromEditView(R.id.editText_examination_conclusion, this);
        String notes = ViewHelper
                .getTextFromEditView(R.id.editText_examination_notes, this);
        String treatment = ViewHelper
                .getTextFromEditView(R.id.editText_examination_treatment, this);

        examination.setDate(examDate);
        examination.setComplaints(complaints);
        examination.setConclusion(conclusion);
        examination.setTreatment(treatment);
        examination.setNotes(notes);
        examination.setId(selectedPatientId);
        return examination;
    }

    private Date getExaminationDate() {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker_examinationDate);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker_examinationTime);

        Date date = new Date();

        if(datePicker != null && timePicker != null){
            date = new Date(datePicker.getYear(),
                    datePicker.getMonth(),
                    datePicker.getDayOfMonth(),
                    timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute());
        }

        return date;
    }

    public IUowMedic getData() {
        if(mData == null){
            mData = new LocalData(getApplicationContext());
        }
        return mData;
    }
}
