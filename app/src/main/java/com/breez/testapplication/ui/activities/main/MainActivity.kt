package com.breez.testapplication.ui.activities.main

import android.os.Bundle
import com.breez.testapplication.R
import com.breez.testapplication.ui.fragments.main.MainViewModel
import com.breez.testapplication.ui.base.BaseNActivity
import com.breez.testapplication.ui.fragments.main.MainFragment

class MainActivity : BaseNActivity<MainViewModel>() {
    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java
    override val layoutId: Int = R.layout.main_activity
    override val containerId: Int = R.id.container
    override val observeLiveData: MainViewModel.() -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            replaceFragment(MainFragment.newInstance(),false)
        }
    }

}
