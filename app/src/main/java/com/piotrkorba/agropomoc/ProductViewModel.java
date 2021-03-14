package com.piotrkorba.agropomoc;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository mRepository;
    LiveData<List<ProductCoreInfo>> allProductsFiltered;
    private MutableLiveData<String> filter = new MutableLiveData<>("%");

    public ProductViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ProductRepository(application);
        allProductsFiltered = Transformations.switchMap(filter, v -> mRepository.searchForProducts(v));
    }

    public void insert(Product product) {
        mRepository.insert(product);
    }

    void searchForProducts(String searchQuery) {
        if (searchQuery.isEmpty()) {
            searchQuery = "%";
        } else {
            searchQuery = "%" + searchQuery + "%";
        }
        filter.postValue(searchQuery);
    }
}
