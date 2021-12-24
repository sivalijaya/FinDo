package com.example.findo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.findo.adapter.ItemListAdapter;
import com.example.findo.model.Product;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    private ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Bundle bundle = getIntent().getExtras();
        TextView tv_title_result = findViewById(R.id.title_result);

        tv_title_result.setText(bundle.getString("searchValue"));

        RecyclerView rv = findViewById(R.id.recyclerviewtesti33);

        products.add(new Product(1, 1, "Sepatu", 2, 2, 2, "Adidas", "Jakarta", "hallo hallo", new String[]{"https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg", "https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg"}));
        products.add(new Product(2, 1, "SepatuAAA", 2, 2, 2, "Adidas", "Jakarta", "hallo hallo", new String[]{"https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg", "https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg"}));

        ItemListAdapter adapter = new ItemListAdapter(products);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(this,2));

//        rvLeft.setLayoutManager(new LinearLayoutManager(this));
//        rvRight.setLayoutManager(new LinearLayoutManager(this));

    }
}