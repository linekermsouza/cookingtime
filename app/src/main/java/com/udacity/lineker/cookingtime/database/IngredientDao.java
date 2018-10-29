package com.udacity.lineker.cookingtime.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM ingredient ORDER BY id")
    List<IngredientEntry> loadAllSync();

    @Query("SELECT * FROM ingredient WHERE receiptId=:receiptId")
    List<IngredientEntry> findIngredientsForReceipt(final int receiptId);

    @Insert
    void insert(IngredientEntry ingredientEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(IngredientEntry ingredientEntry);

    @Delete()
    void delete(IngredientEntry ingredientEntry);
}
