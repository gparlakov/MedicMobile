package com.parlakov.medic.dto;

import com.parlakov.medic.localdata.LocalData;

/**
 * Created by georgi on 13-11-16.
 */
public class CancelAppointmentDTO {
    private LocalData data;
    private long examinationId;

    public LocalData getData() {
        return data;
    }

    public void setData(LocalData data) {
        this.data = data;
    }

    public long getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(long examinationId) {
        this.examinationId = examinationId;
    }

    public CancelAppointmentDTO(LocalData data, long examinationId){
        setData(data);
        setExaminationId(examinationId);
    }
}
