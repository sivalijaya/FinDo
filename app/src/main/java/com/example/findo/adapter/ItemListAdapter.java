package com.example.findo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.R;
import com.example.findo.model.Product;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private List<Product> mProducts;

    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.layout_listitem, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(productView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemListAdapter.ViewHolder holder, int position) {
        Product product = mProducts.get(position);

        // Set item views based on your views and data model
        TextView txt_productTitle = holder.productTitle;
        TextView txt_productPrice = holder.productPrice;
        TextView txt_totalSold = holder.total_sold;
        ImageView img_productImage = holder.productImage;

        txt_productTitle.setText(product.getName());
        txt_productPrice.setText(Long.toString(product.getPrice()));
        txt_totalSold.setText(Integer.toString(product.getSold()));

//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = firebaseDatabase.getReference();
//        DatabaseReference getImage = databaseReference.child("image");
//
//        getImage.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                String link = snapshot.getValue(String.class);
//
//                Picasso.get().load(link).into(img_productImage);
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView productImage;
        public TextView productTitle, productPrice, total_sold;

        public ViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.price);
            total_sold = itemView.findViewById(R.id.total_sold);
        }
    }

    public ItemListAdapter(List<Product> products) {
        mProducts = products;
    }
}
