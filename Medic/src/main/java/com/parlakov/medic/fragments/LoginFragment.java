package com.parlakov.medic.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.parlakov.medic.R;
import com.parlakov.medic.remotedata.Data;
import com.parlakov.medic.util.TextHelper;

import java.io.IOException;

/**
 * Created by georgi on 13-11-1.
 */
public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View loginView = inflater.inflate(R.layout.fragment_login, container, false);
        initUi(loginView);

        return loginView;
    }

    private void initUi(final View loginView) {
        Button loginButton = (Button) loginView.findViewById(R.id.login_doLoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(loginView);
            }
        });
    }

    private void doLogin(View view) {
        String username = TextHelper.getTextFromEditView(R.id.login_usernameEditText, view);
        String password = TextHelper.getTextFromEditView(R.id.login_passwordEditText, view);


        Data data = new Data();
        String result = null;
        try {
            data.getUsers().login(username, password);
            result = "Logged in!";

        } catch (IOException e) {
            result = "Not logged in. Error: " + e.getMessage();
        }

        Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
    }

}
