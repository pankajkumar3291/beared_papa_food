package com.smartitventures.beardpapa.model_class.register_user


import com.google.gson.annotations.SerializedName

data class RegisterUserRequest(
    @SerializedName("addresses")
    val addresses: List<Addresse>,
    @SerializedName("emailAddresses")
    val emailAddresses: List<EmailAddresse>,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("merchant")
    val merchant: Merchant,
    @SerializedName("metadata")
    val metadata: Metadata,
    @SerializedName("phoneNumbers")
    val phoneNumbers: List<PhoneNumber>
) {
    data class Addresse(
        @SerializedName("address1")
        val address1: String,
        @SerializedName("city")
        val city: String,
        @SerializedName("country")
        val country: String,
        @SerializedName("state")
        val state: String,
        @SerializedName("zip")
        val zip: String
    )

    data class EmailAddresse(
        @SerializedName("emailAddress")
        val emailAddress: String
    )

    data class Merchant(
        @SerializedName("id")
        val id: String
    )

    data class Metadata(
        @SerializedName("dobDay")
        val dobDay: Int,
        @SerializedName("dobMonth")
        val dobMonth: Int,
        @SerializedName("dobYear")
        val dobYear: Int
    )

    data class PhoneNumber(
        @SerializedName("phoneNumber")
        val phoneNumber: String
    )
}