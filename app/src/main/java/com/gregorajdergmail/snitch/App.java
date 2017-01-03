package com.gregorajdergmail.snitch;

import android.app.Application;

import com.gregorajdergmail.snitch.di.AppComponent;
import com.gregorajdergmail.snitch.di.DaggerAppComponent;


public class App extends Application {

    private static AppComponent appComponent;

    public static AppComponent getComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.
                builder().build();

    }
}
