package com.example.findo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.adapter.CategoryAdapter;
import com.example.findo.model.ProductCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements CategoryAdapter.CategoryAdapterListener {

    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    private ArrayList<ProductCategory> mproductcategories = new ArrayList<>();
    private ArrayList<ProductCategory> temp = new ArrayList<>();
    private DatabaseReference mDatabase;
    private CategoryAdapter adapter;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton btn_photo = findViewById(R.id.photo);
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                } else {
                    String[] permissions = new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    };
                    ActivityCompat.requestPermissions(HomeActivity.this, permissions, CAMERA_PERMISSION_CODE);
                }
            }
        });

        RecyclerView rvProducts = findViewById(R.id.recyclerview_home);
        adapter = new CategoryAdapter(mproductcategories, this, this);
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fetchDataFromFirebase();

        TextView btn_male = findViewById(R.id.btn_male);
        TextView btn_reset = findViewById(R.id.btn_reset);
        TextView btn_female = findViewById(R.id.btn_female);

        btn_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mproductcategories.clear();
                for (ProductCategory productCategory : temp) {
                    if (productCategory.getName().contains("Male")) {
                        mproductcategories.add(productCategory);
                    }
                }
                selectedButton(btn_male);
                unselectedButton(btn_female);
                btn_reset.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        });

        btn_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mproductcategories.clear();
                for (ProductCategory productCategory : temp) {
                    if (productCategory.getName().toLowerCase().contains("female")) {
                        mproductcategories.add(productCategory);
                    }
                }
                selectedButton(btn_female);
                unselectedButton(btn_male);
                btn_reset.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mproductcategories.clear();
                for (ProductCategory productCategory : temp) {
                    mproductcategories.add(productCategory);
                }
                unselectedButton(btn_male);
                unselectedButton(btn_female);
                btn_reset.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void selectedButton(TextView btn_text) {
        btn_text.setTextColor(ContextCompat.getColor(this, R.color.white));
        btn_text.setBackground(ContextCompat.getDrawable(this, R.drawable.button_rectangle));
    }

    private void unselectedButton(TextView btn_text) {
        btn_text.setTextColor(ContextCompat.getColor(this, R.color.yellow_main_2));
        btn_text.setBackground(ContextCompat.getDrawable(this, R.drawable.button_rectangle_white));
    }

    private void fetchDataFromFirebase() {
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("product_category");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    ProductCategory productCategory = new ProductCategory(categorySnapshot);
                    mproductcategories.add(productCategory);
                }
                temp = new ArrayList<>(mproductcategories);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("fdatabase", "onDataChange: " + error.getMessage());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            Bitmap finalPhoto = (Bitmap) bundle.get("data");
            Intent intent = new Intent(this, PhotoResultActivity.class);
            intent.putExtra("data", finalPhoto);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull @org.jetbrains.annotations.NotNull String[] permissions, @androidx.annotation.NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                } else {
                    Toast.makeText(HomeActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void categoryAdapterClick(int position) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("searchValue", mproductcategories.get(position).getName());
        intent.putExtra("searchValueCategory", mproductcategories.get(position).getId().toString());
        startActivity(intent);
    }
}