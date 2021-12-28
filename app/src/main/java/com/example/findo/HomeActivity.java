package com.example.findo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.adapter.CategoryAdapter;
import com.example.findo.adapter.ItemListAdapter;
import com.example.findo.model.Product;
import com.example.findo.model.ProductCategory;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements CategoryAdapter.CategoryAdapterListener {

    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    ImageView iv_photo;

    private ArrayList<Product> mproducts = new ArrayList<>();
    private ArrayList<ProductCategory> mproductcategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //just testing bitmap
        ImageButton btn_photo = findViewById(R.id.photo);
        iv_photo = findViewById(R.id.testphoto);

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

        //dummy data
        mproducts.clear();
        mproducts.add(new Product(1, 1, "Sepatu", 2, 2, 2, "Adidas", "Jakarta", "hallo hallo", new String[]{"https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg", "https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg"}));
        mproducts.add(new Product(2, 1, "Sepatu aDIDAS", 2, 2, 2, "Adidas", "Jakarta", "hallo hallo", new String[]{"https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg", "https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg"}));
        mproducts.add(new Product(3, 1, "Sepatu aDIDASs", 2, 2, 2, "Adidas", "Jakarta", "hallo hallo", new String[]{"https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg", "https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg"}));
        mproducts.add(new Product(4, 1, "Sepatu aDIDASss", 2, 2, 2, "Adidas", "Jakarta", "hallo hallo", new String[]{"https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg", "https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg"}));
        mproducts.add(new Product(5, 1, "Sepatu aDIDASsss", 2, 2, 2, "Adidas", "Jakarta", "hallo hallo", new String[]{"https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg", "https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg"}));

        mproductcategories.add(new ProductCategory(1, "shoe", mproducts));
        mproductcategories.add(new ProductCategory(2, "bag", mproducts));
        mproductcategories.add(new ProductCategory(3, "electronic", mproducts));

//        ItemListAdapter adapter = new ItemListAdapter(mproducts);
        CategoryAdapter adapter = new CategoryAdapter(mproductcategories, this, this);
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            Bitmap finalPhoto = (Bitmap) bundle.get("data");
            Intent intent = new Intent(this, ArResultActivity.class);
            intent.putExtra("data", finalPhoto);
            startActivity(intent);
        }
    }

    @Override
    public void categoryAdapterClick(int position) {
        Log.d("test", "arListResultClick: " + position);
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("searchValue", mproductcategories.get(position).getName());
        startActivity(intent);
    }
}