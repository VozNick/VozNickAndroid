package com.vmm408.voznickandroid.ui.main.nav1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.Screens
import com.vmm408.voznickandroid.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*

enum class FragmentList(val simpleName: String, val destination: BaseFragment) {
    HR_ZONE_PROGRESS_BAR("HrZone Progress bar", Screens.Nav1.getHrZoneScreen()),
    SET_PHOTO_INTO_VIEW("Set photo into view", Screens.Nav1.getSetPhotoScreen()),
    SET_PHOTO_SAMPLE_TWO_INTO_VIEW(
        "Set photo sample two into view",
        Screens.Nav1.getSetPhotoSampleTwoScreen()
    ),
    ADD_GOOGLE_SIGN_IN("Add Google Sign In", Screens.Nav1.getGoogleSignInScreen()),

    BAR_CHARTS("Bar charts", Screens.Nav1.getBarChartsScreen()),
    OBSERVER_PATTERN("Observer pattern", Screens.Nav1.getObserverPatternScreen()),
    ACTION_BUTTON("Action button", Screens.Nav1.getActionButtonScreen()),
    CONVERT_LOCAL_TIME("Convert local time", Screens.Nav1.getConvertLocalTimeScreen()),
    SHARED_PREFERENCES("Shared preferences", Screens.Nav1.getSharedPreferencesScreen()),
    COROUTINE_TIMER("Coroutine timer", Screens.Nav1.getCoroutineTimerScreen())
}

class HomeFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_home
    override val TAG = "HomeFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentList.values().forEach { fragment ->
            LayoutInflater.from(context).inflate(R.layout.view_item_single_line, rootView, false)
                ?.apply {
                    findViewById<TextView>(R.id.title)?.text = fragment.simpleName
                    setOnClickListener {
                        replace(android.R.id.content, fragment.destination)
                    }
                }
                ?.also {
                    rootView?.addView(it)
                }
        }
    }
}