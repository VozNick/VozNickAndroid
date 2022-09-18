package com.vmm408.voznickandroid.ui.main.nav2.checksampleone.withbottomsheet

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.model.CheckSampleOne
import com.vmm408.voznickandroid.ui.Screens
import com.vmm408.voznickandroid.ui.global.BaseBottomSheetFragment
import kotlinx.android.synthetic.main.fragment_check_sample_one.*
import kotlinx.android.synthetic.main.item_row_check_box_sample_one.view.*

class CheckSampleOneWithSaveBottomSheet : BaseBottomSheetFragment() {
    override val layoutRes=R.layout.bottom_sheet_check_sample_one_with_save
    override val TAG = "CheckSampleOneWithSaveBottomSheet"

    var list: ArrayList<CheckSampleOne>? = null
    var function: (() -> Unit)? = null

    private val tempList = ArrayList<CheckSampleOne>()
    private val checkSampleAdapter: CheckSampleOneWithSaveAdapter by lazy { CheckSampleOneWithSaveAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        initLayoutListener()
        backButton?.setOnClickListener { activity?.onBackPressed() }
        title?.text = ""
        saveButton?.apply {
            isVisible = true
            isEnabled = true
            setOnClickListener {
                list?.clear()
                list?.addAll(tempList)
                activity?.onBackPressed()
            }
        }
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = checkSampleAdapter
        }
        list?.map { it.copy() }?.let {
            tempList.addAll(it)
            checkSampleAdapter.notifyDataSetChanged()
        }
    }

    private fun initLayoutListener() {
        (view?.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
        view?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                val dialog = dialog as BottomSheetDialog?
                (dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?)?.apply {
                    val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(this)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior.peekHeight = 0
                    behavior.addBottomSheetCallback(object :
                        BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            if (BottomSheetBehavior.STATE_SETTLING == newState) {
                                dismiss()
                            }
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                    })
                }
            }
        })
    }

    inner class CheckSampleOneWithSaveAdapter :
        RecyclerView.Adapter<CheckSampleOneWithSaveAdapter.Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_row_check_box_sample_one, parent, false)
        )

        override fun onBindViewHolder(holder: Holder, position: Int) =
            holder.bind(tempList[position])

        override fun getItemCount() = tempList.count()

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
                itemView.setOnClickListener {
                    add(
                        android.R.id.content,
                        Screens.Nav2.getSubCheckSampleOneWithSaveScreen(checkSampleOne) {
                            notifyItemChanged(adapterPosition)
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        function?.invoke()
    }
}