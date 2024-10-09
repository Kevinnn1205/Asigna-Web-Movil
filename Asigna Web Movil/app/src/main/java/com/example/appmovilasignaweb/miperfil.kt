package com.example.appmovilasignaweb

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONException

class miperfil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_miperfil)

        // Handling the margins for the screen edges
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Logic for clicking "Logout"
        val cerrarSesion = findViewById<TextView>(R.id.textView16)
        cerrarSesion.setOnClickListener {
            val intent = Intent(applicationContext, inicio_sesion::class.java)
            startActivity(intent)
            finish() // Close the current activity to prevent the user from returning
        }

        // Get user data when the activity starts
        fetchUserData()
    }

    private fun fetchUserData() {
        // Initialize the Volley request queue
        val queue = Volley.newRequestQueue(this)

        // Define the URL for the API endpoint
        val url = config.urlProfile // Change to your API URL

        // Create a JsonArrayRequest
        val jsonArrayRequest = object : JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    // Assuming the response is an array of user objects
                    for (i in 0 until response.length()) {
                        val userJson = response.getJSONObject(i)
                        val user = UserRegistro(
                            tipo_documento = userJson.getString("tipo_documento"),
                            numero_documento = userJson.getString("numero_documento"),
                            rol = userJson.getString("rol"),
                            nombre_completo = userJson.getString("nombre_completo"),
                            username = userJson.getString("username")
                        )

                        // Update the UI with the user data
                        updateUserInfo(user)
                    }
                } catch (e: JSONException) {
                    Log.e("Error JSON", "Error parsing user data: ${e.message}")
                }
            },
            Response.ErrorListener { error ->
                Log.e("Error Volley", "Error retrieving user data: ${error.message}")
            }
        ) {
            // Add headers if needed
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer tu_token_aqui" // Change to your actual authorization token
                return headers
            }
        }

        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest)
    }


    private fun updateUserInfo(user: UserRegistro) {
        // Update the UI elements with the user data
        findViewById<TextView>(R.id.textViewTipo_documento).text = user.tipo_documento
        findViewById<TextView>(R.id.textViewNumero_documento).text = user.numero_documento
        findViewById<TextView>(R.id.textViewRol).text = user.rol
        findViewById<TextView>(R.id.textViewNombre_completo).text = user.nombre_completo
        findViewById<TextView>(R.id.textViewUsername).text = user.username
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
