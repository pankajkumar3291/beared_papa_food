package com.smartitventures.beardpapa.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import com.smartitventures.beardpapa.interfaces.BaseUrls
import com.smartitventures.beardpapa.model_class.register_local.LocalRegisterResponse
import com.smartitventures.beardpapa.model_class.register_user.RegisterUserRequest
import com.smartitventures.beardpapa.model_class.register_user.RegisterUserResponse
import com.smartitventures.beardpapa.networking.ApiClient
import com.smartitventures.beardpapa.other_class.CheckValidation
import com.smartitventures.beardpapa.other_class.GlobalProgressDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityRegister : AppCompatActivity(){

    private var compsiteDisposable: CompositeDisposable = CompositeDisposable()
    private val apiInterfaceClover: ApiClient.WikiApiService = ApiClient.clover()
    private val apiInterfaceLocal: ApiClient.WikiApiService = ApiClient.local()
    private val addresslist: ArrayList<RegisterUserRequest.Addresse> = ArrayList()
    private val emaillist: ArrayList<RegisterUserRequest.EmailAddresse> = ArrayList()
    private val phonelist: ArrayList<RegisterUserRequest.PhoneNumber> = ArrayList()
    private var marchentId: String = ""
    private var merchantToken: String = ""
    private var countryName: String? = null
    private var dayOfMonth: Int = 0
    private var monthOfYear: Int = 0
    private var year: Int = 0
    private var globalProgressDialog: GlobalProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(com.smartitventures.beardpapa.R.layout.activity_register)
        globalProgressDialog = GlobalProgressDialog(this)
        clickevent()
        createCalender()
    }

    private fun createCalender(){
        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener{_, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel(myCalendar, etDateOfBirth)
            }
        etDateOfBirth.setOnClickListener{
            DatePickerDialog(
                this@ActivityRegister, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
    private fun clickevent() {
        tvSelectMerchant.setOnClickListener{
            startActivityForResult(
                Intent(this@ActivityRegister, ActivityAllMerchant::class.java),
                22
            )
        }
        tvLogin.setOnClickListener{
            //todo   open login page
        }
        countryCodePicker2.setOnCountryChangeListener(CountryCodePicker.OnCountryChangeListener{ selectedCountry ->
            countryName = selectedCountry.name
        })
        btnRegister.setOnClickListener{
            if (checkvalidation()){
                globalProgressDialog?.show()
                compsiteDisposable.add(
                    apiInterfaceLocal.registerLocal(
                        BaseUrls.localRegisterUrl,
                        etFname.text.toString() + etLname.text.toString(),
                        etEmail.text.toString(),
                        etPwd.text.toString(),
                        "2"
                    ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
                        object : io.reactivex.functions.Consumer<LocalRegisterResponse> {
                            override fun accept(t: LocalRegisterResponse?) {
                                if (t != null) {
                                    if (t.isSuccess) {
                                        callCloverApi(t.payload.userId.toString())
                                    }
                                    else
                                    {
//                                        callCloverApi(t.payload.userId.toString())
                                    }
                                } else {
                                    globalProgressDialog!!.dismiss()
                                }
                            }
                        },
                        object : io.reactivex.functions.Consumer<Throwable> {
                            override fun accept(t: Throwable?) {
                                globalProgressDialog!!.dismiss()
                            }
                        })
                )
            }
        }
    }
    private fun callCloverApi(userId: String) {
        addresslist.clear()
        emaillist.clear()
        phonelist.clear()
        var register: RegisterUserRequest?
        addresslist.add(
            RegisterUserRequest.Addresse(
                etAddress.text.toString(),
                etAddress3.text.toString(),
                countryCodePicker2.selectedCountryNameCode,
                etAddress2.text.toString(),
                etAddress4.text.toString()
            )
        )
        emaillist.add(RegisterUserRequest.EmailAddresse(etEmail.text.toString()))
        phonelist.add(RegisterUserRequest.PhoneNumber(etPhone.text.toString()))

        register = RegisterUserRequest(
            addresslist, emaillist, etFname.text.toString(),
            etLname.text.toString(), RegisterUserRequest.Merchant(marchentId),
            RegisterUserRequest.Metadata(dayOfMonth, monthOfYear, year), phonelist
        )
//marchentId    merchantToken
        compsiteDisposable.add(
            apiInterfaceClover.registerUser("https://apisandbox.dev.clover.com/v3/merchants/"+marchentId+"/customers?access_token="+merchantToken, register).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeOn(
                Schedulers.io()
            ).subscribe(object : io.reactivex.functions.Consumer<RegisterUserResponse> {
                override fun accept(t: RegisterUserResponse?) {
                    if (t != null) {
                        addresslist.clear()
                        emaillist.clear()
                        phonelist.clear()
                        Toast.makeText(
                            applicationContext,
                            "accept" + t.firstName + t.lastName,
                            Toast.LENGTH_SHORT
                        ).show()
                        callRegisterFinalApi(userId,t.id)
                    } else {
                        globalProgressDialog!!.dismiss()
                    }
                }
            }, object : io.reactivex.functions.Consumer<Throwable>{
                override fun accept(t: Throwable?) {
                    globalProgressDialog!!.dismiss()
                    Toast.makeText(applicationContext,"throwable",Toast.LENGTH_LONG).show()
                }
            })
        )
    }
    private fun callRegisterFinalApi(userId: String, id: String){
        compsiteDisposable.add(
            apiInterfaceLocal.registerFinalApi(userId, id, marchentId, merchantToken).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeOn(
                Schedulers.io()
            ).subscribe(object : io.reactivex.functions.Consumer<LocalRegisterResponse> {
                override fun accept(t: LocalRegisterResponse?) {
                    if (t != null) {
                        if (t.isSuccess) {
                            globalProgressDialog!!.dismiss()
                            finish()
                        }
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

    private fun updateLabel(myCalendar: Calendar, dateEditText: EditText) {
        val myFormat: String = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        dateEditText.setText(sdf.format(myCalendar.time))

        dayOfMonth = Arrays.asList(dateEditText.text.toString().split("-")).get(0).get(0).toInt()
        monthOfYear = Arrays.asList(dateEditText.text.toString().split("-")).get(0).get(1).toInt()
        year = Arrays.asList(dateEditText.text.toString().split("-")).get(0).get(2).toInt()

        Toast.makeText(
            applicationContext,
            "" + dateEditText + " " + monthOfYear + " " + year,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 22){
            if (data != null) {
                marchentId = data.getStringExtra("mId")
                merchantToken = data.getStringExtra("token")
                tvSelectMerchant.setText("Change Merchant")
            }
        }
    }

    private fun checkvalidation(): Boolean {
        var isvalidate = true
        if (TextUtils.isEmpty(etFname.text.toString())) {
            etFname.error = "Please enter first Name"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etLname.text.toString())) {
            etLname.error = "Please enter last Name"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etPhone.text.toString())) {
            etPhone.error = "Please enter phone number"
            isvalidate = false
        } else if (etPhone.text.toString().length < 10) {
            etPhone.error = "please enter at lease 10 digits"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etEmail.text.toString())) {
            etEmail.error = "Please enter Email"
            isvalidate = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
            etEmail.error = "please enter valid Email"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etDateOfBirth.text.toString())) {
            etDateOfBirth.error = "please enter DOB"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etPwd.text.toString())) {
            etPwd.error = "Please enter password"
            isvalidate = false
        } else if (!CheckValidation.isValidPassword(etPwd.text.toString())) {
            etPwd.error = "Password at least 1 character or digit or special charater"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etCPwd.text.toString())) {
            etCPwd.error = "Please enter confirm password"
            isvalidate = false
        } else if (!etPwd.text.toString().equals(etCPwd.text.toString())) {
            etCPwd.error = "Confirm password doesn't match"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etAddress.text.toString())) {
            etAddress.error = "Please enter address"
            isvalidate = false
        }
        if (TextUtils.isEmpty(etAddress2.text.toString())) {
            etAddress2.setError("Please enter state")
            isvalidate = false
        }
        if (TextUtils.isEmpty(etAddress3.text.toString())) {
            etAddress3.setError("Please Enter city")
            isvalidate = false
        }
        if (TextUtils.isEmpty(marchentId) && TextUtils.isEmpty(merchantToken)) {
            Toast.makeText(this@ActivityRegister, "Please Select Merchant", Toast.LENGTH_SHORT)
                .show()
            isvalidate = false
        }
        if (TextUtils.isEmpty(etAddress4.text.toString())) {
            etAddress4.setError("Please Enter Zip Code")
            isvalidate = false
        }
        if (isvalidate)
            return true
        else
            return false
    }
}
