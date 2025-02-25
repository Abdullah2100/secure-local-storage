package com.example.localscurestorage

import android.app.Application
import com.example.localscurestorage.DI.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class AppApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            modules(databaseModule) // Make sure to include your module
        }
    }
}