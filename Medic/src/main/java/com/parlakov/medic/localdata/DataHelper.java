package com.parlakov.medic.localdata;

import android.content.Context;

/**
 * Created by georgi on 13-11-16.
 */
public class DataHelper {
    private static DataHelper ourInstance = new DataHelper();
    private static Context context;
    private LocalData mLocalData;

    public static DataHelper getInstance(Context context) {
        setContext(context);
        return ourInstance;
    }

    public static void setContext(Context context) {
        if(DataHelper.context == null){
            DataHelper.context = context;
        }
    }

    private DataHelper() {
    }

    public LocalData getLocalData(){
        if(mLocalData == null){
            mLocalData = new LocalData(context);
        }
        return mLocalData;
    }

    public void closeLocalData(){
        if(mLocalData != null){
            mLocalData.closeDb();
            mLocalData = null;
        }
    }
}
