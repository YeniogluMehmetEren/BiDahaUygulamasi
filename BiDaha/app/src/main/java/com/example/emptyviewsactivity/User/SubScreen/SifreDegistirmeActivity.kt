package com.example.emptyviewsactivity.User.SubScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.emptyviewsactivity.User.MainActivity
import com.example.emptyviewsactivity.R
import com.google.firebase.auth.FirebaseAuth

private lateinit var auth: FirebaseAuth
class SifreDegistirmeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sifre_degistirme)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val buttonSifreDegistirme = findViewById<Button>(R.id.buttonSifreDegistirme)
        val eposta = findViewById<TextView>(R.id.editTextTextEposta)
        val bilgi = findViewById<TextView>(R.id.textViewBilgi)
        val epostaTekrarYolla = findViewById<TextView>(R.id.textViewEpostaTekrar)

        buttonSifreDegistirme.setOnClickListener {
            val emailAdresi = eposta.text.toString().trim()
            if (emailAdresi.isEmpty()) {
                Toast.makeText(this, "Lütfen e-posta adresinizi girin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(emailAdresi).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    buttonSifreDegistirme.visibility = View.GONE
                    eposta.visibility = View.GONE
                    bilgi.visibility = View.VISIBLE
                    epostaTekrarYolla.visibility = View.VISIBLE
                }else{
                    Toast.makeText(this, "Bir hata meydana geldi.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        epostaTekrarYolla.setOnClickListener {
            auth.sendPasswordResetEmail(eposta.text.toString().trim()).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "E-Posta tekrardan yollanıldı.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "E-Posta tekrardan yollanılılırken bir hata meydana geldi.", Toast.LENGTH_SHORT).show()
                }

            }
            bilgi.visibility = View.GONE
            epostaTekrarYolla.visibility = View.GONE
            eposta.visibility = View.VISIBLE
            buttonSifreDegistirme.visibility = View.VISIBLE
        }

    }
}