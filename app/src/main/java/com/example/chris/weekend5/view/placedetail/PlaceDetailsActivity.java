package com.example.chris.weekend5.view.placedetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chris.weekend5.MapsApplication;
import com.example.chris.weekend5.R;
import com.example.chris.weekend5.model.Place;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class PlaceDetailsActivity extends AppCompatActivity
{
    
    private TextView tvName;
    private ImageView ivPlace;
    private TextView tvDAddress;
    private TextView tvPhone;
    private TextView tvPrice;
    private TextView tvRating;
    private TextView tvWebsite;
    private GeoDataClient mGeoDataClient;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        
        bindViews();
        
        displayDetails();
    }
    
    private void displayDetails()
    {
        Intent intent = getIntent();
        tvName.setText(intent.getStringExtra("name"));
        loadPic(intent.getStringExtra("id"));
        tvDAddress.setText(intent.getStringExtra("address"));
        tvPhone.setText(intent.getStringExtra("phone"));
        tvPrice.setText(convertPriceRange(intent));
        tvRating.setText("" + intent.getFloatExtra("rating", 0));
        tvWebsite.setText(intent.getStringExtra("website"));
    }
    
    private String convertPriceRange(Intent intent)
    {
        String price = "";
        for (int i = 0; i < intent.getIntExtra("price", 0); i++)
        {
            price += "$";
        }
        return price;
    }
    
    private void bindViews()
    {
        mGeoDataClient = Places.getGeoDataClient(this, null);
        
        tvName = findViewById(R.id.tvDName);
        ivPlace = findViewById(R.id.ivPlace);
        tvDAddress = findViewById(R.id.tvDAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvPrice = findViewById(R.id.tvPrice);
        tvRating = findViewById(R.id.tvRating);
        tvWebsite = findViewById(R.id.tvWebsite);
    }
    
    private void loadPic(String id)
    {
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(id);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>()
        {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task)
            {
                try
                {
                    // Get the list of photos.
                    PlacePhotoMetadataResponse photos = task.getResult();
                    // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                    PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                    // Get the first photo in the list.
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                    // Get the attribution text.
                    CharSequence attribution = photoMetadata.getAttributions();
                    // Get a full-size bitmap for the photo.
                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoResponse> task)
                        {
                            PlacePhotoResponse photo = task.getResult();
                            Bitmap bitmap = photo.getBitmap();
                            Glide.with(getApplicationContext()).asBitmap().load(bitmap).into(ivPlace);
                        }
                    });
                    photoMetadataBuffer.release();
                }
                catch (Exception e)
                {
                    Toast.makeText(PlaceDetailsActivity.this, "No Picture Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        MapsApplication.get(this).clearDetailsComponent();
    }
}
