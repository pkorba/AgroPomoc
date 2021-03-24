package com.piotrkorba.agropomoc;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProductRepository {
    private ProductDao mProductDao;
    private RegistryInfoDao mRegistryInfoDao;

    ProductRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mProductDao = db.productDao();
        mRegistryInfoDao = db.RegistryInfoDao();
    }

    LiveData<List<ProductCoreInfo>> searchForProducts(String searchQuery) {
        return mProductDao.searchForProducts(searchQuery);
    }

    LiveData<Date> getDate() {
        return mRegistryInfoDao.getDate();
    }

    LiveData<Product> getProduct(int id) {
        return mProductDao.getProduct(id);
    }

    LiveData<Boolean> showSnackbar() {
        return mRegistryInfoDao.showSnackbar();
    }

    LiveData<Boolean> showLoadingScreen() {
        return mRegistryInfoDao.showLoadingScreen();
    }

    public void checkForUpdates(Context context, Date currentDate) {
        new checkForUpdatesAsyncTask(context, mRegistryInfoDao, currentDate).execute();
    }

    public static class checkForUpdatesAsyncTask extends AsyncTask <Void, Void, Void> {

        private RegistryInfoDao mAsyncRegDao;
        private Context mContext;
        private Date mDate;

        checkForUpdatesAsyncTask(Context context, RegistryInfoDao regDao, Date currentDate) {
            mContext = context;
            mAsyncRegDao = regDao;
            mDate = currentDate;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (NetworkUtils.checkNetworkConnection(mContext)) {
                int remoteRegistryVersion = NetworkUtils.checkRegistryVersion();
                Date localDate = mAsyncRegDao.getDateOneShot();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                if (remoteRegistryVersion > mAsyncRegDao.getVersionNumber() && !fmt.format(mDate).equals(fmt.format(localDate))) {
                    mAsyncRegDao.setSnackbar(true);
                } else {
                    mAsyncRegDao.setSnackbar(false);
                }
            }
            mAsyncRegDao.updateDate(mDate);
            return null;
        }
    }

    public void update(Context context) {
        new updateAsyncTask(context, mProductDao, mRegistryInfoDao).execute();
    }

    private static class updateAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProductDao mAsyncProdDao;
        private RegistryInfoDao mAsyncRegDao;
        private Context mContext;

        updateAsyncTask(Context context, ProductDao prodDao, RegistryInfoDao regDao) {
            mAsyncProdDao = prodDao;
            mAsyncRegDao = regDao;
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (NetworkUtils.checkNetworkConnection(mContext)) {
                int remoteVersion = NetworkUtils.checkRegistryVersion();
                int localVersion = mAsyncRegDao.getVersionNumber();
                if (localVersion < remoteVersion) {
                    String dataString = NetworkUtils.getRegistryContent();
                    if (dataString != null) {
                        try {
                            JSONArray ja = NetworkUtils.convertStringJSON(dataString);
                            mAsyncProdDao.deleteAll();
                            mAsyncProdDao.resetAutoincrement();
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
                                mAsyncProdDao.insert(product);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mAsyncRegDao.updateVersionNumber(remoteVersion);
                    mAsyncRegDao.setSnackbar(false);
                }
            }
            boolean loadingScreen = mAsyncRegDao.showLoadingScreenOneShot();
            mAsyncRegDao.setLoadingScreen(!loadingScreen);
            return null;
        }
    }
}
