package com.vmm408.voznickandroid.ui.global

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.di.DI.APP_SCOPE
import com.vmm408.voznickandroid.lifecycle.LifecycleObserver
import com.vmm408.voznickandroid.network.response.Base
import com.vmm408.voznickandroid.ui.global.*
import com.vmm408.voznickandroid.ui.global.mvp.BaseView
import com.vmm408.voznickandroid.ui.global.widgets.ActionButtonFragment
import io.realm.Realm
import kotlinx.android.synthetic.main.view_base_alert.view.*
import org.greenrobot.eventbus.EventBus
import toothpick.Scope
import toothpick.Toothpick
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

abstract class BaseFragment : Fragment(), BaseView, LifecycleObserver {
    abstract val layoutRes: Int
    abstract val TAG: String
    protected open val parentScopeName: String by lazy {
        (parentFragment as? BaseFragment)?.fragmentScopeName ?: APP_SCOPE
    }
    private lateinit var fragmentScopeName: String
    protected lateinit var scope: Scope
        private set
    protected val realm: Realm by inject()
    private var instanceStateSaved: Boolean = false
    private var isAlertShowing = false

    private val delegate = object : LayoutChangeListener.Delegate {
        private val uiHandler = Handler(Looper.getMainLooper())

        override fun layoutDidChange(oldHeight: Int, newHeight: Int, tempBottom: Int) {
            uiHandler.post {
                if (tempBottom <= resources.getDimension(R.dimen.navigation_height).toInt() ?: 82
                ) {
                    return@post
                }
                keyboardStateChanged(newHeight > oldHeight)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME) ?: objectScopeName()
        if (Toothpick.isScopeOpen(fragmentScopeName)) {
            scope = Toothpick.openScope(fragmentScopeName)
        } else {
            scope = Toothpick.openScopes(parentScopeName, fragmentScopeName)
            installModules(scope)
        }
        Toothpick.inject(this, scope)
        lifecycle.addObserver(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addOnLayoutChangeListener(LayoutChangeListener().also {
            it.delegate = delegate
        })
        view?.findViewById<ViewGroup>(R.id.rootView)?.setOnClickListener {
            hideKeyboard()
        }
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    open fun onMessageEvent(event: LogoutEvent?) {
//        deleteUserDataAndLogOut()
//    }
//
//    private fun deleteUserDataAndLogOut() {
//        RealmHelper.realm?.where(Token::class.java)?.findAll()?.forEach { token ->
//            RealmHelper.remove(arrayListOf(token))
//        }
//        RealmHelper.realm?.where(User::class.java)?.findAll()?.forEach { user ->
//            RealmHelper.remove(arrayListOf(user))
//        }
//        userToken = null
//        qrCode = null
//
//        RealmHelper.realm?.where(OpenFirstTime::class.java)?.findAll()?.forEach { time ->
//            RealmHelper.remove(arrayListOf(time))
//        }
//
//        RealmHelper.realm?.where(ConnectedDevice::class.java)?.findAll()?.forEach { time ->
//            RealmHelper.remove(arrayListOf(time))
//        }
//
//        context?.let {
//            if (isGoogleSignedIn(it)) {
//                getGoogleSignInClient(it).signOut()
//                getGoogleSignInClient(it).revokeAccess()
//            }
//        }
//
//        startActivity(Intent(activity, SplashActivity::class.java))
//        activity?.finish()
//    }

    fun addTopMarginForFragment() {
        context?.let {
            (view?.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin =
                getStatusBarHeight(it)
        }
    }

    fun Activity.setStatusBarNormal() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    fun Activity.setFullScreen() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
        outState.putString(STATE_SCOPE_NAME, fragmentScopeName)
    }

    override fun onStart() {
        super.onStart()
//        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needCloseScope()) KTP.closeScope(scope.name)
    }

    private fun needCloseScope(): Boolean =
        when {
            activity?.isChangingConfigurations == true -> false
            activity?.isFinishing == true -> true
            else -> isRealRemoving()
        }

    private fun isRealRemoving(): Boolean =
        isRemoving || (parentFragment as? BaseFragment)?.isRealRemoving() ?: false

    protected open fun installModules(scope: Scope) {}

    protected fun replace(
        @IdRes hostId: Int = android.R.id.content,
        fragment: BaseFragment,
        isAddToBackStack: Boolean = true
    ) {
        val trans = activity?.supportFragmentManager?.beginTransaction()
            ?.replace(hostId, fragment, fragment.TAG)
        trans?.setReorderingAllowed(true)
        if (isAddToBackStack) {
            trans?.addToBackStack(fragment.TAG)
        }
        trans?.commit()
    }

    protected fun add(@IdRes hostId: Int = android.R.id.content, fragment: BaseFragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(hostId, fragment, fragment.TAG)
            ?.addToBackStack(fragment.TAG)
            ?.setReorderingAllowed(true)
            ?.commit()
    }

    protected fun finishFragment() = activity?.supportFragmentManager?.popBackStack()

    protected fun findFragment(fragmentTag: String): Fragment? =
        activity?.supportFragmentManager?.findFragmentByTag(fragmentTag)

    protected fun findWidgetFragment(@IdRes fragmentTag: Int): Fragment? =
        childFragmentManager.findFragmentById(fragmentTag)

    protected fun findButton(@IdRes id: Int): ActionButtonFragment? =
        childFragmentManager.findFragmentById(id) as? ActionButtonFragment

//    protected fun findToolbar(@IdRes id: Int = R.id.toolbarView): ToolbarFragment? =
//        childFragmentManager.findFragmentById(id) as? ToolbarFragment

    fun hideKeyboard() {
        try {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun connectListener() {}

    override fun disconnectListener() {}

    fun showBaseAlert(
        icon: String? = null,
        title: String? = null,
        subtitle: String? = null,
        negativeText: String? = null,
        positiveText: String? = null,
        alertListener: AlertListener? = null
    ) {
        try {
            if (isAlertShowing) return
            val view = LayoutInflater.from(context).inflate(R.layout.view_base_alert, null).apply {

                if (icon == null) this?.icon?.visibility = View.GONE
                else this?.icon?.text = icon

                if (title == null) this?.title?.visibility = View.GONE
                else this?.title?.text = title

                if (subtitle == null) this?.subtitle?.visibility = View.GONE
                else this?.subtitle?.text = subtitle

                if (negativeText == null) this?.negative?.visibility = View.GONE
                else this?.negative?.text = negativeText

                if (positiveText == null) this?.positive?.visibility = View.GONE
                else this?.positive?.text = positiveText

                this?.negative?.setOnClickListener {
                    isAlertShowing = false
                    alertListener?.actionNegative()
                }
                this?.positive?.setOnClickListener {
                    isAlertShowing = false
                    alertListener?.actionPositive()
                }
            }

            AlertDialog.Builder(context).create()?.apply {
                setView(view)
                setCancelable(false)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                isAlertShowing = true
                show()
            }
        } catch (ex: Exception) {
            isAlertShowing = false
            ex.printStackTrace()
        }
    }

    interface AlertListener {
        fun actionNegative() {}
        fun actionPositive() {}
    }

    override fun apiError(error: String, function: (() -> Unit)?, apiException: Base?) {
        when (error) {
            API_INTERNET_ERROR -> showBaseAlert(
                null,
                apiException?.title,
                getString(R.string.check_internet_connection),
                null,
                getString(R.string.re_try),
                object : AlertListener {
                    override fun actionPositive() {
                        function?.let { it() }
                    }
                })
            API_SERVER_ERROR -> showBaseAlert(
                null,
                apiException?.title,
                if (null == apiException?.message) null else apiException.message,
                null,
                getString(R.string.ok_label)
            )
        }
    }

    open fun keyboardStateChanged(isShown: Boolean) {}
}

