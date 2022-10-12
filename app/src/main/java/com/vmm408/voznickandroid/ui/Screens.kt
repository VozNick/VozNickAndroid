package com.vmm408.voznickandroid.ui

import com.vmm408.voznickandroid.model.CheckSampleOne
import com.vmm408.voznickandroid.ui.main.nav1.HomeFragment
import com.vmm408.voznickandroid.ui.main.nav1.actionbutton.CustomActionButtonFragment
import com.vmm408.voznickandroid.ui.main.nav2.NumberIncreaseModalBottomSheet
import com.vmm408.voznickandroid.ui.main.nav1.adgooglesignin.AddGoogleSignInFragment
import com.vmm408.voznickandroid.ui.main.nav1.charts.BarChartsWithTabsFragment
import com.vmm408.voznickandroid.ui.main.nav1.hrzonefragment.HrZoneFragment
import com.vmm408.voznickandroid.ui.main.nav1.localtime.ConvertLocalTimeFragment
import com.vmm408.voznickandroid.ui.main.nav1.observerpattern.ObserverPatternFragment
import com.vmm408.voznickandroid.ui.main.nav1.setphotofragment.SetPhotoFragment
import com.vmm408.voznickandroid.ui.main.nav1.setphotosampletwofragment.SetPhotoSampleTwoFragment
import com.vmm408.voznickandroid.ui.main.nav1.sharedPreferences.SharedPreferencesFragment
import com.vmm408.voznickandroid.ui.main.nav1.timerandcoroutine.CoroutineTimerFragment
import com.vmm408.voznickandroid.ui.main.nav2.checksampleone.CheckSampleOneFragment
import com.vmm408.voznickandroid.ui.main.nav2.UserFieldsFragment
import com.vmm408.voznickandroid.ui.main.nav2.checksampleone.withsave.CheckSampleOneWithSaveFragment
import com.vmm408.voznickandroid.ui.main.nav2.checksampleone.SubCheckSampleOneFragment
import com.vmm408.voznickandroid.ui.main.nav2.checksampleone.withsave.SubCheckSampleOneWithSaveFragment
import com.vmm408.voznickandroid.ui.main.nav3.CollapsingToolbarSampleOneFragment
import com.vmm408.voznickandroid.ui.main.nav3.CollapsingToolbarSampleTwoFragment

object Screens {
    //    fun getOnBoardingScreen() = OnBoardingFragment.newInstance()
//    fun getOnBoardingStepFragment(position: Int) = OnBoardingStepFragment.newInstance().apply {
//        this.position = position
//    }

//    object Hosts {
//        fun getNav1Host() = Nav1Host()
//        fun getNav2Host() = Nav2Host()
//        fun getNav3Host() = Nav3Host()
//        fun getNav4Host() = Nav4Host()
//    }

    object Nav1 {
        fun getHomeScreen() = HomeFragment()

        fun getHrZoneScreen() = HrZoneFragment()
        fun getSetPhotoScreen() = SetPhotoFragment()
        fun getSetPhotoSampleTwoScreen() = SetPhotoSampleTwoFragment()
        fun getGoogleSignInScreen() = AddGoogleSignInFragment()
        fun getBarChartsScreen() = BarChartsWithTabsFragment()
        fun getObserverPatternScreen() = ObserverPatternFragment()
        fun getActionButtonScreen() = CustomActionButtonFragment()
        fun getConvertLocalTimeScreen() = ConvertLocalTimeFragment()
        fun getSharedPreferencesScreen() = SharedPreferencesFragment()
        fun getCoroutineTimerScreen() = CoroutineTimerFragment()
    }

    object Nav2 {
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
        ) = SubCheckSampleOneWithSaveFragment().apply {
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

        fun getNumberIncreasePicker(
            numberListRange: IntRange,
            lastValue: Int,
            callBackNewValue: (newValue: Int) -> Unit
        ) = NumberIncreaseModalBottomSheet().apply {
            this.valueListRange = numberListRange
            this.lastValue = lastValue
            this.callback = callBackNewValue
        }
    }

    object Nav3 {
        fun getCollapsingToolbarSampleOneScreen() = CollapsingToolbarSampleOneFragment()
        fun getCollapsingToolbarSampleTwoScreen() = CollapsingToolbarSampleTwoFragment()
    }

    object Nav4 {}
}