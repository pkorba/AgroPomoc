package com.piotrkorba.agropomoc;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.Date;
import java.util.List;

/**
 * Interface between UI and data layer represented by the repository.
 */
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
        // Change LiveData object value based on filter content.
        allProductsFiltered = Transformations.switchMap(filter, v -> mRepository.searchForProducts(v));
        // Provide observable object which tells UI when to show progress bar.
        mShowLoadingScreen = mRepository.showLoadingScreen();
        // Provide observable object which tells UI when to show snackbar reminding user about available update.
        mShowSnackbar = mRepository.showSnackbar();
        mDate = mRepository.getDate();
    }

    void checkForUpdates(Date currentDate) {
        mRepository.checkForUpdates(getApplication(), currentDate);
    }

    void update() {
        mRepository.update(getApplication());
    }

    /**
     * Updates filter used to change values displayed in RecyclerView list.
     * @param searchQuery String value to search for in the database
     */
    void searchForProducts(String searchQuery) {
        if (searchQuery.isEmpty()) {
            searchQuery = "%";
        } else {
            searchQuery = "%" + searchQuery + "%";
        }
        filter.postValue(searchQuery);
    }
}
