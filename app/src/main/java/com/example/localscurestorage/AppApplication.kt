package com.example.localscurestorage

import android.app.Application
import com.example.localscurestorage.DI.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module


class AppApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            module {
               databaseModule
            }

        }
    }
}