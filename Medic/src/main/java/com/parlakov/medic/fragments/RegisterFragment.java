package com.parlakov.medic.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parlakov.medic.R;
import com.parlakov.medic.data.Data;
import com.parlakov.medic.models.User;

/**
 * Created by georgi on 13-11-1.
 */
public class RegisterFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View loginView =
                inflater.inflate(R.layout.fragment_register, container, false);
        initUi(loginView);

        return loginView;
    }

    private void initUi(final View loginView) {
        Button loginButton =
                (Button) loginView.findViewById(R.id.register_doRegisterButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister(loginView);
            }
        });
    }

    private void doRegister(View view) {
        User newUser = getUser(view);

        Data data = new Data();

        String registered = data.getUsers().register(newUser);

    }

    private User getUser(View view) {
        String username = getTextFromEditView(R.id.register_usernameEditText, view);
        String password = getTextFromEditView(R.id.register_passwordEditText, view);
        String email = getTextFromEditView(R.id.register_emailEditText, view);
        String displayName = getTextFromEditView(R.id.register_displayNameEditText, view);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setDisplayName(displayName);
        newUser.setEmail(email);

        return newUser;
    }
}
