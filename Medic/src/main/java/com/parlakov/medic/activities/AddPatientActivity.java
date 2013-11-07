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
import android.widget.ImageView;
import android.widget.Toast;

import com.parlakov.medic.R;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.models.Patient;
import com.parlakov.medic.util.Util;

import java.io.File;
import java.io.IOException;

/**
 * Created by georgi on 13-11-6.
 */
public class AddPatientActivity extends Activity {

    public final static String PICTURE_PATH_NAME = "patientImagePath";
    public final static int REQUEST_CODE_TAKE_PICTURE = 1010;

    private String patientImagePath;

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

    private void doTakePicture() {
        try {
            Resources r = getResources();

            Uri photoFileUri = Util.getTimeStampPhotoUri(r);
            patientImagePath = photoFileUri.getPath();
            Intent mediaIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mediaIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri);

            startActivityForResult(mediaIntent, REQUEST_CODE_TAKE_PICTURE);
        } catch (IOException ex) {
            Toast.makeText(this, "Could not create picture directory medic/pictures/newPic", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_TAKE_PICTURE:
                handleAddPicture(resultCode);
                break;
            default:
                break;
        }
    }

    private void handleAddPicture(int resultCode) {
        if (resultCode == RESULT_OK) {
            ImageView view = (ImageView) findViewById(R.id.newPatientImage);
            view.setImageDrawable(Drawable.createFromPath(patientImagePath));
        }
        else{
            File newImageFile = new File(patientImagePath);
            newImageFile.delete();
            patientImagePath = null;
        }
    }

    private void doAddPatient() {
        View rootView = findViewById(R.id.root_add_patient_activity);
        String firstName = Util.getTextFromEditView(R.id.addPatientFristNameEditView, rootView);
        String lastName = Util.getTextFromEditView(R.id.addPatientLastNameEditView, rootView);
        String ageAsString = Util.getTextFromEditView(R.id.addPatientAgeEditText, rootView);
        int age = 0;
        if (ageAsString != null && !ageAsString.isEmpty()) {
            age = Integer.parseInt(ageAsString);
        }
        String phone = Util.getTextFromEditView(R.id.addPatientPhoneEditText, rootView);

        Patient newPatient = new Patient(firstName, lastName, age, phone);
        if (this.patientImagePath != null) {
            newPatient.setImagePath(patientImagePath);
        }

        LocalData data = new LocalData(this.getBaseContext());
        data.getPatients().add(newPatient);

        finish();
    }

    //<editor-fold desc="State Save and restore">
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(PICTURE_PATH_NAME, patientImagePath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        patientImagePath = savedInstanceState.getString(PICTURE_PATH_NAME);
    }
    //</editor-fold>
}
