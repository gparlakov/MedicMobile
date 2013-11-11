package com.parlakov.medic.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.drm.DrmStore;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.parlakov.medic.R;
import com.parlakov.medic.activities.AddEditPatientActivity;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.models.Patient;
import com.parlakov.medic.util.ImageHelper;
import com.parlakov.medic.util.ViewHelper;

/**
 * Created by georgi on 13-11-8.
 */
public class PatientDetailsFragment extends Fragment {

    public static final String PATIENT_TO_EDIT_EXTRA = "patient to edit";

    private long mId;
    private String mPhotoPath;
    private Patient mPatient;
    private View mPatientDetailsView;

    public PatientDetailsFragment(){
        this(0);
    }

    public PatientDetailsFragment(long id){
        mId = id;
        mPatient = new Patient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPatientDetailsView =
                inflater.inflate(R.layout.fragment_patient_details, container, false);

        setHasOptionsMenu(true);

        return mPatientDetailsView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mId != 0){
            getPatientData(mPatientDetailsView);
            showPatientInfo(mPatientDetailsView);
        }
    }

    private void getPatientData(View view) {
        LocalData data = new LocalData(view.getContext());
        mPatient = data.getPatients().getById(mId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.patient_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean handled = true;

        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_editPatientData:
                handleEditPatient();
                break;
            case R.id.action_deletePatient:
                handleDeletePatient();
                break;
            case R.id.action_callPatient:
                handleCallPatient();
                break;
            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }

        return handled;
    }

    private void handleEditPatient() {
        if(mId == 0){
            showToastNotFound();
        }

        Intent editPatientIntent = new Intent(getActivity().getApplicationContext(),
                AddEditPatientActivity.class);
        editPatientIntent.putExtra(PATIENT_TO_EDIT_EXTRA, mPatient);
        startActivity(editPatientIntent);

    }

    private void handleDeletePatient() {
        if(mId == 0){
            showToastNotFound();
        }
        try{
            LocalData data = new LocalData(getView().getContext());
            data.getPatients().delete(mPatient);
            ImageHelper.deletePhoto(mPhotoPath);
            getActivity().finish();
        }
        catch(SQLiteException ex){
            Toast.makeText(getView().getContext(),
                    getString(R.string.exception_patientNotDeleted), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void showPatientInfo(View view) {
        String name = mPatient.getLastName();
        String firstName = mPatient.getFirstName();
        if(firstName != null && !firstName.isEmpty()){
            name = firstName + " " + name;
        }
        ViewHelper.setTextToTextView(R.id.textViewPatientName, view, name);
        ViewHelper.setTextToTextView(R.id.textViewPatientPhone, view,
                mPatient.getPhone());
        ViewHelper.setTextToTextView(R.id.textViewPatientAge, view,
                String.valueOf(mPatient.getAge()));

        mPhotoPath = mPatient.getPhotoPath();
        if(mPhotoPath!= null && !mPhotoPath.isEmpty()){
            ImageView patientPhotoView = (ImageView) view.findViewById(R.id.imageView_patientDetails_Photo);
            /*patientPhotoView.setImageDrawable(Drawable.createFromPath(mPhotoPath));*/
            ImageHelper.loadImageFromFileAsync(mPhotoPath, patientPhotoView);
        }
    }

    private void handleCallPatient() {
        String patientPhoneNumber = mPatient.getPhone();
        if(patientPhoneNumber == null || patientPhoneNumber.isEmpty()){
            Toast.makeText(getActivity(),
                    getString(R.string.toast_noPatientNumber),
                    Toast.LENGTH_LONG)
            .show();
            return;
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + patientPhoneNumber));

        startActivity(callIntent);
    }

    private void showToastNotFound() {
        Toast.makeText(getView().getContext(),
                getString(R.string.error_patientIdCanNotBeFound),
                Toast.LENGTH_LONG)
                .show();
    }
}
