package com.parlakov.medic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.parlakov.medic.Global;
import com.parlakov.medic.R;
import com.parlakov.medic.async.AsyncDrawable;
import com.parlakov.medic.async.BitmapWorkerTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageHelper {

    // Creates Uri for the new photo file using a time stamp to
    // create unique file name.
    public static Uri getTimeStampPhotoUri(Context context) throws IOException {
        Uri photoFileUri;

        String appPicturesPath = getPicturesFolder(context);

        String newPhotoName = "patientImage" + new SimpleDateFormat("DDMMyyyyHHmmss").format(new Date()) + ".jpg";

        File newPhotoFile = getOrCreatePicturesFolderAndFile(appPicturesPath, newPhotoName);

        photoFileUri = Uri.fromFile(newPhotoFile);
        return photoFileUri;
    }

    // Creates the file where the photo will be stored
    // If the directories do not exist creates them too.
    public static File getOrCreatePicturesFolderAndFile(String appPicturesPath, String newPhotoName) throws IOException {
        File photosDir = new File(appPicturesPath);

        if (!photosDir.exists()) {
            Boolean created = photosDir.mkdirs();
            if (!created) {
                throw new IOException("Could not create directory");
            }
        }

        File photoFile = new File(photosDir, newPhotoName);
        return photoFile;
    }

    // According to user selection gets the pictures folder
    // in which to save the patients photos.
    public static String getPicturesFolder(Context context) {
        SharedPreferences prefs = context
                .getSharedPreferences(Global.PROPERTYS_NAME, Context.MODE_PRIVATE);

        int saveDataLocation = prefs
                .getInt(Global.PROPERTY_SAVE_LOCATION,
                        Global.NOT_CHOSEN_LOCATION);

        Resources resources = context.getResources();

        String mainPath;
        if (saveDataLocation == Global.SAVE_LOCATION_SD_CARD) {
            mainPath = Environment.getExternalStorageDirectory().getPath();
        }
        else { // Global.SAVE_LOCATION_DEVICE_MEMORY
            mainPath = Environment
                    .getExternalStorageDirectory()
                    .getPath();
        }

        String appFolderName = resources.getString(R.string.app_sd_card_folder);
        String appPicturesFolderName = resources.getString(R.string.app_pictures_folder);

        String appPicturesPath =
                mainPath + File.separator +
                appFolderName + File.separator +
                appPicturesFolderName;

        return appPicturesPath;
    }

    public static void deletePhoto(String photoPath) {
        if(photoPath != null && !photoPath.isEmpty()){
            File photo = new File(photoPath);

            if(photo != null){
                photo.delete();
            }
        }
    }

    // Gets the bitmap from file downsized to fit the desired width height
    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight){
        Bitmap result = null;

        if(filePath != null && !filePath.isEmpty()){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            options.inJustDecodeBounds = false;

            result = BitmapFactory.decodeFile(filePath, options);
        }

        return result;
    }

    // calculates the sample size from given required width/ height
    // so 2800x1980 sized photo is downsized when used for thumbnail
    public static int calculateInSampleSize(
            BitmapFactory.Options options,
            int reqWidth, int reqHeight){

        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight){
            int halfWidth = width / 2;
            int halfHeight = height / 2;

            while(halfWidth / inSampleSize > reqWidth ||
                    halfHeight / inSampleSize > reqHeight){
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void loadImageFromFileAsync(String fileName, ImageView imageView){
        if(cancelPotentialWork(fileName, imageView)){
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable drawable = new AsyncDrawable(null, null, task);
            imageView.setImageDrawable(drawable);
            task.execute(fileName);
        }
    }

    // Checks if this imageView has a bitmapWorkerTask already attached to it
    // if so and the filename is different stops previous work
    // if filename is the same - the same work is being done
    public static boolean cancelPotentialWork(String fileName, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapFileName = bitmapWorkerTask.mFileName;
            if (bitmapFileName != null && !bitmapFileName.equals(fileName)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    // Gets the associated bitmapWorkerTask from an imageView (AsyncDrawable)
    public static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
}