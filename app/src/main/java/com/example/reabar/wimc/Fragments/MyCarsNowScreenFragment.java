package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.reabar.wimc.FragmentCommunicator;
import com.example.reabar.wimc.Model.ModelCloudinary;
import com.example.reabar.wimc.Model.Parking;
import com.example.reabar.wimc.R;

import java.util.List;


public class MyCarsNowScreenFragment extends Fragment {

    ProgressBar progressBar;
    FragmentCommunicator fragmentCommunicator;
    FragmentTransaction fragmentTransaction;
    MyCarsNowAdapter adapter;
    ListView parkingsList;
    List<Parking> parkings;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCommunicator.passString("enableDrawer");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cars_now, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.myCarsNow_ProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        parkingsList = (ListView) view.findViewById((R.id.myCarsNowList));

        //get the list by from function


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

            TextView myParkingCarDetails = (TextView) convertView.findViewById(R.id.myParkingCarModelYear);
            TextView myParkingCarCity = (TextView) convertView.findViewById(R.id.parkingCityInput);
            TextView myParkingCarStreet = (TextView) convertView.findViewById(R.id.parkingStreetInput);
            TextView myParkingLotName = (TextView) convertView.findViewById(R.id.parkingLotNameInput);
            TextView myParkingLotFloor = (TextView) convertView.findViewById(R.id.parkingLotFloorInput);
            ImageView parkingPhoto = (ImageView) convertView.findViewById(R.id.parkingPhoto);

            Parking parking = parkings.get(position);
            myParkingCarDetails.setText(parking.getCarId());
            myParkingCarCity.setText(parking.getCity());
            myParkingCarStreet.setText(parking.getStreet() + " " + parking.getStreetNumber());
            myParkingLotName.setText(parking.getParkingLotName());
            myParkingLotFloor.setText(parking.getParkingLotFloor());
            //load image from cloudinary
            ModelCloudinary cloudinary = new ModelCloudinary(getActivity());
            Bitmap image = cloudinary.loadImage(parking.getCarId());
            if(image != null){
                parkingPhoto.setImageBitmap(image);
            }




            return convertView;
        }
    }


}
