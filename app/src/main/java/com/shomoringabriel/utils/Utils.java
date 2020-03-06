package com.shomoringabriel.utils;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.shomoringabriel.database.DecagonDatabase;
import com.shomoringabriel.filterRepository.FilterRepository;

import static com.shomoringabriel.filterRepository.FilterRepository.countUserFilters;

public class Utils extends MultiDexApplication {

    private static Utils singletonInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        singletonInstance = this;

        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseConfig(DatabaseConfig.builder(DecagonDatabase.class)
                        .build())
                .build());
        // This instantiates DBFlow
        //FlowManager.init(new FlowConfig.Builder(this).build());

        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V); // set to verbose logging

        long l = countUserFilters();

        if( l <= 0){
            new FilterRepository()
                    .getNetworkFilterParameters();
        }

    }

    public static Context getAppContext(){
        return singletonInstance.getApplicationContext();
    }

}
