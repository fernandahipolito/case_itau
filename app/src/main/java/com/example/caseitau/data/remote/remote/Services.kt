package com.example.caseitau.data.remote.remote

import com.example.caseitau.data.model.Category
import com.example.caseitau.data.model.Transaction
import retrofit2.Call
import retrofit2.http.GET

interface Services {
    @GET ("lancamentos/")
    fun getTransactions(): Call<List<Transaction>>

    @GET ("categorias/")
    fun getCategories(): Call<List<Category>>

}
