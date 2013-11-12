package com.parlakov.medic.activities;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.parlakov.medic.R;
import com.parlakov.medic.fragments.DatePickerFragment;
import com.parlakov.medic.fragments.PatientDetailsFragment;
import com.parlakov.medic.fragments.TimePickerFragment;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.models.Examination;
import com.parlakov.medic.util.PatientsSimpleCursorAdapterSetterAsyncSetter;
import com.parlakov.medic.util.ViewHelper;
import com.parlakov.uow.IUowMedic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by georgi on 13-11-11.
 */
public class AddEditExaminationActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Examination mExamination;

    public IUowMedic mData;

    public long mPatientId;
    private FragmentManager mFragmentManager;
    private Button mSetDateButton;
    private Button mSetTimeButton;
    private Calendar mCalendar;

    public Examination getExamination() {
        if(mExamination == null){
            mExamination = new Examination();
        }
        return mExamination;
    }

    private Button getButtonSetDate() {
        if(mSetDateButton == null){
            mSetDateButton = (Button) findViewById(R.id.button_setExaminationDate);
        }

        return mSetDateButton;
    }

    private Button getButtonSetTime() {
        if(mSetTimeButton == null){
            mSetTimeButton = (Button) findViewById(R.id.button_setExaminationHour);
        }

        return mSetTimeButton;
    }

    private FragmentManager getFragmentManagerLazy() {
        if(mFragmentManager == null){
            mFragmentManager = getSupportFragmentManager();
        }
        return mFragmentManager;
    }

    private Calendar getCalendar(){
        if(mCalendar == null){
            mCalendar = Calendar.getInstance();
        }
        return mCalendar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_examination);

        mPatientId = getIntent().getLongExtra(PatientDetailsFragment.PATIENT_ID_EXTRA, 0);
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
        final Activity that = this;

        getButtonSetDate().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment =
                        new DatePickerFragment((DatePickerDialog.OnDateSetListener) that);

                datePickerFragment.show(getFragmentManagerLazy(), "datePickerDialog");
            }
        });

        getButtonSetTime().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment =
                        new TimePickerFragment((TimePickerDialog.OnTimeSetListener) that);

                timePickerFragment.show(getFragmentManagerLazy(), "timePickerDialog");
            }
        });


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

        finish();
    }

    private Examination getExaminationFromUi() {
        Examination examination = getExamination();

        Spinner patientSpinner = (Spinner) findViewById(R.id.spinner_patients);
        long selectedPatientId = patientSpinner.getSelectedItemId();

        Date examDate = getCalendar().getTime();

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
        examination.setPatientId(selectedPatientId);
        return examination;
    }

    public IUowMedic getData() {
        if(mData == null){
            mData = new LocalData(getApplicationContext());
        }
        return mData;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        getCalendar().set(year, monthOfYear, dayOfMonth);

        String dateString = new SimpleDateFormat("EEE dd/MM/yyyy")
                .format(getCalendar().getTime());

        getButtonSetDate().setText(dateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        getCalendar().set(Calendar.HOUR_OF_DAY, hourOfDay);
        getCalendar().set(Calendar.MINUTE, minute);

        String timeString = new SimpleDateFormat("HH:mm")
                .format(getCalendar().getTime());

        getButtonSetTime().setText(timeString);
    }
}
