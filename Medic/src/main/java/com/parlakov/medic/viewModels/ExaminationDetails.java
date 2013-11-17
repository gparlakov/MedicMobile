package com.parlakov.medic.viewModels;

import com.parlakov.medic.models.Examination;

/**
 * Created by georgi on 13-11-16.
 */
public  class ExaminationDetails extends Examination {

    private String mPatientName;
    private String mPatientPhotoPath;


    public String getPatientName() {
        return mPatientName;
    }

    public void setPatientName(String patientName) {
        mPatientName = patientName;
    }

    public String getPatientPhotoPath() {
        return mPatientPhotoPath;
    }

    public void setPatientPhotoPath(String mPatientPhotoPath) {
        this.mPatientPhotoPath = mPatientPhotoPath;
    }

}
