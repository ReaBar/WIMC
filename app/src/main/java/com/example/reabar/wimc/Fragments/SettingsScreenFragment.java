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
import com.example.reabar.wimc.Model.Car;
import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;

public class SettingsScreenFragment extends Fragment {

    FragmentCommunicator fragmentCommunicator;
    EditText settingsPasswordInput;
    EditText carLicenseInput;
    EditText carColorInput;
    EditText carModelInput;
    EditText carCompanyInput;

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
                    Model.getInstance().updatePassword(settingsPasswordInput.getText().toString(), new Model.UpdatePasswordListener() {
                        @Override
                        public void success(boolean success) {
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
                    });
                }
            }
        });


        carCompanyInput = (EditText) view.findViewById(R.id.carCompanyInput);
        carColorInput = (EditText) view.findViewById(R.id.carColorInput);
        carLicenseInput = (EditText) view.findViewById(R.id.carLicenseInput);
        carModelInput = (EditText) view.findViewById(R.id.carModelInput);
        Button addNewCarButton = (Button) view.findViewById(R.id.newCarButton);
        addNewCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(carCompanyInput.getText().toString().matches("") || carColorInput.getText().toString().matches("") || carLicenseInput.getText().toString().matches("") || carModelInput.getText().toString().matches("")){
                    Toast.makeText(MyApplication.getAppActivity(), "You must fill all the information about the new car",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Car newCar = new Car(carLicenseInput.getText().toString(), carColorInput.getText().toString(), carModelInput.getText().toString(), carCompanyInput.getText().toString(), Model.getInstance().getCurrentUserID());
                    Model.getInstance().addCarToDB(newCar, new Model.AddNewCarListener() {
                        @Override
                        public void success(boolean success) {
                            if (success) {
                                Toast.makeText(MyApplication.getAppActivity(), "New Car Added!",
                                        Toast.LENGTH_SHORT).show();
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
