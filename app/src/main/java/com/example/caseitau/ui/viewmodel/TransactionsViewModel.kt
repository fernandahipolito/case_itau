package com.example.caseitau.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.caseitau.R
import com.example.caseitau.data.Results
import com.example.caseitau.data.repository.TransactionsRepository
import com.example.caseitau.data.model.Transaction

class TransactionsViewModel(private val dataSource : TransactionsRepository):ViewModel() {

    val transactionsLiveData : MutableLiveData<List<Transaction>> = MutableLiveData()
    val transactionsSum : MutableLiveData<Double> = MutableLiveData()
    val viewFlipperLiveData : MutableLiveData<Pair<Int,Int?>> = MutableLiveData()

    fun getTransactions(month:Int){
        dataSource.getTransactions { result : Results ->
            when(result){
                is Results.SuccessTransaction ->{
                    val monthList:ArrayList<Transaction> = ArrayList()

                    var sum: Double = 0.0
                    for(transaction in result.transactions){
                        if(transaction.month==month){
                            monthList.add(transaction)
                            sum += transaction.amount
                        }
                    }
                    transactionsSum.value = sum
                    transactionsLiveData.value = monthList
                    viewFlipperLiveData.value = Pair(VIEW_FLIPPER_TRANSACTIONS,null)
                }
                is Results.ApiError -> {
                    if (result.statusCode == 401) {
                        viewFlipperLiveData.value =
                            Pair(VIEW_FLIPPER_ERROR, R.string.error_401)
                    }else{
                        viewFlipperLiveData.value =
                            Pair(VIEW_FLIPPER_ERROR, R.string.error_400)
                    }
                }
                is Results.ServerError ->{
                    viewFlipperLiveData.value =
                        Pair(VIEW_FLIPPER_ERROR, R.string.error_500)
                }
            }
        }
    }
    class ViewModelFactory(private val dataSource : TransactionsRepository):
            ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(TransactionsViewModel::class.java)){
                return TransactionsViewModel(dataSource) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
            }
    companion object{
        private const val VIEW_FLIPPER_TRANSACTIONS = 1
        private const val VIEW_FLIPPER_ERROR = 2
    }
}