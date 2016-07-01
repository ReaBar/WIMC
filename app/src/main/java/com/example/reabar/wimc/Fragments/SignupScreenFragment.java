package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reabar.wimc.FragmentCommunicator;
import com.example.reabar.wimc.R;


public class SignupScreenFragment extends Fragment {

    FragmentCommunicator fragmentCommunicator;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup_screen, container, false);


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }




}
