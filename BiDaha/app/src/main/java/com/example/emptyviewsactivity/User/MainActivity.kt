package com.example.emptyviewsactivity.User

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.emptyviewsactivity.Admin.AdminScreen
import com.example.emptyviewsactivity.DB.Storage
import com.example.emptyviewsactivity.R
import com.example.emptyviewsactivity.User.SubScreen.SifreDegistirmeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    private lateinit var girisEkraniEmaili: TextView
    private lateinit var girisEkraniSifresi: TextView

    private lateinit var buttonKayitOl: Button
    private lateinit var buttonGirisYap: Button
    private lateinit var buttonSifreSifirla: Button

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_giris_yap)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dbref = Storage(this)
        auth = FirebaseAuth.getInstance()
        val db = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app/")


        girisEkraniEmaili = findViewById(R.id.girisEkraniEmaili)
        girisEkraniSifresi = findViewById(R.id.girisEkraniSifresi)

        buttonKayitOl = findViewById(R.id.buttonKayitOl)
        buttonGirisYap = findViewById(R.id.buttonGirisYap)
        buttonSifreSifirla = findViewById(R.id.buttonSifreSifirlama)

        buttonGirisYap.setOnClickListener {
            val girilenEmail: String = girisEkraniEmaili.text.toString()
            val girilenSifre: String = girisEkraniSifresi.text.toString()


            if (girilenEmail.isNotEmpty() && girilenSifre.isNotEmpty()) {
                dbref.emailIsExistInUser(girilenEmail) { emailMevcutMu ->
                    if (emailMevcutMu) {
                        auth.signInWithEmailAndPassword(girilenEmail, girilenSifre)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    dbref.findUserByUid(auth.currentUser!!.uid) { user ->
                                        if (user != null && user.isItActive) {
                                            val profileUpdates = userProfileChangeRequest {
                                                displayName = user.name+" "+user.surname
                                            }
                                            auth.currentUser!!.updateProfile(profileUpdates)
                                            db.getReference("Users").child(auth.currentUser!!.uid).child("userId").setValue(auth.currentUser!!.uid)

                                            val intent = Intent(this@MainActivity, IlkUygulamaEkrani::class.java)
                                            startActivity(intent)
                                        }
                                        else
                                            Toast.makeText(this, "Kullanici aktif değil.", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(this, "Yanlış şifre", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }else{
                        dbref.emailIsExistInAdmin(girilenEmail) { emailMevcutMu ->
                            if (emailMevcutMu) {
                                auth.signInWithEmailAndPassword(girilenEmail, girilenSifre)
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            dbref.findAdminByUid(auth.currentUser!!.uid) { user ->
                                                if (user != null && user.isItActive) {
                                                    val profileUpdates = userProfileChangeRequest {
                                                        displayName = user.name+" "+user.surname
                                                    }
                                                    auth.currentUser!!.updateProfile(profileUpdates)
                                                    db.getReference("Admins").child(auth.currentUser!!.uid).child("userId").setValue(auth.currentUser!!.uid)

                                                    val intent = Intent(this@MainActivity, AdminScreen::class.java)
                                                    startActivity(intent)
                                                }
                                                else
                                                    Toast.makeText(this, "Kullanici aktif değil. Lütfen bizimle iletişime geçiniz.", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            Toast.makeText(this, "Yanlış şifre", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }else {Toast.makeText(this, "Kullanici bulunamadi.", Toast.LENGTH_SHORT).show()}
                        }
                    }
                }
            }else{Toast.makeText(this, "Bos alanlari doldurunuz.", Toast.LENGTH_SHORT).show()}

        }

        buttonKayitOl.setOnClickListener {
            val intent = Intent(this@MainActivity, KayitOlActivity::class.java)
            startActivity(intent)
        }

        buttonSifreSifirla.setOnClickListener {
            val intent = Intent(this@MainActivity, SifreDegistirmeActivity::class.java)
            startActivity(intent)
        }

    }

}