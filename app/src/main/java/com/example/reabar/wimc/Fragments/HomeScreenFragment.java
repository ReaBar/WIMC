package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.reabar.wimc.FragmentCommunicator;
import com.example.reabar.wimc.Model.Car;
import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.R;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenFragment extends Fragment {

    ProgressBar progressBar;
    FragmentCommunicator fragmentCommunicator;
    FragmentTransaction fragmentTransaction;
    CarsNotParkingAdapter adapter;
    ListView carsList;
    List<Car> cars;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCommunicator = (FragmentCommunicator) getActivity();
        if (Model.getInstance().getCurrentUser() == null) {
            fragmentCommunicator.passString("cancelDrawer");
            fragmentCommunicator.passString("LoginScreenFragment");
        }
        fragmentCommunicator.passString("enableDrawer");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        TextView user = (TextView) view.findViewById(R.id.loggedinUser);
        user.setText("Hello " + Model.getInstance().getCurrentUser().getEmail());

        progressBar = (ProgressBar) view.findViewById(R.id.homepageProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        cars =  Model.getInstance().getAllCars();

        if(Model.getInstance().getCurrentUser() != null) {
            Model.getInstance().getMyUnparkedCars(Model.getInstance().getCurrentUser().getEmail(), new Model.SyncListener() {
                @Override
                public void PassData(Object allCars) {
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
        }

        carsList = (ListView) view.findViewById(R.id.listCarsNotParkingNow);
        adapter = new CarsNotParkingAdapter();
        carsList.setAdapter(adapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
    }





    public class CarsNotParkingAdapter extends BaseAdapter {

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
                convertView= layoutInflater.inflate(R.layout.fragment_home_screen_row,null);
            }
            else{
                Log.d("TAG", "use convert view:" + position);
            }

            TextView carLicense = (TextView) convertView.findViewById(R.id.home_car_license);
            TextView carModelCompany = (TextView) convertView.findViewById(R.id.home_car_modelCompany);

            Car car = cars.get(position);
            carLicense.setText(car.getCarId());
            carModelCompany.setText(car.getCompany() + " " + car.getModel());

            return convertView;
        }
    }

}
