package com.example.obligatoriskopgave.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obligatoriskopgave.R
import com.example.obligatoriskopgave.salesItems.ItemDetailsDialog
import com.example.obligatoriskopgave.salesItems.Items
import com.example.obligatoriskopgave.salesItems.humanDate

class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    private var itemsList = emptyList<Items>()

    fun setItemsList(itemsList: List<Items>) {
        this.itemsList = itemsList
        notifyDataSetChanged()
    }
    fun clearItemsList() {
        this.itemsList = emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_sales_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemsList[position]
        holder.description.text = item.description
        holder.price.text = item.price.toString() + " kr."
        holder.sellerEmail.text = "Seller: " + item.sellerEmail

        holder.itemView.setOnClickListener {
            // create an instance of the ItemDetailsDialog class and pass the item to it
            val dialog = ItemDetailsDialog(holder.itemView.context, item, null) {
                // handle delete click here
            }
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.item_description)
        val price: TextView = itemView.findViewById(R.id.item_price)
        val sellerEmail: TextView = itemView.findViewById(R.id.item_seller_email)
    }
}
