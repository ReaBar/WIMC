package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reabar.wimc.FragmentCommunicator;
import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.Model.User;
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;


public class LoginScreenFragment extends Fragment {

    FragmentCommunicator fragmentCommunicator;
    EditText emailInput;
    EditText passwordInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCommunicator = (FragmentCommunicator) getActivity();
        fragmentCommunicator.passString("cancelDrawer");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_screen, container, false);

        emailInput = (EditText) view.findViewById(R.id.emailInput);
        passwordInput = (EditText) view.findViewById(R.id.passwordInput);
        Button loginButton = (Button) view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailInput.getText().toString().matches("") || passwordInput.getText().toString().matches("")){
                    Toast.makeText(MyApplication.getAppActivity(), "You must enter email and password",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    User loginUser = new User(emailInput.getText().toString());
                    Model.getInstance().signInUser(loginUser, passwordInput.getText().toString(), new Model.LoginListener(){
                        @Override
                        public void success(boolean success) {
                            User tempUser = Model.getInstance().getCurrentUser();
                            if(success){
                                Log.d("LoginFragment", "logged in as: " + tempUser.getEmail());
                                fragmentCommunicator.passString("HomeScreenFragment");
                            }
                        }
                        @Override
                        public void failed(String message) {
                            Toast.makeText(MyApplication.getAppActivity(), message,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Button signupButton = (Button) view.findViewById(R.id.signUpButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCommunicator.passString("SignUpScreenFragment");
            }
        });

        Button forgotPasswordButton = (Button) view.findViewById(R.id.resetPasswordButton);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailInput.getText().toString().matches("")) {
                    Toast.makeText(MyApplication.getAppActivity(), "Please enter your email",
                            Toast.LENGTH_SHORT).show();

                } else {
                    Model.getInstance().resetPassword(emailInput.getText().toString(), new Model.ResetPasswordListener() {
                        @Override
                        public void success(boolean success) {
                            Toast.makeText(MyApplication.getAppActivity(), "Please check your mailbox to reset the password",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failed(String message) {
                            Toast.makeText(MyApplication.getAppActivity(), message,
                                    Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        });
         return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
