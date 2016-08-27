package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v4.app.Fragment;

import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.R;

/**
 * Created by admin on 8/27/16.
 */
public class ParkingPhotoFragment extends Fragment {

    public String photoName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_praking_photo_screen, container, false);
        final ImageView parkingPhoto = (ImageView) view.findViewById(R.id.parkingPhoto1);

        Model.getInstance().loadImage(photoName, new Model.LoadImageListener() {
            @Override
            public void onResult(Bitmap imageBmp) {
                parkingPhoto.setImageBitmap(imageBmp);
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
