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

import com.parlakov.medic.MainActivity;
import com.parlakov.medic.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageHelper {
    public static Uri getTimeStampPhotoUri(Context context) throws IOException {
        Uri photoFileUri;

        String appPicturesPath = getPicturesFolder(context);

        String newPhotoName = "patientImage" + new SimpleDateFormat("DDMMyyyyHHmmss").format(new Date()) + ".jpg";

        File newPhotoFile = getOrCreatePicturesFolderAndFile(appPicturesPath, newPhotoName);

        photoFileUri = Uri.fromFile(newPhotoFile);
        return photoFileUri;
    }

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

    public static String getPicturesFolder(Context context) {
        SharedPreferences prefs = context
                .getSharedPreferences(MainActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
        int saveDataLocation = prefs
                .getInt(MainActivity.APP_SAVE_DATA_LOCATION, MainActivity.NOT_CHOSEN_LOCATION);

        Resources resources = context.getResources();

        String mainPath;
        if (saveDataLocation == MainActivity.SAVE_LOCATION_SD_CARD) {
            mainPath = Environment.getExternalStorageDirectory().getPath();
        } else {
            mainPath = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
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

    public static Bitmap decodeSampledBitmapFromFile(String file, int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        return  BitmapFactory.decodeFile(file, options);
    }

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

    public static boolean cancelPotentialWork(String fileName, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapFileName = bitmapWorkerTask.mFileName;
            if (bitmapFileName != fileName) {
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