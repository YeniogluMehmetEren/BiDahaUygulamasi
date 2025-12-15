package com.example.emptyviewsactivity.Admin.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.emptyviewsactivity.Admin.SubScreen.ProductYonetmeActivity
import com.example.emptyviewsactivity.Admin.SubScreen.SiparisOnaylamaActivity
import com.example.emptyviewsactivity.Admin.SubScreen.UserYonetmeActivity
import com.example.emptyviewsactivity.R
import com.example.emptyviewsactivity.User.KayitOlActivity
import com.example.emptyviewsactivity.User.SubScreen.IsimDegistirmeActivity


class AdminSettingsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnYonetUser: Button = view.findViewById(R.id.btnYonetUser)
        btnYonetUser.setOnClickListener {
            val intent = Intent(requireActivity(), UserYonetmeActivity::class.java)
            startActivity(intent)
        }

        val btnYonetProduct: Button = view.findViewById(R.id.btnYonetProduct)
        btnYonetProduct.setOnClickListener {
            val intent = Intent(requireActivity(), ProductYonetmeActivity::class.java)
            startActivity(intent)
        }

        val btnSiparisOnaylama: Button = view.findViewById(R.id.btnSiparisOnaylama)
        btnSiparisOnaylama.setOnClickListener {
            val intent = Intent(requireActivity(), SiparisOnaylamaActivity::class.java)
            startActivity(intent)
        }

        val btnAdinEkle: Button = view.findViewById(R.id.btnAdminEkle)
        btnAdinEkle.setOnClickListener {
            val intent = Intent(requireActivity(), KayitOlActivity::class.java)
            startActivity(intent)
        }
    }
}