package com.example.obligatoriskopgave.salesItems

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.obligatoriskopgave.R
import com.example.obligatoriskopgave.login.LoginViewModel

class ItemDetailsDialog(
    context: Context,
    private val item: Items,
    private val loginViewModel: LoginViewModel?,
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

        descriptionTextView.text = item.description
        priceTextView.text = "${item.price} kr."
        sellerEmailTextView.text = "Seller: ${item.sellerEmail}"
        sellerPhoneTextView.text = "Phone: ${item.sellerPhone}"
        timeTextView.text = humanDate(item.time.toLong())

        Glide.with(context)
            .load(item.pictureUrl)
            .placeholder(R.drawable.loading)
            .error(R.drawable.loading)
            .into(pictureImageView)

        val userEmail = loginViewModel?.userEmail?.value.toString()

        if (item.sellerEmail == userEmail) {
            Log.d("USER", "User Item Found")
            deleteButton.setOnClickListener {
                onDeleteClicked()
                dismiss()
            }
        } else {
            deleteButton.visibility = View.GONE
        }

        val exitButton = findViewById<Button>(R.id.exit_button)
        exitButton.setOnClickListener {
            dismiss()
        }
    }
}


