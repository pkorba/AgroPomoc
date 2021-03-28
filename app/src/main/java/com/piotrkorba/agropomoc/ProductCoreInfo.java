package com.piotrkorba.agropomoc;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

/**
 * Class used to retrieve product name, crop name, and pest name from the database.
 */
public class ProductCoreInfo {
    @NonNull
    @ColumnInfo(name = "id")
    public int id;

    @NonNull
    @ColumnInfo(name = "nazwa")
    public String nazwa;

    @ColumnInfo(name = "uprawa")
    public String uprawa;

    @ColumnInfo(name = "agrofag")
    public String agrofag;
}
