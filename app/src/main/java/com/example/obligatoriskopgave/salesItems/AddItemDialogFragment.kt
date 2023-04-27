package com.example.obligatoriskopgave.salesItems

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.obligatoriskopgave.R
import com.example.obligatoriskopgave.home.ItemsAdapter
import com.example.obligatoriskopgave.login.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddItemDialogFragment : DialogFragment() {

    interface OnAddItemListener {
        fun onAddItem(item: Items)
    }


    private lateinit var itemsAdapter: ItemsAdapter

    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemsAdapter = ItemsAdapter()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_add_item, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
            .setTitle(R.string.add_item_dialog_title)
            .setPositiveButton(R.string.add) { _, _ ->
                val userEmail = loginViewModel.userEmail.value
                val itemDescription =
                    dialogView.findViewById<EditText>(R.id.editTextDescription).text.toString()
                val itemPrice =
                    dialogView.findViewById<EditText>(R.id.editTextPrice).text.toString().toInt()
                val phoneNumber =
                    dialogView.findViewById<EditText>(R.id.editTextPhone).text.toString()
                val imgURL =
                    dialogView.findViewById<EditText>(R.id.editTextImage).text.toString()

                val item = Items(
                    id = 0,
                    description = itemDescription,
                    price = itemPrice,
                    sellerEmail = userEmail.orEmpty(),
                    sellerPhone = phoneNumber,
                    time = System.currentTimeMillis()/1000,
                    pictureUrl = imgURL
                )

                addItemToApi(item)
                Log.d("USER", "Item Created")
                Toast.makeText(requireContext(), "Item Created", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                // handle negative button click here
            }
        return builder.create()
    }

    private fun addItemToApi(item: Items) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://anbo-salesitems.azurewebsites.net/api/") // replace with your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val itemsApi = retrofit.create(ItemsService::class.java)

        itemsApi.addItem(item).enqueue(object : Callback<Items> {
            override fun onResponse(call: Call<Items>, response: Response<Items>) {
                // handle successful response
                itemsAdapter.clearItemsList()
                itemsApi.getAllItems().enqueue(object : Callback<List<Items>> {
                    override fun onResponse(call: Call<List<Items>>, response: Response<List<Items>>) {
                        // update the RecyclerView with the latest items
                        val items = response.body()
                        items?.let {
                            itemsAdapter.setItemsList(items)
                        }
                    }

                    override fun onFailure(call: Call<List<Items>>, t: Throwable) {
                        // handle network call failure
                    }
                })
            }

            override fun onFailure(call: Call<Items>, t: Throwable) {
                // handle network call failure
            }
        })
    }

}
