package com.parlakov.medic.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by georgi on 13-10-30.
 */
public class Examination implements Serializable {
    private long mId;
    private long mPatientId;
    private long mDate;
    private String mComplaints;
    private String mConclusion;
    private String mTreatment;
    private String mNotes;
    private Boolean mCancelled;

    public long getPatientId() {
        return mPatientId;
    }

    public void setPatientId(long patientId) {
        mPatientId = patientId;
    }

    public long getDateInMillis() {
        return mDate;
    }

    public void setDateInMillis(long date) {
        mDate = date;
    }

    public String getComplaints() {
        return mComplaints;
    }

    public void setComplaints(String complaints) {
        mComplaints = complaints;
    }

    public String getConclusion() {
        return mConclusion;
    }

    public void setConclusion(String conclusion) {
        mConclusion = conclusion;
    }

    public String getTreatment() {
        return mTreatment;
    }

    public void setTreatment(String treatment) {
        mTreatment = treatment;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public Boolean getCancelled() {
        return mCancelled;
    }

    public void setCancelled(Boolean mCancelled) {
        this.mCancelled = mCancelled;
    }
}
