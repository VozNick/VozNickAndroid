package com.vmm408.voznickandroid.ui.main.nav1

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment
import kotlinx.android.synthetic.main.view_item_single_line.view.*

class MainListAdapter(private val activity: Activity?, private val listener: OnClickListener) :
    RecyclerView.Adapter<MainListAdapter.Holder>() {
    private val list = FragmentList.values()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(activity).inflate(R.layout.view_item_single_line, parent, false)
    )

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(fragment: FragmentList) {
            itemView.title?.text = fragment.simpleName
            itemView.setOnClickListener { listener.onClick(fragment.destination) }
        }
    }

    class OnClickListener(val clickListener: (destination: BaseFragment) -> Unit) {
        fun onClick(destination: BaseFragment) = clickListener(destination)
    }
}