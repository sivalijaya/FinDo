package com.example.findo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findo.R;
import com.example.findo.model.ShippingMethod;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ShippingMethodAdapter extends RecyclerView.Adapter<ShippingMethodAdapter.ViewHolder> {

    private List<ShippingMethod> mShippingMethods;
    private ShippingMethodAdapterListener mShippingMethodAdapterListener;
    private int selectedItem = -1;

    @Override
    public ShippingMethodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View shippingMethodView = inflater.inflate(R.layout.layout_shippingmethod, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(shippingMethodView, mShippingMethodAdapterListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShippingMethodAdapter.ViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        ShippingMethod shippingMethod = mShippingMethods.get(position);

        // Set item views based on your views and data model
        LinearLayout ll_shippingmethod = holder.ll_shippingmethod;
        ImageView iv_product_image = holder.iv_shippingmethod_image;
        TextView tv_shippingmethod_name = holder.tv_shippingmethod_name;
        TextView tv_shippingmethod_estimation = holder.tv_shippingmethod_estimation;
        TextView tv_shippingmethod_price = holder.tv_shippingmethod_price;

        tv_shippingmethod_name.setText(shippingMethod.getName());
        tv_shippingmethod_estimation.setText(shippingMethod.getEstimation());
        tv_shippingmethod_price.setText(decimalFormat.format(shippingMethod.getPrice()));
        Picasso.get().load(shippingMethod.getImage()).into(iv_product_image);

        if (position == selectedItem) {
            //selected item.
            ll_shippingmethod.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.recycler_select));
        } else {
            // unselected item.
            ll_shippingmethod.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.yellow_main));
        }
    }

    @Override
    public int getItemCount() {
        return mShippingMethods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView iv_shippingmethod_image;
        public TextView tv_shippingmethod_name, tv_shippingmethod_estimation, tv_shippingmethod_price;
        public ShippingMethodAdapterListener shippingMethodAdapterListener;
        public LinearLayout ll_shippingmethod;

        public ViewHolder(View itemView, ShippingMethodAdapterListener shippingMethodAdapterListener) {
            super(itemView);

            ll_shippingmethod = itemView.findViewById(R.id.ll_shippingmethod);
            iv_shippingmethod_image = itemView.findViewById(R.id.iv_shippingmethod_image);
            tv_shippingmethod_name = itemView.findViewById(R.id.tv_shippingmethod_name);
            tv_shippingmethod_estimation = itemView.findViewById(R.id.tv_shippingmethod_estimation);
            tv_shippingmethod_price = itemView.findViewById(R.id.tv_shippingmethod_price);
            this.shippingMethodAdapterListener = shippingMethodAdapterListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            shippingMethodAdapterListener.shippingMethodAdapterClick(getAdapterPosition());
            int position = getAdapterPosition();
            //Make sure your position is available on your list.
            if (position != RecyclerView.NO_POSITION) {
                if (position == selectedItem) {
                    return;
                }

                int currentSelected = selectedItem; //Create a temp var to deselect.
                selectedItem = position; //Check item.

                if (currentSelected != -1) {
                    notifyItemChanged(currentSelected); //Deselected previous.
                }

                notifyItemChanged(selectedItem); //Select current item.
            }
        }
    }

    public ShippingMethodAdapter(List<ShippingMethod> shippingMethods, ShippingMethodAdapterListener mShippingMethodAdapterListener) {
        this.mShippingMethods = shippingMethods;
        this.mShippingMethodAdapterListener = mShippingMethodAdapterListener;
    }

    public interface ShippingMethodAdapterListener {
        void shippingMethodAdapterClick(int position);
    }
}
