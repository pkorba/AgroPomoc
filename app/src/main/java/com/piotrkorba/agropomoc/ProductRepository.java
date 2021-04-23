package com.piotrkorba.agropomoc;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ProductRepository class implements methods used to run database queries through data access objects (DAOs).
 * All long running queries run in the background.
 */
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
        new checkForUpdatesAsyncTask(new WeakReference<>(context), mRegistryInfoDao, currentDate).execute();
    }

    public void update(Context context) {
        new updateAsyncTask(new WeakReference<>(context), mProductDao, mRegistryInfoDao).execute();
    }

    // Below are static inner classes used to run tasks on separate threads in the background.

    /**
     * Checks if network connection is currently available.
     * If so, downloads current ŚOR registry version from remote server.
     */
    public static class checkForUpdatesAsyncTask extends AsyncTask <Void, Void, Void> {

        private RegistryInfoDao mAsyncRegDao;
        private WeakReference<Context> mContext;
        private Date mDate;

        checkForUpdatesAsyncTask(WeakReference<Context> context, RegistryInfoDao regDao, Date currentDate) {
            mContext = context;
            mAsyncRegDao = regDao;
            mDate = currentDate;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Check if network connection is available
            if (NetworkUtils.checkNetworkConnection(mContext)) {
                // Get info about version number from remote server.
                int remoteRegistryVersion = NetworkUtils.checkRegistryVersion();
                if (remoteRegistryVersion > mAsyncRegDao.getVersionNumber()) {
                    // Signal the need to show 'update avaliable' dialog in UI.
                    mAsyncRegDao.setSnackbar(true);
                }
            }
            // Update the date of last update check.
            mAsyncRegDao.updateDate(mDate);
            return null;
        }
    }

    /**
     * Update the ŚOR registry in database.
     */
    private static class updateAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProductDao mAsyncProdDao;
        private RegistryInfoDao mAsyncRegDao;
        private WeakReference<Context> mContext;

        updateAsyncTask(WeakReference<Context> context, ProductDao prodDao, RegistryInfoDao regDao) {
            mAsyncProdDao = prodDao;
            mAsyncRegDao = regDao;
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Check if network connection is available
            if (NetworkUtils.checkNetworkConnection(mContext)) {
                // Get registry version from remote server
                int remoteVersion = NetworkUtils.checkRegistryVersion();
                int localVersion = mAsyncRegDao.getVersionNumber();
                if (localVersion < remoteVersion) {
                    // Download new registry
                    String dataString = NetworkUtils.getRegistryContent();
                    if (dataString != null) {
                        try {
                            // Convert registry to JSON
                            JSONArray ja = NetworkUtils.convertStringJSON(dataString);
                            // Clear current content of the database
                            mAsyncProdDao.deleteAll();
                            mAsyncProdDao.resetAutoincrement();
                            // Put JSON content into database
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
                    // Reset the flag used to signal the need to update the registry.
                    mAsyncRegDao.setSnackbar(false);
                }
            }
            // Show loading screen during update process. More info in activity class.
            boolean loadingScreen = mAsyncRegDao.showLoadingScreenOneShot();
            mAsyncRegDao.setLoadingScreen(!loadingScreen);
            return null;
        }
    }
}
