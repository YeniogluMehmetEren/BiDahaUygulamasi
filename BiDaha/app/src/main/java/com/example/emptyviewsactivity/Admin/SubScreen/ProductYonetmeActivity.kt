package com.example.emptyviewsactivity.Admin.SubScreen

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.emptyviewsactivity.R
import android.app.AlertDialog
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyviewsactivity.DB.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductYonetmeActivity : AppCompatActivity() {

    private lateinit var adapter: AdminProductAdapter
    private val productList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_yonetme)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = AdminProductAdapter(productList,
            onEditClick = { product -> editProduct(product) },
            onDeleteClick = { product -> deleteConfirm(product) }
        )
        recyclerView.adapter = adapter

        fetchAllProducts()
    }

    private fun fetchAllProducts() {
        val db = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app")
        val ref = db.getReference("Products")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (child in snapshot.children) {
                    val product = child.getValue(Product::class.java)
                    if (product != null) {
                        product.productId = child.key
                        productList.add(product)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun deleteConfirm(product: Product) {
        AlertDialog.Builder(this)
            .setTitle("İlanı Sil")
            .setMessage("'${product.title}' ilanını kalıcı olarak silmek istiyor musunuz?")
            .setPositiveButton("Sil") { _, _ ->
                val db = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app")
                if (product.productId != null) {
                    db.getReference("Products").child(product.productId!!).removeValue()
                    Toast.makeText(this, "İlan silindi.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("İptal", null)
            .show()
    }


    private fun editProduct(product: Product) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("İlanı Düzenle")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val inputTitle = EditText(this)
        inputTitle.hint = "Ürün Adı"
        inputTitle.setText(product.title)
        layout.addView(inputTitle)

        val inputPrice = EditText(this)
        inputPrice.hint = "Fiyat"
        inputPrice.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        inputPrice.setText(product.price.toString())
        layout.addView(inputPrice)

        builder.setView(layout)

        builder.setPositiveButton("Kaydet") { _, _ ->
            val newTitle = inputTitle.text.toString()
            val newPrice = inputPrice.text.toString().toDoubleOrNull()

            if (product.productId != null && newPrice != null) {
                val db = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app")
                val ref = db.getReference("Products").child(product.productId!!)

                ref.child("title").setValue(newTitle)
                ref.child("price").setValue(newPrice)

                Toast.makeText(this, "İlan güncellendi.", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("İptal", null)
        builder.show()
    }
}