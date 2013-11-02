package com.parlakov.medic.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parlakov.medic.R;
import com.parlakov.medic.localdata.LocalData;
import com.parlakov.medic.localdata.MedicDbHelper;
import com.parlakov.medic.models.Patient;

/**
 * Created by georgi on 13-11-1.
 */
public class HomeFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_main, container, false);

        initializeUi(homeView);

        return homeView;
    }

    private void initializeUi(final View homeView) {
        Button createDbButton = (Button) homeView.findViewById(R.id.buttonCreateDB);
        createDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCreateDb(homeView);
            }
        });
    }

    private void doCreateDb(View homeView) {
        Patient newPatient = new Patient("Stamat", "Patkov", 20, "tel.08994747");

        LocalData data = new LocalData(homeView.getContext());
        data.getPatients().add(newPatient);
    }



}
