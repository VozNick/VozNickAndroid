package com.vmm408.voznickandroid.ui.main.nav2.checksampleone.withsave

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.model.CheckSampleOne
import com.vmm408.voznickandroid.model.SubCheckSampleOne
import com.vmm408.voznickandroid.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_check_sample_one.*
import kotlinx.android.synthetic.main.item_row_check_box_sample_one.view.*

class SubCheckSampleOneWithSaveFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_check_sample_one
    override val TAG = "SubCheckSampleOneWithSaveFragment"

    var checkSampleWithSave: CheckSampleOne? = null
    var function: (() -> Unit)? = null

    private val tempList = ArrayList<SubCheckSampleOne>()
    private val checkSampleAdapter: SubCheckSampleOneWithSaveAdapter by lazy { SubCheckSampleOneWithSaveAdapter() }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }
        title?.text = checkSampleWithSave?.name ?: ""
        saveButton?.apply {
            isVisible = true
            isEnabled = true
            setOnClickListener {
                checkSampleWithSave?.checkList = tempList
                activity?.onBackPressed()
            }
        }
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = checkSampleAdapter
        }
        checkSampleWithSave?.checkList
            ?.map { it.copy() }
            ?.let {
                tempList.addAll(it)
                checkSampleAdapter.notifyDataSetChanged()
            }
    }

    inner class SubCheckSampleOneWithSaveAdapter :
        RecyclerView.Adapter<SubCheckSampleOneWithSaveAdapter.Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_row_check_box_sample_one, parent, false)
        )

        override fun onBindViewHolder(holder: Holder, position: Int) =
            holder.bind(tempList[position])

        override fun getItemCount() = tempList.count()

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(subCheckSampleOne: SubCheckSampleOne) {
                itemView.text?.text = subCheckSampleOne.name
                itemView.checkbox?.setImageResource(R.drawable.selector_checkbox_sample_three_red_grey)
                itemView.checkbox?.isSelected = subCheckSampleOne.isChecked
                itemView.setOnClickListener {
                    tempList[adapterPosition].isChecked = !subCheckSampleOne.isChecked
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