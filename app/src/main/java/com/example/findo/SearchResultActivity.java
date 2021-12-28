package com.example.findo;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.adapter.ItemListAdapter;
import com.example.findo.model.Product;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity implements ItemListAdapter.ItemListAdapterListener {

    private ArrayList<Product> products = new ArrayList<>();
    RecyclerView rv;
    int width = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Bundle bundle = getIntent().getExtras();
        TextView tv_title_result = findViewById(R.id.title_result);

        tv_title_result.setText(bundle.getString("searchValue"));

        rv = findViewById(R.id.rv_searchResult);

        products.add(new Product(1, 1, "Sepatu", 2, 2, 2, "Adidas", "Jakarta", "hallo hallo", new String[]{"https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg", "https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg"}));
        products.add(new Product(2, 1, "SepatuAAA", 2, 2, 2, "Adidas", "Jakarta", "hallo hallo", new String[]{"https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg", "https://static.republika.co.id/uploads/images/inpicture_slide/google-_150902081143-333.jpg"}));


        RelativeLayout rl = findViewById(R.id.search_activity_container);
        rl.post(new Runnable() {
            @Override
            public void run() {
                float dip = 26f;
                Resources r = getResources();
                float px = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dip,
                        r.getDisplayMetrics()
                );
                width = (int) (rl.getMeasuredWidth() - px);
                ItemListAdapter adapter = new ItemListAdapter(products, width / 2, SearchResultActivity.this);

                rv.setAdapter(adapter);
                rv.setLayoutManager(new GridLayoutManager(SearchResultActivity.this, 2));
            }
        });


//        rvLeft.setLayoutManager(new LinearLayoutManager(this));
//        rvRight.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void itemListAdapterClick(int position) {
        //todo intent to product detail
        Log.d("test", "arListResultClick: " + position);
        Toast.makeText(SearchResultActivity.this, position + "System is busy!", Toast.LENGTH_SHORT).show();
    }
}