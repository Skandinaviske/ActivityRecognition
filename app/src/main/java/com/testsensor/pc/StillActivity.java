package com.testsensor.pc;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StillActivity extends FragmentActivity implements OnMapReadyCallback {

    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private ImageView c_img;
    private GoogleMap map;
    LocationManager locationManager;
    LocationListener locationListener;
    Marker marker;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Global PreActivity;

    private AssetManager assetManager;
    MediaPlayer m;
    String content="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("诺克萨斯之手","RRRRSSS");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.still_activity);

        c_img = (ImageView) findViewById(R.id.c_img);
        //c_img.setVisibility(View.VISIBLE);

        //m =    playRing();
        //m.start();

        Bitmap bitmap = Loadpic("Still");
        c_img.setImageBitmap(bitmap);

        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();

        //Toast.makeText(getApplicationContext(), "Still", Toast.LENGTH_LONG).show();

        addDB();
        /*
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                //get the location name from latitude and longitude
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addresses =
                            geocoder.getFromLocation(latitude, longitude, 1);
                    String result = addresses.get(0).getLocality()+":";
                    result += addresses.get(0).getCountryName();
                    LatLng latLng = new LatLng(latitude, longitude);
                    if (marker != null){
                        marker.remove();
                        marker = map.addMarker(new MarkerOptions().position(latLng).title(result));
                        //map.setMaxZoomPreference(20);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                    }
                    else{
                        marker = map.addMarker(new MarkerOptions().position(latLng).title(result));
                        //map.setMaxZoomPreference(20);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    */
    }


    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processExtraData();
         }

    private void processExtraData(){
        addDB();
    }

    public void addDB(){
        Intent intent2 = getIntent();
        String thiss=intent2.getStringExtra("PreAct");
        Log.d("PreAct",thiss);
        ContentValues cv = new ContentValues();
        if(!thiss.equals("Still")) {
            cv.put(NotesDB.STIME, getTime());
        }
        cv.put(NotesDB.TIME, getTime());
        cv.put(NotesDB.CURRENTAC,"Still");
        dbWriter.insert(NotesDB.TABLE_NAME,null,cv);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }


//    private MediaPlayer playRing() {
//        MediaPlayer player = null;
//        try {
//            player = new MediaPlayer();
//            assetManager = getAssets();
//            AssetFileDescriptor fileDescriptor = assetManager.openFd("LS.mp3");
//            player.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
//                    fileDescriptor.getLength());
//            player.prepare();
//            player.start();
//           // player.setOnCompletionListener(onCompletionListener);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return player;
//    }

//    @Override
//    public void onStop() {
//        Log.d("WWWWHHHHYYYY", "onStop: ");
//        super.onStop();
//        //m.pause();
//        //m.stop();
//        m.release();
//    }

    private String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String str = format.format(date);
        return  str;
    }

}
