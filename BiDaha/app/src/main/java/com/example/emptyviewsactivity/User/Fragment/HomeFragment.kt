package com.example.emptyviewsactivity.User.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emptyviewsactivity.R
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyviewsactivity.DB.Product
import com.example.emptyviewsactivity.User.SubScreen.ProductAdapter
import com.example.emptyviewsactivity.User.SubScreen.ProductDetailFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyList: TextView
    private lateinit var etSearch: EditText

    private lateinit var productAdapter: ProductAdapter
    private val productList = ArrayList<Product>()
    private val fullProductList = ArrayList<Product>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        progressBar = view.findViewById(R.id.progressBarHome)
        tvEmptyList = view.findViewById(R.id.tvEmptyList)
        etSearch = view.findViewById(R.id.etSearch)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        productAdapter = ProductAdapter(
            productList,
            onItemClick =
                { clickedProduct ->
                    val detailFragment = ProductDetailFragment()

                    val bundle = Bundle()
                    bundle.putSerializable("selectedProduct", clickedProduct)
                    detailFragment.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(android.R.id.content, detailFragment)
                        .addToBackStack(null)
                        .commit()
                })
        recyclerView.adapter = productAdapter

        fetchProductsFromFirebase()

        setupSearchLogic()
    }

    private fun fetchProductsFromFirebase() {
        progressBar.visibility = View.VISIBLE

        val database = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app")
        val ref = database.getReference("Products")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                fullProductList.clear()

                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        if (product != null) {
                            productList.add(0, product)
                            fullProductList.add(0, product)
                        }
                    }
                }

                productAdapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
                if (productList.isEmpty()) {
                    tvEmptyList.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmptyList.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Veri hatası: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSearchLogic() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterList(query: String) {
        val lowerCaseQuery = query.lowercase().trim()
        productList.clear()

        if (lowerCaseQuery.isEmpty()) {
            productList.addAll(fullProductList)
        } else {
            for (item in fullProductList) {
                if ((item.title?.lowercase()?.contains(lowerCaseQuery) == true) ||
                    (item.price.toString().contains(lowerCaseQuery))) {
                    productList.add(item)
                }
            }
        }
        productAdapter.notifyDataSetChanged()
        if (productList.isEmpty()) {
            tvEmptyList.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            tvEmptyList.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}