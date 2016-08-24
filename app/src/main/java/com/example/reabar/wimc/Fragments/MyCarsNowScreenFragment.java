package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reabar.wimc.FragmentCommunicator;
import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.Model.ModelCloudinary;
import com.example.reabar.wimc.Model.Parking;
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;

import java.util.ArrayList;
import java.util.List;


public class MyCarsNowScreenFragment extends Fragment {

    ProgressBar progressBar;
    FragmentCommunicator fragmentCommunicator;
    FragmentTransaction fragmentTransaction;
    MyCarsNowAdapter adapter;
    ListView parkingsList;
    List<Parking> parkings;
    ModelCloudinary cloudinary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cars_now, container, false);
        fragmentCommunicator = (FragmentCommunicator) getActivity();


        cloudinary = new ModelCloudinary(getActivity());

        if(parkings == null) {
            parkings = new ArrayList<>();
        }

        progressBar = (ProgressBar) view.findViewById(R.id.myCarsNow_ProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        parkingsList = (ListView) view.findViewById((R.id.myCarsNowList));

        //get the list by from function
        Model.getInstance().getMyParkingSpots(new Model.SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                parkings = (ArrayList) data;
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new MyCarsNowAdapter();
        parkingsList.setAdapter(adapter);


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


    public class MyCarsNowAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return parkings.size();
        }

        @Override
        public Object getItem(int position) {
            return parkings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                convertView= layoutInflater.inflate(R.layout.fragment_my_cars_now_row,null);
            }
            else{
                Log.d("TAG", "use convert view:" + position);
            }

            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"Alyssa_Kayla.ttf"); // create a typeface from the raw ttf
            TextView yourCarParkingHere = (TextView) convertView.findViewById(R.id.parkingText1);
            yourCarParkingHere.setTypeface(typeface);
            TextView parkingLotDetails = (TextView) convertView.findViewById(R.id.parkingLotText);
            TextView myParkingCarDetails = (TextView) convertView.findViewById(R.id.myParkingCarModelYear);
            myParkingCarDetails.setTypeface(typeface);

            TextView myParkingCarCityStreetNumber = (TextView) convertView.findViewById(R.id.parkingCityStreetInput1);
            TextView myParkingLotName = (TextView) convertView.findViewById(R.id.parkingLotNameInput);
            TextView myParkingLotFloor = (TextView) convertView.findViewById(R.id.parkingLotFloorInput);
            final ImageView parkingPhoto = (ImageView) convertView.findViewById(R.id.parkingPhoto);
            ImageButton gpsLink = (ImageButton) convertView.findViewById(R.id.googleMapsLocation);
            Button stopParkingButton = (Button) convertView.findViewById(R.id.stopParkingButton);


            final Parking parking = parkings.get(position);
            myParkingCarDetails.setText(myParkingCarDetails.getText().toString() + parking.getCarId());
            myParkingCarCityStreetNumber.setText(parking.getStreet() + " " + parking.getStreetNumber() + "  " + parking.getCity());
            if(parking.getParkingLotName() != null || parking.getParkingLotName() != ""){
                parkingLotDetails.setText("");
                myParkingLotName.setText(parking.getParkingLotName());
                myParkingLotFloor.setText(parking.getParkingLotFloor());
            }

            //load image from cloudinary
            Model.getInstance().loadImage(parking.getCarId()+"_"+parking.getStartParking(), new Model.LoadImageListener() {
                @Override
                public void onResult(Bitmap imageBmp) {
                    if(imageBmp != null){
                        parkingPhoto.setImageBitmap(imageBmp);
                    }
                }
            });

            gpsLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Object[] data = new Object[2];
                    data[0] = parking.getLatitude();
                    data[1] = parking.getLongitude();
                    fragmentCommunicator.passData(data, "MapScreenFragment");
                }
            });

            stopParkingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Model.getInstance().stopParking(parking);
                    Toast.makeText(MyApplication.getAppActivity(), "Parking has ended. Drive carefully!",
                            Toast.LENGTH_SHORT).show();
                    fragmentCommunicator.passString("HomeScreenFragment");

                }
            });
            return convertView;
        }
    }


}
