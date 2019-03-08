package com.testsensor.pc;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VehicleActivity extends FragmentActivity implements OnMapReadyCallback {

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    private GoogleMap mMap;

    private ImageView c_img;
    private SQLiteDatabase dbReader;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    String content="";
    //MediaPlayer m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vehicle_activity);

        c_img = (ImageView) findViewById(R.id.c_img);
        //c_img.setVisibility(View.VISIBLE);

        Bitmap bitmap = Loadpic("Vehicle");
        c_img.setImageBitmap(bitmap);


        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();
        addDB();
       // Toast.makeText(getApplicationContext(), "Vehicle", Toast.LENGTH_LONG).show();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(VehicleActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(VehicleActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        //markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.yourmarkericon);
        fetchLastLocation();
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(VehicleActivity.this, currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(VehicleActivity.this);
                } else {
                    Toast.makeText(VehicleActivity.this, "No Location recorded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are Here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        //Adding the created the marker on the map
        googleMap.addMarker(markerOptions);
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void addDB(){

        Intent intent2 = getIntent();
        String thiss=intent2.getStringExtra("PreAct");

        Log.d("PreAct",thiss);
        //PreActivity=(Global)getApplication();
        //if(PreActivity.getPreActivity())

        ContentValues cv = new ContentValues();

        if(!thiss.equals("Vehicle")) {
            cv.put(NotesDB.STIME, getTime());
        }
        cv.put(NotesDB.TIME, getTime());


        //cv.put(NotesDB.ID,1);

        cv.put(NotesDB.CURRENTAC,"Vehicle");
        dbWriter.insert(NotesDB.TABLE_NAME,null,cv);

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processExtraData();
    }

    private void processExtraData(){
        addDB();

    }

    private String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String str = format.format(date);
        return  str;
    }


    public Bitmap Loadpic(String Activity){
        Bitmap bitmap = null;
        AssetManager assetManager = this.getAssets();
        try {
            InputStream inputStream = assetManager.open(Activity+".jpg");//filename是assets目录下的图片名
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
