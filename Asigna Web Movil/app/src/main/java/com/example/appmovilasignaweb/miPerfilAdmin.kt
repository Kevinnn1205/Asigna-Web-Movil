package com.example.appmovilasignaweb

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class miPerfilAdmin : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mi_perfil_admin)

        // Inicializamos SharedPreferences
        sharedPreferences = getSharedPreferences("SesionUsuario", MODE_PRIVATE)

        // Configuración para adaptar la ventana a las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Lógica para el clic en "Cerrar sesión" (ícono y texto)
        val cerrarSesionIcono = findViewById<ImageView>(R.id.imageView15)
        val cerrarSesionTexto = findViewById<TextView>(R.id.textView15)

        // Configurar el clic tanto en el ícono como en el texto
        cerrarSesionIcono.setOnClickListener { cerrarSesion() }
        cerrarSesionTexto.setOnClickListener { cerrarSesion() }
    }

    // Función para cerrar la sesión
    private fun cerrarSesion() {
        // Limpiar las preferencias guardadas (por ejemplo, token de usuario)
        val editor = sharedPreferences.edit()
        editor.clear() // O puedes remover solo el token con editor.remove("token")
        editor.apply()

        // Redirigir al usuario a la pantalla de inicio de sesión
        val intent = Intent(applicationContext, inicio_sesion::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual para que el usuario no pueda volver a esta pantalla
    }

    // Función para volver a la actividad anterior
    fun volver(view: View) {
        val intent = Intent(application, moduloInformacionAdmin::class.java)
        startActivity(intent)
    }

    fun irdesactivarcuenta(view: View) {
        val intent = Intent(application, desactivarCuentaAdmin::class.java)
        startActivity(intent)
    }

    fun irCrearCuenta(view: View) {
        val intent = Intent(application, crear_cuenta::class.java)
        startActivity(intent)
    }

    fun irCambiarContrasena(view: View) {
        val intent = Intent(application, CambiarcontraAdmin::class.java)
        startActivity(intent)
    }
}
