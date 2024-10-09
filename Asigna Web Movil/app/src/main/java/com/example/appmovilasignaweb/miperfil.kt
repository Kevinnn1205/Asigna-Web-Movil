package com.example.appmovilasignaweb

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class miperfil : AppCompatActivity() {

    private val PICK_IMAGE = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_miperfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Lógica para el clic en "Cerrar sesión"
        val cerrarSesion = findViewById<TextView>(R.id.textView16)
        cerrarSesion.setOnClickListener {
            val intent = Intent(applicationContext, inicio_sesion::class.java) // Asegúrate de que LoginActivity es la pantalla de inicio de sesión
            startActivity(intent)
            finish() // Cierra la actividad actual para evitar que el usuario regrese a esta pantalla
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            // Obtener la URI de la imagen seleccionada
            imageUri = data?.data
            val imagenUsuario = findViewById<ImageView>(R.id.imageView16)
            // Establecer la imagen seleccionada como la imagen de usuario
            imagenUsuario.setImageURI(imageUri)
        }
    }

    fun volver(view: View) {
        val intent = Intent(application, espacios::class.java)
        startActivity(intent)
    }

    fun irdesactivarcuenta(view: View) {
        val intent = Intent(application, desactivar_cuenta::class.java)
        startActivity(intent)
    }

    fun irCambiarContrasena(view: View) {
        val intent = Intent(application, cambiarcontrasena::class.java)
        startActivity(intent)
    }
}
