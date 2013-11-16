package com.parlakov.medic.async;

import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import com.parlakov.medic.dto.CancelAppointmentDTO;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.models.Examination;

/**
 * Created by georgi on 13-11-16.
 */
public class CancelAppointmentWorker extends AsyncTask<CancelAppointmentDTO, Void, Boolean> {
    @Override
    protected Boolean doInBackground(CancelAppointmentDTO... params) {
        CancelAppointmentDTO dataTranferObject = params[0];
        LocalData data = dataTranferObject.getData();
        long examinationId = dataTranferObject.getExaminationId();

        try {
            Examination examination = data.getExaminations()
                    .getById(examinationId);

            examination.setCancelled(true);

            data.getExaminations().update(examination);

            return true;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean deleted) {
        if (deleted) {

        } else {

        }
    }
}
