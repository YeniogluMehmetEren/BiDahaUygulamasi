package com.example.emptyviewsactivity.User.SubScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emptyviewsactivity.R
import android.graphics.BitmapFactory
import android.graphics.Color
import android.telecom.Call
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.emptyviewsactivity.API.Currency.CurrencyService
import com.example.emptyviewsactivity.DB.Product
import com.example.emptyviewsactivity.User.Fragment.EditProductFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.emptyviewsactivity.API.ExchangeRateResponse
import retrofit2.Callback
import retrofit2.Response

class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    private lateinit var product: Product

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        product = arguments?.getSerializable("selectedProduct") as? Product ?: return

        val ivImage: ImageView = view.findViewById(R.id.ivDetailImage)
        val tvTitle: TextView = view.findViewById(R.id.tvDetailTitle)
        val tvPrice: TextView = view.findViewById(R.id.tvDetailPrice)
        val tvDollarPrice: TextView = view.findViewById(R.id.tvDetailDollarPrice)
        val tvDesc: TextView = view.findViewById(R.id.tvDetailDesc)
        val btnAction1: Button = view.findViewById(R.id.btnAction1)
        val btnAction2: Button = view.findViewById(R.id.btnAction2)

        tvTitle.text = product.title
        tvPrice.text = product.price.toString() + " TL"
        tvDesc.text = product.description

        if (!product.imageUrl.isNullOrEmpty()) {
            try {
                val imageBytes = Base64.decode(product.imageUrl, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ivImage.setImageBitmap(decodedImage)
            } catch (e: Exception) {
                ivImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserId != null && currentUserId == product.sellerId) {
            setupSellerMode(btnAction1, btnAction2)
        } else {
            setupBuyerMode(btnAction1, btnAction2)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(CurrencyService::class.java)

        service.getRates().enqueue(object : Callback<ExchangeRateResponse> {
            override fun onResponse(call: retrofit2.Call<ExchangeRateResponse?>,
                                    response: Response<ExchangeRateResponse?>) {
                if (response.isSuccessful) {
                    val rates = response.body()?.rates
                    val dolarKuru = rates?.get("USD")

                    if (dolarKuru != null) {
                        val urunFiyatiTL = product.price.toString().toDouble()
                        val dolarFiyati = urunFiyatiTL * dolarKuru
                        val dolarPrice = String.format("%.2f", dolarFiyati)
                        tvDollarPrice.text = dolarPrice + " $"
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<ExchangeRateResponse?>, t: Throwable) {
            }
        })

    }

    private fun setupSellerMode(btnLeft: Button, btnRight: Button) {
        btnLeft.text = "Düzenle"
        btnLeft.setOnClickListener {
            val editFragment = EditProductFragment()

            val bundle = Bundle()
            bundle.putSerializable("productToEdit", product)
            editFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(android.R.id.content, editFragment)
                .addToBackStack(null)
                .commit()
        }

        btnRight.text = "İlanı Sil"
        btnRight.setBackgroundColor(Color.RED)
        btnRight.setOnClickListener {
            deleteProduct()
        }
    }

    private fun setupBuyerMode(btnLeft: Button, btnRight: Button) {
        btnLeft.text = "Sepete Ekle"
        btnLeft.setOnClickListener {
            addToCart()
        }

        btnRight.visibility = View.GONE
    }

    private fun deleteProduct() {
        val database = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app")
        val ref = database.getReference("Products").child(product.productId!!)

        ref.removeValue().addOnCompleteListener { task ->
            if (!isAdded) return@addOnCompleteListener

            if (task.isSuccessful) {
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Ürün Silinemedi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addToCart() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return

        val database = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app/")
        val cartRef = database.getReference("Carts").child(currentUser.uid).child(product.productId!!)

        cartRef.setValue(product)
            .addOnSuccessListener {
                context?.let { ctx ->
                    Toast.makeText(ctx, "Ürün sepete eklendi", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                context?.let { ctx ->
                    Toast.makeText(ctx, "Hata!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}