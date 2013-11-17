package com.parlakov.medic;

import android.app.Application;
import android.content.Context;

/**
 * Created by georgi on 13-11-15.
 */
public class Global extends Application{
    public static final int NOT_CHOSEN_LOCATION = 0;
    public static final int SAVE_LOCATION_SD_CARD = 1;
    public static final int SAVE_LOCATION_DEVICE_MEMORY = 2;

    public static final String PROPERTYS_NAME = "com.parlakov.medic.APP_PREFERENCES";
    public static final String PROPERTY_SAVE_LOCATION = "save location property";

    public static int EXAMINATION_LENGTH_IN_MINUTES = 30;
}
