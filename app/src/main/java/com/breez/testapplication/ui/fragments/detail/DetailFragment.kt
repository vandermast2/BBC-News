package com.breez.testapplication.ui.fragments.detail

import android.os.Bundle
import android.view.View
import com.breez.testapplication.R
import com.breez.testapplication.ui.base.BaseNFragment
import com.breez.testapplication.utils.MiscellaneousUtils
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : BaseNFragment<DetailViewModel>() {
    override fun getName(): String  = collectionId.toUpperCase()
    override val viewModelClass: Class<DetailViewModel> = DetailViewModel::class.java
    override val layoutId: Int = R.layout.detail_fragment
    override val observeLiveData: DetailViewModel.() -> Unit = {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = MyWebViewClient()
            loadUrl(collectionId)
        }
    }

    companion object {
        private val EXTRA_COLLECTION_ID = MiscellaneousUtils.getExtra("collectionId")
        fun newInstance(collectionId: String?) = DetailFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_COLLECTION_ID, collectionId ?: "")
            }
        }

    }

    private val collectionId by lazy {
        arguments?.getString(EXTRA_COLLECTION_ID)
            ?: ""
    }





}
