package com.smartitventures.beardpapa.model_class.register_local


import com.google.gson.annotations.SerializedName

data class LocalRegisterResponse(
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
        @SerializedName("user_id")
        val userId: Int
    )
}