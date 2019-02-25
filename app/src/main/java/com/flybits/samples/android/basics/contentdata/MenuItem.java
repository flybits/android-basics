package com.flybits.samples.android.basics.contentdata;

import android.os.Parcel;
import android.os.Parcelable;

/************************************************************************
 * SETUP: Step 2a - Content Data class for MenuItem Content Template
 ***********************************************************************/
public class MenuItem implements Parcelable {

    public String name;
    public String description;
    public String price;
    public Nutrition nutrition;

    public MenuItem(){}

    protected MenuItem(Parcel in) {
        name = in.readString();
        description = in.readString();
        price = in.readString();
        nutrition = in.readParcelable(Nutrition.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeParcelable(nutrition, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };
}

