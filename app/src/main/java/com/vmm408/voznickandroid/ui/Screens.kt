package com.vmm408.voznickandroid.ui

import com.vmm408.voznickandroid.model.CheckSampleOne
import com.vmm408.voznickandroid.ui.main.nav1.HomeFragment
import com.vmm408.voznickandroid.ui.main.nav1.Nav1Host
import com.vmm408.voznickandroid.ui.main.nav1.adgooglesignin.AddGoogleSignInFragment
import com.vmm408.voznickandroid.ui.main.nav1.hrzonefragment.HrZoneFragment
import com.vmm408.voznickandroid.ui.main.nav1.setphotofragment.SetPhotoFragment
import com.vmm408.voznickandroid.ui.main.nav1.setphotosampletwofragment.SetPhotoSampleTwoFragment
import com.vmm408.voznickandroid.ui.main.nav2.checksampleone.CheckSampleOneFragment
import com.vmm408.voznickandroid.ui.main.nav2.Nav2Host
import com.vmm408.voznickandroid.ui.main.nav2.UserFieldsFragment
import com.vmm408.voznickandroid.ui.main.nav2.checksampleone.withsave.CheckSampleOneWithSaveFragment
import com.vmm408.voznickandroid.ui.main.nav2.checksampleone.SubCheckSampleOneFragment
import com.vmm408.voznickandroid.ui.main.nav2.checksampleone.withbottomsheet.CheckSampleOneWithSaveBottomSheet
import com.vmm408.voznickandroid.ui.main.nav2.checksampleone.withsave.SubCheckSampleOneWithSaveFragment
import com.vmm408.voznickandroid.ui.main.nav3.CollapsingToolbarSampleOneFragment
import com.vmm408.voznickandroid.ui.main.nav3.CollapsingToolbarSampleTwoFragment
import com.vmm408.voznickandroid.ui.main.nav3.Nav3Host
import com.vmm408.voznickandroid.ui.main.nav4.Nav4Host

object Screens {
    //    fun getOnBoardingScreen() = OnBoardingFragment.newInstance()
//    fun getOnBoardingStepFragment(position: Int) = OnBoardingStepFragment.newInstance().apply {
//        this.position = position
//    }

    object Hosts {
        fun getNav1Host() = Nav1Host()
        fun getNav2Host() = Nav2Host()
        fun getNav3Host() = Nav3Host()
        fun getNav4Host() = Nav4Host()
    }

    object Nav1Host {
        fun getHomeScreen() = HomeFragment()

        fun getHrZoneScreen() = HrZoneFragment()
        fun getSetPhotoScreen() = SetPhotoFragment()
        fun getSetPhotoSampleTwoScreen() = SetPhotoSampleTwoFragment()
        fun getGoogleSignInScreen() = AddGoogleSignInFragment()
    }

    object Nav2Host {
        fun getUserFieldsScreen() = UserFieldsFragment()

        fun getCheckSampleOneScreen(list: ArrayList<CheckSampleOne>, function: () -> Unit) =
            CheckSampleOneFragment().apply {
                this.list = list
                this.function = function
            }

        fun getSubCheckSampleOneScreen(checkSampleOne: CheckSampleOne, function: () -> Unit) =
            SubCheckSampleOneFragment().apply {
                this.checkSample = checkSampleOne
                this.function = function
            }

        fun getCheckSampleOneWithSaveScreen(list: ArrayList<CheckSampleOne>, function: () -> Unit) =
            CheckSampleOneWithSaveFragment().apply {
                this.list = list
                this.function = function
            }

        fun getSubCheckSampleOneWithSaveScreen(
            checkSampleOne: CheckSampleOne,
            function: () -> Unit
        ) =
            SubCheckSampleOneWithSaveFragment().apply {
                this.checkSampleWithSave = checkSampleOne
                this.function = function
            }

//        fun getCheckSampleOneWithSaveBottomSheetScreen(
//            checkSampleOne: CheckSampleOne,
//            function: () -> Unit
//        ) =
//            CheckSampleOneWithSaveBottomSheet().apply {
//                this.checkSampleWithSave = checkSampleOne
//                this.function = function
//            }
    }

    object Nav3Host {
        fun getCollapsingToolbarSampleOneScreen() = CollapsingToolbarSampleOneFragment()
        fun getCollapsingToolbarSampleTwoScreen() = CollapsingToolbarSampleTwoFragment()
    }

    object Nav4Host {}
}