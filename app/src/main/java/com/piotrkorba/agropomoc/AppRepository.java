package com.piotrkorba.agropomoc;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AppRepository {
    private ProductDao mProductDao;

    AppRepository (Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mProductDao = db.productDao();
    }

    public void insert (Product product) {
        new insertAsyncTask(mProductDao).execute(product);
    }

    private static class insertAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao mAsyncTaskDao;

        insertAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Product... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    LiveData<List<ProductCoreInfo>> searchForProducts(String searchQuery) {
        return mProductDao.searchForProducts(searchQuery);
    }
}
