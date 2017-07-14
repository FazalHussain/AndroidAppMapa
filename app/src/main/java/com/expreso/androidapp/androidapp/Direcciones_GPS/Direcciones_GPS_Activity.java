package com.expreso.androidapp.androidapp.Direcciones_GPS;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.expreso.androidapp.androidapp.MainActivity;
import com.expreso.androidapp.androidapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Direcciones_GPS_Activity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener,
        LocationListener, DirectionFinderListener {

    public static final String ENDPOINT_URL = "http://52.67.125.51/";
    private static final long MINUTE = 60 * 1000;
    private GetTodos getTodos;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private static final String TAG = Direcciones_GPS_Activity.class.getSimpleName();
    ImageView image;
    ImageView image1;
    ImageView image2;
    public ArrayList<Info> InfoList = new ArrayList<>();
    private List<Todo> todos;
    private ArrayList<Marker> markersToClear;
    private boolean isGPSPointFound;
    private double distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direcciones__gps);
        //Add below line after setContentView to disable rotation.
        //    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        image = (ImageView) findViewById(R.id.gpsactual);
        image1 = (ImageView) findViewById(R.id.gpsactual1);
        image2 = (ImageView) findViewById(R.id.gpsactual2);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(Direcciones_GPS_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Direcciones_GPS_Activity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, Direcciones_GPS_Activity.this);
                Toast.makeText(getApplicationContext(), "Update GPS Position", Toast.LENGTH_SHORT).show();

            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String origin = "";
                    String destination = "";
                    ArrayList<String> destinationlist = new ArrayList<String>();
                    for(Todo todo : todos){
                        String[] geo = todo.getGeolocation().split(",");
                        double lati = Double.parseDouble(geo[0]);
                        double lngi = Double.parseDouble(geo[1]);

                        LatLng latLng = new LatLng(lati, lngi);

                        LatLng latlng_origin = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());

                        Geocoder geocoder = null;
                        List<Address> addresses_origin = null;
                        List<Address> addresses_destination = null;
                        geocoder = new Geocoder(Direcciones_GPS_Activity.this, Locale.getDefault());

                        addresses_origin = geocoder.getFromLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude(), 1);
                        addresses_destination = geocoder.getFromLocation(lati,lngi, 1);

                        origin = addresses_origin.get(0).getAddressLine(0);
                        destination = addresses_destination.get(0).getAddressLine(0);
                        destinationlist.add(destination);

                    }

                    try {
                        new DirectionFinder(Direcciones_GPS_Activity.this, origin, destinationlist).execute();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Todo todo_ = null;
                    for (Marker marker : markersToClear) {
                        marker.remove();
                    }
                    for(Todo todo : todos){
                        todo_ = todo;
                        String[] geo = todo.getGeolocation().split(",");
                        double lati = Double.parseDouble(geo[0]);
                        double lngi = Double.parseDouble(geo[1]);

                        LatLng latLng = new LatLng(lati, lngi);

                        LatLng latlng_origin = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());

                        Location originLocation = new Location("");

                        originLocation.setLatitude(mLastLocation.getLatitude());
                        originLocation.setLongitude(mLastLocation.getLongitude());

                        Location destinationLocation = new Location("");

                        destinationLocation.setLatitude(lati);
                        destinationLocation.setLongitude(lngi);

                        distance = originLocation.distanceTo(destinationLocation);



                        if(distance<=1){
                            isGPSPointFound = true;
                            break;
                        }


                    }

                    if(isGPSPointFound){
                        Log.d(TAG+"  DistanceFound:", String.valueOf(distance));
                        String[] geo = todo_.getGeolocation().split(",");
                        double lati = Double.parseDouble(geo[0]);
                        double lngi = Double.parseDouble(geo[1]);

                        LatLng latLng = new LatLng(lati, lngi);
                        Marker myMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(todo_.getRazonSocial()
                        ).snippet(todo_.getAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    }else {
                        new AlertDialog.Builder(Direcciones_GPS_Activity.this)
                                .setTitle("Alert!")
                                .setMessage("Approach at least 10 meters to the place")
                                .show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ENDPOINT_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        getTodos =retrofit.create(GetTodos.class);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        loadTodos();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
    }

    private void loadTodos() {

        Call<Result> call=getTodos.all();
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if(response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    Result resultado = response.body();
                    Log.d("Status:" , "Correct" +resultado);
                    Result result = response.body();
                    displayResult(result);

                } else {

                    Log.d("Status:" , "else Correct");
                    Toast.makeText(getApplicationContext(), "No Details Found", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

                t.printStackTrace();
                Toast.makeText(Direcciones_GPS_Activity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                Log.d("Status:" , "Failed");


            }
        });


    }

    private void displayResult(Result r) {
        markersToClear = new ArrayList<Marker>();
        if (r != null) {
            todos = r.getTodos();
            String tmp = " ";
            for (Todo todo : todos) {
                tmp += todo.getId() + "| " + todo.getRazonSocial() + " | " + todo.getGeolocation() ;
//                resulTv.setText(tmp);

                String[] geo = todo.getGeolocation().split(",");
                double lati = Double.parseDouble(geo[0]);
                double lngi = Double.parseDouble(geo[1]);

                LatLng latLng = new LatLng(lati, lngi);

                try {
                    Marker myMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(todo.getRazonSocial()
//                ).snippet(todo.getAddress()).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconsvisitblue)));
                    ).snippet(todo.getAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                    markersToClear.add(myMarker);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        } else {
//          resulTv.setText("Error Get Todos");
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
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
    public void onMapReady(GoogleMap googleMap) {

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
     //   mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(true);


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);


        }

        // Set a listener for info window events.
        mMap.setOnInfoWindowClickListener(this);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        Log.d(TAG, String.valueOf(location.getLatitude()));
        Log.d(TAG, String.valueOf(location.getLongitude()));
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        //    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.supplier));

        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    //*******

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // Podemos utilizar el snippet para colocar el codigo unico
        // para poder realizar el filtrado mediante retrofit

        Toast.makeText(this, "Info window clicked" +marker.getTitle() ,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {
        for(Route routefound : route){
            Log.d(TAG+" Distance:", String.valueOf(routefound.distance.text)+" Duration: "+routefound.duration.text);
        }

    }


    //*******
}
