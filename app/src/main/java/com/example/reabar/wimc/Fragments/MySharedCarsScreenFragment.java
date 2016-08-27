package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reabar.wimc.Model.Car;
import com.example.reabar.wimc.Model.Model;
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MySharedCarsScreenFragment extends Fragment {

    ProgressBar progressBar;
    MySharedCarsAdapter adapter;
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
        View view = inflater.inflate(R.layout.fragment_my_shared_cars_screen, container, false);

        TextView title = (TextView) view.findViewById(R.id.logoText);
        TextView text = (TextView) view.findViewById(R.id.textTextView);
        Typeface english = Typeface.createFromAsset(getActivity().getAssets(), "KOMIKAX_.ttf"); // create a typeface from the raw ttf
        Typeface hebrew = Typeface.createFromAsset(getActivity().getAssets(), "OpenSansHebrew-Bold.ttf"); // create a typeface from the raw ttf

        if(Locale.getDefault().getDisplayLanguage().equals("עברית"))
        {
            title.setTypeface(hebrew);
            text.setTypeface(hebrew);
        }
        else
        {
            title.setTypeface(english);
            text.setTypeface(english);
        }


        if(cars == null) {
            cars = new ArrayList<>();
        }

        progressBar = (ProgressBar) view.findViewById(R.id.mySharedCars_ProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        carsList= (ListView) view.findViewById(R.id.listShredCars);
        Model.getInstance().getListOfSharedCars(Model.getInstance().getCurrentUser().getEmail(), new Model.SyncListener() {
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

        adapter = new MySharedCarsAdapter();
        carsList.setAdapter(adapter);
        carsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MyApplication.getAppActivity(), "Row Clicked!",
                        Toast.LENGTH_SHORT).show();

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



    public class MySharedCarsAdapter extends BaseAdapter {

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
                convertView= layoutInflater.inflate(R.layout.fragment_my_shared_cars_screen_row,null);

            }
            else{
                Log.d("TAG", "use convert view:" + position);
            }

            TextView carLicense = (TextView) convertView.findViewById(R.id.mySharedCars_model_company);
            TextView carModelCompany = (TextView) convertView.findViewById(R.id.mySharedCars_car_license);
            Car car = cars.get(position);
            carLicense.setText(car.getCarId());
            carModelCompany.setText(car.getCompany() + " " + car.getModel());
            return convertView;
        }
    }

}
