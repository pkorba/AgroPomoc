package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class SorActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ProductViewModel mProductViewModel;
    ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sor);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        // final ProductListAdapter adapter = new ProductListAdapter(this);
        adapter = new ProductListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mProductViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProductViewModel.class);
        mProductViewModel.allProductsFiltered.observe(this, new Observer<List<ProductCoreInfo>>() {
            @Override
            public void onChanged(List<ProductCoreInfo> productCoreInfos) {
                adapter.setProducts(productCoreInfos);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sor_menu, menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query != null) {
            mProductViewModel.searchForProducts(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (query != null) {
            mProductViewModel.searchForProducts(query);
        }
        return true;
    }
}