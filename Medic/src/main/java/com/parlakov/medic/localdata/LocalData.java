package com.parlakov.medic.localdata;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;

import com.parlakov.medic.MainActivity;
import com.parlakov.medic.models.Examination;
import com.parlakov.medic.models.Patient;
import com.parlakov.medic.models.User;
import com.parlakov.uow.IRepository;
import com.parlakov.uow.IUow;
import com.parlakov.uow.IUsersRepository;

/**
 * Created by georgi on 13-11-2.
 */
public class LocalData {

    private IUsersRepository<User> mUsers;
    private LocalPatients mPatients;
    private SQLiteOpenHelper mDbHelper;

    public LocalData(Context context){
        String dbPath = getDbLocationPathFromPreferences(context);

        MedicDbHelper dbHelper = new MedicDbHelper(context, dbPath);

        mDbHelper = dbHelper;
    }

    private String getDbLocationPathFromPreferences(Context context) {
        SharedPreferences prefs = context
                .getSharedPreferences(MainActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);

        int saveDataLocation = prefs
                .getInt(MainActivity.APP_SAVE_DATA_LOCATION, MainActivity.NOT_CHOSEN_LOCATION);

        String dbPath = null;

        if (saveDataLocation != MainActivity.SAVE_LOCATION_SD_CARD){
            dbPath = context.getDatabasePath(MedicDbHelper.DATABASE_NAME).getAbsolutePath();
        }
        else{
            dbPath = MedicDbHelper.getSDDatabasePath();
        }
        return dbPath;
    }

    public LocalData(SQLiteOpenHelper dbHelper){
        mDbHelper = dbHelper;
    }



    public IUsersRepository<User> getUsers() {
        if(mUsers == null){
            mUsers = new LocalUsers();
        }
        return mUsers;
    }


    public LocalPatients getPatients() {
        if(mPatients == null){
            mPatients = new LocalPatients((MedicDbHelper)mDbHelper);
        }
        return mPatients;
    }


    public IRepository<Examination> getExaminations() {
        return null;
    }
}
