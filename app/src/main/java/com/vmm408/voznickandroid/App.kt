package com.vmm408.voznickandroid

import android.app.Application
import com.vmm408.voznickandroid.di.DI
import com.vmm408.voznickandroid.di.module.AppModule
import io.realm.BuildConfig
import io.realm.Realm
import io.realm.RealmConfiguration
import toothpick.Scope
import toothpick.configuration.Configuration
import toothpick.ktp.KTP

class App : Application() {
    lateinit var scope: Scope

    override fun onCreate() {
        super.onCreate()
        initToothpick()
        initRealm()
        initAppScope()
    }

    private fun initToothpick() {
        if (BuildConfig.DEBUG) {
            KTP.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            KTP.setConfiguration(Configuration.forProduction())
        }
    }

    private fun initRealm() {
        Realm.init(this)
        val mRealmConfiguration = RealmConfiguration.Builder()
            .name(BuildConfig.LIBRARY_PACKAGE_NAME)
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build()

        Realm.getInstance(mRealmConfiguration)
        Realm.setDefaultConfiguration(mRealmConfiguration)
    }

    private fun initAppScope() {
        scope = KTP.openScope(DI.APP_SCOPE)
            .installModules(AppModule(this))
    }

    override fun onTerminate() {
        super.onTerminate()
        KTP.closeScope(scope.name)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }
}