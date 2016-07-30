package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reabar.wimc.FragmentCommunicator;
import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;

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

        settingsPasswordInput = (EditText) view.findViewById(R.id.settingsPasswordInput);
        Button settingsPasswordButton = (Button) view.findViewById(R.id.settingsPasswordButton);
        settingsPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        public void PassData(Object data) {

                        }
                    });
                }
            }
        });



        Button MyCars = (Button) view.findViewById(R.id.button);
        MyCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCommunicator.passString("ManageMyCarsScreenFragment");
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
