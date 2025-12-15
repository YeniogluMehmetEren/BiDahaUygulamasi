package com.example.emptyviewsactivity.User.SubScreen

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.emptyviewsactivity.DB.Storage
import com.example.emptyviewsactivity.User.IlkUygulamaEkrani
import com.example.emptyviewsactivity.R
import com.google.firebase.auth.FirebaseAuth

private lateinit var auth : FirebaseAuth

class ProfilBilgileriActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profil_bilgileri)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        val dbRef = Storage(this)
        val uid = auth.currentUser!!.uid

        dbRef.findAllByUid(uid) { user ->
            if (user != null) {
                val kullaniciIsmi = findViewById<TextView>(R.id.textViewKullaniciIsmi)
                val kullaniciEpostasi = findViewById<TextView>(R.id.textViewKullaniciEposta)

                kullaniciIsmi.text = auth.currentUser!!.displayName.toString()
                kullaniciEpostasi.text = auth.currentUser!!.email.toString()
            }
        }

    }
}