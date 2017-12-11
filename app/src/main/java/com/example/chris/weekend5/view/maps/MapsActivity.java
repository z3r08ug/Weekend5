package com.example.chris.weekend5.view.maps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chris.weekend5.MapsApplication;
import com.example.chris.weekend5.R;
import com.example.chris.weekend5.utils.adapters.RecyclerAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, MapsContract.View
{
    
    private static final String TAG = MapsActivity.class.getSimpleName() + "_TAG";
    private GoogleMap mMap;
    @Inject
    MapsPresenter presenter;
    protected GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10;
    private CameraPosition mCameraPosition;
    
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private Location mLastKnownLocation;
    
    public static final String KEY_CAMERA_POSITION = "camera_position";
    public static final String KEY_LOCATION = "location";
    public static final int  PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    
    public static final int M_MAX_ENTRIES = 30;
    
    
    
    private List<com.example.chris.weekend5.model.Place> places;
    private List<com.example.chris.weekend5.model.Place> catPlaces;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState != null)
        {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_maps);
        
        bindViews();
    }
    
    private void bindViews()
    {
        MapsApplication.get(this).getMapsComponent().inject(this);
        
        places = new ArrayList<>();
        catPlaces = new ArrayList<>();
        
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);
        
        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        
        recyclerView = findViewById(R.id.rvPlaces);
        
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
        
        presenter.attachView(this);
        
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
        if (mMap != null)
        {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }
    
    private void getLocationPermission()
    {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "getLocationPermission: permission is granted");
            mLocationPermissionGranted = true;
            updateLocationUI();
        }
        else
        {
            Log.d(TAG, "getLocationPermission: permission is not granted ask");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    
    
    private void showCurrentPlace()
    {
        if (mMap == null)
        {
            return;
        }
        
        if (mLocationPermissionGranted)
        {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task)
                        {
                            if (task.isSuccessful() && task.getResult() != null)
                            {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                                
                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES)
                                {
                                    count = likelyPlaces.getCount();
                                }
                                else
                                {
                                    count = M_MAX_ENTRIES;
                                }
                                
                                int i = 0;
                                
                                for (final PlaceLikelihood placeLikelihood : likelyPlaces)
                                {
                                    
                                    places.add(new com.example.chris.weekend5.model.Place(
                                            (String) placeLikelihood.getPlace().getAddress(),
                                            (String) placeLikelihood.getPlace().getAttributions(),
                                            placeLikelihood.getPlace().getId(),
                                            placeLikelihood.getPlace().getLatLng(),
                                            (String) placeLikelihood.getPlace().getName(),
                                            (String) placeLikelihood.getPlace().getPhoneNumber(),
                                            placeLikelihood.getPlace().getPlaceTypes(),
                                            placeLikelihood.getPlace().getPriceLevel(),
                                            placeLikelihood.getPlace().getRating(),
                                            placeLikelihood.getPlace().getViewport(),
                                            placeLikelihood.getPlace().getWebsiteUri()
                                    ));
                                    
                                    mMap.addMarker(new MarkerOptions()
                                            .title(places.get(i).getName())
                                            .position(places.get(i).getLatLng())
                                            .snippet(places.get(i).getAddress()));
                                    
                                    i++;
                                    if (i > (count - 1))
                                    {
                                        break;
                                    }
                                }
                                likelyPlaces.release();
                                recyclerAdapter = new RecyclerAdapter(MapsActivity.this, places);
                                recyclerView.setAdapter(recyclerAdapter);
                                
                            }
                            else
                            {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });
        }
        else
        {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");
            
            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title("Default")
                    .position(mDefaultLocation)
                    .snippet("Info"));
            
            // Prompt the user for permission.
            getLocationPermission();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        
        MenuItem item = menu.findItem(R.id.category);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, R.layout.spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem)
                {
                    case "All Categories":
                        addAllMarkers();
                        recyclerAdapter = new RecyclerAdapter(MapsActivity.this, places);
                        recyclerView.setAdapter(recyclerAdapter);
                        break;
                    case "Airport":
                        addCatMarkers(Place.TYPE_AIRPORT);
                        break;
                    case "Bank":
                        addCatMarkers(Place.TYPE_BANK);
                        break;
                    case "Book Store":
                        addCatMarkers(Place.TYPE_BOOK_STORE);
                        break;
                    case "Car Wash":
                        addCatMarkers(Place.TYPE_CAR_WASH);
                        break;
                    case "Clothing Store":
                        addCatMarkers(Place.TYPE_CLOTHING_STORE);
                        break;
                    case "Convenience Store":
                        addCatMarkers(Place.TYPE_CONVENIENCE_STORE);
                        break;
                    case "Department Store":
                        addCatMarkers(Place.TYPE_DEPARTMENT_STORE);
                        break;
                    case "Electronics Store":
                        addCatMarkers(Place.TYPE_ELECTRONICS_STORE);
                        break;
                    case "Finance":
                        addCatMarkers(Place.TYPE_FINANCE);
                        break;
                    case "Food":
                        addCatMarkers(Place.TYPE_FOOD);
                        break;
                    case "Gas Station":
                        addCatMarkers(Place.TYPE_GAS_STATION);
                        break;
                    case "Hair Care":
                        addCatMarkers(Place.TYPE_HAIR_CARE);
                        break;
                    case "Liquor Store":
                        addCatMarkers(Place.TYPE_LIQUOR_STORE);
                        break;
                    case "Park":
                        addCatMarkers(Place.TYPE_PARK);
                        break;
                    case "Restaurant":
                        addCatMarkers(Place.TYPE_RESTAURANT);
                        break;
                    case "School":
                        addCatMarkers(Place.TYPE_SCHOOL);
                        break;
                    case "Stadium":
                        addCatMarkers(Place.TYPE_STADIUM);
                        break;
                }
                
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {
            
            }
        });
        return true;
    }
    
    private void addAllMarkers()
    {
        mMap.clear();
        for (int i =0; i < places.size(); i++)
        {
            mMap.addMarker(new MarkerOptions()
                    .title(places.get(i).getName())
                    .position(places.get(i).getLatLng())
                    .snippet(places.get(i).getAddress()));
        }
    }
    
    private void addCatMarkers(int cat)
    {
        catPlaces.clear();
        for (com.example.chris.weekend5.model.Place p : places)
        {
            for (Integer i : p.getPlaceTypes())
            {
                if (i == cat)
                    catPlaces.add(p);
            }
        }
        if (catPlaces.size() == 0)
            Toast.makeText(this, "There are none near you.", Toast.LENGTH_SHORT).show();
    
        recyclerAdapter = new RecyclerAdapter(MapsActivity.this, catPlaces);
        recyclerView.setAdapter(recyclerAdapter);
        
        mMap.clear();
        for (int i =0; i < catPlaces.size(); i++)
        {
            mMap.addMarker(new MarkerOptions()
                    .title(catPlaces.get(i).getName())
                    .position(catPlaces.get(i).getLatLng())
                    .snippet(catPlaces.get(i).getAddress()));
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search:
            {
                try
                {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                }
                catch (GooglePlayServicesRepairableException e)
                {
                    // TODO: Handle the error.
                }
                catch (GooglePlayServicesNotAvailableException e)
                {
                    // TODO: Handle the error.
                }
                break;
            }
        }
        
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Place place = PlaceAutocomplete.getPlace(this, data);
                places.add(new com.example.chris.weekend5.model.Place(
                        place.getAddress().toString(),
                        (String)place.getAttributions(),
                        place.getId(),
                        place.getLatLng(),
                        place.getName().toString(),
                        place.getPhoneNumber().toString(),
                        place.getPlaceTypes(),
                        place.getPriceLevel(),
                        place.getRating(),
                        place.getViewport(),
                        place.getWebsiteUri()
                ));
                mMap.addMarker(new MarkerOptions()
                        .title(place.getName().toString())
                        .position(place.getLatLng())
                        .snippet(place.getAddress().toString()));
                
                recyclerAdapter = new RecyclerAdapter(this, places);
                recyclerView.setAdapter(recyclerAdapter);
    
                Timber.d("Place: " + place.getName());
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Timber.i(status.getStatusMessage());
                
            } else if (resultCode == RESULT_CANCELED)
            {
                // The user canceled the operation.
            }
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults)
    {
        mLocationPermissionGranted = false;
        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }
    
    private void updateLocationUI()
    {
        if (mMap == null)
        {
            return;
        }
        else
        {
            Log.d(TAG, "updateLocationUI: map is not null");
            try
            {
                if (mLocationPermissionGranted)
                {
                    Timber.d("updateLocationUI: permission granted");
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    showCurrentPlace();
                }
                else
                {
                    Timber.d("updateLocationUI: permission not granted");
                    mMap.setMyLocationEnabled(false);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    mLastKnownLocation = null;
                    getLocationPermission();
                }
            }
            catch (SecurityException e)
            {
                Timber.e("Exception: %s", e.getMessage());
            }
        }
    }
    
    
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        
        updateLocationUI();
        
        getDeviceLocation();
    }
    
    private void getDeviceLocation()
    {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try
        {
            if (mLocationPermissionGranted)
            {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener()
                {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if (task.isSuccessful())
                        {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }
                        else
                        {
                            Timber.d("Current location is null. Use defaults");
                            Timber.e("exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        }
        catch(SecurityException e)
        {
            Timber.e("Exception: %s", e.getMessage());
            Log.e("Exception: %s", e.getMessage());
        }
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        MapsApplication.get(this).clearMapsComponent();
    }
    
    @Override
    public void showError(String s)
    {
            Timber.d(s);
    }
}
