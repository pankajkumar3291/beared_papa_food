package com.smartitventures.beardpapa.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.smartitventures.beardpapa.R
import com.smartitventures.beardpapa.model_class.get_all_merchant.GetAddress
import com.smartitventures.beardpapa.model_class.get_all_merchant.GetAllMerchantsRequest
import com.smartitventures.beardpapa.model_class.get_all_merchant.GetMerchantAddress
import com.smartitventures.beardpapa.networking.ApiClient
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_all_merchant.*
import kotlinx.android.synthetic.main.row_mechant.view.*

class ActivityAllMerchant : AppCompatActivity() {

    private var items: ArrayList<GetAllMerchantsRequest.Payload.Allmerchant> = ArrayList()
    private var address: ArrayList<String> = ArrayList()
    private var compsiteDisposable: CompositeDisposable = CompositeDisposable()
    private val apiInterface: ApiClient.WikiApiService = ApiClient.local()
    private var count = 0


    //https://sandbox.dev.clover.com/v3/merchants/MMD5VA20VTCX1/address"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_merchant)
        swipLoader.isRefreshing = true
        backArrow.setOnClickListener {finish()}

        compsiteDisposable!!.add(
            apiInterface.getAllMerchant("http://192.168.0.40/beardpapa/public/api/customer/merchants").observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeOn(
                Schedulers.io()
            ).subscribe(object : io.reactivex.functions.Consumer<GetAllMerchantsRequest> {
                override fun accept(t: GetAllMerchantsRequest?) {

                    if (t != null) {
                        if (t.isSuccess && t.payload.allmerchants.size > 0) {
                            items.addAll(t.payload.allmerchants)
                            getMerchantAddress()
                        } else {
                            swipLoader.visibility = View.GONE
                            textView30.visibility = View.VISIBLE
                            swipLoader.isRefreshing = false
                        }
                    }
                    else
                    {
                        swipLoader.visibility = View.GONE
                        textView30.visibility = View.VISIBLE
                        swipLoader.isRefreshing = false
                    }
                }
            }, object : io.reactivex.functions.Consumer<Throwable> {
                override fun accept(t: Throwable?) {
//                    Toast.makeText(applicationContext, "throwable", Toast.LENGTH_SHORT).show()
                    swipLoader.visibility = View.GONE
                    textView30.visibility = View.VISIBLE
                    swipLoader.isRefreshing = false
                }
            })
        )
    }

    private fun getMerchantAddress() {
        if (count <= items.size - 1) {
            compsiteDisposable.add(
                apiInterface.getMerchantAddress(
                    "https://apisandbox.dev.clover.com/v3/merchants/" + items.get(
                        count
                    ).mId + "/address?access_token=" + items.get(count).token
                ).observeOn(
                    AndroidSchedulers.mainThread()
                ).subscribeOn(
                    Schedulers.io()
                ).subscribe(object : io.reactivex.functions.Consumer<GetMerchantAddress> {
                    override fun accept(t: GetMerchantAddress?) {
                        if (t != null) {
                            if (t.address1 != null) {
                                items.get(count).getAddress = GetAddress()
                                items.get(count).getAddress.address =
                                    t.address1 + "," + t.city + "," + t.country + "," + t.state + "," + t.zip
                                items.get(count).getAddress.phoneNumber = t.phoneNumber
                                count++
                                getMerchantAddress()
                            }
                        }
                    }
                }, object : io.reactivex.functions.Consumer<Throwable> {
                    override fun accept(t: Throwable?) {
                        Toast.makeText(applicationContext, "throwable", Toast.LENGTH_SHORT).show()
                    }
                })
            )
        } else if (count == items.size) {
            recAllMerchant.setHasFixedSize(true)
            recAllMerchant.adapter = AllMerchantAdapter(this@ActivityAllMerchant, items)
            swipLoader.isRefreshing = false
        }
    }
    // todo =============================== Adapter ====================================


    class AllMerchantAdapter(
        context: Context,
        val items: List<GetAllMerchantsRequest.Payload.Allmerchant>
    ) : RecyclerView.Adapter<AllMerchantAdapter.AllMerchantViewHolder>(){
        var context = context;
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMerchantViewHolder {
            var view: View
            view = LayoutInflater.from(context).inflate(R.layout.row_mechant, parent, false)
            return AllMerchantViewHolder(view)
        }

        override fun getItemCount():Int{
            return items.size
        }

        override fun onBindViewHolder(holder: AllMerchantViewHolder, position: Int){
            var allmerchant: GetAllMerchantsRequest.Payload.Allmerchant = items.get(position)
            holder.tvName.setText(allmerchant.name)
            Picasso.get().load(allmerchant.thumbnail).into(holder.imgMerchant)
            holder.address.setText(allmerchant.getAddress.address)
            holder.tvPhone.setText(allmerchant.getAddress.phoneNumber)
            holder.merchantRow.setOnClickListener {
                val intent = Intent()
                var tempcontext: Context
                intent.putExtra("mId", allmerchant.mId)
                intent.putExtra("token", allmerchant.token)
                tempcontext = context as ActivityAllMerchant
                tempcontext.setResult(Activity.RESULT_OK, intent)
                tempcontext.finish()
            }
        }

        class AllMerchantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvName: TextView = itemView.textView13
            var tvPhone: TextView = itemView.textView15
            var address: TextView = itemView.textView17
            var imgMerchant: ImageView = itemView.imageView5
            var merchantRow: ConstraintLayout = itemView.marchentrow
        }
    }
}
