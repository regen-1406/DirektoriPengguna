package com.example.direktoripengguna.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.direktoripengguna.R
import com.example.direktoripengguna.data.Post

class PostAdapter : RecyclerView.Adapter<PostAdapter.VH>() {

    private val items = mutableListOf<Post>()

    @SuppressLint("NotifyDataSetChanged")
    fun submit(data: List<Post>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    fun tambahDiAtas(post: Post) {
        items.add(0, post)
        notifyItemInserted(0)
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvJudul: TextView = view.findViewById(R.id.tvJudul)
        val tvPostId: TextView = view.findViewById(R.id.tvPostId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val post = items[position]
        holder.tvJudul.text = post.title
        holder.tvPostId.text = "post id ${post.id}"
    }

    override fun getItemCount() = items.size
}
