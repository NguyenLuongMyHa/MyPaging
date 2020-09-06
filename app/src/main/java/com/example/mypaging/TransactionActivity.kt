package com.example.mypaging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
            viewModel.examplePagingFlow.collectLatest {
                pagingAdapter.submitData(it)
            }
        }
    }

    private fun setupList() {
        pagingAdapter = TransactionDataAdapter()
        rec_example.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pagingAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                TransactionViewModelFactory(initTransaction())
            )[TransactionViewModel::class.java]
    }

    private fun initTransaction(): ArrayList<Transaction> {
        var transactionList = ArrayList<Transaction>()
        for (i in 1..200) {
            var transaction = Transaction(i, "category $i", randomAmount())
            transactionList.add(transaction)
        }
        return transactionList
    }

    private fun randomAmount(): Int{
        return Random.nextInt(-500000, 500000)
    }
}