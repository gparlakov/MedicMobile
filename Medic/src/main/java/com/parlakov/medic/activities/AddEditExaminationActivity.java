package com.parlakov.medic.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
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
import com.parlakov.medic.async.PatientsAdapterWorker;
import com.parlakov.medic.fragments.DatePickerFragment;
import com.parlakov.medic.fragments.ExaminationsListFragment;
import com.parlakov.medic.fragments.PatientDetailsFragment;
import com.parlakov.medic.fragments.TimePickerFragment;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.models.Examination;
import com.parlakov.medic.util.TextHelper;
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
    private Spinner mPatientsSpinner;

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

    public Spinner getPatientsSpinner(){
        if(mPatientsSpinner == null){
            mPatientsSpinner = (Spinner) findViewById(R.id.spinner_patients);
        }

        return mPatientsSpinner;
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
        // TODO - check for possible concurrency with other method that uses the DB
        // but how?
        if(examinationId != 0){
            new AsyncTask<Long, Void, Examination>() {
                @Override
                protected Examination doInBackground(Long... params) {
                    long examinationId = params[0];

                    Examination examination;
                    try{
                        examination = getData().getExaminations()
                                .getById(examinationId);

                        return examination;
                    }
                    catch (SQLiteException ex){
                        ex.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Examination examination) {
                    if(examination == null){
                        showExaminationNotFoundAndFinish();
                        return;
                    }
                    mExamination = examination;
                    mPatientId = mExamination.getPatientId();
                    setExaminationData();
                }
            }.execute(examinationId);
        }
    }

    private void showExaminationNotFoundAndFinish() {
        Toast.makeText(this,
                getString(R.string.toast_exception_examination_not_found_id_db),
                Toast.LENGTH_LONG).show();
        finish();
    }

    private void initialize() {
        try{
            // will call setExaminationData when finished to put data on Ui
            PatientsAdapterWorker getter =
                    new PatientsAdapterWorker();

            getter.execute(this);
        }
        catch (SQLiteException ex) {
            Toast.makeText(this,
                    getString(R.string.toast_exception_dbNoFoundMaybeSDMissing),
                    Toast.LENGTH_LONG)
                    .show();
        }
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

        //TODO - check for overlapping of dates/appointments

        //TODO - check if a patient is present

        return isValid;
    }

    private Examination getExaminationFromUi() {
        Examination examination = getExamination();

        Spinner patientSpinner = (Spinner) findViewById(R.id.spinner_patients);
        long selectedPatientId = patientSpinner.getSelectedItemId();

        long examDate = getCalendar().getTimeInMillis();

        String complaints = TextHelper
                .getTextFromEditView(R.id.editText_examination_complaints, this);
        String conclusion = TextHelper
                .getTextFromEditView(R.id.editText_examination_conclusion, this);
        String notes = TextHelper
                .getTextFromEditView(R.id.editText_examination_notes, this);
        String treatment = TextHelper
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
            TextHelper.setTextToEditView(R.id.editText_examination_complaints, this,
                    mExamination.getComplaints());

            TextHelper.setTextToEditView(R.id.editText_examination_treatment, this,
                    mExamination.getTreatment());

            TextHelper.setTextToEditView(R.id.editText_examination_conclusion, this,
                    mExamination.getConclusion());

            TextHelper.setTextToEditView(R.id.editText_examination_notes, this,
                    mExamination.getNotes());

            getCalendar().setTimeInMillis(mExamination.getDateInMillis());
            updateUiDate();
            updateUiTime();
        }
    }

    //</editor-fold>
}
