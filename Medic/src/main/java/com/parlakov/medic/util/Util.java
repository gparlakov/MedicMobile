package com.parlakov.medic.util;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;

import com.parlakov.medic.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by georgi on 13-11-6.
 */
public class Util {

    public static String getTextFromEditView(int id, View rootView){
        EditText view = (EditText) rootView.findViewById(id);
        String text = null;

        if (view != null){
            text = view.getText().toString();
        }

        return text;
    }

    public static Uri getTimeStampPhotoUri(Resources resources) throws IOException{
        Uri photoFileUri = null;

        String appSDCardDir = Environment.getExternalStorageDirectory().getPath();
        String appFolderName = resources.getString(R.string.app_sd_card_folder);
        String appPicturesFolderName = resources.getString(R.string.app_pictures_folder);
        String separator = File.separator;

        String appPicturesPath = appSDCardDir + separator + appFolderName + separator + appPicturesFolderName;

        String newPhotoName = "patientImage" + new SimpleDateFormat("DDMMyyyyHHmmss").format(new Date()) + ".jpg";
        File photosDir = new File(appPicturesPath);
        if(!photosDir.exists()){
            if(!photosDir.mkdirs()){
                throw new IOException("Could not create directory");
            }
        }

        File newPhotoFile = new File(photosDir, newPhotoName);

        photoFileUri = Uri.fromFile(newPhotoFile);
        return photoFileUri;
    }

}
