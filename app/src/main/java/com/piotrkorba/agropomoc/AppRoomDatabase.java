package com.piotrkorba.agropomoc;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * AppRoomDatabase contains code responsible for creating the database.
 * AppRoomDatabase is implemented as a singleton to prevent having multiple instances opened at the same time.
 * Once database is created, all interactions with database happen through repository classes.
 */
@Database(entities = {Product.class, RegistryInfo.class, Note.class}, version = 5, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppRoomDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract RegistryInfoDao RegistryInfoDao();
    public abstract NoteDao noteDao();
    private static AppRoomDatabase INSTANCE;
    // This callback is called when the database has opened.
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
                            .addMigrations(MIGRATION_5_6)
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                    // Room database is not populated with initial data after invoking .build() on a database builder.
                    // Migrations and callbacks are triggered only on real read/write operation.
                    // Perform any read/write operation to overcome this behaviour.
                    INSTANCE.query("SELECT 1", null);
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Empty migration - database migration currently doesn't alter database schema.
        }
    };

    // Populate the database with initial data set only if database is empty.
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final ProductDao mDao;
        private final RegistryInfoDao registryDao;

        PopulateDbAsync(AppRoomDatabase db) {
            mDao = db.productDao();
            registryDao = db.RegistryInfoDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Populate registryinfo_table
            if (registryDao.getRegistryData().length < 1) {
                registryDao.deleteAll();
                registryDao.resetAutoincrement();
                registryDao.insert(new RegistryInfo());
                // Set initial date to Unix Epoch
                registryDao.updateDate(new Date(0));
                registryDao.setLoadingScreen(false);
                // Do not show update snackbar.
                registryDao.setSnackbar(false);
                registryDao.updateVersionNumber(0);
            }
            /*
            // Don't download records on the first run
            if (mDao.anyProduct().length < 1) {
                mDao.deleteAll();
                mDao.resetAutoincrement();
                String str = NetworkUtils.getRegistryContent();
                if (str != null) {
                    JSONArray ja = NetworkUtils.convertStringJSON(str);
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
             */
            return null;
        }
    }
}
