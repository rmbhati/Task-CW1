package com.kgk.task1.ui

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.kgk.task1.R
import com.kgk.task1.base.BaseActivity
import com.kgk.task1.databinding.ActivityMainBinding
import com.kgk.task1.utils.CustomProgress
import com.kgk.task1.utils.toast
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {
    private lateinit var activityBinding: ActivityMainBinding
    private val viewModel by viewModel<HomeViewModel>()
    private var dialog: CustomProgress? = null
    private var page = 1
    private var listData: List<HomeResponse>? = null
    private var adapter: HomeAdapter? = null

    override fun initView() {
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityBinding.viewmodel = viewModel
        dialog = CustomProgress.createDialog(this, "Loading...")
        viewModel.fetchingData.observe(this) { showLoading(it) }
        viewModel.errorData.observe(this) { onFailed(it) }
        viewModel.homeData.observe(this) { onSuccess(it) }
    }

    override fun initData() {
        viewModel.getHomeData()
        fab.setOnClickListener {
            if (listData != null) {
                page += 1
                loadRecyclerData()
            }
        }
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            dialog!!.show()
        } else {
            dialog!!.cancel()
        }
    }

    private fun onSuccess(response: List<HomeResponse>) {
        listData = response
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        loadRecyclerData()
    }

    private fun loadRecyclerData() {
        adapter = HomeAdapter(this, listData!!, page)
        recyclerView.adapter = adapter
    }

    private fun onFailed(error: String) {
        toast(error)
    }
}