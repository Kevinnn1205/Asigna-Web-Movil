package com.example.appmovilasignaweb

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.appmovilasignaweb.models.userRegistro
import org.json.JSONException

class miperfil : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var id_user: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_miperfil)

        // Manejo de márgenes para los bordes de la pantalla
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MiAppPreferences", MODE_PRIVATE)

        // Lógica para hacer clic en "Cerrar sesión"
        val cerrarSesion = findViewById<TextView>(R.id.textView15)
        cerrarSesion.setOnClickListener {
            val intent = Intent(applicationContext, inicio_sesion::class.java)
            startActivity(intent)
            finish() // Cerrar la actividad actual para evitar que el usuario regrese
        }

        // Obtener los datos del usuario cuando la actividad inicia
        fetchUserData()
    }

    private fun fetchUserData() {
        // Inicializar la cola de solicitudes de Volley
        val queue = Volley.newRequestQueue(this)

        // Recuperar el token de SharedPreferences
        val token = sharedPreferences.getString("TOKEN", null)

        // Verificar que el token no sea nulo
        if (token == null) {
            Toast.makeText(this, "Token no encontrado. Por favor, inicie sesión nuevamente.", Toast.LENGTH_SHORT).show()
            return
        }

        // Definir la URL para el endpoint de la API
        val url = config.urlProfile // Cambia a la URL de tu API

        // Crear una JsonObjectRequest
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    // Suponiendo que la respuesta es un objeto de usuario
                    val userJson = response.getJSONObject("user") // Ajusta según la estructura real de la respuesta
                    val user = userRegistro(
                        tipo_documento = userJson.getString("tipo_documento"),
                        numero_documento = userJson.getString("numero_documento"),
                        rol = userJson.getString("rol"),
                        nombre_completo = userJson.getString("nombre_completo"),
                        username = userJson.getString("username"),
                        id_user = userJson.getString("id_user") // Obtén el id_user
                    )

                    // Aquí puedes almacenar id_user o hacer cualquier otra operación necesaria
                    id_user = user.id_user // Guardar el id_user para usarlo más adelante
                    Log.d("User ID", "ID del usuario: $id_user") // Ejemplo de uso

                } catch (e: JSONException) {
                    Log.e("Error JSON", "Error al analizar los datos del usuario: ${e.message}")
                }
            },
            Response.ErrorListener { error ->
                Log.e("Error Volley", "Error al recuperar los datos del usuario: ${error.message}")
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Cambia al token real
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

    fun irCambiarContrasena(view: View) {
        val intent = Intent(application, cambiarcontrasena::class.java)
        startActivity(intent)
    }
}
