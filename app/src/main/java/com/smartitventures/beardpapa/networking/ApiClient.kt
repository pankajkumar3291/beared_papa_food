package com.smartitventures.beardpapa.networking

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.smartitventures.beardpapa.interfaces.BaseUrls
import com.smartitventures.beardpapa.model_class.get_all_merchant.GetAllMerchantsRequest
import com.smartitventures.beardpapa.model_class.get_all_merchant.GetMerchantAddress
import com.smartitventures.beardpapa.model_class.login_model.LoginResponse
import com.smartitventures.beardpapa.model_class.register_local.LocalRegisterResponse
import com.smartitventures.beardpapa.model_class.register_user.RegisterUserRequest
import com.smartitventures.beardpapa.model_class.register_user.RegisterUserResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


class ApiClient {
    companion object {
        fun clover(): WikiApiService {
            val retrofit = Retrofit.Builder()
                .client(okHttpClient())
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()
                )
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl("https://apisandbox.dev.clover.com/")
                .build()
            return retrofit.create(WikiApiService::class.java)
        }

        fun local(): WikiApiService {
            val retrofit = Retrofit.Builder()
                .client(okHttpClient())
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()
                )
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl(BaseUrls.localBaseUrl)
                .build()
            return retrofit.create(WikiApiService::class.java)
        }

        fun okHttpClient():OkHttpClient
        {
            val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client: OkHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging).build()
            return  client
        }
    }

    interface WikiApiService {
        @FormUrlEncoded
        @POST("customer/login/en")
        fun loginApi(@Field("email") email: String, @Field("password") password: String): Observable<LoginResponse>

        @GET
        fun getAllMerchant(@Url url: String): Observable<GetAllMerchantsRequest>

        @GET
        fun getMerchantAddress(@Url url: String): Observable<GetMerchantAddress>
        @POST
        fun registerUser(@Url url:String, @Body registerUserRequest: RegisterUserRequest): Observable<RegisterUserResponse>

        @FormUrlEncoded
        @POST
        fun registerLocal(
            @Url url: String, @Field("name") name: String, @Field("email") email: String, @Field(
                "password"
            ) pwd: String, @Field("role_id") roleId: String
        ): Observable<LocalRegisterResponse>

        @FormUrlEncoded
        @POST("customer/register/en")
        fun registerFinalApi(@Field("user_id") userId: String, @Field("customer_id") customerId: String, @Field(
                "mid"
            ) mId: String, @Field("access_token") accessToken: String
        ): Observable<LocalRegisterResponse>

//curl --request GET \
//  --url 'https://apisandbox.dev.clover.com/v3/merchants/MMD5VA20VTCX1/customers/6ZQ3Y04CPY4S4?expand=addresses%2CemailAddresses%2CphoneNumbers%2C&access_token=fdfcf9d6-f054-16a5-4848-1d49ff5109d7' \
//  --header 'accept: application/json'

        fun getUserDetailClover(@Url url: String): Observable<RegisterUserRequest>
    }
}