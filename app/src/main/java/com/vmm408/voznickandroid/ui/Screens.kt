package com.vmm408.voznickandroid.ui

import com.vmm408.voznickandroid.ui.main.nav1.HomeFragment
import com.vmm408.voznickandroid.ui.main.nav1.Nav1Host
import com.vmm408.voznickandroid.ui.main.nav1.adgooglesignin.AddGoogleSignInFragment
import com.vmm408.voznickandroid.ui.main.nav1.hrzonefragment.HrZoneFragment
import com.vmm408.voznickandroid.ui.main.nav1.setphotofragment.SetPhotoFragment
import com.vmm408.voznickandroid.ui.main.nav2.Nav2Host
import com.vmm408.voznickandroid.ui.main.nav2.UserFieldsFragment
import com.vmm408.voznickandroid.ui.main.nav3.Nav3Host
import com.vmm408.voznickandroid.ui.main.nav4.Nav4Host

object Screens {
    //    fun getOnBoardingScreen() = OnBoardingFragment.newInstance()
//    fun getOnBoardingStepFragment(position: Int) = OnBoardingStepFragment.newInstance().apply {
//        this.position = position
//    }

    fun getNav1Host() = Nav1Host.newInstance()
    fun getNav2Host() = Nav2Host.newInstance()
    fun getNav3Host() = Nav3Host.newInstance()
    fun getNav4Host() = Nav4Host.newInstance()

    fun getHomeScreen() = HomeFragment.newInstance()

    fun getHrZoneScreen() = HrZoneFragment.newInstance()
    fun getSetPhotoScreen() = SetPhotoFragment.newInstance()
    fun getGoogleSignInScreen() = AddGoogleSignInFragment.newInstance()

    fun getUserFieldsScreen() = UserFieldsFragment.newInstance()
}