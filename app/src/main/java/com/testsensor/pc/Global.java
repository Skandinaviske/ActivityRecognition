package com.testsensor.pc;

import android.app.Application;

public class Global extends Application {
    private String PreActivity="";

    public String getPreActivity(){
        return PreActivity;
    }

    public void setLabel(String s){
        this.PreActivity = s;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        setLabel("s"); //初始化全局变量
    }
}
