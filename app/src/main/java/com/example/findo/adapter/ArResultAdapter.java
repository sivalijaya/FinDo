package com.example.findo.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.ArResultActivity;
import com.example.findo.R;
import com.example.findo.model.Product;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.mlkit.vision.label.ImageLabel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArResultAdapter extends RecyclerView.Adapter<ArResultAdapter.ViewHolder>{

    private List<FirebaseVisionImageLabel> mImageLabel = new ArrayList();
    private ArListResultListener mArListResultListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.layout_arresult, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(productView, mArListResultListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FirebaseVisionImageLabel imageLabel = mImageLabel.get(position);


        TextView txt_itemName = holder.itemName;
        TextView txt_accuracy = holder.itemAccuracy;


        if(imageLabel.getConfidence() > 0.7){
            txt_accuracy.setTextColor(Color.GREEN);
        }else if(imageLabel.getConfidence() < 0.3){
            txt_accuracy.setTextColor(Color.RED);
        }else{
            txt_accuracy.setTextColor(Color.rgb(255, 127, 0));
        }

        txt_accuracy.setText(Float.toString(imageLabel.getConfidence()));
        txt_itemName.setText(imageLabel.getText());
    }

    @Override
    public int getItemCount() {
        return mImageLabel.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName, itemAccuracy;
        public ArListResultListener arListResultListener;

        public ViewHolder(View itemView, ArListResultListener arListResultListener) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            itemAccuracy = itemView.findViewById(R.id.itemAccuracy);
            this.arListResultListener = arListResultListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            arListResultListener.arListResultClick(getAdapterPosition());
        }
    }

    public ArResultAdapter(List<FirebaseVisionImageLabel> mImageLabel, ArListResultListener mArListResultListener) {
        this.mImageLabel = mImageLabel;
        this.mArListResultListener = mArListResultListener;
    }

    public interface ArListResultListener{
        void arListResultClick(int position);
    }
}
