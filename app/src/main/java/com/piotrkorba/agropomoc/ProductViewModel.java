package com.piotrkorba.agropomoc;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.Date;
import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository mRepository;
    LiveData<List<ProductCoreInfo>> allProductsFiltered;
    LiveData<Boolean> mShowLoadingScreen;
    LiveData<Boolean> mShowSnackbar;
    LiveData<Date> mDate;
    private MutableLiveData<String> filter = new MutableLiveData<>("%");

    public ProductViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ProductRepository(application);
        allProductsFiltered = Transformations.switchMap(filter, v -> mRepository.searchForProducts(v));
        mShowLoadingScreen = mRepository.showLoadingScreen();
        mShowSnackbar = mRepository.showSnackbar();
        mDate = mRepository.getDate();
    }

    void checkForUpdates(Date currentDate) {
        mRepository.checkForUpdates(getApplication(), currentDate);
    }

    void update() {
        mRepository.update(getApplication());
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
