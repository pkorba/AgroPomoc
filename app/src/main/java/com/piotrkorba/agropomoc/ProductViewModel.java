package com.piotrkorba.agropomoc;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<List<ProductCoreInfo>> mAllProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllProducts = mRepository.getAllProducts();
    }

    LiveData<List<ProductCoreInfo>> getAllProducts() {
        return mAllProducts;
    }

    public void insert(Product product) {
        mRepository.insert(product);
    }

    LiveData<List<ProductCoreInfo>> searchForProducts(String searchQuery) {
        return mRepository.searchForProducts(searchQuery);
    }
}
