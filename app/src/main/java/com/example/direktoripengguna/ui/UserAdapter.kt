package com.example.direktoripengguna.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.direktoripengguna.R
import com.example.direktoripengguna.data.User

class UserAdapter(
    private val onClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.VH>() {

    private var items: List<User> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submit(data: List<User>) {
        items = data
        notifyDataSetChanged()
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNama)
        val tvDetail: TextView = view.findViewById(R.id.tvDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val user = items[position]
        holder.tvNama.text = user.name
        holder.tvDetail.text = "@${user.username} · ${user.email}"
        holder.itemView.setOnClickListener { onClick(user) }
    }

    override fun getItemCount() = items.size
}
