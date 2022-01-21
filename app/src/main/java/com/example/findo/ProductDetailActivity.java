package com.example.findo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.findo.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView btn_arvisualize, btn_buynow, tv_product_price, product_title, total_sold, tv_stock, tv_brand, tv_shippingfrom, tv_description;
    private ImageView iv_product;

    private Product product;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        btn_arvisualize = findViewById(R.id.btn_arvisualize);
        tv_product_price = findViewById(R.id.tv_product_price);
        product_title = findViewById(R.id.product_title);
        total_sold = findViewById(R.id.total_sold);
        tv_stock = findViewById(R.id.tv_stock);
        tv_brand = findViewById(R.id.tv_brand);
        tv_shippingfrom = findViewById(R.id.tv_shippingfrom);
        tv_description = findViewById(R.id.tv_description);
        iv_product = findViewById(R.id.iv_product);
        btn_arvisualize = findViewById(R.id.btn_arvisualize);
        btn_buynow = findViewById(R.id.btn_buynow);

        btn_arvisualize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductDetailActivity.this, "AR Visualize!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductDetailActivity.this, "Buy Now!", Toast.LENGTH_SHORT).show();
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProductDetailActivity.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.layout_bottomsheet,
                        findViewById(R.id.bottomSheetContainer)
                );
                TextView productPrice = bottomSheetView.findViewById(R.id.product_price);
                TextView productStock = bottomSheetView.findViewById(R.id.product_stock);
                ImageView productImage = bottomSheetView.findViewById(R.id.product_image);
                TextView btn_plus = bottomSheetView.findViewById(R.id.btn_plus);
                TextView btn_minus = bottomSheetView.findViewById(R.id.btn_minus);
                TextView btn_continue = bottomSheetView.findViewById(R.id.btn_continue);
                EditText ed_quantity = bottomSheetView.findViewById(R.id.ed_quantity);
                ImageView btn_close = bottomSheetView.findViewById(R.id.btn_close);

                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                productPrice.setText(decimalFormat.format(product.getPrice()));
                productStock.setText(product.getStock().toString());
                Picasso.get().load(product.getPhoto().get(0)).into(productImage);

                btn_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Integer.parseInt(ed_quantity.getText().toString()) >= product.getStock()) {

                        } else {
                            int count = Integer.parseInt(ed_quantity.getText().toString()) + 1;
                            ed_quantity.setText(String.valueOf(count));
                        }
                    }
                });

                btn_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Integer.parseInt(ed_quantity.getText().toString()) == 0) {

                        } else {
                            int count = Integer.parseInt(ed_quantity.getText().toString()) - 1;
                            ed_quantity.setText(String.valueOf(count));
                        }
                    }
                });

                btn_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: 22-Jan-22 intent to cashierPage
                    }
                });

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.cancel();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("product").child(bundle.getString("productId"));
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                product = new Product(snapshot);

                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                tv_product_price.setText(decimalFormat.format(product.getPrice()));
                product_title.setText(product.getName());
                total_sold.setText(product.getSold().toString());
                tv_stock.setText(product.getStock().toString());
                tv_brand.setText(product.getBrand());
                tv_shippingfrom.setText(product.getShippingFrom());
                tv_description.setText(product.getDescription());
                Picasso.get().load(product.getPhoto().get(0)).into(iv_product);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("fdatabase", "onDataChange: " + error.getMessage());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }
}