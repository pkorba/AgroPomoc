package com.piotrkorba.agropomoc;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * Interface between UI and data layer represented by the repository.
 */
public class SingleProductViewModel extends AndroidViewModel {
    private ProductRepository mRepository;

    public SingleProductViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ProductRepository(application);
    }

    LiveData<Product> getProduct(int id) {
        return mRepository.getProduct(id);
    }

}
