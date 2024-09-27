package com.example.appmovilasignaweb

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.window.application
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config.Companion.urllogin
import com.example.appmovilasignaweb.config.config.Companion.urluserRegistro
import org.json.JSONException
import org.json.JSONObject

class inicio_sesion : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_sesion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Inicializar Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this)

        sharedPreferences = getSharedPreferences("MiAppPreferences", MODE_PRIVATE)

        // Inicializar EditTexts y Button
        usernameEditText = findViewById(R.id.txtCorreoelectronico)
        passwordEditText = findViewById(R.id.txtContraseña)
        loginButton = findViewById(R.id.btnGuardar)

        // Configurar el listener del botón de inicio de sesión
        loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val url = urllogin + "/login/"

        val jsonBody = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            Response.Listener { response ->
                try {
                    val token = response.getString("token")
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                    // Guardar el token en SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putString("TOKEN", token)
                    editor.apply()

                    // Redirigir a otra actividad
                    val intent = Intent(this, espacios::class.java)
                    startActivity(intent)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Manejar el error
                error.printStackTrace()
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        ) {}

        requestQueue.add(jsonObjectRequest)
    }



    fun volver(view: View) {
        var intent = Intent(application, MainActivity::class.java)
        startActivity(intent)
    }

    fun irrecuperarcontra(view: View) {
        var intent = Intent(application, recuperar_contra::class.java)
        startActivity(intent)
    }

    fun irespacio(view: View) {
        var intent = Intent(application, espacios::class.java)
        startActivity(intent)
    }
}
