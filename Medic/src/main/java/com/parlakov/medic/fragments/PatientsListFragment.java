package com.parlakov.medic.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.parlakov.medic.R;
import com.parlakov.medic.activities.AddEditPatientActivity;
import com.parlakov.medic.activities.PatientManagementActivity;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.MedicDbContract;
import com.parlakov.medic.util.BitmapWorkerTask;
import com.parlakov.medic.util.ImageHelper;

/**
 * Created by georgi on 13-11-5.
 */
public class PatientsListFragment extends ListFragment {

    private LocalData mData;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        handlePatientItemClicked(id);
    }

    private void handlePatientItemClicked(long id) {
        Intent showPatientDetails = new Intent(getActivity(), PatientManagementActivity.class);
        showPatientDetails.putExtra(PatientManagementActivity.PATIENT_ID_EXTRA, id);
        startActivity(showPatientDetails);
    }

    private void initialize() {
        Context context = getListView().getContext();
        mData = new LocalData(context);
        Cursor patients = mData.getPatients().getAll();

        SimpleCursorAdapter adapter = getPatiensSimpleCursorAdapter(context, patients);

        this.setListAdapter(adapter);

        this.setEmptyText(getString(R.string.emptyPatientsList));
    }

    private SimpleCursorAdapter getPatiensSimpleCursorAdapter(
            Context context, Cursor patients) {
        String[] from = new String[]
                {
                        MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH,
                        MedicDbContract.Patient.COLUMN_NAME_FIRST_NAME,
                        MedicDbContract.Patient.COLUMN_NAME_LAST_NAME,
                };

        int[] to = new int[]
                {
                        R.id.listItemPatientImage,
                        R.id.listItemPatientFirstName,
                        R.id.listItemPatientLastName
                };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
                R.layout.item_patient,
                patients,
                from,
                to,
                0);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex ==
                        cursor.getColumnIndex(MedicDbContract.Patient.COLUMN_NAME_PHOTO_PATH)) {
                    String picturePath = cursor.getString(columnIndex);
                    ImageView patientPicture = (ImageView) view;

                    if (picturePath != null && !picturePath.isEmpty()) {
                        ImageHelper.loadImageFromFileAsync(picturePath, patientPicture);
                    }
                    else {
                        patientPicture.setImageResource(R.drawable.ic_default_picture);
                    }
                    return true;
                }

                return false;
            }
        });

        return adapter;
    }

    //<editor-fold desc="action bar management">
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.addpatientmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean handled = true;

        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_addPatient:
                Intent newPatientIntent = new Intent(getActivity(), AddEditPatientActivity.class);
                startActivity(newPatientIntent);
                break;
            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }

        return handled;
    }
    //</editor-fold>

    @Override
    public void onStop() {
        super.onStop();
        mData.getPatients().close();
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }
}
