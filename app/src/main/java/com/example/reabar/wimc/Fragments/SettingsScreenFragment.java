package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;

import java.util.Locale;

public class SettingsScreenFragment extends Fragment {

    FragmentCommunicator fragmentCommunicator;
    EditText settingsPasswordInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCommunicator = (FragmentCommunicator) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings_screen, container, false);

        Button settingsPasswordButton = (Button) view.findViewById(R.id.settingsPasswordButton);
        TextView settings = (TextView) view.findViewById(R.id.settingsText);

        Typeface english = Typeface.createFromAsset(getActivity().getAssets(), "KOMIKAX_.ttf"); // create a typeface from the raw ttf
        Typeface hebrew = Typeface.createFromAsset(getActivity().getAssets(), "OpenSansHebrew-Bold.ttf"); // create a typeface from the raw ttf
        if(Locale.getDefault().getDisplayLanguage().equals("עברית"))
        {
            settingsPasswordButton.setTypeface(hebrew);
            settings.setTypeface(hebrew);
        }
        else
        {
            settingsPasswordButton.setTypeface(english);
            settings.setTypeface(english);
        }


        settingsPasswordInput = (EditText) view.findViewById(R.id.settingsPasswordInput);
        settingsPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //hide keyboard after click
                InputMethodManager inputManager = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);


                if(settingsPasswordInput.getText().toString().matches("")){
                    Toast.makeText(MyApplication.getAppActivity(), "You must enter the new password",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Model.getInstance().updatePassword(settingsPasswordInput.getText().toString(), new Model.SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {
                            if (success) {
                                Toast.makeText(MyApplication.getAppActivity(), "Password Updated.",
                                        Toast.LENGTH_SHORT).show();
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
