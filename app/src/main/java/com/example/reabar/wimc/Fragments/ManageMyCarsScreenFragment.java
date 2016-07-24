package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reabar.wimc.FragmentCommunicator;
import com.example.reabar.wimc.Model.Car;
import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;

import java.util.ArrayList;
import java.util.List;

public class ManageMyCarsScreenFragment extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentCommunicator fragmentCommunicator;
    CarScreenFragment carFragment;

    EditText carLicenseInput;
    EditText carColorInput;
    EditText carModelInput;
    EditText carCompanyInput;

    MyCarsAdapter adapter;
    ListView carsList;
    List<Car> cars;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_my_cars_screen, container, false);

        cars = Model.getInstance().getAllCars();

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
                    Car newCar = new Car(carLicenseInput.getText().toString(), carColorInput.getText().toString(), carModelInput.getText().toString(), carCompanyInput.getText().toString(), Model.getInstance().getCurrentUser().getEmail());
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



        carsList= (ListView) view.findViewById(R.id.carsListView);
        Model.getInstance().getOwnedCars(Model.getInstance().getCurrentUser().getEmail(), new Model.SyncListener() {
            @Override
            public void PassData(Object allCars) {
                cars = (ArrayList) allCars;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void isSuccessful(boolean s) {
            }

            @Override
            public void failed(String s) {
            }


        });

        adapter = new MyCarsAdapter();
        carsList.setAdapter(adapter);
        carsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MyApplication.getAppActivity(), "Row Clicked!",
                        Toast.LENGTH_SHORT).show();
                carFragment = new CarScreenFragment();
                carFragment.carOwners = cars.get(position).getUsersList();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, carFragment, "CarScreenFragment");
                fragmentTransaction.addToBackStack(null).commit();

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public class MyCarsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cars.size();
        }

        @Override
        public Object getItem(int position) {
            return cars.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                    convertView= layoutInflater.inflate(R.layout.fragment_manage_my_cars_screen_row,null);

                }
                else{
                    Log.d("TAG", "use convert view:" + position);
                }

            TextView carLicense = (TextView) convertView.findViewById(R.id.cars_list_row_car_license);
            TextView carModelCompany = (TextView) convertView.findViewById(R.id.cars_list_row_car_model_company);
            Car car = cars.get(position);
            carLicense.setText(car.getCarId());
            carModelCompany.setText(car.getCompany() + " " + car.getModel());

//            if(car.getUsersList().size()  == 1){
//                User1.setText(car.getUsersList().get(0));
//                User2.setText("");
//                User3.setText("");
//            }
//
//            if(car.getUsersList().size() == 2){
//                User1.setText(car.getUsersList().get(0));
//                User2.setText(car.getUsersList().get(1));
//                User3.setText("");
//
//            }
//            if(car.getUsersList().size() == 3){
//                User1.setText(car.getUsersList().get(0));
//                User2.setText(car.getUsersList().get(1));
//                User3.setText(car.getUsersList().get(2));
//            }
            return convertView;
        }
    }

}
