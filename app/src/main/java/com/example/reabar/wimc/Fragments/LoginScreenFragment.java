package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reabar.wimc.FragmentCommunicator;
import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.Model.User;
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;

import java.util.Locale;


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


        Button loginButton = (Button) view.findViewById(R.id.loginButton);
        Button signupButton = (Button) view.findViewById(R.id.signUpButton);
        Button forgotPasswordButton = (Button) view.findViewById(R.id.resetPasswordButton);

        TextView loginLogo = (TextView) view.findViewById(R.id.logoTextLogin);
        Typeface english = Typeface.createFromAsset(getActivity().getAssets(), "KOMIKAX_.ttf"); // create a typeface from the raw ttf
        Typeface hebrew = Typeface.createFromAsset(getActivity().getAssets(), "OpenSansHebrew-Bold.ttf"); // create a typeface from the raw ttf
        loginLogo.setTypeface(english);

        if(Locale.getDefault().getDisplayLanguage().equals("עברית"))
        {
            loginButton.setTypeface(hebrew);
            signupButton.setTypeface(hebrew);
            forgotPasswordButton.setTypeface(hebrew);
        }


        emailInput = (EditText) view.findViewById(R.id.emailInput);
        passwordInput = (EditText) view.findViewById(R.id.passwordInput);




        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //hide keyboard after click
                InputMethodManager inputManager = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);


                if(emailInput.getText().toString().matches("") || passwordInput.getText().toString().matches("")){
                    Toast.makeText(MyApplication.getAppActivity(), "You must enter email and password",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    User loginUser = new User(emailInput.getText().toString());
                    Model.getInstance().signInUser(loginUser, passwordInput.getText().toString(), new Model.SyncListener(){
                        @Override
                        public void isSuccessful(boolean success) {
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

                        @Override
                        public void passData(Object data) {

                        }
                    });
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCommunicator.passString("SignUpScreenFragment");
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailInput.getText().toString().matches("")) {
                    Toast.makeText(MyApplication.getAppActivity(), "Please enter your email",
                            Toast.LENGTH_SHORT).show();

                } else {
                    Model.getInstance().resetPassword(emailInput.getText().toString(), new Model.SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {
                            Toast.makeText(MyApplication.getAppActivity(), "Please check your mailbox to reset the password",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failed(String message) {
                            Toast.makeText(MyApplication.getAppActivity(), message,
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void passData(Object data) {

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
