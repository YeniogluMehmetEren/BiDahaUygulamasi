package com.example.emptyviewsactivity.Admin.SubScreen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyviewsactivity.DB.User
import com.example.emptyviewsactivity.R

class AdminUserAdapter(
    private val userList: ArrayList<User>,
    private val onFreezeClick: (User) -> Unit,
    private val onDeleteClick: (User) -> Unit
) : RecyclerView.Adapter<AdminUserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEmail: TextView = itemView.findViewById(R.id.tvAdminUserEmail)
        val tvStatus: TextView = itemView.findViewById(R.id.tvAdminUserStatus)
        val btnFreeze: Button = itemView.findViewById(R.id.btnAdminFreezeUser)
        val btnDelete: Button = itemView.findViewById(R.id.btnAdminDeleteUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_adminekranindaki_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        holder.tvEmail.text = user.email ?: "Email Yok"

        if (!user.isItActive) {
            holder.tvStatus.text = "Durum: DONDURULDU"
            holder.tvStatus.setTextColor(Color.RED)
            holder.btnFreeze.text = "Aktif Et"
            holder.btnFreeze.setBackgroundColor(Color.GREEN)
        } else {
            holder.tvStatus.text = "Durum: Aktif"
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"))
            holder.btnFreeze.text = "Dondur"
            holder.btnFreeze.setBackgroundColor(Color.parseColor("#FFA000"))
        }

        holder.btnFreeze.setOnClickListener { onFreezeClick(user) }
        holder.btnDelete.setOnClickListener { onDeleteClick(user) }
    }

    override fun getItemCount() = userList.size
}