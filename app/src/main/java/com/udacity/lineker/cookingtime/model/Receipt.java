package com.udacity.lineker.cookingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Receipt implements Parcelable {
    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String image;

    public Receipt() {

    }

    private Receipt(Parcel p){
        id = p.readInt();
        name = p.readString();
        ingredients = p.readArrayList(Ingredient.class.getClassLoader());
        steps = p.readArrayList(Step.class.getClassLoader());
        servings = p.readInt();
        image = p.readString();
    }

    public static final Parcelable.Creator<Receipt>
            CREATOR = new Parcelable.Creator<Receipt>() {

        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}