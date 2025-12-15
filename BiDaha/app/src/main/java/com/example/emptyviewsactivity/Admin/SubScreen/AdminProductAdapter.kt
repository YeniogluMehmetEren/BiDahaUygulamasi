package com.example.emptyviewsactivity.Admin.SubScreen

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyviewsactivity.DB.Product
import com.example.emptyviewsactivity.R

class AdminProductAdapter(
    private var productList: MutableList<Product>,
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : RecyclerView.Adapter<AdminProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.ivItemImage)
        val tvTitle: TextView = view.findViewById(R.id.tvAdminProductTitle)
        val tvPrice: TextView = view.findViewById(R.id.tvAdminProductPrice)
        val btnEdit: ImageView = view.findViewById(R.id.btnAdminEditProduct)
        val btnDelete: ImageView = view.findViewById(R.id.btnAdminDeleteProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adminekranindaki_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.tvTitle.text = product.title
        holder.tvPrice.text = "${product.price} TL"

        if (!product.imageUrl.isNullOrEmpty()) {
            try {
                val imageBytes = Base64.decode(product.imageUrl, Base64.DEFAULT)
                val decoded = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                holder.ivImage.setImageBitmap(decoded)
            } catch (e: Exception) {
                holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        } else {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.btnEdit.setOnClickListener { onEditClick(product) }
        holder.btnDelete.setOnClickListener { onDeleteClick(product) }
    }

    override fun getItemCount() = productList.size
}