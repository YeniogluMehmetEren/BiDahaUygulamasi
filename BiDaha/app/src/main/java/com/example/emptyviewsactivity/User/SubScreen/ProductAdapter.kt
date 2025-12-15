package com.example.emptyviewsactivity.User.SubScreen

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

class ProductAdapter(private val productList: ArrayList<Product>, private val onItemClick: (Product) -> Unit ,private val onDeleteClick: ((Product) -> Unit)? = null) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivItemImage: ImageView = itemView.findViewById(R.id.ivItemImage)
        val tvItemTitle: TextView = itemView.findViewById(R.id.tvItemTitle)
        val tvItemPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
        val btnDeleteItem: ImageView = itemView.findViewById(R.id.btnDeleteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = productList[position]

        holder.tvItemTitle.text = currentProduct.title
        holder.tvItemPrice.text = currentProduct.price.toString() + " TL"

        if (!currentProduct.imageUrl.isNullOrEmpty()) {
            try {
                val imageBytes = Base64.decode(currentProduct.imageUrl, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                holder.ivItemImage.setImageBitmap(decodedImage)
            } catch (e: Exception) {
                holder.ivItemImage.setImageResource(android.R.drawable.ic_menu_gallery)
                e.printStackTrace()
            }
        } else {
            holder.ivItemImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        if (onDeleteClick != null) {
            holder.btnDeleteItem.visibility = View.VISIBLE
            holder.btnDeleteItem.setOnClickListener {
                onDeleteClick.invoke(currentProduct)
            }
        } else {
            holder.btnDeleteItem.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClick(currentProduct)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}