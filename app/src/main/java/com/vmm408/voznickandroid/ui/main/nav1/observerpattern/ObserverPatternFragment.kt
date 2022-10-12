package com.vmm408.voznickandroid.ui.main.nav1.observerpattern

import android.os.Bundle
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_observer_pattern.*
import kotlinx.android.synthetic.main.fragment_observer_view.*
import kotlin.properties.Delegates

object Observer {
    val valueObservers = mutableMapOf<Int, (Int) -> Unit>()
    var newValue: Int by Delegates.observable(0) { _, _, newValue ->
        valueObservers.forEach { it.value(newValue) }
    }
}

class ObserverPatternFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_observer_pattern
    override val TAG = "ObserverPatternFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arrayListOf(
            button1,
            button2,
            button3,
            button4,
            button5,
            button6,
            button7,
            button8,
            button9
        ).forEachIndexed { index, textView ->
            textView?.setOnClickListener { Observer.newValue = index.plus(1) }
        }

        add(R.id.view1, ObserverViewFragment(1))
        add(R.id.view2, ObserverViewFragment(2))
        add(R.id.view3, ObserverViewFragment(3))
        add(R.id.view4, ObserverViewFragment(4))
    }
}

class ObserverViewFragment(private val customId: Int) : BaseFragment() {
    override val layoutRes = R.layout.fragment_observer_view
    override val TAG = "ObserverViewFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        textView?.apply {
            setOnClickListener { _ ->
                isSelected = !isSelected
                if (isSelected) {
                    Observer.valueObservers[customId] = {
                        text = it.toString()
                    }
                } else {
                    Observer.valueObservers.remove(customId)
                    text = ""
                }
            }
        }
    }
}