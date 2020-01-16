package com.smartitventures.beardpapa.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_login.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.smartitventures.beardpapa.R
import com.smartitventures.beardpapa.model_class.login_model.LoginResponse
import com.smartitventures.beardpapa.networking.ApiClient
import com.smartitventures.beardpapa.other_class.CheckValidation
import com.smartitventures.beardpapa.other_class.GlobalProgressDialog
import com.smartitventures.beardpapa.other_class.SavedUserDetail
import com.smartitventures.beardpapa.other_class.SavedUserDetail.*
import com.smartitventures.beardpapa.other_class.Session
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ActivityLogin : AppCompatActivity() {

    private var isPasswordShow: Boolean = false
    private var compsiteDisposable: CompositeDisposable = CompositeDisposable()
    private val apiInterface: ApiClient.WikiApiService = ApiClient.local()
    private var session: Session? = null;
    private var globalProgressDialog: GlobalProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        session = Session.getInstance(this)
        globalProgressDialog=GlobalProgressDialog(this)
        loginButtonClick()

    }

    private fun loginButtonClick() {
        //todo    creat new User
        textView6.setOnClickListener {
            startActivity(Intent(this@ActivityLogin, ActivityRegister::class.java))
        }
        // todo     password hide show
        imageView4.setOnClickListener {
            if (isPasswordShow) {
                imageView4.setImageResource(R.drawable.ic_checked_eye)
                isPasswordShow = false
                edPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                imageView4.setImageResource(R.drawable.ic_unchecked_eye)
                isPasswordShow = true
                edPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }
        //  todo   Login Button click
        btnLogin.setOnClickListener {
            if (checkValidations()) {
                compsiteDisposable.add(
                    apiInterface.loginApi(
                        etEmail.text.toString(),
                        edPassword.text.toString()
                    ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
                        object : io.reactivex.functions.Consumer<LoginResponse> {
                            override fun accept(t: LoginResponse?) {

                                if (t != null) {
                                    if (t!!.isSuccess) {
                                        session!!.put(NAME, t.payload.name)
                                        session!!.put(EMAIL, t.payload.email)
                                        session!!.put(USER_ID, t.payload.userId)
                                        session!!.put(
                                            ACCESS_TOKEN,
                                            t.payload.accessToken
                                        )
                                        session!!.put(API_TOKEN, t.payload.apiToken)
                                        session!!.put(
                                            CLOVER_USER_ID,
                                            t.payload.customerId
                                        )
                                        session!!.put(SavedUserDetail.MID, t.payload.mid)
                                        session!!.put(SavedUserDetail.PROFILE, t.payload.profile)
                                        startActivity(
                                            Intent(
                                                this@ActivityLogin,
                                                ActivityUserProfile::class.java
                                            )
                                        )
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@ActivityLogin,
                                            t.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                session!!.put(SavedUserDetail.NAME, t!!.payload.name)

                            }
                        },
                        object : io.reactivex.functions.Consumer<Throwable> {
                            override fun accept(t: Throwable?) {
                                Toast.makeText(applicationContext, "throwable", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                )
            }
        }
    }

    private fun checkValidations(): Boolean {
        var isvalidate = true
        if (TextUtils.isEmpty(etEmail.text.toString())) {
            etEmail.setError("Please Enter Email")
            isvalidate = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
            etEmail.setError("Invalid Email Address")
            isvalidate = false
        }
        if (TextUtils.isEmpty(edPassword.text.toString())) {
            edPassword.setError("Please Enter Password")
            isvalidate = false
        } else if (edPassword.text.toString().length < 8) {
            edPassword.setError("Password must be 8 character or digit")
            isvalidate = false
        } else if (!CheckValidation.isValidPassword(edPassword.text.toString())) {
            edPassword.setError("Password at least 1 character or digit or special charater")
            isvalidate = false
        }
        if (isvalidate)
            return true
        else
            return false
    }
}
