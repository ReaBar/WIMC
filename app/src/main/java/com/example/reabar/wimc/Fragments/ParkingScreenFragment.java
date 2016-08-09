package com.example.reabar.wimc.Fragments;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reabar.wimc.FragmentCommunicator;
import com.example.reabar.wimc.GPSTracker;
import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.Model.Parking;
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;


public class ParkingScreenFragment extends Fragment implements  LocationListener{

    FragmentTransaction fragmentTransaction;
    FragmentCommunicator fragmentCommunicator;


    public String carID;
    public TextView carNumber;
    EditText city;
    EditText street;
    EditText number;
    EditText parkingLotName;
    EditText FloorNumber;
    EditText RowColor;
    TextView gpsText;
    ImageView imageView;
    Button gpsLocation;
    GPSTracker gps1;
    double longtitude;
    double latitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCommunicator = (FragmentCommunicator) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parking_screen, container, false);

        gpsText = (TextView) view.findViewById(R.id.gpsText);
        carNumber = (TextView) view.findViewById(R.id.carnumber);
        carNumber.setText("Save location for car " + carID);
        city = (EditText) view.findViewById(R.id.parking_cityInput);
        street = (EditText) view.findViewById(R.id.parking_streetInput);
        number = (EditText) view.findViewById(R.id.parking_streetNumberInput);
        parkingLotName = (EditText) view.findViewById(R.id.parking_parkingLotInput);
        FloorNumber = (EditText) view.findViewById(R.id.parking_parkingLotFloorNumberInput);
        RowColor = (EditText) view.findViewById(R.id.parking_parkingLotRowColorInput);

        Button imageButton = (Button) view.findViewById(R.id.imageButton);


        //GPS Button flow..
        gpsLocation = (Button) view.findViewById(R.id.locationButton);
        gpsLocation.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                gps1 = new GPSTracker(getActivity());
                if(gps1.canGetLocation()){
                    longtitude = gps1.getLongtitude();
                    latitude = gps1.getLatitude();
                    gpsText.setText("Longtitude: " + longtitude + "\nLatitude: " + latitude);
                }
                else {
                    gps1.showSettingsAlert();
                }
            }
        });




        Button saveParking = (Button) view.findViewById(R.id.SaveParkingButton);
        saveParking.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (city.getText().toString().matches("") || street.getText().toString().matches("")) {
                    Toast.makeText(MyApplication.getAppActivity(), "Must enter city and street to save parking",
                            Toast.LENGTH_SHORT).show();
                }
                else {


                    Parking parking = new Parking.ParkingBuilder(carID).street(street.getText().toString()).city(city.getText().toString()).parkingLotName(parkingLotName.getText().toString()).parkingLotFloor(FloorNumber.getText().toString()).parkingLotRowColor(RowColor.getText().toString()).parkingLatitude(latitude).parkingLonitude(longtitude).build();
                    Model.getInstance().parkCar(parking, new Model.SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {
                            Toast.makeText(MyApplication.getAppActivity(), "Parking place saved",
                                    Toast.LENGTH_SHORT).show();
                            fragmentCommunicator.passString("HomeScreenFragment");
                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void PassData(Object data) {

                        }
                    });

                }
            }
        });




        return view;
    }




    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
