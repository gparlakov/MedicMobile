package com.parlakov.medic.activities;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parlakov.medic.R;
import com.parlakov.medic.fragments.DatePickerFragment;
import com.parlakov.medic.fragments.ExaminationsListFragment;
import com.parlakov.medic.fragments.PatientDetailsFragment;
import com.parlakov.medic.fragments.TimePickerFragment;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.models.Examination;
import com.parlakov.medic.util.PatientsSimpleCursorAdapterSetterAsyncSetter;
import com.parlakov.medic.util.ViewHelper;
import com.parlakov.uow.IUowMedic;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by georgi on 13-11-11.
 */
public class AddEditExaminationActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String EXAMINATION_SAVE = "selected date";
    //<editor-fold desc="members and getters">
    public IUowMedic mData;
    public long mPatientId;

    private FragmentManager mFragmentManager;
    private Button mSetDateButton;
    private Button mSetTimeButton;
    private Calendar mCalendar;
    private Examination mExamination;

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

    public IUowMedic getData() {
        if(mData == null){
            mData = new LocalData(getApplicationContext());
        }
        return mData;
    }
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_examination);

        // if a patient was selected to add examination for sets his id
        Intent intent = getIntent();

        mPatientId = intent
                .getLongExtra(PatientDetailsFragment.PATIENT_ID_EXTRA, 0);

        if(savedInstanceState == null){
               getExaminationFromDbAsync(intent);
        }
        else{
            // take examination data from savedInstance
            mExamination = (Examination)
                    savedInstanceState.getSerializable(EXAMINATION_SAVE);
        }
    }

    private void getExaminationFromDbAsync(Intent intent) {
        long examinationId = intent
                .getLongExtra(ExaminationsListFragment.EXAMINATION_TO_EDIT_ID_EXTRA, 0);

        // do this in main tread because patient id is needed to set the spinner position
        // if done async maybe the position won't be available at needed time
        if(examinationId != 0){
            mExamination = getData().getExaminations().getById(examinationId);
            mPatientId = mExamination.getPatientId();
        }
    }

    private void initialize() {
        PatientsSimpleCursorAdapterSetterAsyncSetter getter =
                new PatientsSimpleCursorAdapterSetterAsyncSetter();

        getter.execute(this);
    }

    private void initializeUi() {

        getButtonSetDate().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSetDate();
            }
        });

        getButtonSetTime().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSetTime();
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

    private void doSetDate() {
        DatePickerFragment datePickerFragment =
                new DatePickerFragment(this, getCalendar());

        datePickerFragment.show(getFragmentManagerLazy(), "datePickerDialog");
    }

    private void doSetTime() {
        TimePickerFragment timePickerFragment =
                new TimePickerFragment(this, getCalendar());

        timePickerFragment.show(getFragmentManagerLazy(), "datePickerDialog");
    }

    private void doSaveExamination() {
        Examination examination = getExaminationFromUi();
        if(!isValidExamination(examination)){
            return;
        }
        IUowMedic data = getData();

        data.getExaminations().add(examination);

        finish();
    }

    private boolean isValidExamination(Examination examination) {
        Boolean isValid = true;
        if(getCalendar().compareTo(Calendar.getInstance()) <= 0){
            Toast.makeText(this, getString(R.string.toast_dateMustBeLaterThanNow),
                    Toast.LENGTH_LONG).show();

            isValid = false;
        }

        //TODO - check for overlapping of dates

        return isValid;
    }

    private Examination getExaminationFromUi() {
        Examination examination = getExamination();

        Spinner patientSpinner = (Spinner) findViewById(R.id.spinner_patients);
        long selectedPatientId = patientSpinner.getSelectedItemId();

        long examDate = getCalendar().getTimeInMillis();

        String complaints = ViewHelper
                .getTextFromEditView(R.id.editText_examination_complaints, this);
        String conclusion = ViewHelper
                .getTextFromEditView(R.id.editText_examination_conclusion, this);
        String notes = ViewHelper
                .getTextFromEditView(R.id.editText_examination_notes, this);
        String treatment = ViewHelper
                .getTextFromEditView(R.id.editText_examination_treatment, this);

        examination.setDateInMillis(examDate);
        examination.setComplaints(complaints);
        examination.setConclusion(conclusion);
        examination.setTreatment(treatment);
        examination.setNotes(notes);
        examination.setPatientId(selectedPatientId);
        return examination;
    }

    //<editor-fold desc="Date and Time set listener implementation">
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        getCalendar().set(year, monthOfYear, dayOfMonth);

        updateUiDate();
    }

    private void updateUiDate() {
        String dateString = new SimpleDateFormat("EEE dd/MM/yyyy")
                .format(getCalendar().getTime());

        getButtonSetDate().setText(dateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        getCalendar().set(Calendar.HOUR_OF_DAY, hourOfDay);
        getCalendar().set(Calendar.MINUTE, minute);

        updateUiTime();
    }

    private void updateUiTime() {
        String timeString = new SimpleDateFormat("HH:mm")
                .format(getCalendar().getTime());

        getButtonSetTime().setText(timeString);
    }
    //</editor-fold>

    //<editor-fold desc="lifecycle management">
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
            mData.closeDb();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(EXAMINATION_SAVE, mExamination);
    }

    public void setExaminationData() {
        if(mExamination != null){
            ViewHelper.setTextToEditView(R.id.editText_examination_complaints, this,
                    mExamination.getComplaints());

            ViewHelper.setTextToEditView(R.id.editText_examination_treatment, this,
                    mExamination.getTreatment());

            ViewHelper.setTextToEditView(R.id.editText_examination_conclusion, this,
                    mExamination.getConclusion());

            ViewHelper.setTextToEditView(R.id.editText_examination_notes, this,
                    mExamination.getNotes());

            getCalendar().setTimeInMillis(mExamination.getDateInMillis());
            updateUiDate();
            updateUiTime();
        }
    }

    //</editor-fold>
}
