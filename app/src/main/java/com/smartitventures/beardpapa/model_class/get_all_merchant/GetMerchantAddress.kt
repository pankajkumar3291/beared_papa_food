package com.smartitventures.beardpapa.model_class.get_all_merchant


import com.google.gson.annotations.SerializedName

data class GetMerchantAddress(
    @SerializedName("address1")
    val address1: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("href")
    val href: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("zip")
    val zip: String
)