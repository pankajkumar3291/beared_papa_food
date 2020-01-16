package com.smartitventures.beardpapa.model_class.login_model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("isError")
    val isError: Boolean,
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("payload")
    val payload: Payload
) {
    data class Payload(
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("api_token")
        val apiToken: String,
        @SerializedName("customer_id")
        val customerId: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("mid")
        val mid: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("profile")
        val profile: Any,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("verified")
        val verified: Int
    )
}