package com.example.obligatoriskopgave.salesItems

import android.util.Log
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.min

class ItemsRepository {
    private val url = "https://anbo-salesitems.azurewebsites.net/api/ "

    private val itemsService: ItemsService
    private var sortOption = ""
    private var originalItems: List<Items>? = null
    val itemsLiveData: MutableLiveData<List<Items>> = MutableLiveData<List<Items>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create()) // GSON
            .build()
        itemsService = build.create(ItemsService::class.java)
        getItems()
    }

    fun getItems() {
        itemsService.getAllItems().enqueue(object : Callback<List<Items>> {
            override fun onResponse(call: Call<List<Items>>, response: Response<List<Items>>) {
                if (response.isSuccessful) {
                    val itemsList: List<Items>? = response.body()
                    originalItems = itemsList
                    itemsLiveData.postValue(itemsList!!)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("ITEMS_SERVICE", message)
                }
            }

            override fun onFailure(call: Call<List<Items>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("ITEMS_SERVICE", t.message!!)
            }
        })
    }

    fun addItem(item: Items) {
        itemsService.addItem(item).enqueue(object : Callback<Items> {
            override fun onResponse(call: Call<Items>, response: Response<Items>) {
                if (response.isSuccessful) {
                    Log.d("ITEMS_SERVICE", "Added: " + response.body())
                    updateMessageLiveData.postValue("Added: " + response.body())
                    getItems()
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("ITEMS_SERVICE", message)
                }
            }

            override fun onFailure(call: Call<Items>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("ITEMS_SERVICE", t.message!!)
            }
        })
    }

    fun deleteItem(id: Int) {
        itemsService.deleteItem(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("ITEMS_SERVICE", "Deleted item with id: $id")
                    updateMessageLiveData.postValue("Deleted item with id: $id")
                    getItems()
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("ITEMS_SERVICE", message)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("ITEMS_SERVICE", t.message!!)
            }
        })
    }

    fun sortByTitleAscending() {
        itemsLiveData.value = itemsLiveData.value?.sortedBy { it.description }
        sortOption = "description"
    }

    fun sortByTitleDescending() {
        itemsLiveData.value = itemsLiveData.value?.sortedByDescending { it.description }
    }

    fun sortByPrice() {
        itemsLiveData.value = itemsLiveData.value?.sortedBy { it.price }
        sortOption = "price"
    }

    fun sortByPriceDescending() {
        itemsLiveData.value = itemsLiveData.value?.sortedByDescending { it.price }
    }

    fun filterByDescription(description: String) {
        val filteredList = originalItems?.let { items ->
            if (description.isBlank()) {
                items // return originalItems list if description is blank
            } else {
                items.filter { it.description.contains(description, ignoreCase = true) }
            }
        }
        itemsLiveData.value = filteredList?: emptyList()
    }

    fun filterByMinPrice(minPrice: Int?) {
        val filteredList = originalItems?.let { items ->
            if (minPrice == null) {
                items // return originalItems list if minPrice is null
            } else {
                items.filter { it.price >= minPrice }
            }
        }
        itemsLiveData.value = filteredList?.sortedBy { it.description } ?: emptyList()
    }

    fun filterByMaxPrice(maxPrice: Int?) {
        val filteredList = originalItems?.let { items ->
            if (maxPrice == null) {
                items // return originalItems list if maxPrice is null
            } else {
                items.filter { it.price <= maxPrice }
            }
        }
        itemsLiveData.value = filteredList?.sortedBy { it.description } ?: emptyList()
    }


    fun filterBySeller(sellerEmail: String?) {
        itemsLiveData.value = originalItems?.let { items ->
            if (sellerEmail.isNullOrEmpty()) {
                items.sortedBy { it.description }
            } else {
                items.filter { it.sellerEmail == sellerEmail }.sortedBy { it.description }
            }
        }
    }
}