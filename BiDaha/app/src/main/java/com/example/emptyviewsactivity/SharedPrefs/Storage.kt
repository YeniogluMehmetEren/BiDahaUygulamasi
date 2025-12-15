package com.example.myapp.utils
/*
import android.content.Context
import android.content.SharedPreferences
import com.example.emptyviewsactivity.SharedPrefs.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Storage(context: Context) {
    private val prefsName = "AppPrefs"
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    private val gson = Gson()
    // User Islemeri
    fun kullaniciListesiniKaydet(users: List<User>) {
        val json = gson.toJson(users)
        sharedPref.edit().putString("user_list", json).apply()
    }
    fun kullaniciListesiniGetir(): MutableList<User> {
        val json = sharedPref.getString("user_list", null)
        val type = object : TypeToken<MutableList<User>>() {}.type
        return if (json != null) gson.fromJson(json, type) else mutableListOf()
    }
    fun kullaniciEkle(user: User) {
        val users = kullaniciListesiniGetir()
        users.add(user)
        kullaniciListesiniKaydet(users)
    }
    fun kullaniciyiGuncelle(eskiUser: User, updatedUser: User) {
        val users = kullaniciListesiniGetir()
        val index = users.indexOf(eskiUser)
        if (index != -1) {
            users[index] = updatedUser
            kullaniciListesiniKaydet(users)
        }
    }
        fun kullaniciyiEkpostaIleBul(email: String): User? {
            val users = kullaniciListesiniGetir()
            return users.find { it.email == email }
        }
        fun girisDogrulama(email: String, password: String): Boolean {
            val user = kullaniciyiEkpostaIleBul(email)
            return user != null && user.password == password
        }
        fun epostaZatenVarMi(email: String): Boolean {
            val user = kullaniciyiEkpostaIleBul(email)
            return user?.email.equals(email)
        }
        fun kullaniciyiSil(user: User) {
            val users = kullaniciListesiniGetir()
            users.remove(user)
        }
        fun tumKullanicilariSil() {
            sharedPref.edit().remove("user_list").apply()
        }
        //Admin
        fun adminListesiniKaydet(admins: List<User>) {
            val json = gson.toJson(admins)
            sharedPref.edit().putString("admin_list", json).apply()
        }
        fun adminListesiniGetir(): MutableList<User> {
            val json = sharedPref.getString("admin_list", null)
            val type = object : TypeToken<MutableList<User>>() {}.type
            return if (json != null) gson.fromJson(json, type) else mutableListOf()
        }
        fun adminEkle(admin: User) {
            val admins = adminListesiniGetir()
            admins.add(admin)
            adminListesiniKaydet(admins)
        }
        fun adminiEkpostaIleBul(email: String): User? {
            val admins = adminListesiniGetir()
            return admins.find { it.email == email }
        }
        fun adminGirisDogrulama(email: String, password: String): Boolean {
            val admin = adminiEkpostaIleBul(email)
            return admin != null && admin.password == password
        }
        fun adminEpostaZatenVarMi(email: String): Boolean {
            val admin = adminiEkpostaIleBul(email)
            return admin?.email.equals(email)
        }
        fun tumAdminleriSil() {
            sharedPref.edit().remove("admin_list").apply()
        }
        // Test
        fun aktifKullaniciEpostasiniKaydet(key: String, value: String) {
            sharedPref.edit().putString(key, value).apply()
        }
        fun aktifKullaniciEpostasiniCagir(key: String, defaultValue: String = ""): String {
            return sharedPref.getString(key, defaultValue) ?: defaultValue
        }
    fun aktifKullaniciyiSil() {
            sharedPref.edit().remove("aktif_kullanici").apply()
    }




}*/