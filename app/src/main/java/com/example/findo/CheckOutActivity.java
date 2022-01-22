package com.example.findo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.adapter.ShippingMethodAdapter;
import com.example.findo.model.Product;
import com.example.findo.model.ShippingMethod;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckOutActivity extends AppCompatActivity implements ShippingMethodAdapter.ShippingMethodAdapterListener {

    private static final String regex = "^(.+)@(.+)$";
    private ArrayList<ShippingMethod> shippingMethods = new ArrayList<>();
    private ShippingMethodAdapter adapter;
    private DatabaseReference mDatabase;
    private ShippingMethod currentShippingMethod;
    private ImageView iv_product_image;
    private TextView tv_product_name, tv_product_price, tv_product_quantity, tv_product_quantitysummary, tv_product_pricedetail, tv_shippingmethodprice, tv_total_price;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private CheckBox cb_giftWrapping;
    private LinearLayout ll_isgiftwrapping, ll_shippingpricedetail;
    private int quantity = 0, totalprice = 0, productPrice = 0, shippingPrice = 0;
    private boolean isGiftWrapping = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Bundle bundle = getIntent().getExtras();
        quantity = Integer.parseInt(bundle.getString("productQuantity"));

        RecyclerView rvShippingMethod = findViewById(R.id.rv_shippingmethod);
        iv_product_image = findViewById(R.id.iv_product_image);
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_product_price = findViewById(R.id.tv_product_price);
        tv_product_quantity = findViewById(R.id.tv_product_quantity);
        tv_product_quantitysummary = findViewById(R.id.tv_product_quantitysummary);
        tv_product_pricedetail = findViewById(R.id.tv_product_pricedetail);
        tv_shippingmethodprice = findViewById(R.id.tv_shippingmethodprice);
        tv_total_price = findViewById(R.id.tv_total_price);
        cb_giftWrapping = findViewById(R.id.cb_giftWrapping);
        ll_isgiftwrapping = findViewById(R.id.ll_isgiftwrapping);
        ll_shippingpricedetail = findViewById(R.id.ll_shippingpricedetail);

        cb_giftWrapping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb_giftWrapping.isChecked()) {
                    isGiftWrapping = true;
                    updateTotalPrice(productPrice, quantity, true, shippingPrice);
                    ll_isgiftwrapping.setVisibility(View.VISIBLE);
                } else {
                    isGiftWrapping = false;
                    updateTotalPrice(productPrice, quantity, false, shippingPrice);
                    ll_isgiftwrapping.setVisibility(View.GONE);
                }
            }
        });

        adapter = new ShippingMethodAdapter(shippingMethods, this);
        rvShippingMethod.setAdapter(adapter);
        rvShippingMethod.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fetchShippingMethod();
        fetchProduct(bundle.getString("productId"));

        // TODO: 23-Jan-22 add validation for personal information, create database, continue button 
    }

    private void fetchProduct(String productId) {
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("product").child(productId);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Product product = new Product(snapshot);
                Picasso.get().load(product.getPhoto().get(0)).into(iv_product_image);
                tv_product_name.setText(product.getName());
                productPrice = product.getPrice();
                tv_product_price.setText(decimalFormat.format(product.getPrice()));
                tv_product_quantity.setText(String.valueOf(quantity));
                tv_product_quantitysummary.setText("x" + quantity);
                tv_product_pricedetail.setText(decimalFormat.format(product.getPrice() * quantity));
                updateTotalPrice(productPrice, quantity, isGiftWrapping, shippingPrice);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("fdatabase", "onDataChange: " + error.getMessage());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    private void fetchShippingMethod() {
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("shipping_method");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot shippingMethodSnapshot : snapshot.getChildren()) {
                    ShippingMethod shippingMethod = new ShippingMethod(shippingMethodSnapshot);
                    shippingMethods.add(shippingMethod);
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

    private void emailValidation() {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("text");
        System.out.println("Email " + "text" + " is " + (matcher.matches() ? "valid" : "invalid"));
    }

    @Override
    public void shippingMethodAdapterClick(int position) {
        currentShippingMethod = shippingMethods.get(position);
        shippingPrice = currentShippingMethod.getPrice();
        ll_shippingpricedetail.setVisibility(View.VISIBLE);
        tv_shippingmethodprice.setText(decimalFormat.format(shippingPrice));
        updateTotalPrice(productPrice, quantity, isGiftWrapping, shippingPrice);
    }

    private void updateTotalPrice(int productPrice, int quantity, boolean isGiftWrapping, int shippingPrice) {
        totalprice = 0;
        if (isGiftWrapping) {
            totalprice += 5000;
        }
        totalprice += (productPrice * quantity) + shippingPrice;
        tv_total_price.setText(decimalFormat.format(totalprice));
    }
}