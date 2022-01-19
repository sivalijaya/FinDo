package com.example.findo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.findo.model.Transaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CheckOrderActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Transaction transaction;
    private TextView tv_recipientname, tv_recipientemail, tv_recipientphone, tv_recipientaddress, tv_time_issued, tv_shippingmethodprice, tv_pricegiftwrapping;
    private TextView tv_product_name, tv_product_price, tv_product_quantity, total_price, tv_product_pricedetail;
    private TextView orderCreated, paymentAccepted, orderOnTheWay, delivered;
    private ImageView iv_payment_method, iv_product_image, iv_shippingmethod;
    private CheckBox giftWrapping;
    private LinearLayout shipping_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order);

        TextView textHeader = findViewById(R.id.textHeader);
        textHeader.setText("Order Detail");

        TextView btnCheck = findViewById(R.id.btnCheck);
        EditText etSearch = findViewById(R.id.et_search);
        tv_recipientname = findViewById(R.id.tv_recipientname);
        tv_recipientemail = findViewById(R.id.tv_recipientemail);
        tv_recipientphone = findViewById(R.id.tv_recipientphone);
        tv_recipientaddress = findViewById(R.id.tv_recipientaddress);
        tv_time_issued = findViewById(R.id.tv_time_issued);
        tv_shippingmethodprice = findViewById(R.id.tv_shippingmethodprice);
        iv_shippingmethod = findViewById(R.id.iv_shippingmethod);
        tv_pricegiftwrapping = findViewById(R.id.tv_pricegiftwrapping);
        tv_product_pricedetail = findViewById(R.id.tv_product_pricedetail);

        tv_product_name = findViewById(R.id.tv_product_name);
        tv_product_price = findViewById(R.id.tv_product_price);
        tv_product_quantity = findViewById(R.id.tv_product_quantity);
        iv_product_image = findViewById(R.id.iv_product_image);
        total_price = findViewById(R.id.total_price);
        giftWrapping = findViewById(R.id.giftWrapping);
        iv_payment_method = findViewById(R.id.iv_payment_method);

        orderCreated = findViewById(R.id.orderCreated);
        paymentAccepted = findViewById(R.id.paymentAccepted);
        orderOnTheWay = findViewById(R.id.orderOnTheWay);
        delivered = findViewById(R.id.delivered);

        shipping_info = findViewById(R.id.shipping_info);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etSearch.getText().toString().length() >= 16) {
                    btnCheck.setTextColor(ContextCompat.getColor(CheckOrderActivity.this, R.color.white));
                    btnCheck.setBackground(ContextCompat.getDrawable(CheckOrderActivity.this, R.drawable.button_rectangle));
                    btnCheck.setClickable(true);
                } else {
                    btnCheck.setTextColor(ContextCompat.getColor(CheckOrderActivity.this, R.color.yellow_main_2));
                    btnCheck.setBackground(ContextCompat.getDrawable(CheckOrderActivity.this, R.drawable.button_rectangle_white));
                    btnCheck.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etSearch.getText().toString().isEmpty()) {
                    btnCheck.setClickable(false);
                } else {
                    etSearch.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    fetchDataFromFirebase(etSearch.getText().toString());
                }
            }
        });

    }

    private void fetchDataFromFirebase(String orderId) {
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("transaction");
        ValueEventListener postListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int isfound = -1;
                shipping_info.setVisibility(View.GONE);
                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    if (transactionSnapshot.child("order_id").getValue().toString().equals(orderId)) {
                        transaction = new Transaction(transactionSnapshot);
                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                        tv_recipientname.setText(transaction.getRecipient().getName());
                        tv_recipientemail.setText(transaction.getRecipient().getEmail());
                        tv_recipientaddress.setText(transaction.getRecipient().getAddress());
                        tv_recipientphone.setText(transaction.getRecipient().getPhone_number());
                        tv_product_quantity.setText("x" + transaction.getQuantity().toString());
                        tv_shippingmethodprice.setText(decimalFormat.format(transaction.getShipping_method().getPrice()));
                        Picasso.get().load(transaction.getShipping_method().getImage()).into(iv_shippingmethod);


                        LocalDateTime myDateObj = LocalDateTime.parse(transaction.getTime_issued());
                        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        String formattedDate = myDateObj.format(myFormatObj);
                        tv_time_issued.setText(formattedDate);


                        tv_product_name.setText(transaction.getProduct().getName());
                        tv_product_price.setText(decimalFormat.format(transaction.getProduct().getPrice()));
                        tv_product_pricedetail.setText(decimalFormat.format(transaction.getProduct().getPrice()));
                        Picasso.get().load(transaction.getProduct().getPhoto().get(0)).into(iv_product_image);
                        int totalPrice = (transaction.getProduct().getPrice() * transaction.getQuantity()) + transaction.getShipping_method().getPrice();

                        Picasso.get().load(transaction.getPayment_method().getImage()).into(iv_payment_method);

                        //checkbox gift_wrapping
                        if (transaction.getGift_wrapping() == true) {
                            giftWrapping.setChecked(true);
                            tv_pricegiftwrapping.setText(decimalFormat.format(5000));
                            totalPrice += 5000;
                        } else {
                            tv_pricegiftwrapping.setText("-");
                            giftWrapping.setChecked(false);
                        }
                        total_price.setText(decimalFormat.format(totalPrice));

                        //set status
                        if (transaction.getStatus() == 0) {
                            orderCreated.setTextColor(ContextCompat.getColor(CheckOrderActivity.this, R.color.yellow_main_2));
                        } else if (transaction.getStatus() == 1) {
                            orderCreated.setTextColor(ContextCompat.getColor(CheckOrderActivity.this, R.color.yellow_main_2));
                            paymentAccepted.setTextColor(ContextCompat.getColor(CheckOrderActivity.this, R.color.yellow_main_2));
                        } else if (transaction.getStatus() == 2) {
                            orderCreated.setTextColor(ContextCompat.getColor(CheckOrderActivity.this, R.color.yellow_main_2));
                            paymentAccepted.setTextColor(ContextCompat.getColor(CheckOrderActivity.this, R.color.yellow_main_2));
                            orderOnTheWay.setTextColor(ContextCompat.getColor(CheckOrderActivity.this, R.color.yellow_main_2));
                        } else if (transaction.getStatus() == 3) {
                            orderCreated.setTextColor(ContextCompat.getColor(CheckOrderActivity.this, R.color.yellow_main_2));
                            paymentAccepted.setTextColor(ContextCompat.getColor(CheckOrderActivity.this, R.color.yellow_main_2));
                            orderOnTheWay.setTextColor(ContextCompat.getColor(CheckOrderActivity.this, R.color.yellow_main_2));
                            delivered.setTextColor(ContextCompat.getColor(CheckOrderActivity.this, R.color.yellow_main_2));
                        }

                        //flag for check
                        isfound = 1;
                        shipping_info.setVisibility(View.VISIBLE);
                    }
                }
                if (isfound == -1) {
                    Toast.makeText(CheckOrderActivity.this, "Your Transaction Not Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("fdatabase", "onDataChange: " + error.getMessage());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }
}