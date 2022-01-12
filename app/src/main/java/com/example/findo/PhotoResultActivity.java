package com.example.findo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.adapter.ArResultAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.util.ArrayList;
import java.util.List;

public class PhotoResultActivity extends AppCompatActivity implements ArResultAdapter.ArListResultListener {

    List<FirebaseVisionImageLabel> testi = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_result);

        TextView textHeader = findViewById(R.id.textHeader);
        LinearLayout btnCheck = findViewById(R.id.btnCheck);
        textHeader.setText("Photo Result");

        configureAndRunImageLabeler();
    }

    private void configureAndRunImageLabeler() {
//        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getCloudImageLabeler();

        RecyclerView rvArResult = findViewById(R.id.rv_ar_result);
        ImageView ivCameraResult = findViewById(R.id.camera_result);

        Bundle bundle = getIntent().getExtras();
        Bitmap finalPhoto = (Bitmap) bundle.get("data");
//        InputImage image = InputImage.fromBitmap(finalPhoto, 0);

        ivCameraResult.setImageBitmap(finalPhoto);

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(finalPhoto);
        labeler.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        // Task completed successfully
                        testi = labels;
                        ArResultAdapter arResultAdapter = new ArResultAdapter(testi, PhotoResultActivity.this);
                        rvArResult.setAdapter(arResultAdapter);
                        rvArResult.setLayoutManager(new LinearLayoutManager(PhotoResultActivity.this));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        Toast.makeText(PhotoResultActivity.this, "System is busy!", Toast.LENGTH_SHORT).show();
                    }
                });

//        labeler.process(image).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
//            @Override
//            public void onSuccess(@NonNull @NotNull List<ImageLabel> imageLabels) {
//                testi = imageLabels;
//
//                ArResultAdapter arResultAdapter = new ArResultAdapter(testi, ArResultActivity.this);
//                rvArResult.setAdapter(arResultAdapter);
//                rvArResult.setLayoutManager(new LinearLayoutManager(ArResultActivity.this));
//
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        Toast.makeText(ArResultActivity.this, "System is busy!", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    @Override
    public void arListResultClick(int position) {
        Log.d("test", "arListResultClick: " + position);
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("searchValue", testi.get(position).getText());
        intent.putExtra("searchValueCategory", "");
        startActivity(intent);
    }
}