package com.example.direktoripengguna

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.direktoripengguna.network.RetrofitClient
import com.example.direktoripengguna.ui.ErrorDialog
import com.example.direktoripengguna.ui.UserAdapter
import com.example.direktoripengguna.ui.applySystemBarsPadding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var progress: ProgressBar
    private lateinit var btnUlang: Button
    private lateinit var listUser: RecyclerView

    private val adapter = UserAdapter { user ->
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra(PostActivity.EXTRA_USER_ID, user.id)
        intent.putExtra(PostActivity.EXTRA_USER_NAME, user.name)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.root).applySystemBarsPadding()

        tvStatus = findViewById(R.id.tvStatus)
        progress = findViewById(R.id.progress)
        btnUlang = findViewById(R.id.btnUlang)
        listUser = findViewById(R.id.listUser)

        listUser.layoutManager = LinearLayoutManager(this)
        listUser.adapter = adapter

        btnUlang.setOnClickListener { muatUsers() }

        muatUsers()
    }

    private fun muatUsers() {
        progress.visibility = View.VISIBLE
        btnUlang.visibility = View.GONE
        tvStatus.text = "Memuat data..."
        adapter.submit(emptyList())

        lifecycleScope.launch {
            try {
                kotlinx.coroutines.delay(3000)
                val users = RetrofitClient.api.getUsers()
                adapter.submit(users)
                tvStatus.text = "${users.size} pengguna dimuat"
            } catch (e: HttpException) {
                // Server menjawab dengan kode error (404, 500, dst.)
                tampilkanError("Server error: HTTP ${e.code()}. Coba lagi beberapa saat lagi.")
            } catch (e: IOException) {
                // Tidak ada jawaban sama sekali (internet mati, timeout, DNS gagal)
                tampilkanError("Tidak ada koneksi internet. Periksa jaringan Anda, lalu coba lagi.")
            } finally {
                progress.visibility = View.GONE
            }
        }
    }

    private fun tampilkanError(pesan: String) {
        tvStatus.text = "Status: gagal memuat"
        btnUlang.visibility = View.VISIBLE
        ErrorDialog.show(this, pesan) { muatUsers() }
    }
}
