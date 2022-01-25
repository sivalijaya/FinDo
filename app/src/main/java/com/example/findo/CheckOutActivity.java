package com.example.findo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.adapter.ShippingMethodAdapter;
import com.example.findo.model.Product;
import com.example.findo.model.ProductCategory;
import com.example.findo.model.Recipient;
import com.example.findo.model.ShippingMethod;
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
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckOutActivity extends AppCompatActivity implements ShippingMethodAdapter.ShippingMethodAdapterListener {


    private ArrayList<ShippingMethod> shippingMethods = new ArrayList<>();
    private ShippingMethodAdapter adapter;
    private DatabaseReference mDatabase;
    private ShippingMethod currentShippingMethod;
    private ImageView iv_product_image;
    private TextView btn_continue, tv_product_name, tv_product_price, tv_product_quantity, tv_product_quantitysummary, tv_product_pricedetail, tv_shippingmethodprice, tv_total_price, tv_error_name, tv_error_phone, tv_error_email, tv_error_address, tv_error_shipping;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private CheckBox cb_giftWrapping;
    private LinearLayout ll_isgiftwrapping, ll_shippingpricedetail;
    private EditText et_name, et_phone, et_email, et_address;
    private int quantity = 0, totalprice = 0, productPrice = 0, shippingPrice = 0;
    private boolean isGiftWrapping = false, nameCheck = false, emailCheck = false, phoneCheck = false, addressCheck = false, shippingCheck = false;
    private NestedScrollView nestedScrollView;

    private Product product;
    private ProductCategory productCategory;

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

        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        et_address = findViewById(R.id.et_address);
        tv_error_name = findViewById(R.id.tv_error_name);
        tv_error_phone = findViewById(R.id.tv_error_phone);
        tv_error_email = findViewById(R.id.tv_error_email);
        tv_error_shipping = findViewById(R.id.tv_error_shipping);
        tv_error_address = findViewById(R.id.tv_error_address);
        btn_continue = findViewById(R.id.btn_continue);

        nestedScrollView = findViewById(R.id.nestedScrollView);
        rvShippingMethod.setNestedScrollingEnabled(false);

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

        editTextCheck();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (emailCheck && nameCheck && phoneCheck && addressCheck) {
                    // add data to database and intent to next page
                    LocalDateTime nowDate = LocalDateTime.now();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");

                    Random rnd = new Random();
                    int number = rnd.nextInt(99999999);

                    String timeIssued = nowDate.toString();
                    String orderId = dtf.format(nowDate) + String.format("%08d", number);
                    Recipient recipient = new Recipient(et_address.getText().toString(), et_email.getText().toString(), et_name.getText().toString(), et_phone.getText().toString());

                    Transaction transaction = new Transaction(isGiftWrapping, orderId, productCategory, product, recipient, currentShippingMethod, 0, timeIssued, quantity, totalprice);

                    mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("transaction");

                    DatabaseReference newPostRef = mDatabase.push();

                    String test = newPostRef.getKey();
                    newPostRef.setValue(transaction);

                    Intent intent = new Intent(CheckOutActivity.this, ChoosePaymentMethodActivity.class);
                    intent.putExtra("transactionKey", test);
                    intent.putExtra("transactionTotalPrice", String.valueOf(totalprice));
                    intent.putExtra("transactionPhoneNumber", transaction.getRecipient().getPhone_number());
                    startActivity(intent);
                    finish();

                } else {
                    nestedScrollView.fullScroll(View.FOCUS_UP);
                    if (!emailCheck) {
                        if (et_email.getText().toString().isEmpty()) {
                            tv_error_email.setText("Email cannot Empty!");
                            tv_error_email.setVisibility(View.VISIBLE);
                        } else {
                            tv_error_email.setText("Please input valid Email!");
                            tv_error_email.setVisibility(View.VISIBLE);
                        }
                    }
                    if (!nameCheck) {
                        tv_error_name.setText("Name cannot Empty!");
                        tv_error_name.setVisibility(View.VISIBLE);
                    }
                    if (!phoneCheck) {
                        if (et_email.getText().toString().isEmpty()) {
                            tv_error_phone.setText("PhoneNumber cannot Empty!");
                            tv_error_phone.setVisibility(View.VISIBLE);
                        } else {
                            tv_error_phone.setText("Please input valid PhoneNumber!");
                            tv_error_phone.setVisibility(View.VISIBLE);
                        }
                    }
                    if (!addressCheck) {
                        tv_error_address.setText("Address cannot Empty!");
                        tv_error_address.setVisibility(View.VISIBLE);
                    }
                    if (!shippingCheck) {
                        tv_error_shipping.setText("Please choose Shipping Method!");
                        tv_error_shipping.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void editTextCheck() {
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_name.getText().toString().isEmpty()) {
                    tv_error_name.setText("Name cannot Empty!");
                    tv_error_name.setVisibility(View.VISIBLE);
                    nameCheck = false;
                } else {
                    tv_error_name.setVisibility(View.GONE);
                    nameCheck = true;
                }
            }
        });

        et_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_address.getText().toString().isEmpty()) {
                    tv_error_address.setText("Address cannot Empty!");
                    tv_error_address.setVisibility(View.VISIBLE);
                    addressCheck = false;
                } else {
                    tv_error_address.setVisibility(View.GONE);
                    addressCheck = true;
                }
            }
        });

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (emailValidation(et_email.getText().toString())) {
                    tv_error_email.setText("Please input valid Email!");
                    tv_error_email.setVisibility(View.VISIBLE);
                    emailCheck = false;
                } else {
                    tv_error_email.setVisibility(View.GONE);
                    emailCheck = true;
                }
            }
        });

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (phoneValidation(et_phone.getText().toString(), tv_error_phone)) {
                    tv_error_phone.setVisibility(View.VISIBLE);
                    phoneCheck = false;
                } else {
                    tv_error_phone.setVisibility(View.GONE);
                    phoneCheck = true;
                }
            }
        });
    }

    private void fetchProduct(String productId) {
        mDatabase = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("product").child(productId);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                product = new Product(snapshot);

                DatabaseReference DbRef = FirebaseDatabase.getInstance("https://findo-d605f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("product_category").child(product.getProductCategoryId().toString());
                ValueEventListener postCategoryListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot categorySnapshot) {
                        productCategory = new ProductCategory(categorySnapshot, "name");
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
                        Log.d("fdatabase", "onDataChange: " + error.getMessage());
                    }
                };
                DbRef.addValueEventListener(postCategoryListener);

                Picasso.get().load(product.getImages().get(0)).into(iv_product_image);
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


    @Override
    public void shippingMethodAdapterClick(int position) {
        currentShippingMethod = shippingMethods.get(position);
        shippingPrice = currentShippingMethod.getPrice();
        ll_shippingpricedetail.setVisibility(View.VISIBLE);
        tv_shippingmethodprice.setText(decimalFormat.format(shippingPrice));
        updateTotalPrice(productPrice, quantity, isGiftWrapping, shippingPrice);
        shippingCheck = true;
        tv_error_shipping.setVisibility(View.GONE);
        btn_continue.setBackground(ContextCompat.getDrawable(CheckOutActivity.this, R.drawable.button_rectangle));
        btn_continue.setTextColor(ContextCompat.getColor(CheckOutActivity.this, R.color.white));
        btn_continue.setClickable(true);
    }

    private void updateTotalPrice(int productPrice, int quantity, boolean isGiftWrapping, int shippingPrice) {
        totalprice = 0;
        if (isGiftWrapping) {
            totalprice += 5000;
        }
        totalprice += (productPrice * quantity) + shippingPrice;
        tv_total_price.setText(decimalFormat.format(totalprice));
    }

    private boolean emailValidation(String data) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return true;
        }
        return false;
    }

    private boolean phoneValidation(String data, TextView tv_error) {
        if (data.isEmpty()) {
            tv_error.setText("PhoneNumber cannot Empty!");
            return true;
        } else if (!data.startsWith("628")) {
            tv_error.setText("Please start with \"628\"!");
            return true;
        } else if (data.length() < 12) {
            tv_error.setText("Please input valid phoneNumber!");
            return true;
        }
        return false;
    }
}