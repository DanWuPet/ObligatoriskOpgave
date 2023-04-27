package com.example.obligatoriskopgave.salesItems

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.obligatoriskopgave.R
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemDetailsDialog(
    context: Context,
    private val item: Items,
    private val userEmail: String?,
    private val onDeleteClicked: () -> Unit
) : Dialog(context) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_item_details)
        val descriptionTextView = findViewById<TextView>(R.id.item_description)
        val priceTextView = findViewById<TextView>(R.id.item_price)
        val sellerEmailTextView = findViewById<TextView>(R.id.item_seller_email)
        val sellerPhoneTextView = findViewById<TextView>(R.id.item_seller_phone)
        val timeTextView = findViewById<TextView>(R.id.item_time)
        val pictureImageView = findViewById<ImageView>(R.id.item_picture_url)
        val deleteButton = findViewById<Button>(R.id.delete_button)
        Log.d("USER", "Visibility: ${deleteButton.visibility}")
        Log.d("USER", "User Email: $userEmail")


        descriptionTextView.text = item.description
        priceTextView.text = "${item.price} kr."
        sellerEmailTextView.text = "Seller: ${item.sellerEmail}"
        sellerPhoneTextView.text = "Phone: ${item.sellerPhone}"
        timeTextView.text = humanDate(item.time.toLong())

        Glide.with(context)
            .asGif()
            .load(item.pictureUrl)
            .placeholder(R.drawable.loading)
            .error(R.drawable.loading)
            .into(pictureImageView)

        pictureImageView.scaleType = ImageView.ScaleType.FIT_CENTER


        if (item.sellerEmail.lowercase() == userEmail) {
            deleteButton.visibility = View.VISIBLE
            deleteButton.setOnClickListener {
                if (item.sellerEmail.lowercase() == FirebaseAuth.getInstance().currentUser?.email?.lowercase()) {
                    onDeleteClicked()
                    dismiss()
                } else {
                    Toast.makeText(context, "Only the seller can delete this item", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            deleteButton.visibility = View.GONE
        }

        val exitButton = findViewById<Button>(R.id.exit_button)
        exitButton.setOnClickListener {
            dismiss()
        }
    }
    private fun onDeleteClicked() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://anbo-salesitems.azurewebsites.net/api/") // replace with your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val itemsApi = retrofit.create(ItemsService::class.java)

        itemsApi.deleteItem(item.id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onDeleteSuccess()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onDeleteFailure()
            }
        })
    }

    private fun onDeleteSuccess() {
        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show()
        onDeleteClicked.invoke()
    }

    private fun onDeleteFailure() {
        Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
    }

}
