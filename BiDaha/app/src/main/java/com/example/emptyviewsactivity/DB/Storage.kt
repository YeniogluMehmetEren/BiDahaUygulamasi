package com.example.emptyviewsactivity.DB

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Storage(context: Context) {

    private var dbRefUser : DatabaseReference = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
    private var dbRefAdmin : DatabaseReference = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Admins")
    private var authRef : FirebaseAuth = FirebaseAuth.getInstance()


    fun saveUser(eposta: String, sifre: String, user: User) {
        authRef.createUserWithEmailAndPassword(eposta, sifre)
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult.user
                if (firebaseUser != null) {
                    dbRefUser.child(firebaseUser.uid).setValue(user)
                }
            }
            .addOnFailureListener { authException ->
                Log.e("Firebase", "Authentication basarisiz", authException)
            }
    }

    fun emailIsExistInUser(email: String, onResult: (Boolean) -> Unit) {
        val query = dbRefUser.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onResult(snapshot.exists())
            }
            override fun onCancelled(error: DatabaseError) {
                onResult(false)
            }
        })
    }

    fun findUserByUid(uid: String, onResult: (User?) -> Unit) {
        dbRefUser.child(uid).get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    onResult(user)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Kullanıcı verisi alınamadı.", exception)
                onResult(null)
            }
    }

    fun updateUser(uid: String, updatedUser: User) {
        dbRefUser.child(uid).setValue(updatedUser)
    }

    fun findAllByUid(uid: String, onResult: (User?) -> Unit) {
        var done = true
        dbRefUser.child(uid).get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    onResult(user)
                    done = false
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Kullanıcı verisi alınamadı.", exception)
                onResult(null)
            }
        if (done) {
            dbRefAdmin.child(uid).get()
                .addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(User::class.java)
                        onResult(user)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firebase", "Admin verisi alınamadı.", exception)
                    onResult(null)
                }
        }
    }

    fun currentUserIsAdmin(onResult: (Boolean) -> Unit) {
        val currentUserUid = authRef.currentUser?.uid

        if (currentUserUid == null) {
            onResult(false)
            return
        }

        findAdminByUid(currentUserUid) { user ->
            val isAdmin = user != null && user.role == "admin"
            onResult(isAdmin)
        }
    }








    // ADMIN


    fun saveAdmin(eposta: String, sifre: String,user: User) {
        authRef.createUserWithEmailAndPassword(eposta, sifre)
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult.user
                if (firebaseUser != null) {
                    dbRefAdmin.child(firebaseUser.uid).setValue(user)
                }
            }
            .addOnFailureListener { authException ->
                Log.e("Firebase", "Authentication basarisiz", authException)
            }
    }

    fun emailIsExistInAdmin(email: String, onResult: (Boolean) -> Unit) {
        val query = dbRefAdmin.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                onResult(snapshot.exists())
            }
            override fun onCancelled(error: DatabaseError) {
                onResult(false)
            }
        })
    }


    fun findAdminByUid(uid: String, onResult: (User?) -> Unit) {
        dbRefAdmin.child(uid).get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Admin verisi alınamadı.", exception)
                onResult(null)
            }
    }

    fun updateAdmin(uid: String, updatedUser: User) {
        dbRefAdmin.child(uid).setValue(updatedUser)
    }





    // PRODUCT



}