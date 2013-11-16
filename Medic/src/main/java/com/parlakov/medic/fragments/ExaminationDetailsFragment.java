package com.parlakov.medic.fragments;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parlakov.medic.R;
import com.parlakov.medic.async.CancelAppointmentWorker;
import com.parlakov.medic.dto.CancelAppointmentDTO;
import com.parlakov.medic.interfaces.ChildFragmentListener;
import com.parlakov.medic.interfaces.OnCancelResultListener;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.MedicDbContract;
import com.parlakov.medic.models.Examination;
import com.parlakov.medic.util.TextHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by georgi on 13-11-15.
 */
public class ExaminationDetailsFragment extends Fragment
        implements OnCancelResultListener {

    private final long mExaminationId;
    private LocalData mData;
    private Examination mExamination;

    public LocalData getData() {
        if (mData == null) {
            mData = new LocalData(getActivity());
        }
        return mData;
    }

    public void setData(LocalData data) {
        this.mData = data;
    }

    public ExaminationDetailsFragment(long examinationId) {
        mExaminationId = examinationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_examination_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        initialize();
    }

    private void initialize() {
        new AsyncTask<Void, Void, Examination>() {
            @Override
            protected Examination doInBackground(Void... params) {
                try{
                    return getData().getExaminations().getById(mExaminationId);
                } catch (SQLiteException ex){
                    ex.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Examination examination) {
                if(examination == null){
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
                mExamination = examination;
                updateExaminationDate();
                updateExaminationTextFields();
            }
        }.execute();
    }

    private void updateExaminationTextFields(){
        Examination ex = getExamination();
        TextHelper.setTextToTextView(R.id.textView_examinationDetails_complaints,
                getActivity(), ex.getComplaints());

        TextHelper.setTextToTextView(R.id.textView_examinationDetails_conclusions,
                getActivity(), ex.getConclusion());

        TextHelper.setTextToTextView(R.id.textView_examinationDetails_treatment,
                getActivity(), ex.getTreatment());

        TextHelper.setTextToTextView(R.id.textView_examinationDetails_notes,
                getActivity(), ex.getNotes());
    }

    private void updateExaminationDate(){
        Examination ex = getExamination();
        Date date = new Date(ex.getDateInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm ddd - dd/mm/yy");
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
                //TODO
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

    public Examination getExamination() {
        if(mExamination == null){
            mExamination = new Examination();
        }
        return mExamination;
    }
}
