package com.example.emptyviewsactivity.User.SubScreen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.emptyviewsactivity.DB.Storage
import com.example.emptyviewsactivity.DB.User
import com.example.emptyviewsactivity.User.IlkUygulamaEkrani
import com.example.emptyviewsactivity.User.MainActivity
import com.example.emptyviewsactivity.R
import com.google.firebase.auth.FirebaseAuth

private lateinit var auth : FirebaseAuth

class IsimDegistirmeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_isim_degistirme)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dbRef = Storage(this)
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser!!.uid

        val buttonIsimDegistirme = findViewById<Button>(R.id.buttonIsimDegistir)
        val yeniIsim = findViewById<TextView>(R.id.editTextTextYeniIsim)
        val yeniSoyisim = findViewById<TextView>(R.id.editTextTextYeniSoyisim)

        buttonIsimDegistirme.setOnClickListener {
            dbRef.findUserByUid(uid) { user ->
                if (user != null) {
                    val eposta = user.email
                    val aktiflikDurumu = user.isItActive

                    if (yeniIsim.toString() != "" && yeniSoyisim.toString() != "") {
                        val yeniKullanici = User(
                            name = yeniIsim.text.toString(),
                            surname = yeniSoyisim.text.toString(),
                            email = eposta,
                            isItActive = aktiflikDurumu
                        )
                        dbRef.updateUser(uid, yeniKullanici)
                        Toast.makeText(this, "İsim Soyisim başarıyla değiştirildi", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "İsim Soyisim boş bırakılamaz", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            dbRef.findAdminByUid(uid) { user ->
                if (user != null) {
                    val eposta = user.email
                    val aktiflikDurumu = user.isItActive
                    val userId = user.userId
                    val role = user.role

                    if (yeniIsim.toString() != "" && yeniSoyisim.toString() != "") {
                        val yeniKullanici = User(
                            userId = userId,
                            role = role,
                            name = yeniIsim.text.toString(),
                            surname = yeniSoyisim.text.toString(),
                            email = eposta,
                            isItActive = aktiflikDurumu
                        )
                        dbRef.updateAdmin(uid, yeniKullanici)
                        Toast.makeText(this, "İsim Soyisim başarıyla değiştirildi", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "İsim Soyisim boş bırakılamaz", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}