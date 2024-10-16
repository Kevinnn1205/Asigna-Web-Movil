package com.example.appmovilasignaweb

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONException

class miPerfilAdmin : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var id_user: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mi_perfil_admin)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MiAppPreferences", MODE_PRIVATE)

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

        obtenerDatosUsuario()
    }

    // Función para cerrar la sesión
    private fun cerrarSesion() {
        // Limpiar las preferencias guardadas (por ejemplo, token de usuario)
        val editor = sharedPreferences.edit()
        editor.clear() // O puedes remover solo el token con editor.remove("token")
        editor.apply()

        // Mostrar un mensaje de que la sesión ha sido cerrada
        Toast.makeText(this, "Sesión cerrada.", Toast.LENGTH_SHORT).show()

        // Redirigir al usuario a la pantalla de inicio de sesión
        val intent = Intent(applicationContext, inicio_sesion::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual para que el usuario no pueda volver a esta pantalla
    }
    fun obtenerDatosUsuario() {
        val urlDatosUsuario = config.urlProfile
        val token = sharedPreferences.getString("TOKEN", null)

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
                    findViewById<TextView>(R.id.textView_username).text = username
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
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
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
