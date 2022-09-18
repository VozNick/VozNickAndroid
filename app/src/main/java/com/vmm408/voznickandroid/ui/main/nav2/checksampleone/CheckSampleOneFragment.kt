package com.vmm408.voznickandroid.ui.main.nav2.checksampleone

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.model.CheckSampleOne
import com.vmm408.voznickandroid.ui.Screens
import com.vmm408.voznickandroid.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_check_sample_one.*
import kotlinx.android.synthetic.main.item_row_check_box_sample_one.view.*

class CheckSampleOneFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_check_sample_one
    override val TAG = "CheckSampleOneFragment"

    var list: ArrayList<CheckSampleOne>? = null
    var function: (() -> Unit)? = null

    private val checkSampleAdapter: CheckSampleOneAdapter by lazy {
        CheckSampleOneAdapter(list ?: ArrayList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }
        title?.text = ""
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = checkSampleAdapter
        }
    }

    inner class CheckSampleOneAdapter(private val list: ArrayList<CheckSampleOne>) :
        RecyclerView.Adapter<CheckSampleOneAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_row_check_box_sample_one, parent, false)
        )

        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(list[position])

        override fun getItemCount() = list.count()

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("NotifyDataSetChanged")
            fun bind(checkSampleOne: CheckSampleOne) {
                itemView.text?.text = checkSampleOne.name
                itemView.checkbox?.setImageResource(R.drawable.selector_checkbox_level_list_sample_one_red_grey)
                itemView.checkbox?.setImageLevel(
                    when (checkSampleOne.checkList?.count { it.isChecked }) {
                        0 -> 0
                        checkSampleOne.checkList?.size -> 2
                        else -> 1
                    }
                )
//                itemView.checkbox?.isSelected =
//                    null != checkSampleOne.checkList?.firstOrNull { it.isChecked }
                itemView.setOnClickListener {
                    add(android.R.id.content, Screens.Nav2.getSubCheckSampleOneScreen(checkSampleOne) {
                        notifyDataSetChanged()
                    })
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        function?.invoke()
    }
}