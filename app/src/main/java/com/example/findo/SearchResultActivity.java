package com.example.findo;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
    private DatabaseReference mDatabase;
    private int width = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Bundle bundle = getIntent().getExtras();

        try {
            fetchDataFromFirebase(bundle.getString("searchValue"));
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView tv_title_result = findViewById(R.id.title_result);
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
                ItemListAdapter adapter = new ItemListAdapter(products, width / 2, SearchResultActivity.this);

                rv.setAdapter(adapter);
                rv.setLayoutManager(new GridLayoutManager(SearchResultActivity.this, 2));
            }
        });
    }

    @Override
    public void itemListAdapterClick(int position) {
        //todo intent to product detail
        Log.d("test", "arListResultClick: " + position);
        Toast.makeText(SearchResultActivity.this, position + "System is busy!", Toast.LENGTH_SHORT).show();
    }

    private void fetchDataFromFirebase(String searchValue) {
        // TODO: 04-Jan-22 need change logic for search
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("product_category");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    for (DataSnapshot productSnapshot : categorySnapshot.child("product").getChildren()) {
                        if (productSnapshot.child("name").getValue().toString().toLowerCase().contains(searchValue.toLowerCase())) {
                            Product product = new Product(productSnapshot);
                            products.add(product);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(postListener);
    }
}