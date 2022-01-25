package com.example.findo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.findo.model.Transaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class OrderCreatedActivity extends AppCompatActivity {

    private String transactionKey;
    private DatabaseReference mDatabase;
    private Transaction transaction;
    private TextView tv_orderid, tv_total_price, tv_virtualaccount, btn_copyOId, btn_copyVA, btn_home;
    private ImageView iv_payment_method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_created);

        tv_orderid = findViewById(R.id.tv_orderid);
        tv_total_price = findViewById(R.id.tv_total_price);
        iv_payment_method = findViewById(R.id.iv_payment_method);
        tv_virtualaccount = findViewById(R.id.tv_virtualaccount);
        btn_copyOId = findViewById(R.id.btn_copyOId);
        btn_copyVA = findViewById(R.id.btn_copyVA);
        btn_home = findViewById(R.id.btn_home);

        Bundle bundle = getIntent().getExtras();
        transactionKey = bundle.getString("transactionKey");

        fetchTransaction(transactionKey);

        btn_copyVA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("VirtualAccountNumber", transaction.getVirtual_account());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(OrderCreatedActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_copyOId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("OrderId", transaction.getOrder_id());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(OrderCreatedActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderCreatedActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchTransaction(String transactionKey) {
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("transaction").child(transactionKey);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                transaction = new Transaction(snapshot);
                tv_orderid.setText(transaction.getOrder_id());
                tv_total_price.setText(decimalFormat.format(transaction.getTotal_price()));
                Picasso.get().load(transaction.getPayment_method().getImage()).into(iv_payment_method);
                tv_virtualaccount.setText(transaction.getVirtual_account());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("fdatabase", "onDataChange: " + error.getMessage());
            }
        });
    }
}