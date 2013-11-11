package com.parlakov.medic.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parlakov.medic.R;
import com.parlakov.medic.activities.AddEditExaminationActivity;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.LocalExaminations;
import com.parlakov.medic.localdata.MedicDbContract;
import com.parlakov.uow.IUowMedic;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by georgi on 13-11-11.
 */
public class ExaminationsListFragment extends ListFragment {
    private IUowMedic mData;

    public IUowMedic getData() {
        if (mData == null) {
            mData = new LocalData(getActivity().getApplicationContext());
        }
        return mData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        initialize();
    }

    private void initialize() {
        try {
            final IUowMedic data = getData();
            final Context context = getActivity().getApplicationContext();
            final ListFragment that = this;

            // will do the database read on a background thread
            // and add the list view adapter afterwards
            new AsyncTask<Void, Void, SimpleCursorAdapter>() {
                @Override
                protected SimpleCursorAdapter doInBackground(Void... params) {
                    LocalExaminations examinations = (LocalExaminations) data.getExaminations();
                    Cursor allExaminations = examinations.getAllWithPatientNames();

                    String[] fromColumns =
                            {
                                    MedicDbContract.Examination.COLUMN_NAME_DATE,
                                    MedicDbContract.PATIENT_FULL_NAME,
                                    MedicDbContract.Examination.COLUMN_NAME_COMPLAINTS
                            };

                    int[] toViewIds =
                            {
                                    R.id.cursor_item_examination_date,
                                    R.id.cursor_item_examination_patientFullName,
                                    R.id.cursor_item_examination_complaints
                            };

                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                            context,
                            R.layout.cursor_item_examination,
                            allExaminations,
                            fromColumns,
                            toViewIds,
                            0);

                    adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Cursor cursor, int colIndex) {
                            if (colIndex == cursor.getColumnIndex(MedicDbContract.Examination.COLUMN_NAME_DATE)){
                                String stringDate = cursor.getString(colIndex);
                                Date date = new Date(stringDate);
                                String formated = new SimpleDateFormat("dd/MM/yyyy").format(date);
                                ((TextView)view).setText(formated);
                                return true;
                            }

                            if(colIndex == cursor.getColumnIndex(MedicDbContract.PATIENT_FULL_NAME)){
                                String name = cursor.getString(colIndex);
                            }

                            return false;
                        }
                    });

                    return adapter;
                }

                @Override
                protected void onPostExecute(SimpleCursorAdapter simpleCursorAdapter) {
                    that.setListAdapter(simpleCursorAdapter);
                }
            }.execute();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //<editor-fold desc="Action menu">
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.examinations_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean handled = true;

        int selectedItemId = item.getItemId();
        switch (selectedItemId) {
            case R.id.action_addExamination:
                handleAddExamination();
                break;
            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }

        return handled;
    }

    private void handleAddExamination() {
        Intent addExaminationIntent = new Intent(getActivity(),
                AddEditExaminationActivity.class);

        startActivity(addExaminationIntent);
    }
    //</editor-fold>


}
