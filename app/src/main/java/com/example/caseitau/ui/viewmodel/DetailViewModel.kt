package com.example.caseitau.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.caseitau.R
import com.example.caseitau.data.Results
import com.example.caseitau.data.repository.CategoriesRepository

class DetailViewModel (private val dataSource: CategoriesRepository): ViewModel(){

    val categoriesLiveData : MutableLiveData <String> = MutableLiveData()
    val viewFlipperLiveData: MutableLiveData <Pair<Int,Int?>> = MutableLiveData()
    fun getCategories(month: Int){
        dataSource.getCategories { result : Results ->
            when (result){
                is Results.SuccessCategory->{
                    var categorySelected: String = String()
                    for(category in result.transactions){
                        if(category.id == month){
                            categorySelected = category.name
                        }
                    }
                    categoriesLiveData.value = categorySelected
                    viewFlipperLiveData.value = Pair(VIEW_FLIPPER_TRANSACTIONS, null)
                }
                is Results.ApiError->{
                    if(result.statusCode==401){
                        viewFlipperLiveData.value = Pair(VIEW_FLIPPER_ERROR, R.string.error_401)
                    }else{
                        viewFlipperLiveData.value = Pair(VIEW_FLIPPER_ERROR, R.string.error_400)
                    }
                }
                is Results.ServerError->{
                    viewFlipperLiveData.value = Pair(VIEW_FLIPPER_ERROR, R.string.error_500)
                }

            }
        }
    }
    class ViewModelFactory(private val dataSource: CategoriesRepository):
            ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
                return DetailViewModel(dataSource) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
            }
    companion object{
        private const val VIEW_FLIPPER_TRANSACTIONS = 1
        private const val VIEW_FLIPPER_ERROR = 2
    }
}
