package com.vmm408.voznickandroid.ui.global

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.di.DI.APP_SCOPE
import com.vmm408.voznickandroid.helper.AlertListener
import com.vmm408.voznickandroid.helper.showBaseAlert
import com.vmm408.voznickandroid.lifecycle.LifecycleObserver
import com.vmm408.voznickandroid.model.LocalizationEvent
import com.vmm408.voznickandroid.model.NetworkStateEvent
import com.vmm408.voznickandroid.network.response.Base
import com.vmm408.voznickandroid.ui.global.mvp.BaseView
import com.vmm408.voznickandroid.ui.global.widgets.ActionButtonFragment
import com.vmm408.voznickandroid.ui.objectScopeName
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import toothpick.Scope
import toothpick.Toothpick
import toothpick.ktp.KTP

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment(), BaseView, LifecycleObserver {
    abstract val layoutRes: Int
    abstract val TAG: String

    protected open val parentScopeName: String by lazy {
        (parentFragment as? BaseBottomSheetFragment)?.bottomSheetScopeName ?: APP_SCOPE
    }
    private lateinit var bottomSheetScopeName: String
    protected lateinit var scope: Scope
        private set

    private var instanceStateSaved: Boolean = false

    private val delegate = object : LayoutChangeListener.Delegate {
        private val uiHandler = Handler(Looper.getMainLooper())

        override fun layoutDidChange(oldHeight: Int, newHeight: Int, tempBottom: Int) {
            uiHandler.post {
                context?.resources?.getDimension(R.dimen.navigation_height)?.toInt()?.let {
                    if (tempBottom <= it) return@post
                }
                keyboardStateChanged(newHeight > oldHeight)
            }
        }
    }

    open fun keyboardStateChanged(isShown: Boolean) {}
    
    override fun onCreate(savedInstanceState: Bundle?) {
        bottomSheetScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME) ?: objectScopeName()
        if (Toothpick.isScopeOpen(bottomSheetScopeName)) {
            scope = Toothpick.openScope(bottomSheetScopeName)
        } else {
            scope = Toothpick.openScopes(parentScopeName, bottomSheetScopeName)
            installModules(scope)
        }
        Toothpick.inject(this, scope)
        super.onCreate(savedInstanceState)
    }

    protected open fun installModules(scope: Scope) {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnKeyListener { _: DialogInterface, keyCode: Int, keyEvent: KeyEvent ->
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {

                    dismiss()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    @SuppressLint("RestrictedApi", "VisibleForTests")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addOnLayoutChangeListener(LayoutChangeListener().also { it.delegate = delegate })
        view.findViewById<ViewGroup>(R.id.rootView)?.setOnClickListener { hideKeyboard() }
        
        (dialog as BottomSheetDialog).behavior.apply {
            /** makes rounded corners when expanded too **/
            disableShapeAnimations()
            isHideable = false
            isDraggable = false
            isCancelable = false
        }
        dialog?.setOnShowListener { dialogInterface -> dialogOnShowListener(dialogInterface) }
    }

    open fun dialogOnShowListener(dialogInterface: DialogInterface?) {
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onStart() {
        super.onStart()
//        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun connectListener() {
        setLocalization()
    }

    open fun setLocalization() {}

    override fun onPause() {
        super.onPause()
    }

    override fun disconnectListener() {}

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
        outState.putString(STATE_SCOPE_NAME, bottomSheetScopeName)
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needCloseScope()) KTP.closeScope(scope.name)
    }

    private fun needCloseScope(): Boolean = when {
        activity?.isChangingConfigurations == true -> false
        activity?.isFinishing == true -> true
        else -> isRealRemoving()
    }

    private fun isRealRemoving(): Boolean =
        isRemoving || (parentFragment as? BaseBottomSheetFragment)?.isRealRemoving() ?: false

    protected fun replace(
        @IdRes hostId: Int,
        fragment: BaseFragment,
        isAddToBackStack: Boolean = true
    ) = activity?.supportFragmentManager?.beginTransaction()
        ?.replace(hostId, fragment, fragment.TAG)
        ?.setReorderingAllowed(true)
        ?.apply { if (isAddToBackStack) addToBackStack(fragment.TAG) }
        ?.commit()

    protected fun add(
        @IdRes hostId: Int,
        fragment: BaseFragment
    ) = activity?.supportFragmentManager?.beginTransaction()
        ?.add(hostId, fragment, fragment.TAG)
        ?.setReorderingAllowed(true)
        ?.addToBackStack(fragment.TAG)
        ?.commit()

    protected fun finishFragment() = activity?.supportFragmentManager?.popBackStack()

    protected fun findButton(@IdRes id: Int): ActionButtonFragment? =
        childFragmentManager.findFragmentById(id) as? ActionButtonFragment

    protected fun findWidgetFragment(@IdRes fragmentTag: Int): Fragment? =
        childFragmentManager.findFragmentById(fragmentTag)

    protected fun findFragment(fragmentTag: String): Fragment? =
        activity?.supportFragmentManager?.findFragmentByTag(fragmentTag)

    private fun hideKeyboard() {
        try {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                ?.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: NetworkStateEvent) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: LocalizationEvent) {
        setLocalization()
    }

    override fun apiError(error: String, apiException: Base?, function: (() -> Unit)?) {
        val listener = object : AlertListener {
            override fun actionPositive() {
                function?.invoke()
            }

            override fun actionNegative() {
            }
        }
        when (error) {
            API_INTERNET_ERROR -> showBaseAlert(
                context,
                title = apiException?.title,
                subtitle = getString(R.string.check_internet_connection),
                positiveText = getString(R.string.re_try),
                alertListener = listener
            )
            API_SERVER_ERROR -> showBaseAlert(
                context,
                title = apiException?.title,
                subtitle = apiException?.message,
                positiveText = getString(R.string.ok_label),
                alertListener = listener
            )
        }
    }
}