package com.parlakov.medic.localdata;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;

import com.parlakov.medic.Global;
import com.parlakov.medic.R;
import com.parlakov.medic.models.Examination;
import com.parlakov.medic.models.Patient;
import com.parlakov.medic.models.User;
import com.parlakov.uow.IRepository;
import com.parlakov.uow.IUowMedic;
import com.parlakov.uow.IUsersRepository;

/**
 * Created by georgi on 13-11-2.
 */
public class LocalData implements IUowMedic {

    private IUsersRepository<User> mUsers; //TODO - remove if not used
    private IRepository<Patient> mPatients;
    private IRepository<Examination> mExaminations;

    private SQLiteOpenHelper mDbHelper;

    public LocalData(Context context){
        String dbPath = getDbLocationPath(context);

        MedicDbHelper dbHelper = new MedicDbHelper(context, dbPath);

        mDbHelper = dbHelper;
    }

    public IUsersRepository<User> getUsers() {
        if(mUsers == null){
            mUsers = new LocalUsers();
        }
        return mUsers;
    }

    public IRepository<Patient> getPatients() {
        if(mPatients == null){
            mPatients = new LocalPatients((MedicDbHelper)mDbHelper);
        }
        return mPatients;
    }

    public IRepository<Examination> getExaminations() {
        if(mExaminations == null){
            mExaminations = new LocalExaminations(mDbHelper);
        }
        return mExaminations;
    }

    public void closeDb(){
        getPatients().close();
        getExaminations().close();
    }

    public static String getDbLocationPath(Context context) {
        SharedPreferences prefs = context
                .getSharedPreferences(Global.PROPERTYS_NAME, Context.MODE_PRIVATE);

        int saveDataLocation = prefs
                .getInt(Global.PROPERTY_SAVE_LOCATION, Global.NOT_CHOSEN_LOCATION);

        String dbPath;

        if (saveDataLocation == Global.SAVE_LOCATION_SD_CARD){
            dbPath = MedicDbHelper.getSDDatabasePath();
        }
        else{
            dbPath = context.getDatabasePath(MedicDbHelper.DATABASE_NAME)
                    .getAbsolutePath();
        }
        return dbPath;
    }
}
