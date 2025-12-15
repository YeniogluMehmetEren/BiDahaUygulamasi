package com.example.emptyviewsactivity.Admin.SubScreen

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyviewsactivity.DB.Order
import com.example.emptyviewsactivity.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SiparisOnaylamaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminOrderAdapter
    private var orderList = mutableListOf<Order>()
    val dbRef = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_siparis_onaylama)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.rvSiparisler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = AdminOrderAdapter(orderList,
            onApproveClick = { order ->
                updateOrderStatus(order.orderId, "onaylandi")
                dbRef.child("Orders").child(order.orderId).removeValue()
            },
            onRejectClick = { order ->
                updateOrderStatus(order.orderId, "reddedildi")
                dbRef.child("Orders").child(order.orderId).removeValue()
            }
        )
        recyclerView.adapter = adapter

        fetchPendingOrders()
    }

    private fun fetchPendingOrders() {
        val dbRef = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app").reference

        dbRef.child("Orders").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<Order>()

                for (postSnapshot in snapshot.children) {
                    val rawOrder = postSnapshot.getValue(Order::class.java)

                    val realKey = postSnapshot.key

                    if (rawOrder != null && realKey != null) {
                        val fixedOrder = rawOrder.copy(orderId = realKey)

                        if (fixedOrder.status == "bekliyor") {
                            tempList.add(fixedOrder)
                        }
                    }
                }
                adapter.updateList(tempList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }



    private fun updateOrderStatus(orderId: String, newStatus: String) {
        val dbRef = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app").reference

        if (orderId.isEmpty()) {
            Toast.makeText(this, "Hata: Sipariş ID'si boş!", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("SiparisIslem", "İşlem Başladı: ID: $orderId, Yeni Durum: $newStatus")

        dbRef.child("Orders").child(orderId).child("status").setValue(newStatus)
            .addOnSuccessListener {
                Log.d("SiparisIslem", "Başarılı! Veritabanı güncellendi.")
                Toast.makeText(this, "Sipariş $newStatus", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("SiparisIslem", "HATA: ${e.message}")
                Toast.makeText(this, "İşlem başarısız: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
