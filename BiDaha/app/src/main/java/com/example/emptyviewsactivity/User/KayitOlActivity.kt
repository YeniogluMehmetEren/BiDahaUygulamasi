package com.example.emptyviewsactivity.User

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.emptyviewsactivity.DB.Storage
import com.example.emptyviewsactivity.DB.User
import com.example.emptyviewsactivity.R
import com.google.firebase.auth.FirebaseAuth


class KayitOlActivity : AppCompatActivity() {
    private lateinit var kayitIsim: TextView
    private lateinit var kayitSoyisim: TextView
    private lateinit var kayitEmailAdres: TextView
    private lateinit var kayitSifre: TextView

    private lateinit var buttonYeniKayitOl: Button

    private lateinit var switchAdminMi: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kayit_ol)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        kayitIsim = findViewById(R.id.kayitIsim)
        kayitSoyisim = findViewById(R.id.kayitSoyisim)
        kayitEmailAdres = findViewById(R.id.kayitEmailAdres)
        kayitSifre = findViewById(R.id.kayitSifre)
        buttonYeniKayitOl = findViewById(R.id.buttonYeniKayitOl)
        switchAdminMi = findViewById(R.id.checkBoxAdminHesabıMı)

        val dbref = Storage(this)

        dbref.currentUserIsAdmin { isAdmin ->
            if (isAdmin) {
                switchAdminMi.visibility = View.VISIBLE
            } else {
                switchAdminMi.visibility = View.GONE
            }
        }



        buttonYeniKayitOl.setOnClickListener {
            val isim = kayitIsim.text.toString()
            val soyisim = kayitSoyisim.text.toString()
            val eposta = kayitEmailAdres.text.toString()
            val sifre = kayitSifre.text.toString()
            val aktiflikDurumu = true

            if (switchAdminMi.isChecked) {
                dbref.emailIsExistInAdmin(eposta) { emailMevcutMu ->
                    if (emailMevcutMu) {
                        Toast.makeText(
                            this,
                            "Bu E-posta daha once kullanilmis.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val yeniADMIN = User(
                            role = "admin",
                            userId = "",
                            name = isim,
                            surname = soyisim,
                            email = eposta,
                            isItActive = aktiflikDurumu
                        )
                        dbref.saveAdmin(eposta,sifre,yeniADMIN)
                        Toast.makeText(this, "ADMIN kayit basarili.", Toast.LENGTH_SHORT).show()

                        kayitIsim.text = ""
                        kayitSoyisim.text = ""
                        kayitEmailAdres.text = ""
                        kayitSifre.text = ""


                        val intent = Intent(this@KayitOlActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                //  Kullanici ise
                dbref.emailIsExistInUser(eposta) { emailMevcutMu ->
                    if (emailMevcutMu) {
                        Toast.makeText(
                            this,
                            "Bu E-posta daha once kullanilmis.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val yeniKullanici = User(
                            role = "user",
                            userId = "",
                            name = isim,
                            surname = soyisim,
                            email = eposta,
                            isItActive = aktiflikDurumu
                        )
                        dbref.saveUser(eposta,sifre,yeniKullanici)
                        Toast.makeText(this, "Kayit basarili.", Toast.LENGTH_SHORT).show()

                        kayitIsim.text = ""
                        kayitSoyisim.text = ""
                        kayitEmailAdres.text = ""
                        kayitSifre.text = ""

                        val intent = Intent(this@KayitOlActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}