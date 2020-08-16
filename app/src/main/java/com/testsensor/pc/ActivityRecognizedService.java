package com.testsensor.pc;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ActivityRecognizedService extends IntentService {

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }


    private Global PreActivity;
    private SQLiteDatabase dbReader;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    String content="";
    //String PreActivity="";
    @Override
    protected void onHandleIntent(Intent intent) {

        notesDB = new NotesDB(this);
        //dbWriter = notesDB.getWritableDatabase();
        dbReader = notesDB.getReadableDatabase();

        String activity="";
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            Log.d("SSS",result.getMostProbableActivity().getType()+"");
            switch(result.getMostProbableActivity().getType()){
                case 0:
                    activity="Vehicle";
                    break;
                case 1:
                    activity="Walking";
                    break;
                case 2:
                    activity="Walking";
                    break;
                case 4:
                    activity="Walking";
                    break;
                case 5:
                    activity="Walking";
                    break;
                case 8:
                    activity="Running";
                    break;
                case 3:
                    activity="Still";
                    break;
                case 7:
                    activity="Walking";

            }

            PreActivity=(Global)getApplication();

            if(!activity.equals(PreActivity.getPreActivity())) {

                Cursor cursor = dbReader.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);
                cursor.moveToPosition(cursor.getCount());
                //cursor.moveToPrevious();
                while(cursor.moveToPrevious())
                {
                    if(!cursor.getString(cursor.getColumnIndex(NotesDB.STIME)).isEmpty()) {
                        content = cursor.getString(cursor.getColumnIndex(NotesDB.STIME));
                        cursor.close();
                        System.out.println("Start time g MM: " + getTime());
                        System.out.println("Start time: " + content);
                        Date previousdate= strToDateLong(content);
                        Date nowdate= strToDateLong(getTime());
                        long diff = nowdate.getTime() - previousdate.getTime();//Subtle level difference
                        long days = diff / (1000 * 60 * 60 * 24);
                        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                        long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
                        long seconds = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)-minutes*(60*1000))/(1000);
                        if(!PreActivity.getPreActivity().equals("s"))
                        Toast.makeText(getApplicationContext(), PreActivity.getPreActivity()+" for"+hours+"hours"+minutes+"minutes"+seconds+"seconds", Toast.LENGTH_LONG).show();
                        break;
                    }
                }

                if (activity.equals("Walking")) {
                    Intent intentW = new Intent(this, WalkingActivity.class);
                    intentW.putExtra("PreAct",PreActivity.getPreActivity());
                    startActivity(intentW);
                }

                if (activity.equals("Running")){
                    Intent intentR = new Intent(this, RunningActivity.class);
                    intentR.putExtra("PreAct",PreActivity.getPreActivity());
                    startActivity(intentR);
                }

                if (activity.equals("Still")) {
                    Intent intentS = new Intent(this, StillActivity.class);
                    intentS.putExtra("PreAct",PreActivity.getPreActivity());
                    //Log.d("ZAHUISHI","ZAZAZAZAZAZ");
                    startActivity(intentS);
                }

                if (activity.equals("Vehicle")) {
                    Intent intentV = new Intent(this, VehicleActivity.class);
                    intentV.putExtra("PreAct",PreActivity.getPreActivity());
                    startActivity(intentV);
                }

                if(activity.equals("Other")){
                    Intent intentO = new Intent(this,OtherActivity.class);
                    intentO.putExtra("PreAct",PreActivity.getPreActivity());
                    startActivity(intentO);
                }

            }

            PreActivity.setLabel(activity);
            handleDetectedActivities(result.getProbableActivities());


        }
    }


    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        //int A[] = new int[4];
        for( DetectedActivity activity : probableActivities ) {
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e( "ActivityRecogition", "In Vehicle: " + activity.getConfidence() );
                    //A[0]=activity.getConfidence();
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Log.e( "ActivityRecogition", "On Bicycle: " + activity.getConfidence() );
                    //A[1]=activity.getConfidence();
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    Log.e( "ActivityRecogition", "On Foot: " + activity.getConfidence() );
                    //A[2]=activity.getConfidence();
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e( "ActivityRecogition", "Running: " + activity.getConfidence() );
                    //A[1]=activity.getConfidence();
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e( "ActivityRecogition", "Still: " + activity.getConfidence() );
                    //A[2]=activity.getConfidence();
                    break;
                }
                case DetectedActivity.TILTING: {
                    Log.e( "ActivityRecogition", "Tilting: " + activity.getConfidence() );
                    //A[5]=activity.getConfidence();
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e( "ActivityRecogition", "Walking: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 5 ) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            Notification notification = new NotificationCompat.Builder(this, "chat")
                                    .setContentTitle("Hi")
                                    .setContentText("Are you walking?" )
                                    .setWhen(System.currentTimeMillis())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                    .setAutoCancel(true)
                                    .build();
                            manager.notify(1, notification);
                        }
                        System.out.println("NCCACA");
                    }
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.e( "ActivityRecogition", "Unknown: " + activity.getConfidence() );
                    //A[7]=activity.getConfidence();
                    break;
                }
            }
        }
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

    public static int getMax(int A[]) {
        int maxnum=0;
        int comparenum= A[0];
        int i=0;
        while(i<A.length){
            //System.out.println("NWPU");
            if(A[i]>comparenum) {
                comparenum = A[i];
                maxnum = i;
            }
            i++;
        }
        return maxnum;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

}
