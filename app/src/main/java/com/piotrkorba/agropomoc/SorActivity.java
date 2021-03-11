package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class SorActivity extends AppCompatActivity {
    private ProductViewModel mProductViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sor);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ProductListAdapter adapter = new ProductListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // mProductViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        mProductViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProductViewModel.class);

        mProductViewModel.getAllProducts().observe(this, new Observer<List<ProductCoreInfo>>() {
            @Override
            public void onChanged(List<ProductCoreInfo> productCoreInfos) {
                adapter.setProducts(productCoreInfos);
            }
        });
    }
}