package com.example.findo.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.R;
import com.example.findo.model.ProductCategory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements ItemListAdapter.ItemListAdapterListener {

    private List<ProductCategory> mProductCategories;
    private Context context;
    private CategoryAdapterListener mCategoryAdapterListener;
    private int clickPosition;

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productCategoryView = inflater.inflate(R.layout.layout_listcategory, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(productCategoryView, mCategoryAdapterListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryAdapter.ViewHolder holder, int position) {
        ProductCategory productCategory = mProductCategories.get(position);

        // Set item views based on your views and data model
        TextView txt_categoryTitle = holder.tv_categoryTitle;
        RecyclerView rv_itemCategory = holder.rv_itemCategory;

        txt_categoryTitle.setText(productCategory.getName());

        CardView cv = holder.rl_homeActivityContainer;
        cv.post(new Runnable() {
            @Override
            public void run() {
                float dip = 26f;
                Resources r = holder.itemView.getResources();
                float px = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dip,
                        r.getDisplayMetrics()
                );
                int width = (int) (cv.getMeasuredWidth() - px);
                ItemListAdapter adapter = new ItemListAdapter(productCategory.getProduct(), (int) (width / 2.4), CategoryAdapter.this, position);

                rv_itemCategory.setAdapter(adapter);
                rv_itemCategory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductCategories.size();
    }

    @Override
    public void itemListAdapterClick(int position) {
    }

    @Override
    public void itemListAdapterClickFromParent(int parentPosition, int position) {
        Toast.makeText(context.getApplicationContext(), mProductCategories.get(parentPosition).getProduct().get(position).getId() + " System is busy!", Toast.LENGTH_SHORT).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_categoryTitle, btn_viewMore;
        public RecyclerView rv_itemCategory;
        public CardView rl_homeActivityContainer;
        public CategoryAdapterListener categoryAdapterListener;
        public int position;

        public ViewHolder(View itemView, CategoryAdapterListener categoryAdapterListener) {
            super(itemView);

            tv_categoryTitle = itemView.findViewById(R.id.tv_categoryTitle);
            btn_viewMore = itemView.findViewById(R.id.btn_viewMore);
            rv_itemCategory = itemView.findViewById(R.id.rv_itemCategory);
            rl_homeActivityContainer = itemView.findViewById(R.id.categoryLayoutActivity);
            this.categoryAdapterListener = categoryAdapterListener;

            btn_viewMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            categoryAdapterListener.categoryAdapterClick(getAdapterPosition());
        }
    }

    public CategoryAdapter(List<ProductCategory> mProductCategory, Context context, CategoryAdapterListener mCategoryAdapterListener) {
        this.mProductCategories = mProductCategory;
        this.context = context;
        this.mCategoryAdapterListener = mCategoryAdapterListener;

    }

    public interface CategoryAdapterListener {
        void categoryAdapterClick(int position);
    }
}
