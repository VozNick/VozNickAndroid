package com.vmm408.voznickandroid.ui.global

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rd.PageIndicatorView

/** view pager with fragments for activity **/

class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int) = fragmentList[position]

    override fun getCount() = fragmentList.size

    override fun getPageTitle(position: Int) = fragmentTitleList[position]

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    fun getFragments() = fragmentList

    fun addTitle(title: String) = fragmentTitleList.add(title)

    fun removeTitles() = fragmentTitleList.clear()
}


/** view pager with fragments **/

class ViewPagerStateAdapter(
    hostFragment: Fragment,
    private val fragmentList: ArrayList<Fragment>
) : FragmentStateAdapter(hostFragment) {
    override fun getItemCount() = fragmentList.count()
    override fun createFragment(position: Int) = fragmentList[position]
}

fun ViewPagerStateAdapter.initViewPager(
    viewPager: ViewPager2?,
    onPageChangeCallback: ((pageSelected: Int) -> Unit)?
) {
    viewPager?.adapter = this
    viewPager?.offscreenPageLimit = this.itemCount
    viewPager?.registerOnPageChangeCallback(null, onPageChangeCallback)
}

fun ViewPagerStateAdapter.initViewPagerWithPageIndicator(
    viewPager: ViewPager2?,
    pageIndicatorView: PageIndicatorView?,
    onPageChangeCallback: ((pageSelected: Int) -> Unit)?
) {
    viewPager?.adapter = this
    viewPager?.offscreenPageLimit = this.itemCount
    pageIndicatorView?.initPageIndicatorView(this.itemCount)
    viewPager?.registerOnPageChangeCallback(pageIndicatorView, onPageChangeCallback)
}


/** view pager with fragments and tabs **/

class ViewPagerWithTitleStateAdapter(
    hostFragment: Fragment,
    internal val titleList: Array<String>,
    private val fragmentList: Array<Fragment>
) : FragmentStateAdapter(hostFragment) {
    override fun getItemCount() = fragmentList.count()
    override fun createFragment(position: Int) = fragmentList[position]
}

fun ViewPagerWithTitleStateAdapter.initViewPagerWithTabs(
    viewPager: ViewPager2?,
    tabLayout: TabLayout?,
    onPageChangeCallback: ((pageSelected: Int) -> Unit)?
) {
    viewPager?.adapter = this
    viewPager?.offscreenPageLimit = this.itemCount
    viewPager?.registerOnPageChangeCallback(null, onPageChangeCallback)

    if (null != tabLayout && null != viewPager) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.id = position
            tab.text = titleList[position]
        }.attach()
    }
}


/** view pager with views (recyclerView) **/

fun RecyclerView.Adapter<out RecyclerView.ViewHolder>.initViewPagerWithPageIndicator(
    viewPager: ViewPager2?,
    pageIndicatorView: PageIndicatorView?,
    onPageChangeCallback: ((pageSelected: Int) -> Unit)?
) {
    viewPager?.adapter = this
    pageIndicatorView?.initPageIndicatorView(this.itemCount)
    viewPager?.registerOnPageChangeCallback(pageIndicatorView, onPageChangeCallback)
}

private fun PageIndicatorView.initPageIndicatorView(itemCount: Int) {
    if (itemCount > 1) count = itemCount
    else isVisible = false
}

private fun ViewPager2.registerOnPageChangeCallback(
    pageIndicatorView: PageIndicatorView?,
    onPageChangeCallback: ((pageSelected: Int) -> Unit)?
) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            pageIndicatorView?.setSelected(position)
            onPageChangeCallback?.invoke(position)
        }
    })
}