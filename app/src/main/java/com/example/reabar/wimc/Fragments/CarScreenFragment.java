package com.example.reabar.wimc.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.example.reabar.wimc.Model.Car;
import com.example.reabar.wimc.MyApplication;
import com.example.reabar.wimc.R;

import java.util.ArrayList;
import java.util.List;

public class CarScreenFragment extends Fragment {
    Car car;
    List<String> users;
    MyUsersCarAdapter adapter;
    ListView list;
    String carLicense;
    String modelCompany;
    EditText emailSharedinput;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_screen, container, false);

        if(users == null) {
            users = new ArrayList<>();
        }

        if(users.isEmpty()){
            TextView text = (TextView) view.findViewById(R.id.deleteTextView);
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
                car.removeCarUser(users.get(position));
                adapter.notifyDataSetChanged();
                Toast.makeText(MyApplication.getAppActivity(), "User removed successfully",
                        Toast.LENGTH_SHORT).show();
            }
        });


        emailSharedinput = (EditText) view.findViewById(R.id.emailSharedinput);
        Button addUserButton = (Button) view.findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailSharedinput.getText().toString().matches("")) {
                    Toast.makeText(MyApplication.getAppActivity(), "You must enter email of the shared user",
                            Toast.LENGTH_SHORT).show();
                } else {

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
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return users.get(position);
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
            String user = users.get(position);
            username.setText(user);

            return convertView;
        }
    }
}
