package com.example.findo;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.adapter.ItemListAdapter;
import com.example.findo.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class SearchResultActivity extends AppCompatActivity implements ItemListAdapter.ItemListAdapterListener {

    private ArrayList<Product> products = new ArrayList<>();
    private DatabaseReference mDatabase;
    private int width = 0;
    private ItemListAdapter adapter;
    private LinearLayout empty_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Bundle bundle = getIntent().getExtras();

        TextView tv_title_result = findViewById(R.id.title_result);
        TextView btn_popular = findViewById(R.id.btn_popular);
        LinearLayout btn_pricedown = findViewById(R.id.btn_pricedown);
        LinearLayout btn_priceup = findViewById(R.id.btn_priceup);
        ImageView btn_pricedown_imageview = findViewById(R.id.btn_pricedown_imageview);
        ImageView btn_priceup_imageview = findViewById(R.id.btn_priceup_imageview);
        TextView btn_pricedown_text = findViewById(R.id.btn_pricedown_text);
        TextView btn_priceup_text = findViewById(R.id.btn_priceup_text);
        empty_layout = findViewById(R.id.empty_layout);

        tv_title_result.setText(bundle.getString("searchValue"));

        RecyclerView rv = findViewById(R.id.rv_searchResult);
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
                adapter = new ItemListAdapter(products, width / 2, SearchResultActivity.this);

                rv.setAdapter(adapter);
                rv.setLayoutManager(new GridLayoutManager(SearchResultActivity.this, 2));

                if (!bundle.getString("searchValueCategory").isEmpty()) {
                    fetchDataFromFirebaseByCategory(bundle.getString("searchValueCategory"));
                } else {
                    fetchDataFromFirebase(bundle.getString("searchValue"));
                }
            }
        });

        btn_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //resetButtonLayout
                unselectedButtonWithImageView(btn_pricedown, btn_pricedown_text, btn_pricedown_imageview);
                unselectedButtonWithImageView(btn_priceup, btn_priceup_text, btn_priceup_imageview);

                //selectedButtonLayout
                selectedButton(btn_popular);

                Collections.sort(products, Product.COMPARATORSOLDDESCENDING);
                adapter.notifyDataSetChanged();
            }
        });

        btn_pricedown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //resetButtonLayout
                unselectedButton(btn_popular);
                unselectedButtonWithImageView(btn_priceup, btn_priceup_text, btn_priceup_imageview);

                //selectedButtonLayout
                selectedButtonWithImageView(btn_pricedown, btn_pricedown_text, btn_pricedown_imageview);

                Collections.sort(products, Product.COMPARATORPRICEASCENDING);
                adapter.notifyDataSetChanged();
            }
        });

        btn_priceup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //resetButtonLayout
                unselectedButton(btn_popular);
                unselectedButtonWithImageView(btn_pricedown, btn_pricedown_text, btn_pricedown_imageview);

                //selectedButtonLayout
                selectedButtonWithImageView(btn_priceup, btn_priceup_text, btn_priceup_imageview);

                Collections.sort(products, Product.COMPARATORPRICEDESCENDING);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void selectedButtonWithImageView(LinearLayout btn_linearlayout, TextView btn_text, ImageView btn_imageview) {
        btn_linearlayout.setBackground(ContextCompat.getDrawable(this, R.drawable.button_rectangle));
        btn_text.setTextColor(ContextCompat.getColor(this, R.color.white));
        btn_imageview.setColorFilter(ContextCompat.getColor(this, R.color.white));
    }

    private void unselectedButtonWithImageView(LinearLayout btn_linearlayout, TextView btn_text, ImageView btn_imageview) {
        btn_linearlayout.setBackground(ContextCompat.getDrawable(this, R.drawable.button_rectangle_white));
        btn_text.setTextColor(ContextCompat.getColor(this, R.color.yellow_main_2));
        btn_imageview.setColorFilter(ContextCompat.getColor(this, R.color.yellow_main_2));
    }

    private void selectedButton(TextView btn_text) {
        btn_text.setTextColor(ContextCompat.getColor(this, R.color.white));
        btn_text.setBackground(ContextCompat.getDrawable(this, R.drawable.button_rectangle));
    }

    private void unselectedButton(TextView btn_text) {
        btn_text.setTextColor(ContextCompat.getColor(this, R.color.yellow_main_2));
        btn_text.setBackground(ContextCompat.getDrawable(this, R.drawable.button_rectangle_white));
    }

    @Override
    public void itemListAdapterClick(int position) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("productId", products.get(position).getId().toString());
        this.startActivity(intent);
    }

    @Override
    public void itemListAdapterClickFromParent(int parentPosition, int position) {
    }

    private void fetchDataFromFirebase(String searchValue) {
        products.clear();
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("product");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    if (productSnapshot.child("name").getValue().toString().toLowerCase().contains(searchValue.toLowerCase())) {
                        Product product = new Product(productSnapshot);
                        products.add(product);
                    }
                }
                if (!products.isEmpty()) {
                    empty_layout.setVisibility(View.GONE);
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

    private void fetchDataFromFirebaseByCategory(String productCategoryId) {
        // TODO: 04-Jan-22 need change logic for search
        products.clear();
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("product");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    if (Integer.parseInt(productSnapshot.child("product_category_id").getValue().toString()) == Integer.parseInt(productCategoryId)) {
                        Product product = new Product(productSnapshot);
                        products.add(product);
                    }
                }
                if (!products.isEmpty()) {
                    empty_layout.setVisibility(View.GONE);
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
}