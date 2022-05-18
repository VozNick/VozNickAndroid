package com.vmm408.voznickandroid.ui.main.nav1

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.Screens
import com.vmm408.voznickandroid.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*

enum class FragmentList(val simpleName: String, val destination: BaseFragment) {
    HR_ZONE_PROGRESS_BAR("HrZone Progress bar", Screens.Nav1Host.getHrZoneScreen()),
    SET_PHOTO_INTO_VIEW("Set photo into view", Screens.Nav1Host.getSetPhotoScreen()),
    SET_PHOTO_SAMPLE_TWO_INTO_VIEW("Set photo sample two into view", Screens.Nav1Host.getSetPhotoSampleTwoScreen()),
    ADD_GOOGLE_SIGN_IN("Add Google Sign In", Screens.Nav1Host.getGoogleSignInScreen())
}

class HomeFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_home
    override val TAG = "HomeFragment"

    private val listAdapter: MainListAdapter by lazy {
        MainListAdapter(activity) { destination -> replace(android.R.id.content, destination) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(listContainer) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.setHasFixedSize(true)
            this?.adapter = listAdapter
        }
    }
}