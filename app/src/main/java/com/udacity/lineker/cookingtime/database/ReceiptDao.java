package com.udacity.lineker.cookingtime.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ReceiptDao {

    @Query("SELECT * FROM receipt ORDER BY id")
    LiveData<List<ReceiptEntry>> loadAll();

    @Query("SELECT * FROM receipt ORDER BY id")
    List<ReceiptEntry> loadAllSync();

    @Insert
    void insert(ReceiptEntry receiptEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ReceiptEntry receiptEntry);

    @Delete
    void delete(ReceiptEntry receiptEntry);
}
