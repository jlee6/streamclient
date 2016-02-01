package com.jlee.mobile.stream.module;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class StreamEndPoint implements Parcelable {
    private int typeId;
    private String name;
    private Uri uri;

    public StreamEndPoint() {
    }

    private StreamEndPoint(Parcel in) {
        this (in.readInt(), in.readString(), in.readParcelable(Uri.class.getClassLoader()));
    }

    public StreamEndPoint(int type, String name,  Uri uri) {
        this.typeId = type;
        this.name = name;
        this.uri = uri;
    }

    public int getTypeId() {
        return typeId;
    }

    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(typeId);
        parcel.writeString(name);
        parcel.writeParcelable(uri, i);
    }


    public static final Parcelable.Creator<StreamEndPoint> CREATOR
            = new Parcelable.Creator<StreamEndPoint>() {
        public StreamEndPoint createFromParcel(Parcel in) {
            return new StreamEndPoint(in);
        }

        public StreamEndPoint[] newArray(int size) {
            return new StreamEndPoint[size];
        }
    };
}
