package com.vmm408.voznickandroid.ui.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationBarView
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.Screens
import com.vmm408.voznickandroid.ui.global.BaseActivity
import com.vmm408.voznickandroid.ui.global.BaseFragment
import com.vmm408.voznickandroid.ui.global.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override val layoutRes = R.layout.activity_main

    private var pageAdapter = ViewPagerAdapter(supportFragmentManager).apply {
        addFragment(Screens.Nav1.getHomeScreen(), "")
        addFragment(Screens.Nav2.getUserFieldsScreen(), "")
        addFragment(Screens.Nav3.getCollapsingToolbarSampleOneScreen(), "")
//        addFragment(Screens.Hosts.getFeedHost(), "")
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
//            when (position) {
//                0 -> (findFragment(HomeFragment().TAG) as? HomeFragment)?.updateHomeData()
////                1 -> (findFragment(ClinicsFragment().TAG) as? ClinicsFragment)
//                2 -> (findFragment(StoreFragment().TAG) as? StoreFragment)?.updateStoreData()
//                3 -> (findFragment(FeedFragment().TAG) as? FeedFragment)?.updateFeedData()
//            }
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

    private val onNavigationItemListener = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav1 -> viewPager?.currentItem = 0
            R.id.nav2 -> viewPager?.currentItem = 1
            R.id.nav3 -> viewPager?.currentItem = 2
            R.id.nav4 -> viewPager?.currentItem = 3
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewPager?.apply {
            adapter = pageAdapter
            offscreenPageLimit = pageAdapter.count
            addOnPageChangeListener(onPageChangeListener)
        }
        bottomNavigation?.setOnItemSelectedListener(onNavigationItemListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager?.removeOnPageChangeListener(onPageChangeListener)
    }

    override fun onBackPressed() {
        val sfm = supportFragmentManager

        if (sfm.backStackEntryCount > pageAdapter.count) {
            val first = sfm.fragments.lastOrNull()
            when ((first?.view?.parent as? ViewGroup)?.id) {
                R.id.nav1 -> {
                    if (viewPager?.currentItem == 0) {
                        sfm.popBackStack()
                    }
                    viewPager?.currentItem = 0
                }
                R.id.nav2 -> {
                    if (viewPager?.currentItem == 1) {
                        sfm.popBackStack()
                    }
                    viewPager?.currentItem = 1
                }
                R.id.nav3 -> {
                    if (viewPager?.currentItem == 2) {
                        sfm.popBackStack()
                    }
                    viewPager?.currentItem = 2
                }
                R.id.nav4 -> {
                    if (viewPager?.currentItem == 3) {
                        sfm.popBackStack()
                    }
                    viewPager?.currentItem = 3
                }

                else -> first?.tag.let {
                    when (sfm.getBackStackEntryAt(sfm.backStackEntryCount - 1).name) {
//                            "WorkoutVerticalVideoFragment" -> {
//                                val oft =
//                                    (sfm.findFragmentByTag("WorkoutVerticalVideoFragment") as WorkoutVerticalVideoFragment)
//                                        .openFirstTime
//                                if (oft) sfm.popBackStack()
//                            }
//                            "MultiplayerVerticalVideoFragment" -> {
//                                val oft =
//                                    (sfm.findFragmentByTag("MultiplayerVerticalVideoFragment") as MultiplayerVerticalVideoFragment)
//                                        .openFirstTime
//                                if (oft) sfm.popBackStack()
//                            }
//                            "FreestyleInProgressFragment" -> {
//                            }
//                            "InviteFragment" -> {
//                                (sfm.findFragmentByTag("OnDemandFragment") as OnDemandFragment).checkInfoOnDemand()
//                                sfm.popBackStack()
//                            }
//                            "WorkoutStartFragment" -> {
//                                (sfm.findFragmentByTag("OnDemandFragment") as OnDemandFragment).checkInfoOnDemand()
//                                sfm.popBackStack()
//                            }
//                            "WorkoutStartFragment" -> {
//                                (sfm.findFragmentByTag("WorkoutPackInfoFragment") as WorkoutPackInfoFragment).getCategory()
//                                sfm.popBackStack()
//                            }
//                            "WorkoutStartFragment" -> sfm.popBackStack()
                        else -> sfm.popBackStack()
                    }
                }
            }
        } else finish()
    }
}