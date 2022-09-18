package com.vmm408.voznickandroid.ui.main.nav1

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
//    ADD_RECYCLER_ADAPTER("Add Recycler adapter", Screens.Nav1Host.)

    BAR_CHARTS("Bar charts", Screens.Nav1.getBarChartsScreen()),
}

class HomeFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_home
    override val TAG = "HomeFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listContainer?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = MainListAdapter()
        }
    }

    inner class MainListAdapter : RecyclerView.Adapter<MainListAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context).inflate(R.layout.view_item_single_line, parent, false)
        )

        override fun onBindViewHolder(holder: Holder, position: Int) =
            holder.bind(FragmentList.values()[position])

        override fun getItemCount() = FragmentList.values().size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(fragment: FragmentList) {
                itemView.findViewById<TextView>(R.id.title)?.text = fragment.simpleName
                itemView.setOnClickListener {
                    replace(android.R.id.content, fragment.destination)
                }
            }
        }
    }
}