package com.parlakov.medic.async;

import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.widget.Toast;

import com.parlakov.medic.dto.CancelAppointmentDTO;
import com.parlakov.medic.exceptions.MedicException;
import com.parlakov.medic.interfaces.ChildFragmentListener;
import com.parlakov.medic.interfaces.OnCancelResultListener;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.models.Examination;

/**
 * Created by georgi on 13-11-16.
 */
public class CancelAppointmentWorker
        extends AsyncTask<CancelAppointmentDTO, Void, Boolean> {

    private OnCancelResultListener mListener;

    @Override
    protected Boolean doInBackground(CancelAppointmentDTO... params) {
        CancelAppointmentDTO dataTransferObject = params[0];
        LocalData data = dataTransferObject.getData();
        long examinationId = dataTransferObject.getExaminationId();
        mListener = dataTransferObject.getListener();

        try {
            Examination examination = data.getExaminations()
                    .getById(examinationId);

            examination.setCancelled(true);

            data.getExaminations().update(examination);

            return true;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        } catch (MedicException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean isCancelSuccessful) {
        mListener.onCancelResult(isCancelSuccessful);
    }
}
