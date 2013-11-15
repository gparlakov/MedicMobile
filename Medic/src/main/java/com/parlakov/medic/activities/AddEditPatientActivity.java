package com.parlakov.medic.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parlakov.medic.R;
import com.parlakov.medic.fragments.PatientDetailsFragment;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.models.Patient;
import com.parlakov.medic.util.ImageHelper;
import com.parlakov.medic.util.TextGetHelper;

import java.io.File;
import java.io.IOException;

/**
 * Created by georgi on 13-11-6.
 */
public class AddEditPatientActivity extends Activity {

    public final static int REQUEST_CODE_TAKE_PICTURE = 1010;
    private static final String PATIENT_SAVE_STATE = "patient save state";

    private String mPhotoPath;
    private Patient mPatient;
    private LocalData mLocalData;

    public LocalData getLocalData() {
        if(mLocalData == null){
            mLocalData = new LocalData(getApplicationContext());
        }
        return mLocalData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        initializeUi();

        if(savedInstanceState == null){
            Patient patientToEdit = getPatientFromIntentExtra();
            if(patientToEdit != null){
                mPatient = patientToEdit;

                setPatientInfoToEditFields();
            }
            else{
                mPatient = new Patient();
            }
        }
    }

    //<editor-fold desc="UI and buttons">
    private void initializeUi() {
        Button buttonAdd = (Button) findViewById(R.id.buttonAddPatient);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSavePatient();
            }
        });

        Button buttonPhotograph = (Button) findViewById(R.id.buttonPatientPhoto);
        buttonPhotograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPhoto();
            }
        });
    }

    private void doSavePatient() {
        putEditDataInObject();

        try{
            LocalData data = getLocalData();

            if (!isPatientValid()){
                return;
            }

            if(mPatient.getId() != 0){
                data.getPatients().update(mPatient);
            }
            else{
                data.getPatients().add(mPatient);
            }
        }
        catch (SQLiteException ex){
            Toast.makeText(getApplicationContext(),
                    getString(R.string.exception_unableToOpenDb),
                    Toast.LENGTH_LONG);
        }
        catch (Exception ex){
            Toast.makeText(getApplicationContext(),
                    getString(R.string.exception_unknownException),
                    Toast.LENGTH_LONG);
        }

        finish();
    }

    private Boolean isPatientValid() {
        Boolean valid = true;

        String lastName = mPatient.getLastName();
        if( lastName == null || lastName.isEmpty() ){
            Toast.makeText(this, "Last name can not be empty", Toast.LENGTH_LONG)
                    .show();
            valid = false;
        }

        return valid;
    }

    private void doPhoto() {
        try {
            deleteCurrentPhoto();

            Uri photoFileUri = ImageHelper.getTimeStampPhotoUri(getApplicationContext());

            mPhotoPath = photoFileUri.getPath();
            Intent mediaIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mediaIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri);

            startActivityForResult(mediaIntent, REQUEST_CODE_TAKE_PICTURE);
        } catch (IOException ex) {
            Toast.makeText(this, "Could not create picture directory medic/pictures/newPic", Toast.LENGTH_LONG).show();
        }
    }
    //</editor-fold>

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_TAKE_PICTURE:
                handlePhotoResult(resultCode);
                break;
            default:
                break;
        }
    }

    //<editor-fold desc="photo handling">
    private void handlePhotoResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            showTakenPhotoOnView();
        }
        else{
            File newImageFile = new File(mPhotoPath);
            newImageFile.delete();
            mPhotoPath = null;
        }
    }

    private void showTakenPhotoOnView() {
        ImageView view = (ImageView) findViewById(R.id.newPatientImage);

        view.setImageDrawable(Drawable.createFromPath(mPhotoPath));
    }

    private void deleteCurrentPhoto() {
        LocalData data = getLocalData();
        ImageHelper.deletePhoto(mPhotoPath);
    }
    //</editor-fold>

    //<editor-fold desc="State save and restore">
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        putEditDataInObject();

        outState.putSerializable(PATIENT_SAVE_STATE, mPatient);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // takes the image path and shows it on screen
        if(savedInstanceState == null)
            return;

        mPatient = (Patient) savedInstanceState.getSerializable(PATIENT_SAVE_STATE);
        mPhotoPath = mPatient.getPhotoPath();
        showTakenPhotoOnView();

        setPatientInfoToEditFields();
    }
    //</editor-fold>

    private void putEditDataInObject() {
        String firstName = TextGetHelper
                .getTextFromEditView(R.id.addPatientFirstNameEditView, this);
        String lastName = TextGetHelper
                .getTextFromEditView(R.id.addPatientLastNameEditView, this);
        String ageString = TextGetHelper
                .getTextFromEditView(R.id.addPatientAgeEditText, this);
        int age = 0;
        if(ageString != null && !ageString.isEmpty()){
            age = Integer.parseInt(ageString);
        }
        String phone = TextGetHelper
                .getTextFromEditView(R.id.addPatientPhoneEditText, this);

        mPatient.setFirstName(firstName);
        mPatient.setLastName(lastName);
        mPatient.setAge(age);
        mPatient.setPhone(phone);
        mPatient.setImagePath(mPhotoPath);
    }

    private void setPatientInfoToEditFields() {
        String firstName = mPatient.getFirstName();
        String lastName = mPatient.getLastName();
        String age = String.valueOf(mPatient.getAge());
        String phone = mPatient.getPhone();

        EditText firstNameEditText = (EditText) findViewById(R.id.addPatientFirstNameEditView);
        firstNameEditText.setText(firstName);

        EditText lastNameEditText = (EditText) findViewById(R.id.addPatientLastNameEditView);
        lastNameEditText.setText(lastName);

        EditText ageEditText = (EditText) findViewById(R.id.addPatientAgeEditText);
        ageEditText.setText(age);

        EditText phoneEditText = (EditText) findViewById(R.id.addPatientPhoneEditText);
        phoneEditText.setText(phone);

        mPhotoPath = mPatient.getPhotoPath();
        showTakenPhotoOnView();
    }

    private Patient getPatientFromIntentExtra() {
        Intent intent = getIntent();
        Patient patient = (Patient) intent
                .getSerializableExtra(PatientDetailsFragment.PATIENT_TO_EDIT_EXTRA);

        return patient;
    }
}
