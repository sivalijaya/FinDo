package com.example.findo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.R;
import com.example.findo.model.ProductCategory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<ProductCategory> mProductCategories;
    private Context context;

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productCategoryView = inflater.inflate(R.layout.layout_listcategory, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(productCategoryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryAdapter.ViewHolder holder, int position) {
        ProductCategory productCategory = mProductCategories.get(position);

        // Set item views based on your views and data model
        TextView txt_categoryTitle = holder.tv_categoryTitle;
        RecyclerView rv_itemCategory = holder.rv_itemCategory;

        txt_categoryTitle.setText(productCategory.getName());
        ItemListAdapter adapter = new ItemListAdapter(productCategory.getMproduct());
        rv_itemCategory.setAdapter(adapter);
        rv_itemCategory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return mProductCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_categoryTitle;
        public RecyclerView rv_itemCategory;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_categoryTitle = itemView.findViewById(R.id.tv_categoryTitle);
            rv_itemCategory = itemView.findViewById(R.id.rv_itemCategory);
        }
    }

    public CategoryAdapter(List<ProductCategory> mProductCategory, Context context) {
        this.mProductCategories = mProductCategory;
        this.context = context;
    }
}
