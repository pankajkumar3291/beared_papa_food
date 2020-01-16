package com.smartitventures.beardpapa.model_class.get_all_merchant


import com.google.gson.annotations.SerializedName

data class GetAllMerchantsRequest(
    @SerializedName("isError")
    val isError: Boolean,
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("payload")
    val payload: Payload
) {
     class Payload(
        @SerializedName("allmerchants")
        val allmerchants: List<Allmerchant>) {
        data class Allmerchant(
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("imagename")
            val imagename: String,
            @SerializedName("mId")
            val mId: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("thumbnail")
            val thumbnail: String,
            @SerializedName("token")
            val token: String,
            @SerializedName("updated_at")
            val updatedAt: String,
            var getAddress: GetAddress
        )

     }

}