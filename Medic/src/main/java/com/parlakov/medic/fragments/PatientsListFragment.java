package com.parlakov.medic.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.parlakov.medic.localdata.MedicDbHelper;
import com.parlakov.medic.util.ImageHelper;
import com.parlakov.uow.IUowMedic;

/**
 * Created by georgi on 13-11-5.
 */
public class PatientsListFragment extends ListFragment {

    private IUowMedic mData;
    private MedicDbHelper mDbHelper;
    private Cursor mPatients;

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
        mDbHelper = new MedicDbHelper(context,
                LocalData.getDbLocationPathFromPreferences(context));

        mData = new LocalData(mDbHelper);

        mPatients = (Cursor) mData.getPatients().getAll();

        SimpleCursorAdapter adapter = getPatiensSimpleCursorAdapter(context, mPatients);

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
                        // reads the bitmap and image view size and calculates size of bitmap
                        // to return
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
        inflater.inflate(R.menu.add_patient_menu, menu);
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
    public void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mData != null){
            mData.getPatients().close();
        }


        if(mPatients != null){
            mPatients.close();
        }

        if(mDbHelper != null){
            mDbHelper.close();
        }
    }
}
