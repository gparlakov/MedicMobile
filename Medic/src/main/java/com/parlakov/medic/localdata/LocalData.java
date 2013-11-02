package com.parlakov.medic.localdata;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.parlakov.medic.models.Examination;
import com.parlakov.medic.models.Patient;
import com.parlakov.medic.models.User;
import com.parlakov.uow.IRepository;
import com.parlakov.uow.IUow;
import com.parlakov.uow.IUsersRepository;

/**
 * Created by georgi on 13-11-2.
 */
public class LocalData implements IUow {

    private IUsersRepository<User> mUsers;
    private IRepository<Patient> mPatients;
    private SQLiteOpenHelper mDbHelper;

    public LocalData(Context context){
        String dbPath = MedicDbHelper.getDatabasePath();
        MedicDbHelper dbHelper = new MedicDbHelper(context, dbPath);

        mDbHelper = dbHelper;
    }

    public LocalData(SQLiteOpenHelper dbHelper){
        mDbHelper = dbHelper;
    }


    @Override
    public IUsersRepository<User> getUsers() {
        if(mUsers == null){
            mUsers = new LocalUsers();
        }
        return mUsers;
    }

    @Override
    public IRepository<Patient> getPatients() {
        if(mPatients == null){
            mPatients = new LocalPatients((MedicDbHelper)mDbHelper);
        }
        return mPatients;
    }

    @Override
    public IRepository<Examination> getExaminations() {
        return null;
    }
}
