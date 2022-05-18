package com.vmm408.voznickandroid.ui.main.nav2.checksampleone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.model.CheckSampleOne
import com.vmm408.voznickandroid.model.SubCheckSampleOne
import com.vmm408.voznickandroid.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_check_sample_one.*
import kotlinx.android.synthetic.main.item_row_check_box_sample_one.view.*

class SubCheckSampleOneFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_check_sample_one
    override val TAG = "SubCheckSampleOneFragment"

    var checkSample: CheckSampleOne? = null
    var function: (() -> Unit)? = null

    private val checkSampleAdapter: SubCheckSampleOneAdapter by lazy {
        SubCheckSampleOneAdapter(checkSample?.checkList ?: ArrayList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }
        title?.text = checkSample?.name ?: ""
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = checkSampleAdapter
        }
    }

    inner class SubCheckSampleOneAdapter(private val list: ArrayList<SubCheckSampleOne>) :
        RecyclerView.Adapter<SubCheckSampleOneAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_row_check_box_sample_one, parent, false)
        )

        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(list[position])

        override fun getItemCount() = list.count()

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(subCheckSampleOne: SubCheckSampleOne) {
                itemView.text?.text = subCheckSampleOne.name
                itemView.checkbox?.setImageResource(R.drawable.selector_checkbox_sample_three_red_grey)
                itemView.checkbox?.isSelected = subCheckSampleOne.isChecked
                itemView.setOnClickListener {
                    list[adapterPosition].isChecked = !subCheckSampleOne.isChecked
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        function?.invoke()
    }
}