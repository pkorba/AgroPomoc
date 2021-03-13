package com.piotrkorba.agropomoc;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Query("DELETE FROM product_table")
    void deleteAll();

    @Query("SELECT id, nazwa, uprawa, agrofag FROM product_table ORDER BY id ASC")
    LiveData<List<ProductCoreInfo>> getAllProducts();

    @Query("SELECT * FROM product_table WHERE id == :id")
    List<Product> getProduct(int id);

    @Query("SELECT * FROM product_table LIMIT 1")
    ProductCoreInfo[] anyProduct();

    @Query("SELECT id, nazwa, uprawa, agrofag FROM product_table WHERE nazwa LIKE :searchQuery OR uprawa LIKE :searchQuery OR agrofag LIKE :searchQuery")
    LiveData<List<ProductCoreInfo>> searchForProducts(String searchQuery);

    @Query("DELETE FROM sqlite_sequence WHERE name='product_table'")
    void resetAutoincrement();
}
