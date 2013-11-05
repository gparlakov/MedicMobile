package com.parlakov.medic.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parlakov.medic.R;

/**
 * Created by georgi on 13-11-5.
 */
public class PatientsListFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View patienstListViewLayout = inflater.inflate(R.layout.fragment_patients_list, container, false);

        initializeUi(patienstListViewLayout);

        return patienstListViewLayout;
    }

    private void initializeUi(View patienstListView) {

        ListView patientsListView =
                (ListView) patienstListView.findViewById(R.id.patientsListView);

//        ListAdapter adapter = new SimpleAdapter()
    }
}
