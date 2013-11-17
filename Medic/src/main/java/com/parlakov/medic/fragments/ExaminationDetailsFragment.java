package com.parlakov.medic.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parlakov.medic.R;
import com.parlakov.medic.async.CancelAppointmentWorker;
import com.parlakov.medic.dto.CancelAppointmentDTO;
import com.parlakov.medic.exceptions.MedicException;
import com.parlakov.medic.interfaces.ChildFragmentListener;
import com.parlakov.medic.interfaces.OnCancelResultListener;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.LocalExaminations;
import com.parlakov.medic.models.Examination;
import com.parlakov.medic.util.ImageHelper;
import com.parlakov.medic.util.TextHelper;
import com.parlakov.medic.viewModels.ExaminationDetails;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by georgi on 13-11-15.
 */
public class ExaminationDetailsFragment extends Fragment
        implements OnCancelResultListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener ,
        View.OnTouchListener{

    private static final String IDS_LIST = "ids list";
    private static final String CURRENT_POSITION = "current id position";
    private static final String EXAMINATION_DETAILS = "examination details";

    private long mExaminationId;
    private long[] mIdsList;
    private ExaminationDetails mExaminationDetails;
    private LocalData mData;
    private long oldTime;
    private float mStartOfSwipeY;

    private int mCurrentIdPosition;

    public LocalData getData() {
        if (mData == null) {
            mData = new LocalData(getActivity());
        }
        return mData;
    }

    public void setData(LocalData data) {
        this.mData = data;
    }

    public ExaminationDetails getExamination() {
        if(mExaminationDetails == null){
            mExaminationDetails = new ExaminationDetails();
        }
        return mExaminationDetails;
    }

    public ExaminationDetailsFragment(long examinationId, long[] idsList) {
        mExaminationId = examinationId;
        mIdsList = idsList;
        mCurrentIdPosition = -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if(savedInstanceState != null){
            mIdsList = savedInstanceState.getLongArray(IDS_LIST);
            mCurrentIdPosition = savedInstanceState.getInt(CURRENT_POSITION);
            mExaminationId = mIdsList[mCurrentIdPosition];
            mExaminationDetails = (ExaminationDetails) savedInstanceState.getSerializable(EXAMINATION_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_examination_details, container, false);
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mExaminationDetails == null){
            initialize();
        }
        else{
            updateExaminationTextFieldsAndPhoto();
            updateExaminationDate();
        }
    }

    private void initialize() {
        new AsyncTask<Void, Void, ExaminationDetails>() {
            @Override
            protected ExaminationDetails doInBackground(Void... params) {
                ExaminationDetails details = null;
                try{
                    details = ((LocalExaminations) getData().getExaminations())
                            .getDetailsById(mExaminationId);
                    return details;
                }
                catch (SQLiteException ex){
                    ex.printStackTrace();
                    return details;
                }
            }

            @Override
            protected void onPostExecute(ExaminationDetails examinationDetails) {
                if(examinationDetails == null){
                    Activity parentActivity = getActivity();

                    ChildFragmentListener listener =
                            (ChildFragmentListener) parentActivity;

                    String message = parentActivity.getString(
                            R.string.toast_exception_examinationNotFound);

                    Toast.makeText(parentActivity, message, Toast.LENGTH_LONG )
                            .show();

                    listener.onChildFragmentClose();
                    return;
                }
                mExaminationDetails = examinationDetails;
                updateExaminationDate();
                updateExaminationTextFieldsAndPhoto();
            }
        }.execute();
    }

    private void updateExaminationTextFieldsAndPhoto(){
        ExaminationDetails ex = getExamination();
        TextHelper.setTextToTextView(R.id.textView_examinationDetails_complaints,
                getActivity(), ex.getComplaints());

        TextHelper.setTextToTextView(R.id.textView_examinationDetails_conclusions,
                getActivity(), ex.getConclusion());

        TextHelper.setTextToTextView(R.id.textView_examinationDetails_treatment,
                getActivity(), ex.getTreatment());

        TextHelper.setTextToTextView(R.id.textView_examinationDetails_notes,
                getActivity(), ex.getNotes());

        TextHelper.setTextToTextView(R.id.textView_examinationDetails_patientName,
                getActivity(), ex.getPatientName());

        ImageView photo = (ImageView) getActivity()
                .findViewById(R.id.image_examinationDetails_patientPhoto);

        ImageHelper.loadImageFromFileAsync(ex.getPatientPhotoPath(), photo);
    }

    private void updateExaminationDate(){
        Examination ex = getExamination();
        Date date = new Date(ex.getDateInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm ccc - dd/MM/yy");
        String formattedDate = formatter.format(date);

        TextHelper.setTextToTextView(R.id.textView_examinationDetails_date,
                getActivity(), formattedDate);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.at_examinations_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean handled = true;

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_cancelAppointment:
                cancelAppointment(mExaminationId);
                break;
            case R.id.action_setResults:
                //TODO
                break;
            case R.id.action_changeDate:
                changeDate();
                break;
            case R.id.action_changeTime:
                changeTime();
                break;
            case R.id.action_returnToMain:
                ChildFragmentListener listener =
                        (ChildFragmentListener) getActivity();
                listener.onChildFragmentClose();
                break;
            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }

        return handled;
    }

    private void cancelAppointment(long examinationId) {
        CancelAppointmentDTO cancelAppointmentDTO =
                new CancelAppointmentDTO(getData(), examinationId, this);

        new CancelAppointmentWorker().execute(cancelAppointmentDTO);
    }

    @Override
    public void onCancelResult(Boolean isCancelSuccessful) {
        String message = getString(R.string.toast_examinationCanceled);

        if(isCancelSuccessful){
            ChildFragmentListener listener = (ChildFragmentListener) getActivity();
            listener.onChildFragmentClose();
        } else {
            message = getString(R.string.toast_examinationNotCanceled);
        }

        Toast.makeText(getActivity(), message,Toast.LENGTH_LONG).show();
    }

    //<editor-fold desc="Date/Time change">
    private void changeDate() {
        Calendar cal = getCalendarFromExaminationDate();

        DatePickerFragment datePicker = new DatePickerFragment(this, cal);
        datePicker.show(getFragmentManager(), "dateChangePicker");
    }

    private void changeTime() {
        Calendar cal = getCalendarFromExaminationDate();

        TimePickerFragment timePicker = new TimePickerFragment(this, cal);
        timePicker.show(getFragmentManager(), "dateChangePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = getCalendarFromExaminationDate();
        // save the "old" time of examination in case
        oldTime = mExaminationDetails.getDateInMillis();

        cal.set(year, monthOfYear, dayOfMonth);

        setNewDateTime(cal);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar cal = getCalendarFromExaminationDate();
        oldTime = mExaminationDetails.getDateInMillis();

        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);

        setNewDateTime(cal);
    }

    private void setNewDateTime(Calendar cal) {
        mExaminationDetails.setDateInMillis(cal.getTimeInMillis());

        new AsyncTask<Object, Void, Boolean>() {
            private static final String SQLITE_EXCEPTION = "SQLite exception";
            private static final String MEDIC_EXCEPTION = "Medic exception";
            private String mExceptionType;

            @Override
            protected Boolean doInBackground(Object... params) {
                try{
                    getData().getExaminations()
                            .update(mExaminationDetails);

                    return true;
                }
                catch (SQLiteException ex){
                    ex.printStackTrace();
                    mExceptionType = SQLITE_EXCEPTION;
                    return false;
                }
                catch (MedicException e) {
                    e.printStackTrace();
                    mExceptionType = MEDIC_EXCEPTION;
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean examinationUpdated) {
                if(!examinationUpdated){
                    String message = getString(R.string.exception_unableToOpenDb);

                    if(mExceptionType == MEDIC_EXCEPTION){
                        message = getString(R.string.exception_overlapping_examination);
                    }

                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG)
                            .show();

                    mExaminationDetails.setDateInMillis(oldTime);
                    return;
                }

                updateExaminationDate();
            }
        }.execute();
    }

    private Calendar getCalendarFromExaminationDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mExaminationDetails.getDateInMillis());
        return cal;
    }
    //</editor-fold>

    //<editor-fold desc="Touch handle - get prev/next examination">
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Boolean handled = true;

        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                handleSwipe(event);
                break;
            default:
                handled = false;
                break;
        }

        return handled;
    }

    private void handleSwipe(MotionEvent event) {
        float currentY = event.getY();
        if(mStartOfSwipeY == 0){
            mStartOfSwipeY = currentY;
        }
        else{
            float difference = currentY-mStartOfSwipeY;
            if(difference > 0){
                getPreviousExamination();
            }
            else if (difference < 0){
                getNextExamination();
            }
            mStartOfSwipeY = 0;
        }
    }

    private void getPreviousExamination() {
        int position = getExaminationIdPosition();
        if (position > 0){
            mExaminationId = mIdsList[position - 1];
            setExaminationPosition(position - 1);
            initialize();
        }
        else{
            Toast.makeText(getActivity(), getString(R.string.no_more_examinations), Toast.LENGTH_SHORT).show();
        }
    }

    private void getNextExamination() {
        int position = getExaminationIdPosition();
        if (position < mIdsList.length - 1){
            mExaminationId = mIdsList[position + 1];
            setExaminationPosition(position + 1);
            initialize();
        }
        else{
            Toast.makeText(getActivity(), getString(R.string.no_more_examinations), Toast.LENGTH_SHORT).show();
        }

    }

    private int getExaminationIdPosition() {
        if (mCurrentIdPosition == -1){
            for (int i = 0; i < mIdsList.length; i++) {
                if(mIdsList[i] == mExaminationId){
                    mCurrentIdPosition = i;
                    break;
                }
            }
        }

        return mCurrentIdPosition;
    }

    private void setExaminationPosition(int examinationIdPosition) {
        mCurrentIdPosition = examinationIdPosition;
    }
    //</editor-fold>

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_POSITION, mCurrentIdPosition);
        outState.putLongArray(IDS_LIST, mIdsList);
        outState.putSerializable(EXAMINATION_DETAILS, mExaminationDetails);
    }

    @Override
    public void onStop() {
        super.onStop();

        closeDbConnection();

    }

    private void closeDbConnection() {
        if(mData != null){
            mData.closeDb();
            mData = null;
        }
    }
}
