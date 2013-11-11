package com.parlakov.medic.data;

import com.parlakov.medic.models.Examination;
import com.parlakov.medic.models.Patient;
import com.parlakov.uow.IRepository;
import com.parlakov.uow.IUowMedic;

/**
 * Created by georgi on 13-10-30.
 */
public class Data implements IUowMedic {
    private final String BASE_URL = "http://api.everlive.com/v1/";
    private final String API_KEY = "9ZjuCuRmsDNSzQgC";


    HttpRequester mRequester;

    public Users getUsers() {
        return mDoctors;
    }

    @Override
    public IRepository<Patient> getPatients() {
        return null;
    }

    @Override
    public IRepository<Examination> getExaminations() {
        return null;
    }

    Users mDoctors;

    public Data(){
        mRequester = new HttpRequester();
        mDoctors = new Users(BASE_URL, mRequester, API_KEY);
    }

}
