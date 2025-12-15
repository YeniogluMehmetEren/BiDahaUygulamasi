package com.example.emptyviewsactivity.Admin.SubScreen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyviewsactivity.DB.User
import com.example.emptyviewsactivity.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values
import java.util.ArrayList

class UserYonetmeActivity : AppCompatActivity() {

    private lateinit var adapter: AdminUserAdapter
    private val userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_yonetme)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = AdminUserAdapter(userList,
            onFreezeClick = { user -> toggleFreeze(user) },
            onDeleteClick = { user -> deleteUser(user) }
        )
        recyclerView.adapter = adapter

        fetchData()
    }

    private fun fetchData() {
        val db = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app")
        val ref = db.getReference("Users")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (child in snapshot.children) {
                    val user = child.getValue(User::class.java)
                    if (user != null) {
                        user.userId = child.key
                        userList.add(user)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun toggleFreeze(user: User) {
        val db = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app/")
        if (user.userId != null) {
            db.getReference("Users").child(user.userId!!).child("itActive").get().addOnSuccessListener {   snapshot ->
                val currentStatus = snapshot.getValue(Boolean::class.java) ?: false
                db.getReference("Users").child(user.userId!!).child("itActive").setValue(!currentStatus)
                    .addOnSuccessListener {
                        user.isItActive = !currentStatus
                        Toast.makeText(this, "Kullanıcının aktifliği değiştirildi.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun deleteUser(user: User) {
        val db = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app/")
        if (user.userId != null) {
            db.getReference("Users").child(user.userId!!).removeValue()
            db.getReference("Products").orderByChild("userId").equalTo(user.userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (productSnapshot in snapshot.children) {
                            productSnapshot.ref.removeValue()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }
}

