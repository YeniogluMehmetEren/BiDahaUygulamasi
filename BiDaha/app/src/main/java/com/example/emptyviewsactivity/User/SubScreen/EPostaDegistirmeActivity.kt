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
import com.example.emptyviewsactivity.User.MainActivity
import com.example.emptyviewsactivity.R
import com.google.firebase.auth.FirebaseAuth

private lateinit var auth: FirebaseAuth
class EPostaDegistirmeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_eposta_degistirme)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dbRef = Storage(this)
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser!!.uid

        val buttonEpostaDegistirme = findViewById<Button>(R.id.buttonEpostaDegistirme)
        val eskiEposta = findViewById<TextView>(R.id.editTextTextEskiEposta)
        val yeniEposta = findViewById<TextView>(R.id.editTextTextYeniEposta)

        buttonEpostaDegistirme.setOnClickListener {
            dbRef.findUserByUid(uid) { user ->
                if (user != null) {
                    val isim = user.name
                    val soyisim = user.surname
                    val eposta = user.email
                    val aktiflikDurumu = user.isItActive

                    if (eskiEposta.text.toString() == eposta) {
                        if (eposta != yeniEposta.text.toString()) {
                            val yeniKullanici = User(
                                name = isim,
                                surname = soyisim,
                                email = yeniEposta.text.toString(),
                                isItActive = aktiflikDurumu
                            )
                            auth.currentUser!!.verifyBeforeUpdateEmail(yeniEposta.text.toString()).addOnSuccessListener {
                                dbRef.updateUser(uid, yeniKullanici)
                                Toast.makeText(this, "E-Posta başarıyla değiştirildi. /n Yeni E-Postanız ile giriş yapınız.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(this, "Eski E-Posta ile yeni E-Posta aynı olamaz", Toast.LENGTH_SHORT).show()
                        }
                    } else{
                        Toast.makeText(this, "E-Postalar eşleşmiyor", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            dbRef.findAdminByUid(uid) { user ->
                if (user != null) {
                    val isim = user.name
                    val soyisim = user.surname
                    val eposta = user.email
                    val aktiflikDurumu = user.isItActive
                    val userId = user.userId
                    val role = user.role

                    if (eskiEposta.text.toString() == eposta) {
                        if (eposta != yeniEposta.text.toString()) {
                            val yeniKullanici = User(
                                userId = userId,
                                role = role,
                                name = isim,
                                surname = soyisim,
                                email = yeniEposta.text.toString(),
                                isItActive = aktiflikDurumu
                            )
                            auth.currentUser!!.verifyBeforeUpdateEmail(yeniEposta.text.toString()).addOnSuccessListener {
                                dbRef.updateAdmin(uid, yeniKullanici)
                                Toast.makeText(this, "E-Posta başarıyla değiştirildi. /n Yeni E-Postanız ile giriş yapınız.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(this, "Eski E-Posta ile yeni E-Posta aynı olamaz", Toast.LENGTH_SHORT).show()
                        }
                    } else{
                        Toast.makeText(this, "E-Postalar eşleşmiyor", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}