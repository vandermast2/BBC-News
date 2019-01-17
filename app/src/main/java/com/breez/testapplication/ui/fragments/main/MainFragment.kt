package com.breez.testapplication.ui.fragments.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.breez.testapplication.R
import com.breez.testapplication.ui.base.BaseNFragment
import com.breez.testapplication.ui.fragments.list.ListFragment
import com.breez.testapplication.utils.Constants
import com.breez.testapplication.utils.onClick
import kotlinx.android.synthetic.main.main_fragment.*
import com.breez.testapplication.utils.replaceFragmentSafely


class MainFragment : BaseNFragment<MainViewModel>() {
    override fun getName(): String = "NEWS"
    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java
    override val layoutId: Int = R.layout.main_fragment
    override val observeLiveData: MainViewModel.() -> Unit = {}

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnTop.onClick {
            replaceFragmentSafely(
                ListFragment.newInstance(Constants.TOP_FRAGMENT_TAG),Constants.TOP_FRAGMENT_TAG,false,true, R.id.container) }
        btnEverything.onClick {
            Toast.makeText(context!!,"Not implemented",Toast.LENGTH_SHORT).show()
        }
    }

}
