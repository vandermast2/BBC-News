package com.breez.testapplication.ui.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.breez.testapplication.utils.hideKeyboard
import retrofit2.HttpException
import timber.log.Timber

abstract class BaseNActivity<T : BaseNVM> : AppCompatActivity() {
    abstract val viewModelClass: Class<T>
    protected val viewModel: T by lazy(LazyThreadSafetyMode.NONE) { ViewModelProviders.of(this).get(viewModelClass) }
    protected abstract val layoutId: Int
    protected abstract val containerId: Int
    protected abstract val observeLiveData: T.() -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        observeBaseLiveData()
    }

    override fun onResume() {
        super.onResume()
        with(viewModel.alertMessage) {
            value?.let {
                parseError(it)
                value = null
            }
        }
    }

    fun parseError(it: Throwable) {
        if (it is HttpException) {
            Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
        } else {
            Timber.e("Error: ${it}")
        }
    }

    fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting ?: false
    }

    private fun observeBaseLiveData() = with(viewModel) {
        progressLiveData.observe(this@BaseNActivity, Observer {
            it?.let { if (it) showProgress() else hideProgress() }
        })
        alertMessage.observe(this@BaseNActivity, Observer {
            it?.let {
                parseError(it)
                alertMessage.value = null
            }
        })

        observeLiveData()
    }

    /**
     * Replace fragment with tag equals to it's class name {@link Class#getName()}
     *
     * @param fragment             Instance of {@link Fragment}
     * @param needToAddToBackStack boolean value representing necessity for adding fragment to backstack.
     *                             If true fragment will be added to backstack with tag equals
     *                             to it's class name }
     */
    protected fun <T : Fragment> replaceFragment(fragment: T, needToAddToBackStack: Boolean = true): T {
        hideKeyboard()
        val name = fragment.javaClass.name
        with(supportFragmentManager.beginTransaction()) {
            replace(containerId, fragment, name)
            if (needToAddToBackStack) {
                addToBackStack(name)
            }
            commit()
        }
        supportFragmentManager.executePendingTransactions()
        return fragment
    }






}