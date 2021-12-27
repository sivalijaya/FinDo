package com.example.findo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.findo.adapter.ArResultAdapter;
import com.example.findo.adapter.ItemListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArResultActivity extends AppCompatActivity implements ArResultAdapter.ArListResultListener {

    List<ImageLabel> testi = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_result);

        configureAndRunImageLabeler();
    }

    private void configureAndRunImageLabeler() {
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

        RecyclerView rvArResult = findViewById(R.id.rv_ar_result);
        ImageView ivCameraResult = findViewById(R.id.camera_result);

        Bundle bundle = getIntent().getExtras();
        Bitmap finalPhoto = (Bitmap) bundle.get("data");
        InputImage image = InputImage.fromBitmap(finalPhoto, 0);

        ivCameraResult.setImageBitmap(finalPhoto);

        labeler.process(image).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
            @Override
            public void onSuccess(@NonNull @NotNull List<ImageLabel> imageLabels) {
                testi = imageLabels;

                ArResultAdapter arResultAdapter = new ArResultAdapter(testi, ArResultActivity.this);
                rvArResult.setAdapter(arResultAdapter);
                rvArResult.setLayoutManager(new LinearLayoutManager(ArResultActivity.this));

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(ArResultActivity.this, "System is busy!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void arListResultClick(int position) {
        Log.d("test", "arListResultClick: " + position);
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("searchValue", testi.get(position).getText());
        startActivity(intent);
    }
}