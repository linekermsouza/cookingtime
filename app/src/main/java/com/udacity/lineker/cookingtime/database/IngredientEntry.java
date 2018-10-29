package com.udacity.lineker.cookingtime.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "ingredient",foreignKeys = @ForeignKey(entity = ReceiptEntry.class,
        parentColumns = "id",
        childColumns = "receiptId",
        onDelete = CASCADE))
public class IngredientEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String ingredient;
    private int receiptId;
    private double quantity;
    private String measure;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }


    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
