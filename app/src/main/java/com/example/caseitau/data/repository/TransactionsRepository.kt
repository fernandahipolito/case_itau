package com.example.caseitau.data.repository

import com.example.caseitau.data.Results

interface TransactionsRepository {
    fun getTransactions(resultCallback:(result: Results)->Unit)
}