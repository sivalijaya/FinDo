package com.example.findo;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class SearchResultActivity extends AppCompatActivity implements ItemListAdapter.ItemListAdapterListener {

    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Product> temp = new ArrayList<>();
    private DatabaseReference mDatabase;
    private int width = 0;
    private ItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Bundle bundle = getIntent().getExtras();

        TextView tv_title_result = findViewById(R.id.title_result);
        TextView btn_popular = findViewById(R.id.btn_popular);
        LinearLayout btn_pricedown = findViewById(R.id.btn_pricedown);
        LinearLayout btn_priceup = findViewById(R.id.btn_priceup);


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
    }

    @Override
    public void itemListAdapterClick(int position) {
        //todo intent to product detail
        Log.d("test", "arListResultClick: " + position);
        Toast.makeText(SearchResultActivity.this, products.get(position).getId() + "System is busy!", Toast.LENGTH_SHORT).show();
    }

    private void fetchDataFromFirebase(String searchValue) {
        products.clear();
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("product");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    if (productSnapshot.child("name").getValue().toString().toLowerCase().contains(searchValue.toLowerCase())) {
                        Product product = new Product(productSnapshot);
                        products.add(product);
                    }
                }
                temp = new ArrayList<>(products);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    private void fetchDataFromFirebaseByCategory(String productCategoryId) {
        // TODO: 04-Jan-22 need change logic for search
        products.clear();
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("product");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    if (Integer.parseInt(productSnapshot.child("product_category_id").getValue().toString()) == Integer.parseInt(productCategoryId)) {
                        Product product = new Product(productSnapshot);
                        products.add(product);
                    }
                }
                temp = new ArrayList<>(products);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(postListener);
    }
}