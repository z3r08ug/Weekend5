package com.example.chris.weekend5.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by chris on 12/9/2017.
 */

public class Place implements Parcelable
{
    String address;
    String attributions;
    String id;
    LatLng latLng;
    Locale locale;
    String name;
    String phoneNumber;
    List<Integer> placeTypes;
    int priceLevel;
    float rating;
    LatLngBounds viewPort;
    Uri websiteUri;
    String web;
    
    public Place(String address, String attributions, String id, LatLng latLng, String name, String phoneNumber, List<Integer> placeTypes, int priceLevel, float rating, LatLngBounds viewPort, Uri websiteUri)
    {
        this.address = address;
        this.attributions = attributions;
        this.id = id;
        this.latLng = latLng;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.placeTypes = placeTypes;
        this.priceLevel = priceLevel;
        this.rating = rating;
        this.viewPort = viewPort;
        this.websiteUri = websiteUri;
    }
    
    protected Place(Parcel in)
    {
        address = in.readString();
        attributions = in.readString();
        id = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        name = in.readString();
        phoneNumber = in.readString();
        placeTypes = new ArrayList<>();
        in.readList(placeTypes, Integer.class.getClassLoader());
        priceLevel = in.readInt();
        rating = in.readFloat();
//        viewPort = in.readParcelable(LatLngBounds.class.getClassLoader());
        //websiteUri = (Uri) in.readSerializable();
        web = in.readString();
    }
    
    public static final Creator<Place> CREATOR = new Creator<Place>()
    {
        @Override
        public Place createFromParcel(Parcel in)
        {
            return new Place(in);
        }
        
        @Override
        public Place[] newArray(int size)
        {
            return new Place[size];
        }
    };
    
    public String getAddress()
    {
        return address;
    }
    
    public String getAttributions()
    {
        return attributions;
    }
    
    public String getId()
    {
        return id;
    }
    
    public LatLng getLatLng()
    {
        return latLng;
    }
    
    public Locale getLocale()
    {
        return locale;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    
    public List<Integer> getPlaceTypes()
    {
        return placeTypes;
    }
    
    public int getPriceLevel()
    {
        return priceLevel;
    }
    
    public float getRating()
    {
        return rating;
    }
    
    public LatLngBounds getViewPort()
    {
        return viewPort;
    }
    
    public Uri getWebsiteUri()
    {
        return websiteUri;
    }
    
    @Override
    public int describeContents()
    {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
    
        dest.writeString(address);
        dest.writeString(attributions);
        dest.writeString(id);
        dest.writeParcelable(latLng, flags);
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeInt(priceLevel);
        dest.writeFloat(rating);
        dest.writeList(placeTypes);
//        dest.writeParcelable(viewPort, flags);
//        dest.writeSerializable((Serializable) websiteUri);
        dest.writeString(websiteUri+"");
    }
    
}
