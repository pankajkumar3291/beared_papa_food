package com.smartitventures.beardpapa.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.smartitventures.beardpapa.R
import com.smartitventures.beardpapa.model_class.register_user.RegisterUserRequest
import com.smartitventures.beardpapa.networking.ApiClient
import com.smartitventures.beardpapa.other_class.GlobalProgressDialog
import com.smartitventures.beardpapa.other_class.SavedUserDetail.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_profile.*

class ActivityUserProfile : AppCompatActivity() {
    private var compsiteDisposable: CompositeDisposable = CompositeDisposable()
    private val apiInterface: ApiClient.WikiApiService = ApiClient.local()
    private var globalProgressDialog: GlobalProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        globalProgressDialog = GlobalProgressDialog(this)
        swipeRefresh.isRefreshing = true
        getUserCloverDetail()
    }

    private fun getUserCloverDetail() {

        if ((!TextUtils.isEmpty(MID)) && (!TextUtils.isEmpty(CLOVER_USER_ID)) && (!TextUtils.isEmpty(
                ACCESS_TOKEN
            ))
        ) {
            compsiteDisposable.add(
                apiInterface.getUserDetailClover("https://apisandbox.dev.clover.com/v3/merchants/" + MID + "/customers/" + CLOVER_USER_ID + "?expand=addresses,emailAddresses,phoneNumbers,metadata&access_token=" + ACCESS_TOKEN).observeOn(
                    AndroidSchedulers.mainThread()
                ).subscribeOn(
                    Schedulers.io()
                ).subscribe(object : io.reactivex.functions.Consumer<RegisterUserRequest> {
                    override fun accept(t: RegisterUserRequest?) {
                        if (t != null) {
                            textView19.setText(t.firstName + " " + t.lastName)
                            textView21.setText(t.emailAddresses.toString())
                            textView23.setText(t.emailAddresses.get(0).emailAddress)
                            textView190.setText(t.phoneNumbers.get(0).phoneNumber)
                            textView211.setText(t.addresses.get(0).address1+","+t.addresses.get(0).city+","+t.addresses.get(0).state+","+t.addresses.get(0).country+","+t.addresses.get(0).zip)
                        } else {
                            globalProgressDialog!!.dismiss()
                        }
                    }
                }, object : io.reactivex.functions.Consumer<Throwable> {
                    override fun accept(t: Throwable?) {
                        globalProgressDialog!!.dismiss()
                    }
                })
            )
        }
    }
}
