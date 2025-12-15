package com.example.emptyviewsactivity.User.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emptyviewsactivity.R
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyviewsactivity.DB.Order
import com.example.emptyviewsactivity.DB.Product
import com.example.emptyviewsactivity.User.SubScreen.ProductAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SepetFragment : Fragment(R.layout.fragment_sepet) {
    var orderTotalPrice: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sepet, container, false)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnCheckout: Button
    private lateinit var tvEmptyCart: TextView

    private lateinit var cartAdapter: ProductAdapter
    private val cartList = ArrayList<Product>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewCart)
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice)
        btnCheckout = view.findViewById(R.id.btnCheckout)
        tvEmptyCart = view.findViewById(R.id.tvEmptyCart)

        cartAdapter = ProductAdapter(
            productList = cartList,
            onItemClick = { selectedProduct ->
            },
            onDeleteClick = { productToDelete ->
                deleteFromCart(productToDelete)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = cartAdapter

        fetchCartItems()

        btnCheckout.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val database = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app")
            val cartRef = database.getReference("Carts").child(userId)
            if (cartList.isNotEmpty()) {
                val generatedId = database.getReference("Orders").push().key

                if (generatedId != null) {
                    val newOrder = Order(
                        orderId = generatedId,
                        userId = userId,
                        userEmail = user!!.email,
                        totalPrice = orderTotalPrice,
                        status = "bekliyor"
                    )

                    database.getReference("Orders").push().setValue(newOrder).addOnSuccessListener {
                        cartRef.removeValue().addOnSuccessListener {
                            Toast.makeText(requireContext(), "Sipariş oluşturuldu", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(), "Sipariş oluşturulamadı!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun fetchCartItems() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val database = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app")
        val cartRef = database.getReference("Carts").child(userId)

        cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartList.clear()
                var totalPrice = 0.0

                for (itemSnapshot in snapshot.children) {
                    val product = itemSnapshot.getValue(Product::class.java)
                    if (product != null) {
                        cartList.add(product)
                        totalPrice += (product.price ?: 0.0)
                        orderTotalPrice = totalPrice
                    }
                }

                cartAdapter.notifyDataSetChanged()
                tvTotalPrice.text = totalPrice.toString() + " TL"

                if (cartList.isEmpty()) {
                    tvEmptyCart.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmptyCart.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Hata!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteFromCart(product: Product) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app")
        val itemRef = database.getReference("Carts").child(userId).child(product.productId!!)

        itemRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Silindi: ${product.title}", Toast.LENGTH_SHORT).show()
            }
    }
}

