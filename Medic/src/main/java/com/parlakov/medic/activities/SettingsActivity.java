package com.parlakov.medic.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.parlakov.medic.Global;
import com.parlakov.medic.R;
import com.parlakov.medic.fragments.LoginFragment;
import com.parlakov.medic.fragments.RegisterFragment;

/**
 * Created by georgi on 13-11-8.
 */
public class SettingsActivity extends FragmentActivity{
    RadioButton mUseSdCardChoiceRadioButton;
    RadioButton mUseDeviceMemoryChoiceButton;
    private SharedPreferences mPrefs;

    private RadioButton getUseSdCardChoiceRadioButton() {
        if(mUseSdCardChoiceRadioButton == null){
            mUseSdCardChoiceRadioButton = (RadioButton)
                    findViewById(R.id.radioButton_use_sd_card);
        }
        return mUseSdCardChoiceRadioButton;
    }

    private RadioButton getUseDeviceMemoryChoiceRadioButton(){
        if(mUseDeviceMemoryChoiceButton == null){
            mUseDeviceMemoryChoiceButton = (RadioButton)
                    findViewById(R.id.radioButton_useDeviceStorage);
        }
        return mUseDeviceMemoryChoiceButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPrefs = getSharedPreferences(Global.PROPERTYS_NAME, MODE_PRIVATE);

        initUi();

        setChosenPreferenceIfAny();
    }

    private void setChosenPreferenceIfAny() {
        int saveLocation = mPrefs.getInt(Global.PROPERTY_SAVE_LOCATION,
                Global.NOT_CHOSEN_LOCATION);

        if (Global.SAVE_LOCATION_DEVICE_MEMORY == saveLocation) {
            getUseDeviceMemoryChoiceRadioButton().setChecked(true);
        }
        if(Global.SAVE_LOCATION_SD_CARD == saveLocation){
            getUseSdCardChoiceRadioButton().setChecked(true);
        }
    }

    private void initUi() {
        Button done = (Button) findViewById(R.id.button_saveLocationChosen);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveLocationChosen();
            }
        });

        String state = Environment.getExternalStorageState();

        if(!state.equals(Environment.MEDIA_MOUNTED)){
            getUseSdCardChoiceRadioButton().setEnabled(false);
        }

        Button login = (Button) findViewById(R.id.button_settings_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        Button register = (Button) findViewById(R.id.button_settings_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });
    }

    private void doSaveLocationChosen() {
        Boolean useDeviceStorage = getUseDeviceMemoryChoiceRadioButton()
                .isChecked();

        Boolean useSDCardStorage = getUseSdCardChoiceRadioButton()
                .isChecked();


        // if the action is started for result will return OK
        // only if a location is not chosen will return canceled
        setResult(RESULT_OK);

        SharedPreferences.Editor editor = mPrefs.edit();
        if(useDeviceStorage) {
            editor.putInt(Global.PROPERTY_SAVE_LOCATION,
                    Global.SAVE_LOCATION_DEVICE_MEMORY);
        }
        else if(useSDCardStorage){
            editor.putInt(Global.PROPERTY_SAVE_LOCATION,
                    Global.SAVE_LOCATION_SD_CARD);
        }
        else{
            // not chosen location
            setResult(RESULT_CANCELED);
        }
        editor.commit();

        finish();
    }

    private void openLogin(){
        FragmentManager fm = getSupportFragmentManager();

        fm.popBackStack();
        fm.beginTransaction()
                .replace(R.id.container_settings, new LoginFragment())
                .addToBackStack("loginFragment")
                .commit();
    }


    private void openRegister(){
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        fm.beginTransaction()
                .replace(R.id.container_settings, new RegisterFragment())
                .addToBackStack("registerFragment")
                .commit();
    }

}
