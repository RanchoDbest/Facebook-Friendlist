package com.example.rachanasolanki.facebookdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rachana.solanki on 1/17/2018.
 */

public class FriendsModel implements Parcelable{

    public String name;
    public String profile_pic;

    public FriendsModel(String name,String profile_pic) {
        this.name = name;
        this.profile_pic = profile_pic;
    }

    protected FriendsModel(Parcel in) {
        name = in.readString();
        profile_pic = in.readString();
    }

    public static final Creator<FriendsModel> CREATOR = new Creator<FriendsModel>() {
        @Override
        public FriendsModel createFromParcel(Parcel in) {
            return new FriendsModel(in);
        }

        @Override
        public FriendsModel[] newArray(int size) {
            return new FriendsModel[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(profile_pic);
    }
}
