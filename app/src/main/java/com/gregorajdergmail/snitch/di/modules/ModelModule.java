package com.gregorajdergmail.snitch.di.modules;

import com.gregorajdergmail.snitch.model.FileHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {

    @Provides
    @Singleton
    FileHelper provideFileHelper() {
        return new FileHelper();
    }


}
