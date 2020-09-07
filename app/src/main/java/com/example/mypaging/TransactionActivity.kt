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
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random

class TransactionActivity : AppCompatActivity() {
    lateinit var viewModel: TransactionViewModel
    lateinit var pagingAdapter: TransactionDataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        setupViewModel()
        setupList()
        setupView()
    }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.newResult.collectLatest {
                pagingAdapter.submitData(it)
            }
        }
        retry_button.setOnClickListener { pagingAdapter.retry() }
    }

    private fun setupList() {
        pagingAdapter = TransactionDataAdapter()
        rec_example.apply {
            layoutManager = LinearLayoutManager(context)
            //adapter = pagingAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        rec_example.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = TransactionLoadStateAdapter{ pagingAdapter.retry() },
            footer = TransactionLoadStateAdapter{ pagingAdapter.retry() }
        )
        pagingAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            rec_example.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            progress_bar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            retry_button.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
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

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                TransactionViewModelFactory(transactions)
            )[TransactionViewModel::class.java]
    }
    companion object {
        var transactions: ArrayList<Transaction> = initTransaction(0)

        fun initTransaction(lastIndex: Int): ArrayList<Transaction> {
            var transactionList = ArrayList<Transaction>()
            for (i in lastIndex..lastIndex + 99) {
                var transaction = Transaction(i, "category ${i+1}", randomAmount())
                transactionList.add(transaction)
            }
            return transactionList
        }

        private fun randomAmount(): Int{
            return Random.nextInt(-500000, 500000)
        }
    }



}