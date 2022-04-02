package com.vmm408.voznickandroid.ui.main

import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.Screens
import com.vmm408.voznickandroid.ui.global.BaseActivity
import com.vmm408.voznickandroid.ui.global.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ViewPager.OnPageChangeListener,
    BottomNavigationView.OnNavigationItemSelectedListener {
    override val layoutRes = R.layout.activity_main

    private val adapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager).apply {
        addFragment(Screens.getNav1Host(), "")
        addFragment(Screens.getNav2Host(), "")
        addFragment(Screens.getNav3Host(), "")
        addFragment(Screens.getNav4Host(), "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(hostViewPager) {
            this?.adapter = this@MainActivity.adapter
            this?.addOnPageChangeListener(this@MainActivity)
            this?.offscreenPageLimit = this@MainActivity.adapter.count
        }
        with(bottomNavigation) {
            this?.setOnNavigationItemSelectedListener(this@MainActivity)
        }

        /** list of current fragments, ..just for testing **/
        supportFragmentManager.addOnBackStackChangedListener {
            println("** start **")
            for (i in 0 until supportFragmentManager.backStackEntryCount) {
                println(supportFragmentManager.getBackStackEntryAt(i).name.toString())
            }
            println("** end **")
            println()
        }
    }

    override fun onBackPressed() {
        val sfm = supportFragmentManager
        try {
            if (sfm.backStackEntryCount > adapter.count) {
                val first = sfm.fragments.lastOrNull()
                when ((first?.view?.parent as ViewGroup).id) {
                    R.id.nav1Host -> {
                        if (hostViewPager?.currentItem == 0) {
                            sfm.popBackStack()
                        }
                        hostViewPager?.currentItem = 0
                    }
                    else -> when (sfm.getBackStackEntryAt(sfm.backStackEntryCount - 1).name) {
//                        HomeFragment().TAG -> {
//                        }
                        else -> sfm.popBackStack()
                    }
                }
            } else finish()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            supportFragmentManager.popBackStack()
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav1 -> hostViewPager?.currentItem = 0
        }
        return true
    }
}