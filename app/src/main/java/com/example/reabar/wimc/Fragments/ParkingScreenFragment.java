package com.example.reabar.wimc.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.example.reabar.wimc.Model.ModelCloudinary;
import com.example.reabar.wimc.Model.Parking;
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class ParkingScreenFragment extends Fragment implements LocationListener {

    private static final int REQUEST_CODE_LOCATION = 2;

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
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    boolean imageTaken;
    Bitmap bitmap;


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

        imageTaken = false;
        gpsText = (TextView) view.findViewById(R.id.gpsText);
        carNumber = (TextView) view.findViewById(R.id.carnumber);
        carNumber.setText(carNumber.getText().toString() + carID);
        city = (EditText) view.findViewById(R.id.parking_cityInput);
        street = (EditText) view.findViewById(R.id.parking_streetInput);
        number = (EditText) view.findViewById(R.id.parking_streetNumberInput);
        parkingLotName = (EditText) view.findViewById(R.id.parking_parkingLotInput);
        FloorNumber = (EditText) view.findViewById(R.id.parking_parkingLotFloorNumberInput);
        RowColor = (EditText) view.findViewById(R.id.parking_parkingLotRowColorInput);
        imageView = (ImageView) view.findViewById(R.id.imagePlace);

        //take picture flow
        Button imageButton = (Button) view.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });


        //GPS Button flow..
        gpsLocation = (Button) view.findViewById(R.id.locationButton);
        gpsLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gps1 = new GPSTracker(getActivity());
                int permissionCheck = ContextCompat.checkSelfPermission(MyApplication.getAppActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    if (gps1.canGetLocation()) {
                        longtitude = gps1.getLongitude();
                        latitude = gps1.getLatitude();
                        gpsText.setText("Longtitude: " + longtitude + "\nLatitude: " + latitude);
                    } else {
                        gps1.showSettingsAlert();
                    }
                } else {
                    ActivityCompat.requestPermissions(MyApplication.getAppActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION);
                }

            }
        });

        final Date nowDate = new Date();

        //Save parking
        Button saveParking = (Button) view.findViewById(R.id.SaveParkingButton);
        saveParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city.getText().toString().matches("") || street.getText().toString().matches("")) {
                    Toast.makeText(MyApplication.getAppActivity(), "Must enter city and street to save parking",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Parking parking = new Parking.ParkingBuilder(carID).street(street.getText().toString()).streetNumber(number.getText().toString()).city(city.getText().toString()).parkingLotName(parkingLotName.getText().toString()).parkingLotFloor(FloorNumber.getText().toString()).parkingLotRowColor(RowColor.getText().toString()).parkingLatitude(latitude).parkingLonitude(longtitude).startParking(nowDate).build();
                    Model.getInstance().parkCar(parking, new Model.SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {
                            //save image to cloudinary
                            if (imageTaken) {
                                //call cloudinary function
                                ModelCloudinary cloudinary = new ModelCloudinary(getActivity());
                                cloudinary.uploadImage(carID + "_" + nowDate, bitmap);
                            }
                            //go to homepage fragment
                            Toast.makeText(MyApplication.getAppActivity(), "Parking place saved",
                                    Toast.LENGTH_SHORT).show();
                            fragmentCommunicator.passString("HomeScreenFragment");
                        }

                        @Override
                        public void failed(String message) {

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


    //for camera functionality
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                // convert byte array to Bitmap
                bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);

                imageView.setImageBitmap(bitmap);
                imageTaken = true;
            }
        }
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

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // success!
                longtitude = gps1.getLongitude();
                latitude = gps1.getLatitude();
                gpsText.setText("Longtitude: " + longtitude + "\nLatitude: " + latitude);
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }
}

