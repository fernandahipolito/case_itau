package com.example.caseitau.ui

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.example.caseitau.R
import com.example.caseitau.data.model.MonthsEnum
import com.example.caseitau.data.repository.ApiDataSource
import com.example.caseitau.ui.adapters.MonthsAdapter
import com.example.caseitau.ui.adapters.TransactionsAdapter
import com.example.caseitau.ui.viewmodel.TransactionsViewModel
import kotlinx.android.synthetic.main.activity_transactions.*
import java.text.NumberFormat


class TransactionActivity : BaseActivity() {
    var monthNumber: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        setupToolbar(in_toolbar as Toolbar, R.string.transactions_activity_title, null)

        setData()
        setMonths()
    }

    private fun setMonths() {
        with(rv_activity_transactions_months) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                this@TransactionActivity,
                androidx.recyclerview.widget.RecyclerView.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
            var monthsList: ArrayList<MonthsEnum> = ArrayList()
            monthsList.addAll(MonthsEnum.values())
            val list = monthsList.subList(1, monthsList.size)
            adapter = MonthsAdapter(list) { number ->
                monthNumber = number
                setData()
            }
        }
    }

    private fun setData() {
        val viewModel: TransactionsViewModel = TransactionsViewModel.ViewModelFactory(
            ApiDataSource()
        )
            .create(TransactionsViewModel::class.java)

        viewModel.transactionsLiveData.observe(this, Observer {
            it?.let { transactions ->
                with(rv_activity_transactions_list) {
                    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                        this@TransactionActivity,
                        androidx.recyclerview.widget.RecyclerView.VERTICAL,
                        false
                    )
                    setHasFixedSize(true)
                    adapter = TransactionsAdapter(transactions) { transaction ->
                        val intent = DetailActivity.getStartIntent(
                            this@TransactionActivity,
                            transaction.amount,
                            transaction.source,
                            transaction.category
                        )
                        this@TransactionActivity.startActivity(intent)
                    }
                }
            }
        })

        viewModel.viewFlipperLiveData.observe(this, Observer {
            it?.let { viewFlipper ->
                vf_activity_transactions.displayedChild = viewFlipper.first

                viewFlipper.second?.let { errorMessageResId ->
                    tv_activity_transactions_error.text = getString(errorMessageResId)
                }
            }
        })

        viewModel.transactionsSum.observe(this, Observer {
            it?.let { sum ->
                setupToolbar(
                    in_toolbar as Toolbar,
                    0,
                    MonthsEnum.values().get(monthNumber).month + " " + NumberFormat.getCurrencyInstance().format(
                        sum
                    )
                )
            }
        })

        viewModel.getTransactions(monthNumber)

    }
}