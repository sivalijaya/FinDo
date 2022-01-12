package com.example.findo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

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
    private DatabaseReference mDatabase;
    CategoryAdapter adapter;

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
    }

    private void fetchDataFromFirebase() {
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("product_category");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    ProductCategory productCategory = new ProductCategory(categorySnapshot);
                    mproductcategories.add(productCategory);
                }
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
    public void categoryAdapterClick(int position) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("searchValue", mproductcategories.get(position).getName());
        intent.putExtra("searchValueCategory", mproductcategories.get(position).getId().toString());
        startActivity(intent);
    }
}