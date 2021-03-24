package com.piotrkorba.agropomoc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {
    private final LayoutInflater mInflater;
    private List<ProductCoreInfo> mProducts;
    // private ProgressBar mProgressBar;
    public static final String PRODUCT_ID = "com.piotrkorba.agropomoc.extra.PRODUCT_ID";

    ProductListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        // mProgressBar = ((Activity) context).findViewById(R.id.progressBar);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.sor_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (mProducts != null) {
            ProductCoreInfo current = mProducts.get(position);
            holder.productItemViewTitle.setText(current.nazwa);
            holder.productItemViewCrop.setText(current.uprawa);
            holder.productItemViewPest.setText(current.agrofag);
        } else {
            // Data not ready
            holder.productItemViewTitle.setText("Brak danych");
            holder.productItemViewCrop.setText("Brak danych");
            holder.productItemViewPest.setText("Brak danych");
        }
        // mProgressBar.setVisibility(View.GONE);
    }

    void setProducts(List<ProductCoreInfo> products) {
        mProducts = products;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mProducts != null)
            return mProducts.size();
        else return 0;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView productItemViewTitle;
        private TextView productItemViewCrop;
        private TextView productItemViewPest;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productItemViewTitle = itemView.findViewById(R.id.textView_sor_name);
            productItemViewCrop = itemView.findViewById(R.id.textView_sor_crop);
            productItemViewPest = itemView.findViewById(R.id.textView_sor_pest);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mPosition = getLayoutPosition();
            ProductCoreInfo element = mProducts.get(mPosition);
            Intent intent = new Intent(v.getContext(), SingleProductActivity.class);
            intent.putExtra(PRODUCT_ID, element.id);
            v.getContext().startActivity(intent);
        }
    }
}
