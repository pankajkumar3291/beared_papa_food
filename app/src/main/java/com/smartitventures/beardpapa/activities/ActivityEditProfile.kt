package com.smartitventures.beardpapa.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.smartitventures.beardpapa.R
import com.smartitventures.beardpapa.other_class.CheckValidation
import kotlinx.android.synthetic.main.activity_register.*

class ActivityEditProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        clickevent()
    }

    private fun clickevent(){
        tvSelectMerchant.setOnClickListener{
            // todo  open merchant
        }
        tvLogin.setOnClickListener{
            //todo   open login page
        }
        btnRegister.setOnClickListener {
            if (checkvalidation()){
                Toast.makeText(this@ActivityEditProfile,"done",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkvalidation(): Boolean {
        var isvalidate = true
        if (TextUtils.isEmpty(etFname.text.toString())){
            etFname.error = "Please enter first Name"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etLname.text.toString())){
            etLname.error = "Please enter last Name"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etPhone.text.toString())){
            etPhone.error = "Please enter phone number"
            isvalidate = false
        } else if (etPhone.text.toString().length < 10){
            etPhone.error = "please enter at lease 10 digits"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etEmail.text.toString())){
            etEmail.error = "Please enter Email"
            isvalidate = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()){
            etEmail.error = "please enter valid Email"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etDateOfBirth.text.toString())){
            etDateOfBirth.error = "please enter DOB"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etPwd.text.toString())){
            etPwd.error = "Please enter password"
            isvalidate = false
        } else if (!CheckValidation.isValidPassword(etPwd.text.toString())){
            etPwd.error = "Password at least 1 character or digit or special charater"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etCPwd.text.toString())) {
            etCPwd.error = "Please enter confirm password"
            isvalidate = false
        } else if (!etPwd.text.toString().equals(etCPwd.text.toString())){
            etCPwd.error = "Confirm password doesn't match"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etAddress.text.toString())){
            etAddress.error = "Please enter address"
        }
        if (isvalidate)
            return true
        else
            return false
    }
}
