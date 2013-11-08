package com.parlakov.medic.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.parlakov.medic.MainActivity;
import com.parlakov.medic.R;

/**
 * Created by georgi on 13-11-8.
 */
public class ChooseSaveDataLocationActivity extends Activity{

    public RadioButton getUseSdCardChoiseRadioButton() {
        if(mUseSdCardChoiseRadioButton == null){
            mUseSdCardChoiseRadioButton = (RadioButton) findViewById(R.id.radioButton_use_sd_card);
        }

        return mUseSdCardChoiseRadioButton;
    }

    RadioButton mUseSdCardChoiseRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_save_data_location);

        initUi();
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
            getUseSdCardChoiseRadioButton().setEnabled(false);
        }
    }

    private void doSaveLocationChosen() {
        RadioButton deviceStorageChoise = (RadioButton) findViewById(R.id.radioButton_useDeviceStorage);
        Boolean useDeviceStorage = deviceStorageChoise.isChecked();

        Boolean useSDCardStorage = getUseSdCardChoiseRadioButton().isChecked();

        Intent chosenSaveLocationIntent = new Intent();
        String saveLocationExtra = MainActivity.APP_SAVE_DATA_LOCATION_EXTRA;
        if(useDeviceStorage){
            chosenSaveLocationIntent.putExtra(saveLocationExtra, MainActivity.SAVE_LOCATION_DEVICE_MEMORY);
        }
        else if(useSDCardStorage){
            chosenSaveLocationIntent.putExtra(saveLocationExtra, MainActivity.SAVE_LOCATION_SD_CARD);
        }
        else{
            chosenSaveLocationIntent.putExtra(saveLocationExtra, MainActivity.NOT_CHOSEN_LOCATION);
        }

        setResult(RESULT_OK, chosenSaveLocationIntent);
        finish();
    }
}
