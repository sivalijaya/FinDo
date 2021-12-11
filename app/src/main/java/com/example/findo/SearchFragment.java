package com.example.findo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_search, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        RelativeLayout btn_check_order = (RelativeLayout) view.findViewById(R.id.checkOrder);
        btn_check_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo change intent to check order
                Intent intent = new Intent(getActivity(), SplashActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btn_home = (ImageButton) view.findViewById(R.id.findo_logo);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getActivity().getClass().toString().equals(HomeActivity.class.toString())){
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        //listener when editText clicked search/enter
    }
}