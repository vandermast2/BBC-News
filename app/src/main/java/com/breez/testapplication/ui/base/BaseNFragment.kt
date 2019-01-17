package com.breez.testapplication.ui.base


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.breez.testapplication.R
import com.breez.testapplication.utils.Constants
import com.breez.testapplication.utils.hideKeyboard
import kotlinx.coroutines.*
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection
import java.util.*
import java.util.concurrent.TimeUnit

abstract class
BaseNFragment<T : BaseNVM> : Fragment() {
    companion object {
        private const val COROUTINE_DELAY = 1000L
    }

    var startTime: Long = 0

    val dialog by lazy { ProgressDialog.progressDialog(context!!) }

    abstract fun getName(): String

    var popupWindow: ListPopupWindow? = null

    abstract val viewModelClass: Class<T>

    protected val viewModel: T by lazy(LazyThreadSafetyMode.NONE) { ViewModelProviders.of(this).get(viewModelClass) }

    private var canBeClicked = true

    private var errorDialog: androidx.fragment.app.DialogFragment? = null

    private val coroutines = mutableListOf<Deferred<*>>()

    @Suppress("MemberVisibilityCanPrivate")
    protected fun addCoroutine(coroutine: Deferred<*>) {
        coroutines.add(coroutine)
    }

    protected abstract val layoutId: Int

    protected open fun observeBaseLiveData() = with(viewModel) {

        alertMessage.observe(this@BaseNFragment, Observer {
            it?.let {
                parseError(it)
                this@BaseNFragment.showAlert(it.message)
                alertMessage.value = it
            }
        })

        observeLiveData()
    }

    fun parseError(it: Throwable) {
        if (it is HttpException) {
            Toast.makeText(context!!,it.message, Toast.LENGTH_SHORT).show()
        } else {
            Timber.e("Error: ${it}")
        }
    }

    protected abstract val observeLiveData: T.() -> Unit

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startTime = Date().time / 1000
        observeBaseLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.progressLiveData.observe(this, Observer {
            if (it!!) {
                dialog.show()
            } else {
                dialog.dismiss()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        canBeClicked = true
    }

    override fun onStop() {
        super.onStop()
        dialog.dismiss()
    }

    override fun onDestroy() {
        coroutines.forEach { it.cancel() }
        coroutines.clear()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        with(viewModel.alertMessage) {
            value?.let {
                parseError(it)
                this@BaseNFragment.showAlert(it.message)
                value = null
            }
        }
    }


    fun showAlert(text: String?) {
//        context?.alert(text!!)
    }

    protected fun processActionWithDelay(
        delay: Long = Constants.DEFAULT_UI_DELAY,
        timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
        action: () -> Unit
    ) {
        addCoroutine(GlobalScope.async(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                delay(delay)
            }
            if (isActive) {
                action.invoke()
            }
        })
    }

    protected fun <T : Fragment> replaceFragment(
        fragment: T, @IdRes containerId: Int,
        needToAddToBackStack: Boolean = true
    ): T {
        hideKeyboard()
        val name = fragment.javaClass.name
        with(childFragmentManager.beginTransaction()) {
            replace(containerId, fragment, name)
            if (needToAddToBackStack) {
                addToBackStack(name)
            }
            commit()
        }
        childFragmentManager.executePendingTransactions()
        return fragment
    }

    protected fun invokeIfCanAccepted(withDebounce: Boolean = false, invoke: () -> Unit) {
        if (canBeClicked) {
            if (withDebounce) debounceClick()
            invoke()
        }
    }

    // This is something like debounce in rxBinding, but better :)
    private fun debounceClick() {
        addCoroutine(GlobalScope.async(Dispatchers.Main) {
            canBeClicked = false
            async(Dispatchers.Default) {
                delay(COROUTINE_DELAY)
            }.await()
            if (isActive) {
                canBeClicked = true
            }
        })
    }

    protected fun onBackPressed() = invokeIfCanAccepted { activity?.onBackPressed() }

    open fun onViewPagerSelect() {
        // override it if you want use BaseViewPagerAdapter
    }

    protected fun showAlertDialogCustomView(
        customView: View,
        canelable: Boolean = false,
        positiveText: String = getString(R.string.ok),
        positiveListener: (dialog: DialogInterface, which: Int) -> Unit
        = { _, _ -> },
        negativeText: String = getString(R.string.cancel),
        negativeListener: (dialog: DialogInterface, which: Int) -> Unit
        = { dialog, _ -> dialog.cancel() }
    ) {
        AlertDialog.Builder(context)
            .setView(customView)
            .setCancelable(canelable)
            .setPositiveButton(positiveText, positiveListener)
            .setNegativeButton(negativeText, negativeListener)
            .create()
            .show()
    }

//    protected fun tryToOpenUri(uri: String?) {
//        try {
//            withNotNull(uri?.toUri()) { inAppCallBack?.checkAndOpenUri(this) }
//        } catch (exception: Exception) {
//            Timber.e("parse_uri $exception")
//        }
//    }


}


class ProgressDialog {
    companion object {
        fun progressDialog(context: Context): Dialog {
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
            dialog.setContentView(inflate)
            dialog.setCancelable(true)
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            return dialog
        }
    }
}