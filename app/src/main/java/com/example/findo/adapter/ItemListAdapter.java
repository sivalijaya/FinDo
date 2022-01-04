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
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private List<Product> mProducts;
    private int width;
    private ItemListAdapterListener mItemListAdapterListener;

    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.layout_listitem, parent, false);
        productView.getLayoutParams().width = width;

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(productView, mItemListAdapterListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemListAdapter.ViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Product product = mProducts.get(position);

        // Set item views based on your views and data model
        TextView txt_productTitle = holder.productTitle;
        TextView txt_productPrice = holder.productPrice;
        TextView txt_totalSold = holder.total_sold;
        ImageView img_productImage = holder.productImage;

        txt_productTitle.setText(product.getName());
        txt_productPrice.setText(decimalFormat.format(product.getPrice()));
        txt_totalSold.setText(product.getSold().toString());
        Picasso.get().load(product.getPhoto().get(0)).into(img_productImage);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView productImage;
        public TextView productTitle, productPrice, total_sold;
        public ItemListAdapterListener itemListAdapterListener;

        public ViewHolder(View itemView, ItemListAdapterListener itemListAdapterListener) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.price);
            total_sold = itemView.findViewById(R.id.total_sold);
            this.itemListAdapterListener = itemListAdapterListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListAdapterListener.itemListAdapterClick(getAdapterPosition());
        }
    }

    public ItemListAdapter(List<Product> products, int width, ItemListAdapterListener mItemListAdapterListener) {
        mProducts = products;
        this.width = width;
        this.mItemListAdapterListener = mItemListAdapterListener;
    }

    public interface ItemListAdapterListener {
        void itemListAdapterClick(int position);
    }
}
