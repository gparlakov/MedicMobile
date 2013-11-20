package com.parlakov.medic.dto;

import com.parlakov.medic.interfaces.ChildFragmentListener;
import com.parlakov.medic.interfaces.OnCancelResultListener;
import com.parlakov.medic.localdata.LocalData;

/**
 * Created by georgi on 13-11-16.
 */
public class CancelAppointmentDTO {
    private LocalData data;
    private long examinationId;
    private OnCancelResultListener listener;

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

    public void setListener(OnCancelResultListener listener) {
        this.listener = listener;
    }

    public OnCancelResultListener getListener() {
        return listener;
    }

    public CancelAppointmentDTO(LocalData data, long examinationId,
                                OnCancelResultListener listenr) {
        setData(data);
        setExaminationId(examinationId);
        setListener(listenr);
    }
}
