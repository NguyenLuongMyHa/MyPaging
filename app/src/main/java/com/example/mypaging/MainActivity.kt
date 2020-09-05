package com.example.mypaging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel:  TransactionRepositoriesViewModel
    private val adapter = TransactionAdapter()
    private var searchJob : Job?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }
    private fun initView() {
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory())
            .get(TransactionRepositoriesViewModel::class.java)

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rec_transaction.addItemDecoration(decoration)
        initAdapter()
        retry_button.setOnClickListener { adapter.retry() }
        search()
    }
    private fun initAdapter() {
        rec_transaction.adapter = adapter.withLoadStateHeaderAndFooter(
            header = TransactionLoadStateAdapter { adapter.retry() },
            footer = TransactionLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            rec_transaction.isVisible = loadState.source.refresh is LoadState.NotLoading
            progress_bar.isVisible = loadState.source.refresh is LoadState.Loading
            retry_button.isVisible = loadState.source.refresh is LoadState.Error

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private fun search () {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch { viewModel.searchRepo().collectLatest { adapter.submitData(it) } }
    }

}

