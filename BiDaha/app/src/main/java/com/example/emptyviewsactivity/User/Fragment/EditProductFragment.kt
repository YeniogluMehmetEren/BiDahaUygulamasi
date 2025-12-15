package com.example.emptyviewsactivity.User.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emptyviewsactivity.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.example.emptyviewsactivity.DB.Product
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class EditProductFragment : Fragment(R.layout.fragment_edit_product) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_product, container, false)
    }

    private lateinit var ivEditImage: ImageView
    private lateinit var etEditTitle: EditText
    private lateinit var etEditDesc: EditText
    private lateinit var etEditPrice: EditText
    private lateinit var btnUpdate: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var currentProduct: Product
    private var newImageUri: Uri? = null


    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            newImageUri = uri
            ivEditImage.setImageURI(uri)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null && requireArguments().containsKey("productToEdit")) {
            currentProduct = requireArguments().getSerializable("productToEdit") as Product
        } else {
            Toast.makeText(requireContext(), "Hata: Ürün bulunamadı", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
            return
        }

        ivEditImage = view.findViewById(R.id.ivEditImage)
        etEditTitle = view.findViewById(R.id.etEditTitle)
        etEditDesc = view.findViewById(R.id.etEditDesc)
        etEditPrice = view.findViewById(R.id.etEditPrice)
        btnUpdate = view.findViewById(R.id.btnUpdate)
        progressBar = view.findViewById(R.id.progressBarEdit)

        fillData()

        ivEditImage.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        btnUpdate.setOnClickListener {
            startUpdateProcess()
        }
    }

    private fun fillData() {
        etEditTitle.setText(currentProduct.title)
        etEditDesc.setText(currentProduct.description)
        etEditPrice.setText(currentProduct.price.toString())

        if (!currentProduct.imageUrl.isNullOrEmpty()) {
            try {
                val imageBytes = Base64.decode(currentProduct.imageUrl, Base64.DEFAULT)
                val decoded = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ivEditImage.setImageBitmap(decoded)
            } catch (e: Exception) {
            }
        }
    }

    private fun startUpdateProcess() {
        val title = etEditTitle.text.toString().trim()
        val desc = etEditDesc.text.toString().trim()
        val priceStr = etEditPrice.text.toString().trim()

        if (title.isEmpty() || desc.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        btnUpdate.isEnabled = false
        btnUpdate.text = "Kaydediliyor..."

        if (newImageUri != null) {
            val base64Image = encodeImageToBase64(newImageUri!!)
            if (base64Image != null) {
                updateFirebase(title, desc, priceStr.toDouble(), base64Image)
            } else {
                progressBar.visibility = View.GONE
                btnUpdate.isEnabled = true
                btnUpdate.text = "Değişiklikleri Kaydet"
                Toast.makeText(requireContext(), "Resim işlenemedi", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            updateFirebase(title, desc, priceStr.toDouble(), currentProduct.imageUrl)
        }
    }

    private fun updateFirebase(title: String, desc: String, price: Double, imageCode: String?) {
        val database = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app")
        val ref = database.getReference("Products").child(currentProduct.productId!!)

        val updatedProduct = mapOf(
            "productId" to currentProduct.productId,
            "sellerId" to currentProduct.sellerId,
            "title" to title,
            "description" to desc,
            "price" to price,
            "imageUrl" to imageCode
        )

        ref.updateChildren(updatedProduct)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                btnUpdate.isEnabled = true
                btnUpdate.text = "Değişiklikleri Kaydet"
                Toast.makeText(requireContext(), "İlan Güncellendi!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                btnUpdate.isEnabled = true
                btnUpdate.text = "Değişiklikleri Kaydet"
                Toast.makeText(requireContext(), "Hata!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun encodeImageToBase64(imageUri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 300, 300, true)
            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        } catch (e: Exception) { null }
    }
}

