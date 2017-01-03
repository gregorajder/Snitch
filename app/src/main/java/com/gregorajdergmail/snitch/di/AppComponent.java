package com.gregorajdergmail.snitch.di;


import com.gregorajdergmail.snitch.di.modules.ModelModule;
import com.gregorajdergmail.snitch.model.MyFileObserver;
import com.gregorajdergmail.snitch.model.NotificationService;
import com.gregorajdergmail.snitch.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ModelModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);
    void inject(MyFileObserver myFileObserver);
    void inject(NotificationService notificationService);
}

