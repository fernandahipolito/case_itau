package com.example.caseitau.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caseitau.data.model.MonthsEnum
import com.example.caseitau.R
import com.example.caseitau.data.model.Transaction
import kotlinx.android.synthetic.main.item_transaction.view.*
import java.text.NumberFormat

class TransactionsAdapter(
    private val transactions: List <Transaction>,
    private val onItemClickListener: ((transaction : Transaction)->Unit)
) : RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, view: Int): TransactionsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction,parent,false)
        return TransactionsViewHolder(itemView,onItemClickListener)
    }

    override fun getItemCount() = transactions.count()

    override fun onBindViewHolder(viewHolder: TransactionsViewHolder, position: Int) {
        viewHolder.bindView(transactions[position])
    }
    class TransactionsViewHolder(
        itemView: View,
        private val onItemClickListener: ((transaction: Transaction) -> Unit)
    ) : RecyclerView.ViewHolder(itemView){

        private val source = itemView.tv_item_transaction_source_value
        private val amount = itemView.tv_item_transaction_amount_value
        private val month = itemView.tv_item_transaction_month
        private val icon = itemView.iv_item_transaction_category_icon

        fun bindView(transaction: Transaction){
            source.text = transaction.source
            amount.text = NumberFormat.getCurrencyInstance().format(transaction.amount)
            month.text = MonthsEnum.values().get(transaction.month).month

            when(transaction.category){
                1->icon.setBackgroundResource(R.drawable.ic_baseline_directions_car_24)
                2->icon.setBackgroundResource(R.drawable.ic_baseline_attach_money_24)
                3->icon.setBackgroundResource(R.drawable.ic_beauty)
                4->icon.setBackgroundResource(R.drawable.ic_mechanical)
                5->icon.setBackgroundResource(R.drawable.ic_baseline_restaurant_menu_24)
                6->icon.setBackgroundResource(R.drawable.ic_supermarket)
            }

            itemView.setOnClickListener{
                onItemClickListener.invoke(transaction)
            }
        }

    }
}