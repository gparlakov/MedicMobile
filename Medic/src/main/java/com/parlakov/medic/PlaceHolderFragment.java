package com.parlakov.medic;

/**
 * Created by georgi on 13-10-31.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parlakov.medic.data.Data;
import com.parlakov.medic.data.HttpRequester;
import com.parlakov.medic.models.User;

import org.apache.http.message.BasicHeader;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//        TextView textView = (TextView) rootView.findViewById(R.id.lab);
//        textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

        initializeUi(rootView);

        return rootView;
    }

    private void initializeUi(View rootView) {
//        Button sendGetButton = (Button) rootView.findViewById(R.id.buttonSendGetRequest);
//        sendGetButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doSendGet();
//            }
//        });
//
//        Button sendGPostButton = (Button) rootView.findViewById(R.id.buttonSendPostRequest);
//        sendGPostButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doSendPost();
//            }
//        });
    }

    private void doSendPost() {
        User newUser = new User();
        newUser.setDisplayName("NewDoc");
        newUser.setUsername("doctore1");
        newUser.setPassword("doctore1");

        Data data = new Data();
        String response = data.getUsers().register(newUser);
    }

    private void doSendGet() {
        HttpRequester requester = new HttpRequester();
        String textResponse =
                requester.httpGet("http://www.abv.bg/",
                        new BasicHeader("Authorization","Bearer 9ZjuCuRmsDNSzQgC"), null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}