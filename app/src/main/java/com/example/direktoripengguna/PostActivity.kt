package com.example.direktoripengguna

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.direktoripengguna.data.PostBaru
import com.example.direktoripengguna.network.RetrofitClient
import com.example.direktoripengguna.ui.ErrorDialog
import com.example.direktoripengguna.ui.PostAdapter
import com.example.direktoripengguna.ui.applySystemBarsPadding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PostActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_ID = "user_id"
        const val EXTRA_USER_NAME = "user_name"
    }

    private var userId = 0
    private lateinit var etJudul: EditText
    private lateinit var btnTambah: Button
    private lateinit var tvKicker: TextView
    private lateinit var tvStatus: TextView
    private lateinit var listPost: RecyclerView
    private val adapter = PostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        findViewById<View>(R.id.root).applySystemBarsPadding()

        userId = intent.getIntExtra(EXTRA_USER_ID, 0)
        val nama = intent.getStringExtra(EXTRA_USER_NAME) ?: "Postingan"

        tvKicker = findViewById(R.id.tvKicker)
        etJudul = findViewById(R.id.etJudul)
        btnTambah = findViewById(R.id.btnTambah)
        tvStatus = findViewById(R.id.tvStatus)
        listPost = findViewById(R.id.listPost)

        tvKicker.text = nama.uppercase()
        listPost.layoutManager = LinearLayoutManager(this)
        listPost.adapter = adapter

        btnTambah.setOnClickListener { kirimPost() }

        muatPosts()
    }

    private fun muatPosts() {
        tvStatus.text = "Memuat postingan..."
        lifecycleScope.launch {
            try {
                val posts = RetrofitClient.api.getPostsByUser(userId)
                adapter.submit(posts)
                tvStatus.text = "${posts.size} postingan"
            } catch (e: HttpException) {
                tampilkanError("Server error: HTTP ${e.code()}. Coba lagi beberapa saat lagi.")
            } catch (e: IOException) {
                tampilkanError("Tidak ada koneksi internet. Periksa jaringan Anda, lalu coba lagi.")
            }
        }
    }

    private fun tampilkanError(pesan: String) {
        tvStatus.text = "Status: gagal memuat"
        ErrorDialog.show(this, pesan) { muatPosts() }
    }

    private fun kirimPost() {
        val judul = etJudul.text.toString().trim()
        if (judul.isEmpty()) {
            etJudul.error = "Judul tidak boleh kosong"
            return
        }

        btnTambah.isEnabled = false
        lifecycleScope.launch {
            try {
                val baru = PostBaru(
                    title = judul,
                    body = "Dibuat dari aplikasi Direktori Pengguna",
                    userId = userId
                )
                val hasil = RetrofitClient.api.buatPost(baru)
                tvStatus.text = "Tersimpan dengan id ${hasil.id}"
                etJudul.text.clear()
                adapter.tambahDiAtas(hasil)
                listPost.scrollToPosition(0)
            } catch (e: HttpException) {
                tvStatus.text = "Gagal menyimpan: HTTP ${e.code()}"
            } catch (e: IOException) {
                tvStatus.text = "Tidak ada koneksi internet"
            } finally {
                btnTambah.isEnabled = true
            }
        }
    }
}
