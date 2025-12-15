package com.example.emptyviewsactivity.Admin.SubScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyviewsactivity.DB.Order
import com.example.emptyviewsactivity.R

class AdminOrderAdapter(
    private val orderList: MutableList<Order>,
    private val onApproveClick: (Order) -> Unit,
    private val onRejectClick: (Order) -> Unit
) : RecyclerView.Adapter<AdminOrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUser: TextView = view.findViewById(R.id.tvOrderUser)
        val tvPrice: TextView = view.findViewById(R.id.tvOrderPrice)
        val tvStatus: TextView = view.findViewById(R.id.tvOrderStatus)
        val btnApprove: Button = view.findViewById(R.id.btnApproveOrder)
        val btnReject: Button = view.findViewById(R.id.btnRejectOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        holder.tvUser.text = "Müşteri: ${order.userEmail}"
        holder.tvPrice.text = "Tutar: ${order.totalPrice} TL"
        holder.tvStatus.text = "Durum: ${order.status}"

        if (order.status == "onaylandi") {
            holder.btnApprove.isEnabled = false
            holder.btnApprove.text = "Onaylandı"
        } else {
            holder.btnApprove.isEnabled = true
            holder.btnApprove.text = "Onayla"
        }

        holder.btnApprove.setOnClickListener { onApproveClick(order) }
        holder.btnReject.setOnClickListener { onRejectClick(order) }
    }

    override fun getItemCount() = orderList.size

    fun updateList(newList: List<Order>) {
        orderList.clear()
        orderList.addAll(newList)
        notifyDataSetChanged()
    }
}