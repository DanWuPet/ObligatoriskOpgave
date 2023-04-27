package com.example.obligatoriskopgave.salesItems

import retrofit2.Call
import retrofit2.http.*

interface ItemsService {
    @GET("salesItems")
    fun getAllItems(): Call<List<Items>>

    @GET("salesItems/{itemId}")
    fun getItemsById(@Path("itemId") itemsId: Int): Call<Items>

    @GET("salesItems")
    fun getItemsByDescription(@Query("description") description: String): Call<List<Items>>

    @GET("salesItems")
    fun getItemsByPrice(@Query("price") price: Double): Call<List<Items>>

    @POST("salesItems")
    fun addItem(@Body items: Items): Call<Items>

    @DELETE("salesItems/{id}")
    fun deleteItem(@Path("id") id: Int): Call<Void>
}
