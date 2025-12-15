package com.example.emptyviewsactivity.User.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.emptyviewsactivity.DB.Storage
import com.example.emptyviewsactivity.User.MainActivity
import com.example.emptyviewsactivity.R
import com.example.emptyviewsactivity.User.SubScreen.EPostaDegistirmeActivity
import com.example.emptyviewsactivity.User.SubScreen.SifreDegistirmeActivity
import com.example.emptyviewsactivity.User.SubScreen.IsimDegistirmeActivity
import com.example.emptyviewsactivity.User.SubScreen.ProfilBilgileriActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private lateinit var auth: FirebaseAuth

class ProfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()
        val dbRef = Storage(requireContext())
        val uid = auth.currentUser!!.uid
        dbRef.findAllByUid(uid) { user ->
            if (user != null) {
                var isim = user.name
                var soyisim = user.surname

                val textView = view.findViewById<TextView>(R.id.textViewHosGeldin)
                var hosGeldinMassage = "Hoş Geldin $isim $soyisim"
                textView?.text = hosGeldinMassage
            }else{
                Toast.makeText(requireContext(), "Kullanici yuklenirken bir hata olustu.", Toast.LENGTH_SHORT).show()
            }
        }


        val buttonProfilBilgileri = view.findViewById<TextView>(R.id.buttonProfilBilgileri)
        buttonProfilBilgileri.setOnClickListener {
            val intent = Intent(this@ProfilFragment.context, ProfilBilgileriActivity::class.java)
            startActivity(intent)
        }

        val buttonProfilBilgileriniDegistir = view.findViewById<TextView>(R.id.buttonProfilBilgileriniDegistir)
        buttonProfilBilgileriniDegistir.setOnClickListener {
            buttonProfilBilgileriniDegistir.visibility = View.INVISIBLE
        }

        val buttonSifreDegistir = view.findViewById<TextView>(R.id.buttonSifreDegistir)
        buttonSifreDegistir.setOnClickListener {
            val intent = Intent(this@ProfilFragment.context, SifreDegistirmeActivity::class.java)
            startActivity(intent)
        }

        val buttonEpostaDegistir = view.findViewById<TextView>(R.id.buttonEpostaDegistir)
        buttonEpostaDegistir.setOnClickListener {
            val intent = Intent(this@ProfilFragment.context, EPostaDegistirmeActivity::class.java)
            startActivity(intent)
        }

        val buttonIsimSoyisimDegistir = view.findViewById<TextView>(R.id.buttonIsimSoyisimDegistir)
        buttonIsimSoyisimDegistir.setOnClickListener {
            val intent = Intent(this@ProfilFragment.context, IsimDegistirmeActivity::class.java)
            startActivity(intent)
        }

        val buttonDondur = view.findViewById<TextView>(R.id.buttonDondur)
        buttonDondur.setOnClickListener {
            val db = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app/")
            val dbRef = Storage(this@ProfilFragment.requireContext())
            dbRef.findAllByUid(auth.currentUser!!.uid) { user ->
                if (user != null) {
                    db.getReference("Users").child(user.userId!!).child("itActive").get().addOnSuccessListener {   snapshot ->
                        val currentStatus = snapshot.getValue(Boolean::class.java) ?: false
                        db.getReference("Users").child(user.userId!!).child("itActive").setValue(!currentStatus).addOnSuccessListener {
                                user.isItActive = !currentStatus
                                Toast.makeText(requireContext(), "Kullanıcının aktifliği değiştirildi.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            val intent = Intent(this@ProfilFragment.context, MainActivity::class.java)
            startActivity(intent)
        }

        val buttonLogOut = view.findViewById<TextView>(R.id.buttonLogOut)
        buttonLogOut.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle("Hesaptan Çıkış Yap")
            alertDialog.setMessage("Hesabından çıkış yapmak istediğine emin misin?")
            alertDialog.setPositiveButton("Evet", { dialogInterface, which ->
                auth.signOut()
                val intent = Intent(this@ProfilFragment.context, MainActivity::class.java)
                startActivity(intent)
            })
            alertDialog.setNegativeButton("Hayır", { dialogInterface, which ->
                dialogInterface.cancel()
            })
            alertDialog.create().show()
        }


    }
}