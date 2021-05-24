package com.example.caseitau.data

import com.example.caseitau.data.model.Category
import com.example.caseitau.data.model.Transaction

sealed class Results {
    class SuccessCategory(val transactions: List<Category>): Results()
    class ApiError(val statusCode:Int): Results()
    object ServerError: Results()
    class SuccessTransaction(val transactions:List<Transaction>): Results()

}
