package com.smartitventures.beardpapa.model_class


import com.google.gson.annotations.SerializedName

data class GetUserDetailCloverResponse(
    @SerializedName("addresses")
    val addresses: Addresses,
    @SerializedName("customerSince")
    val customerSince: Long,
    @SerializedName("emailAddresses")
    val emailAddresses: EmailAddresses,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("href")
    val href: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("marketingAllowed")
    val marketingAllowed: Boolean,
    @SerializedName("metadata")
    val metadata: Metadata,
    @SerializedName("phoneNumbers")
    val phoneNumbers: PhoneNumbers
) {
    data class Addresses(
        @SerializedName("elements")
        val elements: List<Element>
    ) {
        data class Element(
            @SerializedName("address1")
            val address1: String,
            @SerializedName("city")
            val city: String,
            @SerializedName("country")
            val country: String,
            @SerializedName("customer")
            val customer: Customer,
            @SerializedName("id")
            val id: String,
            @SerializedName("state")
            val state: String,
            @SerializedName("zip")
            val zip: String
        ) {
            data class Customer(
                @SerializedName("id")
                val id: String
            )
        }
    }

    data class EmailAddresses(
        @SerializedName("elements")
        val elements: List<Element>
    ) {
        data class Element(
            @SerializedName("customer")
            val customer: Customer,
            @SerializedName("emailAddress")
            val emailAddress: String,
            @SerializedName("id")
            val id: String
        ) {
            data class Customer(
                @SerializedName("id")
                val id: String
            )
        }
    }

    data class Metadata(
        @SerializedName("customer")
        val customer: Customer,
        @SerializedName("dobDay")
        val dobDay: Int,
        @SerializedName("dobMonth")
        val dobMonth: Int,
        @SerializedName("dobYear")
        val dobYear: Int,
        @SerializedName("modifiedTime")
        val modifiedTime: Long
    ) {
        data class Customer(
            @SerializedName("id")
            val id: String
        )
    }

    data class PhoneNumbers(
        @SerializedName("elements")
        val elements: List<Element>
    ) {
        data class Element(
            @SerializedName("customer")
            val customer: Customer,
            @SerializedName("id")
            val id: String,
            @SerializedName("phoneNumber")
            val phoneNumber: String
        ) {
            data class Customer(
                @SerializedName("id")
                val id: String
            )
        }
    }
}