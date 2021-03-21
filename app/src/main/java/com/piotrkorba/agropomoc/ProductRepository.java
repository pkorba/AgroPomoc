package com.piotrkorba.agropomoc;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductRepository {
    private ProductDao mProductDao;

    ProductRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mProductDao = db.productDao();
    }

    LiveData<List<ProductCoreInfo>> searchForProducts(String searchQuery) {
        return mProductDao.searchForProducts(searchQuery);
    }

    LiveData<Product> getProduct(int id) {
        return mProductDao.getProduct(id);
    }

    public void update(Context context) {
        new updateAsyncTask(context, mProductDao).execute();
    }

    private static class updateAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProductDao mAsyncTaskDao;
        private Context mContext;

        updateAsyncTask(Context context, ProductDao dao) {
            mAsyncTaskDao = dao;
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (NetworkUtils.checkNetworkConnection(mContext)) {
                String dataString = NetworkUtils.getRegistryContent();
                if (dataString != null) {
                    try {
                        JSONArray ja = NetworkUtils.convertStringJSON(dataString);
                        mAsyncTaskDao.deleteAll();
                        mAsyncTaskDao.resetAutoincrement();
                        for (int i = 0; i < ja.length(); ++i) {
                            JSONObject jo = ja.getJSONObject(i);
                            Product product = new Product(
                                    jo.getString("nazwa"),
                                    jo.getString("NrZezw"),
                                    jo.getLong("TerminZezw"),
                                    jo.getLong("TerminDoSprzedazy"),
                                    jo.getLong("TerminDoStosowania"),
                                    jo.getString("Rodzaj"),
                                    jo.getString("Substancja_czynna"),
                                    jo.getString("uprawa"),
                                    jo.getString("agrofag"),
                                    jo.getString("dawka"),
                                    jo.getString("termin"),
                                    jo.getString("nazwa_grupy"),
                                    jo.getString("maloobszarowe"),
                                    jo.getString("zastosowanie/uzytkownik"),
                                    jo.getString("etykieta"));
                            mAsyncTaskDao.insert(product);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }
}
