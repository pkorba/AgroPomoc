package com.piotrkorba.agropomoc;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    private static AppRoomDatabase INSTANCE;
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    public static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppRoomDatabase.class, "app_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final ProductDao mDao;

        PopulateDbAsync(AppRoomDatabase db) {
            mDao = db.productDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDao.deleteAll();

            String str = NetworkUtils.getRegistryContent();
            if (str != null) {
                JSONArray ja = NetworkUtils.convertStringJSON(str);
                if (mDao.anyProduct().length < 1) {
                    try {
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
                            mDao.insert(product);
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
