package com.example.findo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckOrderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order);

        TextView textHeader = findViewById(R.id.textHeader);
        LinearLayout btnCheck = findViewById(R.id.btnCheck);
        textHeader.setText("Order Detail");

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!getActivity().getClass().toString().equals(HomeActivity.class.toString())) {
//                    getActivity().finish();
//                }
            }
        });

    }
}