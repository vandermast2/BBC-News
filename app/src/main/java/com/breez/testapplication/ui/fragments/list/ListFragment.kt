package com.breez.testapplication.ui.fragments.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.breez.testapplication.R
import com.breez.testapplication.dataFlow.network.model.Article
import com.breez.testapplication.dataFlow.network.model.Status
import com.breez.testapplication.ui.adapter.Listener
import com.breez.testapplication.ui.adapter.NewsAdapter
import com.breez.testapplication.ui.base.BaseNFragment
import com.breez.testapplication.ui.fragments.detail.DetailFragment
import com.breez.testapplication.utils.MiscellaneousUtils
import com.breez.testapplication.utils.replaceFragmentSafely
import kotlinx.android.synthetic.main.list_fragment.*

class ListFragment : BaseNFragment<ListViewModel>() {
    private lateinit var newsAdapter: NewsAdapter
    override fun getName(): String = collectionId
    override val viewModelClass: Class<ListViewModel> = ListViewModel::class.java
    override val layoutId: Int = R.layout.list_fragment
    override val observeLiveData: ListViewModel.() -> Unit = {
        news().observe(this@ListFragment, Observer {
            if (it.status == Status.SUCCESS) {
                newsAdapter.setList((it.data?.articles as ArrayList<Article>?)!!)
                newsAdapter.notifyDataSetChanged()
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsAdapter = NewsAdapter(listener = object : Listener {
            override fun onClick(url: String) {
                replaceFragmentSafely(DetailFragment.newInstance(url), "DetailFragment", false, true, R.id.container)
            }

        })
        rvNews.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
        }
        txtType.text = collectionId
    }

    companion object {
        private val EXTRA_COLLECTION_ID = MiscellaneousUtils.getExtra("collectionId")
        fun newInstance(collectionId: String?) = ListFragment().apply {
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
