package com.flybits.samples.android.basics.contentdata;

import android.os.Parcel;
import android.os.Parcelable;

/************************************************************************
 * SETUP: Step 2a - Content Data class for MenuOption Content Template
 ***********************************************************************/
public class MenuOption implements Parcelable {

    public String name;
    public String description;
    public String price;
    public Nutrition nutrition;

    public MenuOption(){}

    protected MenuOption(Parcel in) {
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

    public static final Creator<MenuOption> CREATOR = new Creator<MenuOption>() {
        @Override
        public MenuOption createFromParcel(Parcel in) {
            return new MenuOption(in);
        }

        @Override
        public MenuOption[] newArray(int size) {
            return new MenuOption[size];
        }
    };
}

