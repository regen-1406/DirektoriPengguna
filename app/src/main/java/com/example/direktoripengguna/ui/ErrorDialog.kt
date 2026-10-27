package com.example.direktoripengguna.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.direktoripengguna.R

/** Dialog "Gagal memuat data" dengan tombol TUTUP dan COBA LAGI. */
object ErrorDialog {

    fun show(activity: AppCompatActivity, pesan: String, onRetry: () -> Unit) {
        if (activity.isFinishing || activity.isDestroyed) return

        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_error, null)
        view.findViewById<TextView>(R.id.dlgMessage).text = pesan

        val dialog = AlertDialog.Builder(activity)
            .setView(view)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.findViewById<TextView>(R.id.dlgTutup).setOnClickListener { dialog.dismiss() }
        view.findViewById<TextView>(R.id.dlgCobaLagi).setOnClickListener {
            dialog.dismiss()
            onRetry()
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
