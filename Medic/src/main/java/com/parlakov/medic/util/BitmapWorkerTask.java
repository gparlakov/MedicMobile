package com.parlakov.medic.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by georgi on 13-11-9.
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    private WeakReference<ImageView> mImageView;
    private int mReqWidth;
    private int mReqHeight;

    public String mFileName;

    public BitmapWorkerTask(ImageView imageView){
        mImageView = new WeakReference<ImageView>(imageView);

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if(layoutParams != null){
            mReqWidth = layoutParams.width;
            mReqHeight = layoutParams.height;
        }

        if(mReqHeight == 0){
            mReqHeight = 100;
        }

        if(mReqWidth == 0){
            mReqWidth = 100;
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        mFileName = params[0];
        Bitmap bitmap = ImageHelper
                .decodeSampledBitmapFromFile(mFileName, mReqWidth, mReqHeight);

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled()){
            bitmap = null;
        }

        if(mImageView != null && bitmap != null){
            final ImageView imageView = mImageView.get();
            final BitmapWorkerTask bitmapWorkerTask =
                    ImageHelper.getBitmapWorkerTask(imageView);

            if(this == bitmapWorkerTask && imageView != null){
                mImageView.get().setImageBitmap(bitmap);
            }
        }
    }
}
