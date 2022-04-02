package com.vmm408.voznickandroid.di.module

import android.content.Context
import io.realm.Realm
import toothpick.config.Module

class AppModule(context: Context) : Module() {
    init {
        bind(Context::class.java).toInstance(context)
        bind(Realm::class.java).toInstance(Realm.getDefaultInstance())
    }
}