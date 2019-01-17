package com.breez.testapplication.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.breez.testapplication.AppApplication
import com.breez.testapplication.dataFlow.IDataManager
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import timber.log.Timber
import javax.inject.Inject

abstract class BaseNVM : ViewModel() {
    @Inject
    lateinit var dataManager: IDataManager

    protected val disposal: CompositeDisposable = CompositeDisposable()

    val progressLiveData = MutableLiveData<Boolean>()
    val alertMessage = MutableLiveData<Throwable?>()

    protected val tag: String = javaClass.simpleName

    private val coroutines = mutableListOf<Deferred<*>>()

    init {
        AppApplication.component.inject(this)
    }

    protected fun addCoroutine(coroutine: Deferred<*>): Deferred<*> {
        coroutines.add(coroutine)
        return coroutine
    }

    override fun onCleared() {
        coroutines.forEach { it.cancel() }
        coroutines.clear()
        if (!disposal.isDisposed) disposal.dispose()
        super.onCleared()
    }


    fun showProgress() {
        progressLiveData.postValue(true)
    }

    fun hideProgress() {
        progressLiveData.postValue(false)
    }

    fun <T : Any?> processAsyncProviderCall(call: () -> Deferred<T>,
                                            onSuccess: (T) -> Unit = { /* nothing by default*/ },
                                            onError: (E: Throwable?) -> Unit = { /* nothing by default*/ },
                                            showProgress: Boolean = false): Deferred<*> = processAsyncProviderCallWithFullResult(call, { onSuccess(it) },
            onError, showProgress)


    private fun <T : Any?>
            processAsyncProviderCallWithFullResult(call: () -> Deferred<T>,
                                                   onSuccess: (T) -> Unit = { /* nothing by default*/ },
                                                   onError: (E: Throwable?) -> Unit = { /* nothing by default*/ },
                                                   showProgress: Boolean = false): Deferred<*> {
        return addCoroutine(GlobalScope.async(Dispatchers.Default) {
            if (showProgress) {
                showProgress()
            }
            val job = async(Dispatchers.Unconfined) {
                call()
            }
            with(job.await()) {
                try {
                    if (await()==null){
                        onError(Throwable())
                    }else{
                        onSuccess(await())
                    }
                } catch (t: Throwable) {
                    onError(getCompletionExceptionOrNull())
                }
            }
            if (showProgress) {
                hideProgress()
            }
        })
    }

    protected fun onError(throwable: Throwable?) {
        Timber.e("Error in $tag : ${throwable?.message}")
        showAlert(throwable)
    }

    fun showAlert(throwable: Throwable? = null) {
        alertMessage.value = throwable
    }
}