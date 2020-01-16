package com.smartitventures.beardpapa.model_class.register_user


import com.google.gson.annotations.SerializedName

data class RegisterUserResponse(
    @SerializedName("customerSince")
    val customerSince: Long,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("href")
    val href: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("marketingAllowed")
    val marketingAllowed: Boolean
)