package com.example.findo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.adapter.PaymentMethodAdapter;
import com.example.findo.model.PaymentMethod;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChoosePaymentMethodActivity extends AppCompatActivity implements PaymentMethodAdapter.PaymentMethodAdapterListener {

    private TextView btn_checkout, tv_total_price;
    private RecyclerView rv_paymentmethod;

    private DatabaseReference mDatabase;
    private PaymentMethodAdapter adapter;
    private ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
    private PaymentMethod currentPaymentMethod = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment_method);

        Bundle bundle = getIntent().getExtras();
        String transactionKey = bundle.getString("transactionKey");
        String transactionTotalPrice = bundle.getString("transactionTotalPrice");
        String transactionPhoneNumber = bundle.getString("transactionPhoneNumber");

        btn_checkout = findViewById(R.id.btn_checkout);
        tv_total_price = findViewById(R.id.tv_total_price);
        rv_paymentmethod = findViewById(R.id.rv_paymentmethod);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tv_total_price.setText(decimalFormat.format(transactionTotalPrice));

        adapter = new PaymentMethodAdapter(paymentMethods, this);
        rv_paymentmethod.setAdapter(adapter);
        rv_paymentmethod.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fetchPaymentMethod();

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPaymentMethod != null) {
                    mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("transaction").child(transactionKey);
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {

                            Map<String, Object> transactionUpdate = new HashMap<>();
                            Map<String, Object> paymentMethod = new HashMap<>();
                            paymentMethod.put("id", currentPaymentMethod.getId());
                            paymentMethod.put("name", currentPaymentMethod.getName());
                            paymentMethod.put("image", currentPaymentMethod.getImage());

                            transactionUpdate.put("payment_method", paymentMethod);
                            transactionUpdate.put("virtual_account", generateVirtualAccount(transactionPhoneNumber));

                            mDatabase.updateChildren(transactionUpdate);

                            // TODO: 25-Jan-22 intent to transaction successpage
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void fetchPaymentMethod() {
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("payment_method");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot paymentMethodSnapshot : snapshot.getChildren()) {
                    PaymentMethod paymentMethod = new PaymentMethod(paymentMethodSnapshot, "name");
                    paymentMethods.add(paymentMethod);
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

    private String generateVirtualAccount(String transactionPhoneNumber) {
        if (currentPaymentMethod.getName().toLowerCase().equals("bca")) {
            return "9088" + transactionPhoneNumber;
        } else if (currentPaymentMethod.getName().toLowerCase().equals("mandiri")) {
            return "9012" + transactionPhoneNumber;
        } else if (currentPaymentMethod.getName().toLowerCase().equals("bri")) {
            return "9062" + transactionPhoneNumber;
        } else {
            return "9099" + transactionPhoneNumber;
        }
    }

    @Override
    public void paymentMethodAdapterClick(int position) {
        currentPaymentMethod = paymentMethods.get(position);
        btn_checkout.setBackground(ContextCompat.getDrawable(ChoosePaymentMethodActivity.this, R.drawable.button_rectangle));
        btn_checkout.setTextColor(ContextCompat.getColor(ChoosePaymentMethodActivity.this, R.color.white));
    }
}