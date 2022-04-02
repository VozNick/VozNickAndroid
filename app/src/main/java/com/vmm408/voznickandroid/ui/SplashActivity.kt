package com.vmm408.voznickandroid.ui

import android.content.Intent
import android.os.Bundle
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseActivity
import com.vmm408.voznickandroid.ui.main.MainActivity

class SplashActivity : BaseActivity() {
    override val layoutRes = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}