package com.piotrkorba.agropomoc;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Entity class that represents registry metadata in the database.
 */
@Entity(tableName = "registryinfo_table")
public class RegistryInfo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date updateDate;
    private int versionNumber;
    private boolean versionSnackbar;
    private boolean loadingScreen;

    public boolean isVersionSnackbar() {
        return versionSnackbar;
    }

    public boolean isLoadingScreen() {
        return loadingScreen;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public void setVersionSnackbar(boolean versionSnackbar) {
        this.versionSnackbar = versionSnackbar;
    }

    public void setLoadingScreen(boolean loadingScreen) {
        this.loadingScreen = loadingScreen;
    }
}
