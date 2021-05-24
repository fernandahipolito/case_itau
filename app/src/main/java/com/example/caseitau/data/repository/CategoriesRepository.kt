package com.example.caseitau.data.repository

import com.example.caseitau.data.Results

interface CategoriesRepository {

    fun getCategories(resultCallback:(result: Results)->Unit)
}