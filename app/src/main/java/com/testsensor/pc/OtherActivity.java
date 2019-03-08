package com.testsensor.pc;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OtherActivity extends Activity {

    private ImageView c_img;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    String content="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.other_activity);

        c_img = (ImageView) findViewById(R.id.c_img);
        //c_img.setVisibility(View.VISIBLE);
       // Toast.makeText(getApplicationContext(), "Other", Toast.LENGTH_LONG).show();

        Bitmap bitmap = Loadpic("Other");
        c_img.setImageBitmap(bitmap);

        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();
        addDB();
    }
    public void addDB(){

        Intent intent2 = getIntent();
        String thiss=intent2.getStringExtra("PreAct");

        Log.d("PreAct",thiss);
        //PreActivity=(Global)getApplication();
        //if(PreActivity.getPreActivity())

        ContentValues cv = new ContentValues();

        if(!thiss.equals("Other")) {
            cv.put(NotesDB.STIME, getTime());
        }
        cv.put(NotesDB.TIME, getTime());


        //cv.put(NotesDB.ID,1);

        cv.put(NotesDB.CURRENTAC,"Other");
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