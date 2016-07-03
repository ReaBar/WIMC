package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.reabar.wimc.FragmentCommunicator;
import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.Model.User;
import com.example.reabar.wimc.R;


public class SignupScreenFragment extends Fragment {

    FragmentCommunicator fragmentCommunicator;
    EditText emailInput;
    EditText passwordInput;
    EditText repasswordInput;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup_screen, container, false);
        emailInput = (EditText) view.findViewById(R.id.emailInput);
        passwordInput = (EditText) view.findViewById(R.id.passwordInput);
        repasswordInput = (EditText) view.findViewById(R.id.repasswordInput);

        Button signupButton = (Button) view.findViewById(R.id.registerButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordInput.getText().toString().equals(repasswordInput.getText().toString())){
                    User newUser = new User(emailInput.getText().toString(), passwordInput.getText().toString());
                    Model.getInstance().registerNewUser(newUser);
                }

            }
        });


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }




}
