package com.piotrkorba.agropomoc;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;

/**
 * Data Access Object (DAO) used to access and change registry metadata.
 */
@Dao
public interface RegistryInfoDao {
    @Insert
    void insert(RegistryInfo registryInfo);

    @Query("SELECT * FROM registryinfo_table LIMIT 1")
    RegistryInfo[] getRegistryData();

    @Query("SELECT versionNumber FROM registryinfo_table WHERE id = 1")
    Integer getVersionNumber();

    @Query("SELECT updateDate FROM registryinfo_table WHERE id = 1")
    LiveData<Date> getDate();

    @Query("SELECT updateDate FROM registryinfo_table WHERE id = 1")
    Date getDateOneShot();

    @Query("SELECT versionSnackbar FROM registryinfo_table WHERE id = 1")
    LiveData<Boolean> showSnackbar();

    @Query("SELECT loadingScreen FROM registryinfo_table WHERE id = 1")
    LiveData<Boolean> showLoadingScreen();

    @Query("SELECT loadingScreen FROM registryinfo_table WHERE id = 1")
    Boolean showLoadingScreenOneShot();

    @Query("DELETE FROM registryinfo_table")
    void deleteAll();

    @Query("DELETE FROM sqlite_sequence WHERE name='registryinfo_table'")
    void resetAutoincrement();

    @Query("UPDATE registryinfo_table SET updateDate = :date WHERE id = 1")
    void updateDate(Date date);

    @Query("UPDATE registryinfo_table SET versionNumber = :versionNumber WHERE id = 1")
    void updateVersionNumber(int versionNumber);

    @Query("UPDATE registryinfo_table SET versionSnackbar = :value WHERE id = 1")
    void setSnackbar(boolean value);

    @Query("UPDATE registryinfo_table SET loadingScreen = :value WHERE id = 1")
    void setLoadingScreen(boolean value);
}
