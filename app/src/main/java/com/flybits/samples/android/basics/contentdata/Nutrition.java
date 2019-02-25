package com.flybits.samples.android.basics.contentdata;

import android.os.Parcel;
import android.os.Parcelable;

/************************************************************************
 * SETUP: Step 2b - Content Data class for inner Nutrition which is an object inside MenuItem
 ***********************************************************************/
public class Nutrition implements Parcelable {

    public String calories;
    public String isVegetarian;
    public String isGlutenFree;

    public Nutrition(){}

    protected Nutrition(Parcel in) {
        calories = in.readString();
        isVegetarian = in.readString();
        isGlutenFree = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(calories);
        dest.writeString(isVegetarian);
        dest.writeString(isGlutenFree);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Nutrition> CREATOR = new Creator<Nutrition>() {
        @Override
        public Nutrition createFromParcel(Parcel in) {
            return new Nutrition(in);
        }

        @Override
        public Nutrition[] newArray(int size) {
            return new Nutrition[size];
        }
    };
}