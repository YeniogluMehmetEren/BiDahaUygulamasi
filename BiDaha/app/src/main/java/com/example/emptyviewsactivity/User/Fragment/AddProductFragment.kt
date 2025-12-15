package com.example.emptyviewsactivity.User.Fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.emptyviewsactivity.DB.Product
import com.example.emptyviewsactivity.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream


class AddProductFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    private lateinit var ivProductImage: ImageView
    private lateinit var tvAddPhotoHint: TextView
    private lateinit var etProductTitle: EditText
    private lateinit var etProductDesc: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var btnUploadProduct: Button
    private lateinit var progressBar: ProgressBar

    private var selectedImageUri: Uri? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            ivProductImage.setImageURI(uri)
            tvAddPhotoHint.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        ivProductImage = view.findViewById(R.id.ivProductImage)
        tvAddPhotoHint = view.findViewById(R.id.tvAddPhotoHint)
        etProductTitle = view.findViewById(R.id.etProductTitle)
        etProductDesc = view.findViewById(R.id.etProductDesc)
        etProductPrice = view.findViewById(R.id.etProductPrice)
        btnUploadProduct = view.findViewById(R.id.btnUploadProduct)
        progressBar = view.findViewById(R.id.progressBar)

        ivProductImage.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        btnUploadProduct.setOnClickListener {
            saveProduct()
        }

    }

    private fun saveProduct() {
        val title = etProductTitle.text.toString().trim()
        val desc = etProductDesc.text.toString().trim()
        val priceStr = etProductPrice.text.toString().trim()

        if (title.isEmpty() || desc.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedImageUri == null) {
            Toast.makeText(requireContext(), "Lütfen fotoğraf seçin.", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading(true)

        val base64Image = encodeImageToBase64(selectedImageUri!!)

        if (base64Image != null) {
            saveProductToDatabase(title, desc, priceStr.toDouble(), base64Image)
        } else {
            isLoading(false)
            Toast.makeText(requireContext(), "Fotoğraf işlenirken hata oluştu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun encodeImageToBase64(imageUri: Uri): String? {
        return try {

            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)

            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 500, 500, true)

            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            val imageBytes = outputStream.toByteArray()

            Base64.encodeToString(imageBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveProductToDatabase(title: String, desc: String, price: Double, imageBase64: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            isLoading(false)
            Toast.makeText(requireContext(), "Hata: Kullanıcı girişi yok!", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance("https://mobiluygulamagelistirme-c1824-default-rtdb.europe-west1.firebasedatabase.app/")
        val productRef = database.getReference("Products")

        val newProductId = productRef.push().key

        if (newProductId == null) {
            isLoading(false)
            Toast.makeText(requireContext(), "Hata: Veritabanı ID üretemedi!", Toast.LENGTH_LONG).show()
            return
        }

        val newProduct = Product(
            productId = newProductId,
            sellerId = userId,
            title = title,
            description = desc,
            price = price,
            imageUrl = imageBase64,
        )


        productRef.child(newProductId).setValue(newProduct)
            .addOnSuccessListener {
                isLoading(false)
                Toast.makeText(requireContext(), "Ürün Başarıyla Eklendi!", Toast.LENGTH_LONG).show()
                clearForm()
            }
            .addOnFailureListener { e ->
                isLoading(false)
                Toast.makeText(requireContext(), "Kayıt Hatası: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
            .addOnCanceledListener {
                isLoading(false)
                Toast.makeText(requireContext(), "İşlem iptal edildi.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun isLoading(loading: Boolean) {
        if (loading) {
            progressBar.visibility = View.VISIBLE
            btnUploadProduct.isEnabled = false
            btnUploadProduct.text = "Kaydediliyor..."
        } else {
            progressBar.visibility = View.GONE
            btnUploadProduct.isEnabled = true
            btnUploadProduct.text = "İlanı Yayınla"
        }
    }

    private fun clearForm() {
        etProductTitle.text.clear()
        etProductDesc.text.clear()
        etProductPrice.text.clear()
        ivProductImage.setImageResource(android.R.drawable.ic_menu_camera)
        tvAddPhotoHint.visibility = View.VISIBLE
        selectedImageUri = null

    }
}