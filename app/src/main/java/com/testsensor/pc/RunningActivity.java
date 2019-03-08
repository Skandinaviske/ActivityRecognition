package com.testsensor.pc;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RunningActivity extends Activity  {

    private ImageView c_img;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private AssetManager assetManager;
    String content="";
    MediaPlayer m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.running_activity);

        c_img = (ImageView) findViewById(R.id.c_img);
        //c_img.setVisibility(View.VISIBLE);
        m =  playRing();
       // Toast.makeText(getApplicationContext(), "Running", Toast.LENGTH_LONG).show();

        Bitmap bitmap = Loadpic("Running");
        c_img.setImageBitmap(bitmap);

        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();
        addDB();
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

    public void addDB(){

        Intent intent2 = getIntent();
        String thiss=intent2.getStringExtra("PreAct");

        Log.d("PreAct",thiss);
        //PreActivity=(Global)getApplication();
        //if(PreActivity.getPreActivity())

        ContentValues cv = new ContentValues();

        if(!thiss.equals("Running")) {
            cv.put(NotesDB.STIME, getTime());
        }
        cv.put(NotesDB.TIME, getTime());


        //cv.put(NotesDB.ID,1);

        cv.put(NotesDB.CURRENTAC,"Running");
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

    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    private MediaPlayer playRing() {
        MediaPlayer player = null;
        try {
            player = new MediaPlayer();
            assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("LS.mp3");
            player.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            player.prepare();
            player.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return player;
    }

    @Override
    public void onStop() {
        Log.d("WWWWHHHHYYYY", "onStop: ");
        super.onStop();
        //m.pause();
        //m.stop();
        m.release();
    }
}
