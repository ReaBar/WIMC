package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reabar.wimc.FragmentCommunicator;
import com.example.reabar.wimc.Model.Car;
import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ManageMyCarsScreenFragment extends Fragment {

    FragmentTransaction fragmentTransaction;
    FragmentCommunicator fragmentCommunicator;
    CarScreenFragment carFragment;
    EditText carLicenseInput;
    EditText carColorInput;
    EditText carModelInput;
    EditText carCompanyInput;
    ProgressBar progressBar;
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
        fragmentCommunicator = (FragmentCommunicator) getActivity();

        if(cars == null) {
            cars = new ArrayList<>();
        }

        Button addNewCarButton = (Button) view.findViewById(R.id.newCarButton);
        TextView title = (TextView) view.findViewById(R.id.logoTextManageMyCars);
        Typeface english = Typeface.createFromAsset(getActivity().getAssets(), "KOMIKAX_.ttf"); // create a typeface from the raw ttf
        Typeface hebrew = Typeface.createFromAsset(getActivity().getAssets(), "OpenSansHebrew-Bold.ttf"); // create a typeface from the raw ttf
        if(Locale.getDefault().getDisplayLanguage().equals("עברית"))
        {
            addNewCarButton.setTypeface(hebrew);
            title.setTypeface(hebrew);
        }
        else
        {
            addNewCarButton.setTypeface(english);
            title.setTypeface(english);
        }


        progressBar = (ProgressBar) view.findViewById(R.id.mainProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        carCompanyInput = (EditText) view.findViewById(R.id.carCompanyInput);
        carColorInput = (EditText) view.findViewById(R.id.carColorInput);
        carLicenseInput = (EditText) view.findViewById(R.id.carLicenseInput);
        carModelInput = (EditText) view.findViewById(R.id.carModelInput);
        addNewCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //hide keyboard after click
                InputMethodManager inputManager = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if(carCompanyInput.getText().toString().matches("") || carColorInput.getText().toString().matches("") || carLicenseInput.getText().toString().matches("") || carModelInput.getText().toString().matches("")){
                    Toast.makeText(MyApplication.getAppActivity(), "You must fill all the information about the new car",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Car newCar = new Car(carLicenseInput.getText().toString(), carColorInput.getText().toString(), carModelInput.getText().toString(), carCompanyInput.getText().toString(), Model.getInstance().getCurrentUser().getEmail());
                    Model.getInstance().addCarToDB(newCar, new Model.SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {
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

                        @Override
                        public void passData(Object data) {

                        }
                    });
                }
            }
        });



        carsList= (ListView) view.findViewById(R.id.carsListView);
        Model.getInstance().getOwnedCars(Model.getInstance().getCurrentUser().getEmail(), new Model.SyncListener() {
            @Override
            public void passData(Object allCars) {
                cars = (ArrayList) allCars;
                progressBar.setVisibility(View.GONE);
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

                Object[] data = new Object[]{cars.get(position)};
                fragmentCommunicator.passData(data,"CarScreenFragment");
/*                carFragment = new CarScreenFragment();
                carFragment.sharedUsersList = cars.get(position).getUsersList();
                carFragment.carLicense = cars.get(position).getCarId();
                carFragment.car = cars.get(position);
                carFragment.modelCompany = cars.get(position).getCompany() + " " + cars.get(position).getModel();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frag_container, carFragment, "CarScreenFragment");
                fragmentTransaction.addToBackStack(null).commit();*/
            }
        });

        return view;
    }

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

            return convertView;
        }
    }

}
