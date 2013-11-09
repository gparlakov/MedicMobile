package com.parlakov.medic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;

import com.parlakov.medic.MainActivity;
import com.parlakov.medic.R;
import com.parlakov.medic.localdata.MedicDbHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageHelper {
    public static Uri getTimeStampPhotoUri(Context context) throws IOException {
        Uri photoFileUri;

        String appPicturesPath =
                getPicturesFolderAccordingToUserSaveDataSelection(context);

        String newPhotoName = "patientImage" + new SimpleDateFormat("DDMMyyyyHHmmss").format(new Date()) + ".jpg";

        File newPhotoFile = getOrCreatePicturesFolderAndFile(appPicturesPath, newPhotoName);

        photoFileUri = Uri.fromFile(newPhotoFile);
        return photoFileUri;
    }

    public static File getOrCreatePicturesFolderAndFile(String appPicturesPath, String newPhotoName) throws IOException {
        File photosDir = new File(appPicturesPath);
        if (!photosDir.exists()) {
            if (!photosDir.mkdirs()) {
                throw new IOException("Could not create directory");
            }
        }

        return new File(photosDir, newPhotoName);
    }

    public static String getPicturesFolderAccordingToUserSaveDataSelection(Context context) {
        SharedPreferences prefs = context
                .getSharedPreferences(MainActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
        int saveDataLocation = prefs
                .getInt(MainActivity.APP_SAVE_DATA_LOCATION, MainActivity.NOT_CHOSEN_LOCATION);
        Resources resources = context.getResources();

        String appPicturesPath;
        String separator = File.separator;
        if (saveDataLocation == MainActivity.SAVE_LOCATION_SD_CARD) {
            String mainPath = Environment.getExternalStorageDirectory().getPath();
            String appFolderName = resources.getString(R.string.app_sd_card_folder);
            appPicturesPath = mainPath + separator + appFolderName;
        } else {
            appPicturesPath = context.getDatabasePath(MedicDbHelper.DATABASE_NAME).getPath();
        }

        String appPicturesFolderName = resources.getString(R.string.app_pictures_folder);
        appPicturesPath += separator + appPicturesFolderName;
        return appPicturesPath;
    }
}