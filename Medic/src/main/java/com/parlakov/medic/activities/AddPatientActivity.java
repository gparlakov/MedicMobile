package com.parlakov.medic.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parlakov.medic.R;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.models.Patient;
import com.parlakov.medic.util.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by georgi on 13-11-6.
 */
public class AddPatientActivity extends Activity {

    public final static String PICTURE_PATH_NAME = "patientImagePath";
    public final static int REQUEST_CODE_TAKE_PICTURE = 1010;
    private static final String FIRST_NAME = "first name";
    private static final String LAST_NAME = "last name";
    private static final String AGE = "age";
    private static final String PHONE = "phone";

    private String mPatientImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        initializeUi();
    }

    private void initializeUi() {
        Button buttonAdd = (Button) findViewById(R.id.buttonAddPatient);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddPatient();
            }
        });

        Button buttonTakePicture = (Button) findViewById(R.id.buttonTakeNewPatientPicture);
        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTakePicture();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_TAKE_PICTURE:
                handleTakePicturePicture(resultCode);
                break;
            default:
                break;
        }
    }

    private void handleTakePicturePicture(int resultCode) {
        if (resultCode == RESULT_OK) {
            showTakenImageOnView();
        }
        else{
            File newImageFile = new File(mPatientImagePath);
            newImageFile.delete();
            mPatientImagePath = null;
        }
    }

    private void showTakenImageOnView() {
        ImageView view = (ImageView) findViewById(R.id.newPatientImage);
        view.setImageDrawable(Drawable.createFromPath(mPatientImagePath));
    }

    private void doAddPatient() {
        View rootView = findViewById(R.id.root_add_patient_activity);
        String firstName = Util.getTextFromEditView(R.id.addPatientFirstNameEditView, rootView);
        String lastName = Util.getTextFromEditView(R.id.addPatientLastNameEditView, rootView);
        String ageAsString = Util.getTextFromEditView(R.id.addPatientAgeEditText, rootView);
        int age = 0;
        if (ageAsString != null && !ageAsString.isEmpty()) {
            age = Integer.parseInt(ageAsString);
        }
        String phone = Util.getTextFromEditView(R.id.addPatientPhoneEditText, rootView);

        Patient newPatient = new Patient(firstName, lastName, age, phone);
        if (this.mPatientImagePath != null) {
            newPatient.setImagePath(mPatientImagePath);
        }

        LocalData data = new LocalData(this.getBaseContext());
        data.getPatients().add(newPatient);

        finish();
    }

    private void doTakePicture() {
        try {
            Uri photoFileUri = Util.getTimeStampPhotoUri(getApplicationContext());

            mPatientImagePath = photoFileUri.getPath();
            Intent mediaIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mediaIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri);

            startActivityForResult(mediaIntent, REQUEST_CODE_TAKE_PICTURE);
        } catch (IOException ex) {
            Toast.makeText(this, "Could not create picture directory medic/pictures/newPic", Toast.LENGTH_LONG).show();
        }
    }

    //<editor-fold desc="State save and restore">
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String firstName = Util
                .getTextFromEditView(R.id.addPatientFirstNameEditView, this);
        String lastName = Util
                .getTextFromEditView(R.id.addPatientLastNameEditView, this);
        String age = Util
                .getTextFromEditView(R.id.addPatientAgeEditText, this);
        String phone = Util
                .getTextFromEditView(R.id.addPatientPhoneEditText, this);

        outState.putString(PICTURE_PATH_NAME, mPatientImagePath);
        outState.putString(FIRST_NAME, firstName);
        outState.putString(LAST_NAME, lastName);
        outState.putString(AGE, age);
        outState.putString(PHONE, phone);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // takes the image path and shows it on screen
        mPatientImagePath = savedInstanceState.getString(PICTURE_PATH_NAME);
        showTakenImageOnView();

        String firstName = savedInstanceState.getString(FIRST_NAME);
        String lastName = savedInstanceState.getString(LAST_NAME);
        String age = savedInstanceState.getString(AGE);
        String phone = savedInstanceState.getString(PHONE);

        EditText firstNameEditText = (EditText) findViewById(R.id.addPatientFirstNameEditView);
        firstNameEditText.setText(firstName);

        EditText lastNameEditText = (EditText) findViewById(R.id.addPatientLastNameEditView);
        lastNameEditText.setText(lastName);

        EditText ageEditText = (EditText) findViewById(R.id.addPatientAgeEditText);
        ageEditText.setText(age);

        EditText phoneEditText = (EditText) findViewById(R.id.addPatientPhoneEditText);
        phoneEditText.setText(phone);
    }
    //</editor-fold>
}
