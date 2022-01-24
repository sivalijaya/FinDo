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
import com.example.findo.model.PaymentMethod;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {

    private List<PaymentMethod> mPaymentMethods;
    private PaymentMethodAdapterListener mPaymentMethodAdapterListener;
    private int selectedItem = -1;

    @Override
    public PaymentMethodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View paymentMethodView = inflater.inflate(R.layout.layout_paymentmethod, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(paymentMethodView, mPaymentMethodAdapterListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PaymentMethodAdapter.ViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        PaymentMethod paymentMethod = mPaymentMethods.get(position);

        // Set item views based on your views and data model
        LinearLayout ll_paymentmethod = holder.ll_paymentmethod;
        ImageView iv_paymentmethod_image = holder.iv_paymentmethod_image;
        TextView tv_paymentmethod_name = holder.tv_paymentmethod_name;

        tv_paymentmethod_name.setText(paymentMethod.getName());
        Picasso.get().load(paymentMethod.getImage()).into(iv_paymentmethod_image);

        if (position == selectedItem) {
            //selected item.
            ll_paymentmethod.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.recycler_select));
        } else {
            // unselected item.
            ll_paymentmethod.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.yellow_main));
        }
    }

    @Override
    public int getItemCount() {
        return mPaymentMethods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView iv_paymentmethod_image;
        public TextView tv_paymentmethod_name;
        public PaymentMethodAdapterListener paymentMethodAdapterListener;
        public LinearLayout ll_paymentmethod;

        public ViewHolder(View itemView, PaymentMethodAdapterListener paymentMethodAdapterListener) {
            super(itemView);

            ll_paymentmethod = itemView.findViewById(R.id.ll_paymentmethod);
            iv_paymentmethod_image = itemView.findViewById(R.id.iv_paymentmethod_image);
            tv_paymentmethod_name = itemView.findViewById(R.id.tv_paymentmethod_name);
            this.paymentMethodAdapterListener = paymentMethodAdapterListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            paymentMethodAdapterListener.paymentMethodAdapterClick(getAdapterPosition());
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

    public PaymentMethodAdapter(List<PaymentMethod> paymentMethods, PaymentMethodAdapterListener mPaymentMethodAdapterListener) {
        this.mPaymentMethods = paymentMethods;
        this.mPaymentMethodAdapterListener = mPaymentMethodAdapterListener;
    }

    public interface PaymentMethodAdapterListener {
        void paymentMethodAdapterClick(int position);
    }
}
