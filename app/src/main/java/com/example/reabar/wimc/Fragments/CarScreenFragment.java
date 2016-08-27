package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.reabar.wimc.FragmentCommunicator;
import com.example.reabar.wimc.Model.Car;
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CarScreenFragment extends Fragment {
    public Car car;
    protected List<String> sharedUsersList = new ArrayList<>();
    protected MyUsersCarAdapter adapter;
    protected ListView list;
    protected String carLicense;
    protected String modelCompany;
    protected EditText emailSharedInput;
    FragmentCommunicator fragmentCommunicator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCommunicator = (FragmentCommunicator) getActivity();
        sharedUsersList = car.getUsersList();
        carLicense = car.getCarId();
        modelCompany = car.getCompany() + " " + car.getModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_screen, container, false);

        TextView text = (TextView) view.findViewById(R.id.deleteTextView);
        Button addUserButton = (Button) view.findViewById(R.id.addUserButton);

        Typeface english = Typeface.createFromAsset(getActivity().getAssets(), "KOMIKAX_.ttf"); // create a typeface from the raw ttf
        Typeface hebrew = Typeface.createFromAsset(getActivity().getAssets(), "OpenSansHebrew-Bold.ttf"); // create a typeface from the raw ttf
        if(Locale.getDefault().getDisplayLanguage().equals("עברית"))
        {
            addUserButton.setTypeface(hebrew);
            text.setTypeface(hebrew);
        }
        else
        {
            addUserButton.setTypeface(english);
            text.setTypeface(english);
        }

        if(sharedUsersList == null) {
            sharedUsersList = new ArrayList<>();
        }

        if(sharedUsersList.isEmpty()){
            text.setVisibility(View.GONE);
        }

        TextView licenseTextView = (TextView) view.findViewById(R.id.cars_list_row_car_license);
        licenseTextView.setText(carLicense);
        TextView modelCompanyTextView = (TextView) view.findViewById(R.id.cars_list_row_car_model_company);
        modelCompanyTextView.setText(modelCompany);

        list = (ListView) view.findViewById(R.id.listSharedUsersID);
        adapter = new MyUsersCarAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                car.removeCarUser(sharedUsersList.get(position));
                adapter.notifyDataSetChanged();
                Toast.makeText(MyApplication.getAppActivity(), "User removed successfully",
                        Toast.LENGTH_SHORT).show();
            }
        });


        emailSharedInput = (EditText) view.findViewById(R.id.emailSharedinput);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //hide keyboard after click
                InputMethodManager inputManager = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if (emailSharedInput.getText().toString().matches("")) {
                    Toast.makeText(MyApplication.getAppActivity(), "You must enter email of the shared user",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //addUser user to car by email
                    final String userEmail = emailSharedInput.getText().toString();
                    car.setNewCarUser(userEmail, true);
                    emailSharedInput.setText("");
                    adapter.notifyDataSetChanged();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //sharedUsersList.add(userEmail);
                    Object[] data = new Object[]{car};
                    fragmentCommunicator.passData(data,"CarScreenFragment");
                }
            }
        });

        return  view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class MyUsersCarAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sharedUsersList.size();
        }

        @Override
        public Object getItem(int position) {
            return sharedUsersList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                convertView= layoutInflater.inflate(R.layout.fragment_car_screen_row,null);

            }
            else{
                Log.d("TAG", "use convert view:" + position);
            }

            TextView username = (TextView) convertView.findViewById(R.id.username);
            String user = sharedUsersList.get(position);
            username.setText(user);

            return convertView;
        }
    }
}
