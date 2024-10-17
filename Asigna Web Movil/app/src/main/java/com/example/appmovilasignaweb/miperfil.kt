package com.example.appmovilasignaweb

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONException

class miperfil : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_miperfil)

        // Manejo de márgenes para los bordes de la pantalla
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MiAppPreferences", MODE_PRIVATE)

        // Configurar el listener para cerrar sesión (texto e ícono)
        findViewById<TextView>(R.id.textView15).setOnClickListener { cerrarSesion() }
        findViewById<ImageView>(R.id.imageView15).setOnClickListener { cerrarSesion() }

        // Obtener los datos del usuario cuando la actividad inicia
        obtenerDatosUsuario()
    }

    // Función para cerrar la sesión
    private fun cerrarSesion() {
        // Limpiar las preferencias guardadas (por ejemplo, token de usuario)
        val editor = sharedPreferences.edit()
        editor.clear() // O puedes remover solo el token con editor.remove("TOKEN")
        editor.apply()

        // Mostrar un mensaje de que la sesión ha sido cerrada
        Toast.makeText(this, "Sesión cerrada.", Toast.LENGTH_SHORT).show()

        // Redirigir al usuario a la pantalla de inicio de sesión
        val intent = Intent(applicationContext, inicio_sesion::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual para que el usuario no pueda volver a esta pantalla
    }

    // Método para obtener datos del usuario
    fun obtenerDatosUsuario() {
        val urlDatosUsuario = config.urlProfile
        val token = sharedPreferences.getString("token", null)

        if (token.isNullOrEmpty()) {
            Log.e("Error", "Token no encontrado")
            return
        }

        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, urlDatosUsuario, null,
            Response.Listener { response ->
                try {
                    // Aquí se asume que la respuesta es directamente un objeto userRegistro
                    // Imprimir la respuesta completa para depuración
                    Log.d("Response", response.toString())

                    // Obtener los datos directamente del objeto JSON
                    val nombreCompleto = response.optString("nombre_completo", "No disponible")
                    val username = response.optString("username", "No disponible")
                    val tipoDocumento = response.optString("tipo_documento", "No disponible")
                    val numeroDocumento = response.optString("numero_documento", "No disponible")
                    val rol = response.optString("rol", "No disponible")

                    // Actualizar la interfaz de usuario
                    findViewById<TextView>(R.id.textViewNombre_completo).text = nombreCompleto
                    findViewById<TextView>(R.id.textViewusername).text = username
                    findViewById<TextView>(R.id.textViewTipo_documento).text = tipoDocumento
                    findViewById<TextView>(R.id.textViewNumero_documento).text = numeroDocumento
                    findViewById<TextView>(R.id.textViewrol).text = rol

                } catch (e: JSONException) {
                    Log.e("Error JSON", "Error al analizar los datos del usuario: ${e.message}")
                    Toast.makeText(this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("Error Volley", "Error al recuperar los datos del usuario: ${error.message}")
                Toast.makeText(this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        // Agregar la solicitud a la RequestQueue
        queue.add(jsonObjectRequest)
    }

    // Métodos para navegar a otras actividades
    fun volver(view: View) {
        val intent = Intent(application, espacios::class.java)
        startActivity(intent)
    }

    fun irdesactivarcuenta(view: View) {
        val intent = Intent(application, desactivar_cuenta::class.java)
        startActivity(intent)
    }

    fun irCambiarContra(view: View) {
        val intent = Intent(application, Cambiarcontra::class.java)
        startActivity(intent)
    }
}
